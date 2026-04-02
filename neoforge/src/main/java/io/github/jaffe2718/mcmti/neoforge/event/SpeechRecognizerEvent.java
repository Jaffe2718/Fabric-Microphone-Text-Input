package io.github.jaffe2718.mcmti.neoforge.event;

import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for speech recognizer events.
 */
public abstract class SpeechRecognizerEvent  extends Event implements IModBusEvent {
    @Nullable
    protected final SpeechRecognizer recognizer;

    public SpeechRecognizerEvent(@Nullable SpeechRecognizer recognizer) {
        this.recognizer = recognizer;
    }

    @Nullable
    public SpeechRecognizer getRecognizer() {
        return this.recognizer;
    }
}
