package io.github.jaffe2718.mcmti.fabric;

import io.github.jaffe2718.mcmti.MicrophoneTextInput;
import io.github.jaffe2718.mcmti.event.EventSystem;
import io.github.jaffe2718.mcmti.util.AudioRecorder;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public final class MicrophoneTextInputFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(MicrophoneTextInput.RECOGNIZE_KEY);
        MicrophoneTextInput.init();
        registerEvents();
    }

    public static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(EventSystem::onConfigAltered);
        ClientTickEvents.END_WORLD_TICK.register(EventSystem::showRecognizeStatus);
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> Thread.ofVirtual().start(SpeechRecognizer::init));
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            SpeechRecognizer.deregister();
            AudioRecorder.destroy();
        });
    }
}
