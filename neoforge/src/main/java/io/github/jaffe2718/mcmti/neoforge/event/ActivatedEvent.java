package io.github.jaffe2718.mcmti.neoforge.event;

import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import org.jetbrains.annotations.NotNull;

public class ActivatedEvent extends SpeechRecognizerEvent {

    public ActivatedEvent(@NotNull SpeechRecognizer recognizer) {
        super(recognizer);
    }
}
