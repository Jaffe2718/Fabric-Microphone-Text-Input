package io.github.jaffe2718.mcmti.event;

/**
 * Event types for speech recognizer.
 * Triggered when speech recognizer is registered, activated, deactivated, transcribes audio, or all the recognizers are deregistered with available arguments.
 * @see io.github.jaffe2718.mcmti.util.EventUtil#triggerEvent(EventType, Object...) EventUtil.triggerEvent(EventType, Object...)
 */
public enum EventType {

    /**
     * Triggered when speech recognizer is registered, default priority, actual priority and recognizer is available.
     * <br>
     * Available arguments: {@link Integer defaultPriority}, {@link Integer priority}, {@link io.github.jaffe2718.mcmti.util.SpeechRecognizer recognizer}
     */
    SPEECH_RECOGNIZER_REGISTERED,

    /**
     * Triggered when speech recognizer is activated.
     * <br>
     * Available arguments: {@link io.github.jaffe2718.mcmti.util.SpeechRecognizer recognizer}
     */
    SPEECH_RECOGNIZER_ACTIVATED,

    /**
     * Triggered when speech recognizer is deactivated.
     * <br>
     * Available arguments: {@link io.github.jaffe2718.mcmti.util.SpeechRecognizer recognizer}
     */
    SPEECH_RECOGNIZER_DEACTIVATED,

    /**
     * Triggered when speech recognizer transcribes audio.
     * <br>
     * Available arguments: {@link io.github.jaffe2718.mcmti.util.SpeechRecognizer recognizer} , {@link java.lang.Float audio[]},  {@link String transcription}
     * @see io.github.jaffe2718.mcmti.util.SpeechRecognizer#transcribe(float[]) SpeechRecognizer.transcribe(float[])
     * @see io.github.jaffe2718.mcmti.util.SpeechRecognizer#recognize(float[]) SpeechRecognizer.recognize(float[])
     */
    SPEECH_RECOGNIZER_TRANSCRIBED,

    /**
     * Triggered when all the recognizers are deregistered.
     * <br>
     * Available arguments: {@link net.minecraft.resources.Identifier ids[]} of the deregistered recognizers.
     * @see io.github.jaffe2718.mcmti.util.SpeechRecognizer#deregister() SpeechRecognizer.deregister()
     */
    ALL_SPEECH_RECOGNIZERS_DEREGISTERED


}
