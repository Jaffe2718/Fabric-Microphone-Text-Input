package io.github.jaffe2718.mcmti.util;

import io.github.jaffe2718.mcmti.MicrophoneTextInput;
import io.github.jaffe2718.mcmti.config.McmtiConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public abstract class SpeechRecognizer {

    /**
     * The id of first enabled SpeechRecognizer instance.
     */
    private static volatile int instanceID = Integer.MAX_VALUE;

    private static final Text GLOBAL_UNAVAILABLE_TOAST = Text.translatable("message.mcmti.noRecognizerAvailable");

    /**
     * The recognizer registry.
     * The key is the priority of the recognizer, the value is the recognizer.
     * Smaller value means higher priority.
     * If the priority is already used, the recognizer will be registered to the next available priority.
     * @see SpeechRecognizer#instanceID
     */
    private static final Map<Integer, SpeechRecognizer> recognizerRegistry = new TreeMap<>();

    /**
     * Whether the recognizer is active.
     */
    private boolean active;

    /**
     * Check if the recognizer is enabled by config or other conditions.
     * Only the first enabled in priority will be activated and used.
     * @see SpeechRecognizer#instanceID
     * @see SpeechRecognizer#register(int, Supplier)
     * @see SpeechRecognizer#activate()
     * @see SpeechRecognizer#deactivate()
     * @return true if the recognizer is enabled, false otherwise.
     */
    public abstract boolean enabled();

    /**
     * The toast message when the recognizer is available before activation.
     * @see SpeechRecognizer#activate()
     * @return The toast message when the recognizer is available.
     */
    protected abstract @NotNull Text availableToast();

    /**
     * The toast message when the recognizer is unavailable.
     * @return The toast message when the recognizer is unavailable.
     * @see SpeechRecognizer#instanceUnavailableToast()
     */
    protected abstract @NotNull Text unavailableToast();

    /**
     * Transcribe the audio to text.
     * @param audio The audio data.
     * @return The transcribed text.
     */
    public abstract @NotNull String transcribe(float[] audio);

    /**
     * Check if the recognizer is available,
     * only the <code>transcribe(float[])</code> method of available recognizers is valid,
     * otherwise it will return empty string. If the recognizer is not available,
     * the user should check the config or other conditions.
     * @return true if the recognizer is available, false otherwise.
     * @see SpeechRecognizer#availableToast()
     * @see SpeechRecognizer#unavailableToast()
     */
    protected boolean available() {
        return this.enabled() && this.active;
    }

    /**
     * Activate the recognizer and show the toast message.
     * Override this method to load the model.
     * Remember to call <code>super.activate()</code> in the end of the override method.
     * @throws IOException If the model loading fails.
     */
    protected void activate() throws IOException {
        this.active = true;
        if (MinecraftClient.getInstance() != null
                && MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.sendMessage(this.availableToast(), true);
        }
    }

    /**
     * Deactivate the recognizer.
     * Override this method to free the model resources in priority to save RAM / vRAM.
     * Remember to call <code>super.deactivate()</code> in the subclass.
     * When you need to recognize again, call activate() to reload the model.
     * @see SpeechRecognizer#activate()
     */
    protected void deactivate() {
        this.active = false;
    }

    /**
     * Register the recognizer to the list.
     * The priority of the recognizer is the priority of the recognizer.
     * If the priority is already used, the recognizer will be registered to the next available priority.
     * Auto activate the recognizer if it is enabled and has higher priority than the current instance.
     * @param priority The priority of the recognizer, smaller value means higher priority.
     * @param constructor The constructor of the recognizer with no arguments.
     * @see SpeechRecognizer#instanceID
     * @see SpeechRecognizer#activate()
     * @see SpeechRecognizer#deactivate()
     */
    public static void register(int priority, @NonNull Supplier<? extends SpeechRecognizer> constructor) {
        SpeechRecognizer recognizer = constructor.get();
        if (recognizer == null) {
            MicrophoneTextInput.LOGGER.warn("Failed to register recognizer because the constructor returns null");
            return;
        }
        while (recognizerRegistry.containsKey(priority)) {
            priority++;
        }
        recognizerRegistry.put(priority, recognizer);
        // if the recognizer is enabled and has higher priority than the current instance
        if (priority < instanceID &&recognizer.enabled()) {
            if (recognizerRegistry.containsKey(instanceID)) {
                recognizerRegistry.get(instanceID).deactivate();
            }
            instanceID = priority;
            try {
                recognizer.activate();
            } catch (IOException ioe) {
                MicrophoneTextInput.LOGGER.error("Failed to activate recognizer", ioe);
            }
        }
    }

    /**
     * Initialize the recognizers.
     * Only the first enabled in priority will be activated.
     * Remember to call this method when registering new recognizers.
     * @see SpeechRecognizer#instanceID
     */
    public synchronized static void init() {
        boolean activeFirst = false;
        for (int priority : SpeechRecognizer.recognizerRegistry.keySet()) {
            SpeechRecognizer recognizer = recognizerRegistry.get(priority);
            if (recognizer.enabled() && !activeFirst) {
                try {
                    SpeechRecognizer.instanceID = priority;
                    activeFirst = true;
                    recognizer.activate();   // if failed, the recognizer will show toast message
                } catch (IOException ioe) {
                    MicrophoneTextInput.LOGGER.error("Failed to activate recognizer", ioe);
                }
            } else {
                recognizer.deactivate();     // deactivate other recognizers to save RAM / vRAM
            }
        }
    }

    /**
     * Release all the resources of the recognizers and clear the registry.
     */
    public synchronized static void deregister() {
        for (SpeechRecognizer recognizer : recognizerRegistry.values()) {
            recognizer.deactivate();
        }
        recognizerRegistry.clear();
    }

    /**
     * Using the first enabled recognizer to recognize the audio to text.
     * @param audio The audio data.
     * @return The recognized text.
     */
    public static @NotNull String recognize(float[] audio) {
        for (int priority : recognizerRegistry.keySet()) {
            SpeechRecognizer recognizer = recognizerRegistry.get(priority);
            if (recognizer.enabled()) {
                if (!recognizer.active) {
                    try {
                        recognizer.activate();
                    } catch (IOException ioe) {
                        MicrophoneTextInput.LOGGER.error("Failed to activate recognizer", ioe);
                        return "";
                    }
                }
                String transcription = recognizer.transcribe(audio);
                if (McmtiConfig.encodingRepair) {
                    return repairEncoding(transcription, McmtiConfig.srcEncoding, McmtiConfig.dstEncoding);
                }  else {
                    return transcription;
                }
            }
        }
        MicrophoneTextInput.LOGGER.warn("No enabled SpeechRecognizer found");
        return "";
    }

    /**
     * Check if the instance recognizer is available.
     * @return true if the instance recognizer is available, false otherwise.
     * @see io.github.jaffe2718.mcmti.event.EventSystem#showRecognizeStatus(net.minecraft.client.world.ClientWorld)
     */
    public static boolean instanceAvailable() {
        return recognizerRegistry.containsKey(instanceID) && recognizerRegistry.get(instanceID).available();
    }

    /**
     * Get the unavailable toast message of the instance recognizer.
     * @return The unavailable toast message.
     * @see SpeechRecognizer#unavailableToast()
     * @see io.github.jaffe2718.mcmti.event.EventSystem#showRecognizeStatus(net.minecraft.client.world.ClientWorld)
     */
    public static @NonNull Text instanceUnavailableToast() {
        return recognizerRegistry.containsKey(instanceID) ?
                recognizerRegistry.get(instanceID).unavailableToast() :
                GLOBAL_UNAVAILABLE_TOAST;
    }

    private static @NotNull String repairEncoding(@NotNull String str, String srcEncoding, String dstEncoding) {
        try {
            return new String(str.getBytes(srcEncoding), dstEncoding);
        } catch (UnsupportedEncodingException uee) {
            MicrophoneTextInput.LOGGER.error("Couldn't repair encoding, using default", uee);
            return str;
        }
    }
}
