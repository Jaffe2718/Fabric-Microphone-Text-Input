package io.github.jaffe2718.mcmti.util.neoforge;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import io.github.jaffe2718.mcmti.event.EventSystem;
import io.github.jaffe2718.mcmti.util.AudioRecorder;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;

@SuppressWarnings("unused")
public abstract class EventUtilImpl {

    public static void register() {
        ClientTickEvent.CLIENT_POST.register(EventSystem::onConfigAltered);
        ClientTickEvent.CLIENT_LEVEL_POST.register(EventSystem::showRecognizeStatus);
        ClientLifecycleEvent.CLIENT_STOPPING.register(client -> {
            SpeechRecognizer.destroy();
            AudioRecorder.destroy();
        });
        Thread.ofVirtual().start(EventSystem::recognizeTask).setName("thread.mcmti.recognizer.loop");
    }
}
