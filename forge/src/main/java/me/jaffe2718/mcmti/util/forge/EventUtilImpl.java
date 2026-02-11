package me.jaffe2718.mcmti.util.forge;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import me.jaffe2718.mcmti.event.EventSystem;
import me.jaffe2718.mcmti.util.AudioRecorder;
import me.jaffe2718.mcmti.util.SpeechRecognizer;

@SuppressWarnings("unused")
public abstract class EventUtilImpl {

    public static void register() {
        ClientTickEvent.CLIENT_LEVEL_POST.register(EventSystem::showRecognizeStatus);
        ClientLifecycleEvent.CLIENT_STOPPING.register(client -> {
            SpeechRecognizer.destroy();
            AudioRecorder.destroy();
        });
        new Thread(EventSystem::recognizeTask, "thread.mcmti.recognizer.loop").start();
    }
}
