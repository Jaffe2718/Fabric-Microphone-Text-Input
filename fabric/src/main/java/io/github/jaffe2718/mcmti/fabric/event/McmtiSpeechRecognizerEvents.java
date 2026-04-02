package io.github.jaffe2718.mcmti.fabric.event;

import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.Identifier;

public final class McmtiSpeechRecognizerEvents {

    /**
     * Called when speech recognizer is registered.
     * @see RegisterCallback
     */
    public static final Event<RegisterCallback> SPEECH_RECOGNIZER_REGISTERED = EventFactory.createArrayBacked(RegisterCallback.class,
            callbacks -> (defaultPriority, priority, recognizer) -> {
                for (RegisterCallback callback : callbacks) {
                    callback.onTriggered(defaultPriority, priority, recognizer);
                }
            });

    /**
     * Called when speech recognizer is activated.
     * @see ActivateCallback
     */
    public static final Event<ActivateCallback> SPEECH_RECOGNIZER_ACTIVATED = EventFactory.createArrayBacked(ActivateCallback.class,
            callbacks -> recognizer -> {
                for (ActivateCallback callback : callbacks) {
                    callback.onTriggered(recognizer);
                }
            });

    /**
     * Called when speech recognizer is deactivated.
     * @see DeactivateCallback
     */
    public static final Event<DeactivateCallback> SPEECH_RECOGNIZER_DEACTIVATED = EventFactory.createArrayBacked(DeactivateCallback.class,
            callbacks -> recognizer -> {
                for (DeactivateCallback callback : callbacks) {
                    callback.onTriggered(recognizer);
                }
            });

    /**
     * Called when transcribing.
     * @see TranscribeFinishedCallback
     */
    public static final Event<TranscribeFinishedCallback> SPEECH_RECOGNIZER_TRANSCRIBED = EventFactory.createArrayBacked(TranscribeFinishedCallback.class,
            callbacks -> (recognizer, audio, transcription) -> {
                for (TranscribeFinishedCallback callback : callbacks) {
                    callback.onTriggered(recognizer, audio, transcription);
                }
            });

    /**
     * Called when all speech recognizers are deregistered.
     * @see DeregisterCallback
     */
    public static final Event<DeregisterCallback> ALL_SPEECH_RECOGNIZERS_DEREGISTERED = EventFactory.createArrayBacked(DeregisterCallback.class,
            callbacks -> ids -> {
                for (DeregisterCallback callback : callbacks) {
                    callback.onTriggered(ids);
                }
            });



    @FunctionalInterface
    public interface RegisterCallback {
        /**
         * @param defaultPriority The default priority set by developer.
         * @param priority        The actual priority allocated to the recognizer.
         * @param recognizer      The recognizer that was registered.
         */
        void onTriggered(int defaultPriority, int priority, SpeechRecognizer recognizer);
    }

    @FunctionalInterface
    public interface ActivateCallback {
        /**
         * @param recognizer The recognizer that was activated.
         */
        void onTriggered(SpeechRecognizer recognizer);
    }

    @FunctionalInterface
    public interface DeactivateCallback {
        /**
         * @param recognizer The recognizer that was deactivated.
         */
        void onTriggered(SpeechRecognizer recognizer);
    }

    @FunctionalInterface
    public interface TranscribeFinishedCallback {
        /**
         * @param recognizer    The recognizer that finished transcribing.
         * @param audio         The audio data that was transcribed from.
         * @param transcription The transcription result.
         */
        void onTriggered(SpeechRecognizer recognizer, float[] audio, String transcription);
    }

    @FunctionalInterface
    public interface DeregisterCallback {
        /**
         * @param ids The ids of all the {@link SpeechRecognizer recognizers} that were deregistered.
         */
        void onTriggered(Identifier[] ids);
    }
}
