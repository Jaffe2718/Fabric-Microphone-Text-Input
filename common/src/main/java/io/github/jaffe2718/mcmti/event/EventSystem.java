package io.github.jaffe2718.mcmti.event;

import eu.midnightdust.lib.config.MidnightConfigScreen;
import io.github.jaffe2718.mcmti.MicrophoneTextInput;
import io.github.jaffe2718.mcmti.client.gui.screen.AdvancedConfigWarningScreen;
import io.github.jaffe2718.mcmti.config.McmtiConfig;
import io.github.jaffe2718.mcmti.util.AudioRecorder;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@SuppressWarnings("unused")
public interface EventSystem {

    ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    static void showRecognizeStatus(ClientLevel level) {
        if (Minecraft.getInstance().player instanceof LocalPlayer player
                && Minecraft.getInstance().screen == null) {
            if (AudioRecorder.instance() == null) {
                player.sendOverlayMessage(Component.translatable("message.mcmti.audioInputDeviceLoadFailed"));
            } else if (!SpeechRecognizer.instanceAvailable()) {
                player.sendOverlayMessage(SpeechRecognizer.instanceUnavailableToast());
            } else if (McmtiConfig.mode != McmtiConfig.Mode.AUTO_SEND
                    && MicrophoneTextInput.RECOGNIZE_KEY.isDown()) {
                player.sendOverlayMessage(Component.translatable("message.mcmti.recordingAudio"));
            }
        }
    }

    static void onConfigAltered(Minecraft client) {
        if (McmtiConfig.advancedConfig
                && !MicrophoneTextInput.advancedConfig
                && client.screen instanceof MidnightConfigScreen) {  // advanced config enabled
            Minecraft.getInstance().setScreen(new AdvancedConfigWarningScreen(client.screen));
        }
        MicrophoneTextInput.advancedConfig = McmtiConfig.advancedConfig;    // synchronize with config
    }


    @SuppressWarnings({"InfiniteLoopStatement", "ConstantValue"})
    static void recognizeTask() {
        MicrophoneTextInput.LOGGER.info("Recognize thread started");
        @Nullable Thread vthread = null;
        while (true) {
            try {
                if (Minecraft.getInstance() != null &&
                        Minecraft.getInstance().player instanceof LocalPlayer player
                        && Minecraft.getInstance().screen == null
                        && AudioRecorder.instance() != null) {
                    switch (McmtiConfig.mode) {
                        case AUTO_SEND -> {
                            float[] audio = AudioRecorder.recordCycle();
                            Thread.ofVirtual().start(() -> {
                                String result = SpeechRecognizer.recognize(audio);
                                if (!result.isEmpty()) {
                                    player.sendOverlayMessage(Component.translatable("message.mcmti.messageSent"));
                                    sendChatMessage(player, result);
                                }
                            });
                        }
                        case RELEASE_KEY_TO_SEND -> {
                            if (MicrophoneTextInput.RECOGNIZE_KEY.isDown()) {
                                float[] audio = AudioRecorder.record();   // loop until key released
                                vthread = Thread.ofVirtual().start(() -> {
                                    String result = SpeechRecognizer.recognize(audio);
                                    if (!result.isEmpty()) {
                                        SCHEDULED_EXECUTOR_SERVICE.schedule(
                                                () -> player.sendOverlayMessage(Component.translatable("message.mcmti.messageSent")), 100, TimeUnit.MILLISECONDS);
                                        EventSystem.sendChatMessage(player, result);
                                    }
                                });
                            } else if (vthread != null && vthread.isAlive()) {
                                player.sendOverlayMessage(Component.translatable("message.mcmti.recognizing"));
                            }
                            LockSupport.parkNanos(1000000L);
                        }
                        case RELEASE_KEY_TO_INPUT -> {
                            if (MicrophoneTextInput.RECOGNIZE_KEY.isDown()) {
                                float[] audio = AudioRecorder.record();
                                vthread = Thread.ofVirtual().start(() -> {
                                    String result = SpeechRecognizer.recognize(audio);
                                    if (!result.isEmpty()) {
                                        Minecraft.getInstance().submit(() -> Minecraft.getInstance().setScreen(new ChatScreen(McmtiConfig.prefix + result, McmtiConfig.draftInput))).join();
                                    }
                                });
                            } else if (vthread != null && vthread.isAlive()) {
                                player.sendOverlayMessage(Component.translatable("message.mcmti.recognizing"));
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
    static void sendChatMessage(@NotNull LocalPlayer player, @NotNull String message) {
        final int maxLength = 256 - McmtiConfig.prefix.length();
        while (message.length() > maxLength) {
            player.connection.sendChat(McmtiConfig.prefix + message.substring(0, maxLength));
            message = message.substring(maxLength);
        }
        if (!message.isEmpty()) {            // send the rest
            player.connection.sendChat(McmtiConfig.prefix + message);
        }
    }
}
