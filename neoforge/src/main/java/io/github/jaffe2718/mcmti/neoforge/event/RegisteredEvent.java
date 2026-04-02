package io.github.jaffe2718.mcmti.neoforge.event;

import io.github.jaffe2718.mcmti.util.SpeechRecognizer;

public class RegisteredEvent extends SpeechRecognizerEvent {

    private final int defaultPriority;
    private final int priority;

    public RegisteredEvent(int defaultPriority, int priority, SpeechRecognizer recognizer) {
        super(recognizer);
        this.defaultPriority = defaultPriority;
        this.priority = priority;
    }

    public int getDefaultPriority() {
        return defaultPriority;
    }

    public int getPriority() {
        return priority;
    }
}
