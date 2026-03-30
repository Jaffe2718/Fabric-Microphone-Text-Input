package io.github.jaffe2718.mcmti.util.fabric;

import io.github.jaffe2718.mcmti.event.EventSystem;
import io.github.jaffe2718.mcmti.util.AudioRecorder;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@SuppressWarnings("unused")
public abstract class EventUtilImpl {

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(EventSystem::onConfigAltered);
        ClientTickEvents.END_WORLD_TICK.register(EventSystem::showRecognizeStatus);
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            SpeechRecognizer.deregister();
            AudioRecorder.destroy();
        });
        Thread.ofVirtual().start(EventSystem::recognizeTask).setName("thread.mcmti.recognizer.loop");
    }
}
