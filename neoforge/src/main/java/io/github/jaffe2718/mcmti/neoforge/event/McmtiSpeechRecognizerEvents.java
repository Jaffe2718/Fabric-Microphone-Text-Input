package io.github.jaffe2718.mcmti.neoforge.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.minecraft.util.Identifier;

public interface McmtiSpeechRecognizerEvents {

    /**
     * Called when speech recognizer is registered.
     * @see RegisterCallback
     */
    Event<RegisterCallback> SPEECH_RECOGNIZER_REGISTERED = EventFactory.createLoop();

    /**
     * Called when speech recognizer is activated.
     * @see ActivateCallback
     */
    Event<ActivateCallback> SPEECH_RECOGNIZER_ACTIVATED = EventFactory.createLoop();

    /**
     * Called when speech recognizer is deactivated.
     * @see DeactivateCallback
     */
    Event<DeactivateCallback> SPEECH_RECOGNIZER_DEACTIVATED = EventFactory.createLoop();
    /**
     * Called when transcribing.
     * @see TranscribeFinishedCallback
     */
    Event<TranscribeFinishedCallback> SPEECH_RECOGNIZER_TRANSCRIBED = EventFactory.createLoop();

    /**
     * Called when all speech recognizers are deregistered.
     * @see DeregisterCallback
     */
    Event<DeregisterCallback> ALL_SPEECH_RECOGNIZERS_DEREGISTERED = EventFactory.createLoop();


    @FunctionalInterface
    interface RegisterCallback {
        /**
         * @param defaultPriority The default priority set by developer.
         * @param priority        The actual priority allocated to the recognizer.
         * @param recognizer      The recognizer that was registered.
         */
        void onTriggered(int defaultPriority, int priority, SpeechRecognizer recognizer);
    }

    @FunctionalInterface
    interface ActivateCallback {
        /**
         * @param recognizer The recognizer that was activated.
         */
        void onTriggered(SpeechRecognizer recognizer);
    }

    @FunctionalInterface
    interface DeactivateCallback {
        /**
         * @param recognizer The recognizer that was deactivated.
         */
        void onTriggered(SpeechRecognizer recognizer);
    }

    @FunctionalInterface
    interface TranscribeFinishedCallback {
        /**
         * @param recognizer    The recognizer that finished transcribing.
         * @param audio         The audio data that was transcribed from.
         * @param transcription The transcription result.
         */
        void onTriggered(SpeechRecognizer recognizer, float[] audio, String transcription);
    }

    @FunctionalInterface
    interface DeregisterCallback {
        /**
         * @param ids The ids of all the {@link SpeechRecognizer recognizers} that were deregistered.
         */
        void onTriggered(Identifier[] ids);
    }
}
