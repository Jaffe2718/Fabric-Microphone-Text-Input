package me.jaffe2718.mcmti.event;

import me.jaffe2718.mcmti.MicrophoneTextInput;
import me.jaffe2718.mcmti.config.McmtiConfig;
import me.jaffe2718.mcmti.util.AudioRecorder;
import me.jaffe2718.mcmti.util.SpeechRecognizer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@SuppressWarnings("unused")
public interface EventSystem {

    ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    static void showRecognizeStatus(ClientWorld world) {
        if (MinecraftClient.getInstance().player != null
                && MinecraftClient.getInstance().currentScreen == null) {
            if (AudioRecorder.instance() == null) {
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("message.mcmti.audioInputDeviceLoadFailed"), true);
            } else if (SpeechRecognizer.instance() == null) {
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("message.mcmti.whisperModelLoadFailed"), true);
            } else if (McmtiConfig.mode != McmtiConfig.Mode.AUTO_SEND
                    && MicrophoneTextInput.RECOGNIZE_KEY.isPressed()) {
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("message.mcmti.recordingAudio"), true);
            }
        }
    }


    @SuppressWarnings("InfiniteLoopStatement")
    static void recognizeTask() {
        MicrophoneTextInput.LOGGER.info("Recognize thread started");
        @Nullable Thread vthread = null;
        while (true) {
            try {
                if (MinecraftClient.getInstance() != null
                        && MinecraftClient.getInstance().player != null
                        && MinecraftClient.getInstance().currentScreen == null
                        && AudioRecorder.instance() != null
                        && SpeechRecognizer.instance() != null) {
                    switch (McmtiConfig.mode) {
                        case AUTO_SEND -> {
                            float[] audio = AudioRecorder.recordCycle();
                            new Thread(() -> {
                                String result = SpeechRecognizer.recognize(audio);
                                if (!result.isEmpty()) {
                                    MinecraftClient.getInstance().player.sendMessage(Text.translatable("message.mcmti.messageSent"), true);
                                    SpeechRecognizer.sendChatMessage(MinecraftClient.getInstance().player, result);
                                }
                            }).start();
                        }
                        case RELEASE_KEY_TO_SEND -> {
                            if (MicrophoneTextInput.RECOGNIZE_KEY.isPressed()) {
                                float[] audio = AudioRecorder.record();   // loop until key released
                                vthread = new Thread(() -> {
                                    String result = SpeechRecognizer.recognize(audio);
                                    if (!result.isEmpty()) {
                                        SCHEDULED_EXECUTOR_SERVICE.schedule(
                                                () -> MinecraftClient.getInstance().player.sendMessage(Text.translatable("message.mcmti.messageSent"), true), 100, TimeUnit.MILLISECONDS);
                                        SpeechRecognizer.sendChatMessage(MinecraftClient.getInstance().player, result);
                                    }
                                });
                                vthread.start();
                            } else if (vthread != null && vthread.isAlive()) {
                                MinecraftClient.getInstance().player.sendMessage(Text.translatable("message.mcmti.recognizing"), true);
                            }
                            LockSupport.parkNanos(1000000L);
                        }
                        case RELEASE_KEY_TO_INPUT -> {
                            if (MicrophoneTextInput.RECOGNIZE_KEY.isPressed()) {
                                float[] audio = AudioRecorder.record();
                                vthread = new Thread(() -> {
                                    String result = SpeechRecognizer.recognize(audio);
                                    if (!result.isEmpty()) {
                                        MinecraftClient.getInstance().submit(() -> MinecraftClient.getInstance().setScreen(new ChatScreen(McmtiConfig.prefix + result))).join();
                                    }
                                });
                                vthread.start();
                            } else if (vthread != null && vthread.isAlive()) {
                                MinecraftClient.getInstance().player.sendMessage(Text.translatable("message.mcmti.recognizing"), true);
                            }
                            LockSupport.parkNanos(1000000L);
                        }
                    }
                } else {
                    LockSupport.parkNanos(10000000L);
                }
            } catch (Throwable t) {
                MicrophoneTextInput.LOGGER.error("Error in recognize task", t);
            }
        }
    }
}
