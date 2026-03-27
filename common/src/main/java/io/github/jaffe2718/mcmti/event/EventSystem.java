package io.github.jaffe2718.mcmti.event;

import eu.midnightdust.lib.config.MidnightConfigScreen;
import io.github.jaffe2718.mcmti.MicrophoneTextInput;
import io.github.jaffe2718.mcmti.client.gui.screen.AdvancedConfigWarningScreen;
import io.github.jaffe2718.mcmti.config.McmtiConfig;
import io.github.jaffe2718.mcmti.util.AudioRecorder;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@SuppressWarnings("unused")
public interface EventSystem {

    ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    static void showRecognizeStatus(ClientWorld world) {
        if (MinecraftClient.getInstance().player instanceof ClientPlayerEntity player
                && MinecraftClient.getInstance().currentScreen == null) {
            if (AudioRecorder.instance() == null) {
                player.sendMessage(Text.translatable("message.mcmti.audioInputDeviceLoadFailed"), true);
            } else if (!SpeechRecognizer.instanceAvailable()) {
                player.sendMessage(SpeechRecognizer.instanceUnavailableToast(), true);
            } else if (McmtiConfig.mode != McmtiConfig.Mode.AUTO_SEND
                    && MicrophoneTextInput.RECOGNIZE_KEY.isPressed()) {
                player.sendMessage(Text.translatable("message.mcmti.recordingAudio"), true);
            }
        }
    }

    static void onConfigAltered(MinecraftClient client) {
        if (McmtiConfig.advancedConfig
                && !MicrophoneTextInput.advancedConfig
                && MinecraftClient.getInstance().currentScreen instanceof MidnightConfigScreen) {  // advanced config enabled
            MinecraftClient.getInstance().setScreen(new AdvancedConfigWarningScreen(MinecraftClient.getInstance().currentScreen));
        }
        MicrophoneTextInput.advancedConfig = McmtiConfig.advancedConfig;    // synchronize with config
    }


    @SuppressWarnings("InfiniteLoopStatement")
    static void recognizeTask() {
        MicrophoneTextInput.LOGGER.info("Recognize thread started");
        @Nullable Thread vthread = null;
        while (true) {
            try {
                if (MinecraftClient.getInstance() != null &&
                        MinecraftClient.getInstance().player instanceof ClientPlayerEntity player
                        && MinecraftClient.getInstance().currentScreen == null
                        && AudioRecorder.instance() != null) {
                    switch (McmtiConfig.mode) {
                        case AUTO_SEND -> {
                            float[] audio = AudioRecorder.recordCycle();
                            Thread.ofVirtual().start(() -> {
                                String result = SpeechRecognizer.recognize(audio);
                                if (!result.isEmpty()) {
                                    player.sendMessage(Text.translatable("message.mcmti.messageSent"), true);
                                    sendChatMessage(player, result);
                                }
                            });
                        }
                        case RELEASE_KEY_TO_SEND -> {
                            if (MicrophoneTextInput.RECOGNIZE_KEY.isPressed()) {
                                float[] audio = AudioRecorder.record();   // loop until key released
                                vthread = Thread.ofVirtual().start(() -> {
                                    String result = SpeechRecognizer.recognize(audio);
                                    if (!result.isEmpty()) {
                                        SCHEDULED_EXECUTOR_SERVICE.schedule(
                                                () -> player.sendMessage(Text.translatable("message.mcmti.messageSent"), true), 100, TimeUnit.MILLISECONDS);
                                        EventSystem.sendChatMessage(player, result);
                                    }
                                });
                            } else if (vthread != null && vthread.isAlive()) {
                                player.sendMessage(Text.translatable("message.mcmti.recognizing"), true);
                            }
                            LockSupport.parkNanos(1000000L);
                        }
                        case RELEASE_KEY_TO_INPUT -> {
                            if (MicrophoneTextInput.RECOGNIZE_KEY.isPressed()) {
                                float[] audio = AudioRecorder.record();
                                vthread = Thread.ofVirtual().start(() -> {
                                    String result = SpeechRecognizer.recognize(audio);
                                    if (!result.isEmpty()) {
                                        MinecraftClient.getInstance().submit(() -> MinecraftClient.getInstance().setScreen(new ChatScreen(McmtiConfig.prefix + result, McmtiConfig.draftInput))).join();
                                    }
                                });
                            } else if (vthread != null && vthread.isAlive()) {
                                player.sendMessage(Text.translatable("message.mcmti.recognizing"), true);
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

    /**
     * Send a message to the chat, split it into multiple messages if necessary.
     * Due to the limitation of the chat message length in Minecraft,
     * the message will be split into multiple parts with prefix and not longer than 256 characters.
     *
     * @param player  The player to send the message.
     * @param message The message to send.
     */
    static void sendChatMessage(@NotNull ClientPlayerEntity player, @NotNull String message) {
        final int maxLength = 256 - McmtiConfig.prefix.length();
        while (message.length() > maxLength) {
            player.networkHandler.sendChatMessage(McmtiConfig.prefix + message.substring(0, maxLength));
            message = message.substring(maxLength);
        }
        if (!message.isEmpty()) {            // send the rest
            player.networkHandler.sendChatMessage(McmtiConfig.prefix + message);
        }
    }
}
