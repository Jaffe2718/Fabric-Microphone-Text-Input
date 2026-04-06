package io.github.jaffe2718.mcmti.util.neoforge;

import io.github.jaffe2718.mcmti.event.*;
import io.github.jaffe2718.mcmti.neoforge.MicrophoneTextInputNeoForge;
import io.github.jaffe2718.mcmti.neoforge.event.*;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@SuppressWarnings("unused")
public abstract class EventUtilImpl {

    public static void triggerEvent(@NotNull EventType event, Object... args) throws IllegalArgumentException {
        IEventBus modBus = MicrophoneTextInputNeoForge.getEventBus();
        switch (event) {
            case SPEECH_RECOGNIZER_REGISTERED -> {
                if (args.length == 3 &&
                        args[0] instanceof Integer defaultPriority &&
                        args[1] instanceof Integer priority &&
                        args[2] instanceof SpeechRecognizer recognizer) {
                    modBus.post(new SpeechRecognizerEvent.Registered(defaultPriority, priority, recognizer));
                } else {
                    throw new IllegalArgumentException(String.format("Invalid arguments %s for %s event", Arrays.toString(args), event));
                }
            }
            case SPEECH_RECOGNIZER_ACTIVATED -> {
                if (args.length == 1 && args[0] instanceof SpeechRecognizer recognizer) {
                    modBus.post(new SpeechRecognizerEvent.Activated(recognizer));
                } else {
                    throw new IllegalArgumentException(String.format("Invalid arguments %s for %s event", Arrays.toString(args), event));
                }
            }
            case SPEECH_RECOGNIZER_DEACTIVATED -> {
                if (args.length == 1 && args[0] instanceof SpeechRecognizer recognizer) {
                    modBus.post(new SpeechRecognizerEvent.Deactivated(recognizer));
                } else {
                    throw new IllegalArgumentException(String.format("Invalid arguments %s for %s event", Arrays.toString(args), event));
                }
            }
            case SPEECH_RECOGNIZER_TRANSCRIBED -> {
                if (args.length == 3 &&
                        args[0] instanceof SpeechRecognizer recognizer &&
                        args[1] instanceof float[] audio &&
                        args[2] instanceof String transcription) {
                    modBus.post(new SpeechRecognizerEvent.Transcribed(recognizer, audio, transcription));
                } else {
                    throw new IllegalArgumentException(String.format("Invalid arguments %s for %s event", Arrays.toString(args), event));
                }
            }
            case ALL_SPEECH_RECOGNIZERS_DEREGISTERED -> {
                if (args instanceof Identifier[] ids) {
                    modBus.post(new SpeechRecognizerEvent.Deregistered(ids));
                } else {
                    throw new IllegalArgumentException(String.format("Invalid arguments %s for %s event", Arrays.toString(args), event));
                }
            }
            default -> throw new IllegalArgumentException("Invalid event type: " + event);
        }
    }
}
