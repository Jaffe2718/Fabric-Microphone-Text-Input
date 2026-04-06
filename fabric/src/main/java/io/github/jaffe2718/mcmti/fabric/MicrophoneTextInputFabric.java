package io.github.jaffe2718.mcmti.fabric;

import io.github.jaffe2718.mcmti.MicrophoneTextInput;
import io.github.jaffe2718.mcmti.event.EventSystem;
import io.github.jaffe2718.mcmti.util.AudioRecorder;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

public final class MicrophoneTextInputFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyMappingHelper.registerKeyMapping(MicrophoneTextInput.RECOGNIZE_KEY);
        MicrophoneTextInput.init();
        registerEvents();
    }

    public static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(EventSystem::onConfigAltered);
        ClientTickEvents.END_LEVEL_TICK.register(EventSystem::showRecognizeStatus);
        ClientLifecycleEvents.CLIENT_STARTED.register(_ -> Thread.ofVirtual().start(SpeechRecognizer::init));
        ClientLifecycleEvents.CLIENT_STOPPING.register(_ -> {
            SpeechRecognizer.deregister();
            AudioRecorder.destroy();
        });
    }
}
