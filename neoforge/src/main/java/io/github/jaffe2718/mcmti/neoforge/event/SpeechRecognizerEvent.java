package io.github.jaffe2718.mcmti.neoforge.event;

import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.NotNull;
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

    protected interface HasRecognizer {
        @SuppressWarnings("unused")
        default SpeechRecognizer getRecognizer() {
            if (this instanceof SpeechRecognizerEvent sEvent) {
                return sEvent.recognizer;
            }
            return null;
        }
    }


    public static class Registered extends SpeechRecognizerEvent implements HasRecognizer {

        private final int defaultPriority;
        private final int priority;

        public Registered(int defaultPriority, int priority, SpeechRecognizer recognizer) {
            super(recognizer);
            this.defaultPriority = defaultPriority;
            this.priority = priority;
        }

        @SuppressWarnings("unused")
        public int getDefaultPriority() {
            return this.defaultPriority;
        }

        @SuppressWarnings("unused")
        public int getPriority() {
            return this.priority;
        }
    }

    public static class Activated extends SpeechRecognizerEvent implements HasRecognizer {

        public Activated(@NotNull SpeechRecognizer recognizer) {
            super(recognizer);
        }
    }

    public static class Deactivated extends SpeechRecognizerEvent implements HasRecognizer {

        public Deactivated(@NotNull SpeechRecognizer recognizer) {
            super(recognizer);
        }
    }

    public static class Transcribed extends SpeechRecognizerEvent implements HasRecognizer {

        private final float @NotNull [] audio;

        @NotNull
        private final String transcription;

        public Transcribed(@NotNull SpeechRecognizer recognizer, float @NotNull [] audio, @NotNull String transcription) {
            super(recognizer);
            this.audio = audio;
            this.transcription = transcription;
        }

        /**
         * @return The audio data.
         */
        @SuppressWarnings("unused")
        public float[] getAudio() {
            return this.audio;
        }

        /**
         * @return The transcription of the audio.
         */
        @SuppressWarnings("unused")
        @NotNull
        public String getTranscription() {
            return this.transcription;
        }
    }

    public static class Deregistered extends SpeechRecognizerEvent {

        /**
         * The identifiers of the deregistered recognizers.
         */
        private final Identifier[] ids;

        public Deregistered(Identifier[] ids) {
            super(null);
            this.ids = ids;
        }

        /**
         * @return The identifiers of the deregistered recognizers.
         */
        @SuppressWarnings("unused")
        public Identifier[] getIds() {
            return ids;
        }
    }
}
