package io.github.jaffe2718.mcmti.util.whisper;

import io.github.jaffe2718.whisperjni.LibraryUtils;
import io.github.jaffe2718.whisperjni.WhisperContext;
import io.github.jaffe2718.whisperjni.WhisperFullParams;
import io.github.jaffe2718.whisperjni.WhisperJNI;
import io.github.jaffe2718.mcmti.MicrophoneTextInput;
import io.github.jaffe2718.mcmti.config.McmtiConfig;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

/**
 * The Whisper speech recognizer. (Default)
 * Always enabled with the lowest priority.
 */
public class WhisperSpeechRecognizer extends SpeechRecognizer {

    public static final WhisperJNI WHISPER = new WhisperJNI();

    private @Nullable WhisperContext ctx;

    /**
     * The constructor of the recognizer.
     * @param regId The id of the recognizer when registered.
     */
    public WhisperSpeechRecognizer(@NotNull Identifier regId) {
        super(regId);
    }

    /**
     * @return true means always enabled.
     */
    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected @NonNull Text availableToast() {
        return Text.translatable("message.mcmti.whisperModelLoaded");
    }

    @Override
    protected @NonNull Text unavailableToast() {
        return Text.translatable("message.mcmti.whisperModelLoadFailed");
    }

    @Override
    protected boolean available() {
        return super.available() && this.ctx != null;
    }

    /**
     * Activate the recognizer, load whisper model.
     * @throws IOException If the model loading fails.
     */
    @Override
    protected void activate() throws IOException {
        if (this.ctx != null) {
            this.deactivate(true);
        }
        @NotNull WhisperContext whisperContext;
        try (InputStream modelIn = new URI(McmtiConfig.model).toURL().openStream()) {
            whisperContext = WHISPER.init(modelIn);
        } catch (IllegalArgumentException | MalformedURLException | URISyntaxException e) {
            whisperContext = WHISPER.init(Path.of(McmtiConfig.model));
        }
        this.ctx = whisperContext;
        MicrophoneTextInput.LOGGER.info("Whisper model loaded");
        super.activate();
    }

    /**
     * Deactivate the recognizer, release whisper model.
     */
    @Override
    protected void deactivate() {
        if (this.ctx != null) {
            WHISPER.free(this.ctx);
        }
        this.ctx = null;
        super.deactivate();
    }

    /**
     * @param reload Whether to reload the model after releasing the context, will affect the log output.
     */
    @SuppressWarnings("SameParameterValue")
    private void deactivate(boolean reload) {
        this.deactivate();
        if (reload) {
            MicrophoneTextInput.LOGGER.info("Reloading whisper model...");
        } else {
            MicrophoneTextInput.LOGGER.info("Whisper model released");
        }
    }

    /**
     * Recognize the audio to text.
     * @param audio The audio data.
     * @return The recognized text.
    */
    @Override
    public @NotNull String transcribe(float[] audio) {
        if (this.available()) {
            WhisperFullParams params = McmtiConfig.getParams();
            int flag = WHISPER.full(this.ctx, params, audio, audio.length);
            if (flag == 0 && WHISPER.fullNSegments(this.ctx) > 0) {
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < WHISPER.fullNSegments(this.ctx); i++) {
                    result.append(WHISPER.fullGetSegmentText(this.ctx, i));
                }
                return result.toString();
            }
        }
        return "";
    }

    /**
     * Load the whisper native library.
     * Called once at the beginning of the mod's lifecycle.
     * @return true if the library is loaded successfully, false otherwise.
     * @see MicrophoneTextInput#init()
     */
    public static boolean loadLibrary() {
        try {
            if (McmtiConfig.advancedConfig && McmtiConfig.useCustomDynamicLib) {
                LibraryUtils.findAndLoadVulkanRuntime();
                LibraryUtils.loadLibrary(MicrophoneTextInput.LOGGER, Path.of(McmtiConfig.customDynamicLibDir));
            } else {
                WhisperSpeechRecognizer.WHISPER.loadLibrary(MicrophoneTextInput.LOGGER);
            }
            WhisperJNI.setLogger(MicrophoneTextInput.LOGGER);
        } catch (Exception e) {
            MicrophoneTextInput.LOGGER.error("Failed to load library", e);
            MicrophoneTextInput.LOGGER.warn("Using default library");
            try {
                WhisperSpeechRecognizer.WHISPER.loadLibrary(MicrophoneTextInput.LOGGER);
            } catch (IOException ioe) {
                MicrophoneTextInput.LOGGER.error("Failed to load library", ioe);
                return false;
            }
        }
        return true;
    }
}
