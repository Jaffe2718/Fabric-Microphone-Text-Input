package io.github.jaffe2718.mcmti.neoforge.event;

import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.minecraft.util.Identifier;

public class DeregisteredEvent extends SpeechRecognizerEvent {

    /**
     * The identifiers of the deregistered recognizers.
     */
    private final Identifier[] ids;

    public DeregisteredEvent(Identifier[] ids) {
        super(null);
        this.ids = ids;
    }

    /**
     * @return The identifiers of the deregistered recognizers.
     */
    public Identifier[] getIds() {
        return ids;
    }

    /**
     * Call this method is prohibited because DeregisteredEvent does not have a recognizer.
     * @throws UnsupportedOperationException Always throws UnsupportedOperationException.
     */
    @Override
    public SpeechRecognizer getRecognizer() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("DeregisteredEvent does not have a recognizer.");
    }
}
