package io.github.jaffe2718.mcmti.util.neoforge;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import io.github.jaffe2718.mcmti.MicrophoneTextInput;
import io.github.jaffe2718.mcmti.event.*;
import io.github.jaffe2718.mcmti.neoforge.event.*;
import io.github.jaffe2718.mcmti.util.AudioRecorder;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.minecraft.util.Identifier;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@SuppressWarnings("unused")
public abstract class EventUtilImpl {

    public static void register() {
        ClientTickEvent.CLIENT_POST.register(EventSystem::onConfigAltered);
        ClientTickEvent.CLIENT_LEVEL_POST.register(EventSystem::showRecognizeStatus);
        ClientLifecycleEvent.CLIENT_STARTED.register(client -> SpeechRecognizer.init());
        ClientLifecycleEvent.CLIENT_STOPPING.register(client -> {
            SpeechRecognizer.deregister();
            AudioRecorder.destroy();
        });
        Thread.ofVirtual().start(EventSystem::recognizeTask).setName("thread.mcmti.recognizer.loop");
    }

    public static void triggerEvent(@NotNull EventType event, Object... args) throws IllegalArgumentException {
        ModContainer container = ModList.get().getModContainerById(MicrophoneTextInput.MOD_ID).orElse(null);
        if (container == null || container.getEventBus() == null) {
            throw new IllegalArgumentException(String.format("\"%s\" mod not found", MicrophoneTextInput.MOD_ID));
        }
        switch (event) {
            case SPEECH_RECOGNIZER_REGISTERED -> {
                if (args.length == 3 &&
                        args[0] instanceof Integer defaultPriority &&
                        args[1] instanceof Integer priority &&
                        args[2] instanceof SpeechRecognizer recognizer) {
                    container.getEventBus().post(new RegisteredEvent(defaultPriority, priority, recognizer));
                    McmtiSpeechRecognizerEvents.SPEECH_RECOGNIZER_REGISTERED.invoker().onTriggered(defaultPriority, priority, recognizer);
                } else {
                    throw new IllegalArgumentException(String.format("Invalid arguments %s for %s event", Arrays.toString(args), event));
                }
            }
            case SPEECH_RECOGNIZER_ACTIVATED -> {
                if (args.length == 1 && args[0] instanceof SpeechRecognizer recognizer) {
                    container.getEventBus().post(new ActivatedEvent(recognizer));
                    McmtiSpeechRecognizerEvents.SPEECH_RECOGNIZER_ACTIVATED.invoker().onTriggered(recognizer);
                } else {
                    throw new IllegalArgumentException(String.format("Invalid arguments %s for %s event", Arrays.toString(args), event));
                }
            }
            case SPEECH_RECOGNIZER_DEACTIVATED -> {
                if (args.length == 1 && args[0] instanceof SpeechRecognizer recognizer) {
                    container.getEventBus().post(new DeactivatedEvent(recognizer));
                    McmtiSpeechRecognizerEvents.SPEECH_RECOGNIZER_DEACTIVATED.invoker().onTriggered(recognizer);
                } else {
                    throw new IllegalArgumentException(String.format("Invalid arguments %s for %s event", Arrays.toString(args), event));
                }
            }
            case SPEECH_RECOGNIZER_TRANSCRIBED -> {
                if (args.length == 3 &&
                        args[0] instanceof SpeechRecognizer recognizer &&
                        args[1] instanceof float[] audio &&
                        args[2] instanceof String transcription) {
                    container.getEventBus().post(new TranscribedEvent(recognizer, audio, transcription));
                    McmtiSpeechRecognizerEvents.SPEECH_RECOGNIZER_TRANSCRIBED.invoker().onTriggered(recognizer, audio, transcription);
                } else {
                    throw new IllegalArgumentException(String.format("Invalid arguments %s for %s event", Arrays.toString(args), event));
                }
            }
            case ALL_SPEECH_RECOGNIZERS_DEREGISTERED -> {
                if (args instanceof Identifier[] ids) {
                    container.getEventBus().post(new DeregisteredEvent(ids));
                    McmtiSpeechRecognizerEvents.ALL_SPEECH_RECOGNIZERS_DEREGISTERED.invoker().onTriggered(ids);
                } else {
                    throw new IllegalArgumentException(String.format("Invalid arguments %s for %s event", Arrays.toString(args), event));
                }
            }
            default -> throw new IllegalArgumentException("Invalid event type: " + event);
        }
    }
}
