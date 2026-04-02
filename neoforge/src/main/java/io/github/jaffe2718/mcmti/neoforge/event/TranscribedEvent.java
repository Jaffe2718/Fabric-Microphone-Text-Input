package io.github.jaffe2718.mcmti.neoforge.event;

import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import org.jetbrains.annotations.NotNull;

public class TranscribedEvent extends SpeechRecognizerEvent {

    private final float @NotNull [] audio;

    @NotNull
    private final String transcription;

    public TranscribedEvent(@NotNull SpeechRecognizer recognizer, float @NotNull [] audio, @NotNull String transcription) {
        super(recognizer);
        this.audio = audio;
        this.transcription = transcription;
    }

    public float[] getAudio() {
        return this.audio;
    }

    @NotNull
    public String getTranscription() {
        return this.transcription;
    }
}
