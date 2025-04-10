package github.jaffe2718.mcmti.util;

import github.jaffe2718.mcmti.client.MicrophoneTextInputClient;
import github.jaffe2718.mcmti.config.MicrophoneTextInputConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;


public abstract class EventSystem {

    public static void register() {
        ClientLifecycleEvents.CLIENT_STOPPING.register(minecraftClient -> {
            AudioRecorder.destroy();
            SpeechRecognizer.destroy();
        });
//        ClientTickEvents.START_WORLD_TICK.register(EventSystem::onConfigAltered);
        Thread.ofVirtual().start(EventSystem::recognizeTask);
        Thread.ofVirtual().start(EventSystem::onConfigAltered);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void onConfigAltered() {
        while (true) {
            if (!MicrophoneTextInputConfig.model.equals(SpeechRecognizer.instance().modelPath)
                    || !MicrophoneTextInputConfig.grammar.equals(SpeechRecognizer.instance().grammarPath)) {
                SpeechRecognizer.init();
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private static void recognizeTask() {
        MicrophoneTextInputClient.LOGGER.info("Recognize thread started");
        while (true) {
            if (MinecraftClient.getInstance().player instanceof ClientPlayerEntity player
                    && MinecraftClient.getInstance().currentScreen == null) {
                switch (MicrophoneTextInputConfig.mode) {
                    case AUTO_SEND -> {
                        float[] audio = AudioRecorder.recordCycle();
                        Thread.ofVirtual().start(()->{
                            String result = SpeechRecognizer.recognize(audio);
                            if (!result.isEmpty()) {
                                player.networkHandler.sendChatMessage(MicrophoneTextInputConfig.prefix + result);
                            }
                        });
                    }
                    case RELEASE_KEY_TO_SEND -> {
                        if (MicrophoneTextInputClient.RECOGNIZE_KEY.isPressed()) {
                            float[] audio = AudioRecorder.record();
                            Thread.ofVirtual().start(()->{
                                String result = SpeechRecognizer.recognize(audio);
                                if (!result.isEmpty()) {
                                    player.networkHandler.sendChatMessage(MicrophoneTextInputConfig.prefix + result);
                                }
                            });
                        }
                    }
                    case RELEASE_KEY_TO_INPUT -> {
                        if (MicrophoneTextInputClient.RECOGNIZE_KEY.isPressed()) {
                            float[] audio = AudioRecorder.record();
                            Thread.ofVirtual().start(()->{
                                String result = SpeechRecognizer.recognize(audio);
                                if (!result.isEmpty()) {
                                    MinecraftClient.getInstance().setScreen(new ChatScreen(MicrophoneTextInputConfig.prefix + result));
                                }
                            });
                        }
                    }
                }
            }
        }
    }
}
