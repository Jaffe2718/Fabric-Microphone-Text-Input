package io.github.jaffe2718.qwen3asr_fabric.util;

import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import io.github.jaffe2718.qwen3asr4j.GGUFModelWrapper;
import io.github.jaffe2718.qwen3asr4j.NativeManager;
import io.github.jaffe2718.qwen3asr4j.Qwen3ASR;
import io.github.jaffe2718.qwen3asr4j.result.TranscribeResult;
import io.github.jaffe2718.qwen3asr_fabric.Qwen3ASRFabricClient;
import io.github.jaffe2718.qwen3asr_fabric.config.Qwen3ASRConfig;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Qwen3 ASR SpeechRecognizer class.
 * Download model from <a href="https://huggingface.co/Jaffe2718/Qwen3-ASR-GGUF">Qwen3-ASR-GGUF</a>.
 */
public class Qwen3ASRSpeechRecognizer extends SpeechRecognizer {

    private @Nullable Qwen3ASR ctx;

    /**
     * Override the default enabled method to check the config.
     * You can also set it to <code>true</code> simply, which means always enabled.
     * @see Qwen3ASRConfig#enabled
     * @return true if the config is enabled, false otherwise.
     */
    @Override
    public boolean enabled() {
        return Qwen3ASRConfig.enabled;
    }

    @Override
    protected @NotNull Text availableToast() {
        return Text.literal("Qwen3 ASR is loaded!").setStyle(Style.EMPTY.withColor(0x55FF55));
    }

    @Override
    protected @NotNull Text unavailableToast() {
        return Text.literal("Failed to load Qwen3 ASR model!").setStyle(Style.EMPTY.withColor(0xFF5555));
    }

    /**
     * Check if the Qwen3 ASR model is loaded and available.
     * In your project, you should override this method to check the status of SpeechRecognizer instance.
     * @return true if the model is loaded and available, false otherwise.
     */
    @Override
    protected boolean available() {
        return super.available() && this.ctx != null && this.ctx.isLoaded();
    }

    /**
     * Load the Qwen3 ASR model.
     * In your project, you should override this method to activate the SpeechRecognizer instance.
     * @throws FileNotFoundException if the model file is not found or is not a valid file.
     */
    @Override
    public void activate() throws FileNotFoundException {
        this.deactivate();
        this.ctx = new Qwen3ASR(Qwen3ASRConfig.modelPath, Qwen3ASRFabricClient.LOGGER);
        try {
            super.activate();
        } catch (IOException ignored) {}
    }

    /**
     * Close the Qwen3 ASR model.
     * In your project, you should override this method to deactivate the SpeechRecognizer instance.
     * Timely deactivation of idle SpeechRecognizer instance can save RAM and vRAM.
     * @see SpeechRecognizer#deactivate()
     * @see SpeechRecognizer#init()
     * @see SpeechRecognizer#recognize(float[])
     */
    @Override
    public void deactivate() {
        if (this.ctx != null) {
            this.ctx.close();
        }
        this.ctx = null;
        super.deactivate();
    }

    /**
     * Transcribe the audio to text.
     * In your project, you should override this method to transcribe the audio to text.
     * @param audio the audio data.
     * @return the transcribed text.
     */
    @Override
    public @NotNull String transcribe(float[] audio) {
        if (this.ctx != null && this.ctx.isLoaded()) {
            TranscribeResult result = this.ctx.transcribe(audio, Qwen3ASRConfig.getQwenParams());
            if (result.errorMsg().isBlank()) {
                return result.text();
            } else {
                Qwen3ASRFabricClient.LOGGER.error("Qwen3 ASR error: {}", result.errorMsg());
            }
        }
        return "";
    }

    /**
     * Load native library for Qwen3 ASR.
     * @return true if loaded successfully, false otherwise.
     */
    public static boolean loadNativeLibrary() {
        try {
            if (Qwen3ASRConfig.customLibrary) {
                NativeManager.loadLibrary(Paths.get(Qwen3ASRConfig.customLibraryDir), Qwen3ASRFabricClient.LOGGER);
            } else {
                NativeManager.loadLibrary(Qwen3ASRFabricClient.LOGGER);
            }
        } catch (Exception ignored) {
            return false;
        }
        GGUFModelWrapper.setGGMLGlobalLogger(Qwen3ASRFabricClient.LOGGER);
        return true;
    }
}
