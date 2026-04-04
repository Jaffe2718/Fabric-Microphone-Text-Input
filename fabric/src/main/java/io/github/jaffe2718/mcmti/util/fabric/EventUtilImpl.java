package io.github.jaffe2718.mcmti.util.fabric;

import io.github.jaffe2718.mcmti.event.EventType;
import io.github.jaffe2718.mcmti.fabric.event.McmtiSpeechRecognizerEvents;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@SuppressWarnings("unused")
public abstract class EventUtilImpl {

    /**
     * Trigger the event.
     * @param event The event to trigger.
     * @param args The arguments to pass to the event.
     * @see EventType
     * @throws IllegalArgumentException If the arguments are invalid.
     */
    public static void triggerEvent(@NotNull EventType event, Object... args) throws IllegalArgumentException {
        switch (event) {
            case SPEECH_RECOGNIZER_REGISTERED -> {
                if (args.length == 3 &&
                        args[0] instanceof Integer defaultPriority &&
                        args[1] instanceof Integer priority &&
                        args[2] instanceof SpeechRecognizer recognizer) {
                    McmtiSpeechRecognizerEvents.SPEECH_RECOGNIZER_REGISTERED.invoker().onTriggered(defaultPriority, priority, recognizer);
                } else {
                    throw new IllegalArgumentException(String.format("Invalid arguments %s for %s event", Arrays.toString(args), event));
                }
            }
            case SPEECH_RECOGNIZER_ACTIVATED -> {
                if (args.length == 1 && args[0] instanceof SpeechRecognizer recognizer) {
                    McmtiSpeechRecognizerEvents.SPEECH_RECOGNIZER_ACTIVATED.invoker().onTriggered(recognizer);
                } else {
                    throw new IllegalArgumentException(String.format("Invalid arguments %s for %s event", Arrays.toString(args), event));
                }
            }
            case SPEECH_RECOGNIZER_DEACTIVATED -> {
                if (args.length == 1 && args[0] instanceof SpeechRecognizer recognizer) {
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
                    McmtiSpeechRecognizerEvents.SPEECH_RECOGNIZER_TRANSCRIBED.invoker().onTriggered(recognizer, audio, transcription);
                } else {
                    throw new IllegalArgumentException(String.format("Invalid arguments %s for %s event", Arrays.toString(args), event));
                }
            }
            case ALL_SPEECH_RECOGNIZERS_DEREGISTERED -> {
                if (args instanceof Identifier[] ids) {
                    McmtiSpeechRecognizerEvents.ALL_SPEECH_RECOGNIZERS_DEREGISTERED.invoker().onTriggered(ids);
                } else {
                    throw new IllegalArgumentException(String.format("Invalid arguments %s for %s event", Arrays.toString(args), event));
                }
            }
            default -> throw new IllegalArgumentException("Invalid event type: " + event);
        }
    }
}
