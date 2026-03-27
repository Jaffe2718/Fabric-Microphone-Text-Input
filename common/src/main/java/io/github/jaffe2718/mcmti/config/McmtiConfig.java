package io.github.jaffe2718.mcmti.config;

import eu.midnightdust.lib.config.MidnightConfig;
import eu.midnightdust.lib.util.PlatformFunctions;
import io.github.jaffe2718.whisperjni.LibraryUtils;
import io.github.jaffe2718.whisperjni.WhisperFullParams;
import io.github.jaffe2718.mcmti.MicrophoneTextInput;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import io.github.jaffe2718.mcmti.util.whisper.WhisperSpeechRecognizer;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class McmtiConfig extends MidnightConfig {

    /**
     * Called when the config is saved.
     * Use this to reload the whisper model & grammar
     * Reflash whisper full params when config is saved
     */
    @Override
    public void writeChanges() {
        super.writeChanges();
        Thread.ofVirtual().start(SpeechRecognizer::init);
    }

    public enum Mode {
        AUTO_SEND,
        RELEASE_KEY_TO_SEND,
        RELEASE_KEY_TO_INPUT,
    }

    /**
     * Whisper sampling strategy, BEAM_SEARCH is the default
     */
    public enum SamplingStrategy {
        GREEDY,
        BEAM_SEARCH,
    }

    /**
     * Whisper voice activity detection <br>
     * <br>DISABLED: No voice activity detection
     * <br>BUILT_IN: Built-in ggml-silero-v6.2.0.bin voice activity detection
     * <br>CUSTOM: Custom voice activity detection, path required
     */
    public enum WhisperVad {
        DISABLED,
        BUILT_IN,
        CUSTOM,
    }

    /**
     * Whisper model path or url
     */
    @Entry(category = "general", selectionMode = JFileChooser.FILES_ONLY, width = 4096, fileExtensions = {"bin", "ggml", "gguf"})
    public static String model = "https://huggingface.co/ggerganov/whisper.cpp/resolve/main/ggml-base.bin";

    @Entry(category = "general", width = 15)
    public static String language = "en";

    @Entry(category = "general")
    public static Mode mode = Mode.RELEASE_KEY_TO_SEND;

    @Entry(category = "general", min = 1024, max = 65536)
    @Condition(requiredOption = "mode", requiredValue = "AUTO_SEND")
    public static int recordCycleMs = 5000;    // unit: ms, (sampleRate = 16000Hz), default: 5s

    @Entry(category = "general", min = 64, max = 4096)
    @Condition(requiredOption = "mode", requiredValue = {"RELEASE_KEY_TO_SEND", "RELEASE_KEY_TO_INPUT"})
    public static int recordBufferSize = 1024;    // unit: byte, default: 1024 bytes

    @Entry(category = "general")
    @Condition(requiredOption = "mode", requiredValue = "RELEASE_KEY_TO_INPUT")
    public static boolean draftInput = false;

    @Entry(category = "general", width = 64)
    public static String prefix = "⌈Speech Input⌋";

    @Entry(category = "general")
    public static boolean encodingRepair = false;

    @Entry(category = "general")
    @Condition(requiredOption = "encodingRepair")
    public static String srcEncoding = Charset.defaultCharset().displayName();   // use system encoding as default

    @Entry(category = "general")
    @Condition(requiredOption = "encodingRepair")
    public static String dstEncoding = Charset.defaultCharset().displayName();   // use system encoding as default

    @ApiStatus.Experimental
    @Entry(category = "advanced")
    public static boolean advancedConfig = false;

    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static boolean useCustomDynamicLib = false;

    @Entry(category = "advanced", width = 4096, selectionMode = JFileChooser.DIRECTORIES_ONLY)
    @Condition(requiredOption = "advancedConfig")
    @Condition(requiredOption = "useCustomDynamicLib")
    public static String customDynamicLibDir = "";

    /**
     * Number of thread, 0 for max cores
     */
    @Entry(category = "advanced", min = 0)
    @Condition(requiredOption = "advancedConfig")
    public static int nThreads = 0;

    /**
     * Overwrite the audio context size (0 = use default)
     */
    @Entry(category = "advanced", min = 0)
    @Condition(requiredOption = "advancedConfig")
    public static int audioCtx;

    /**
     * Max tokens to use from past text as prompt for the decoder
     */
    @Entry(category = "advanced", min = 1024)
    @Condition(requiredOption = "advancedConfig")
    public static int nMaxTextCtx = 16384;

    /**
     * Start offset in ms
     */
    @Entry(category = "advanced", min = 0)
    @Condition(requiredOption = "advancedConfig")
    public static int offsetMs;

    /**
     * Audio duration to process in ms
     */
    @Entry(category = "advanced", min = 0)
    @Condition(requiredOption = "advancedConfig")
    public static int durationMs;

    /**
     * Translate
     */
    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static boolean translate;

    /**
     * Do not generate timestamps
     */
    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static boolean noTimestamps;

    /**
     * Initial prompt
     */
    @Entry(category = "advanced", width = 4096)
    @Condition(requiredOption = "advancedConfig")
    public static String initialPrompt = "";

    /**
     * Do not use past transcription (if any) as initial prompt for the decoder
     */
    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static boolean noContext = true;

    /**
     * Force single segment output (useful for streaming)
     */
    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static boolean singleSegment;

    /**
     * Print special tokens
     */
    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static boolean printSpecial;

    /**
     * Decoder option
     */
    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static boolean suppressBlank = true;

    /**
     * Tokenizer option
     */
    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static boolean suppressNonSpeechTokens = true;

    /**
     * Initial decoding temperature
     */
    @Entry(category = "advanced", min = 0f, max = 2f, isSlider = true, precision = 200)
    @Condition(requiredOption = "advancedConfig")
    public static float temperature = 0.0f;

    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static float maxInitialTs = 1.0f;

    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static float lengthPenalty = -1.0f;

    @Entry(category = "advanced", min = 0f)
    @Condition(requiredOption = "advancedConfig")
    public static float temperatureInc =   0.4f;

    @Entry(category = "advanced", min = 0f)
    @Condition(requiredOption = "advancedConfig")
    public static float entropyThold =   2.4f;

    @Entry(category = "advanced", max = 0f)
    @Condition(requiredOption = "advancedConfig")
    public static float logprobThold =  -1.0f;

    @Entry(category = "advanced", min = 0f, max = 1f, isSlider = true, precision = 200)
    @Condition(requiredOption = "advancedConfig")
    public static float noSpeechThold =   0.6f;


    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static SamplingStrategy whisperSamplingStrategy = SamplingStrategy.BEAM_SEARCH;

    /**
     * Specific to greedy sampling strategy
     */
    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    @Condition(requiredOption = "whisperSamplingStrategy", requiredValue = "GREEDY")
    public static int greedyBestOf = -1;

    /**
     * Specific to bean search sampling strategy
     */
    @Entry(category = "advanced", min = 1)
    @Condition(requiredOption = "advancedConfig")
    @Condition(requiredOption = "whisperSamplingStrategy", requiredValue = "BEAM_SEARCH")
    public static int beamSearchBeamSize = 2;

    /**
     * Specific to bean search sampling strategy
     */
    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    @Condition(requiredOption = "whisperSamplingStrategy", requiredValue = "BEAM_SEARCH")
    public static float beamSearchPatience = -1.0f;

    @Entry(category = "advanced", selectionMode = JFileChooser.FILES_ONLY, fileExtensions = {"gbnf"})
    @Condition(requiredOption = "advancedConfig")
    public static String grammar = "";        // No grammar if the field is empty

    /**
     * Penalty for non grammar tokens.
     */
    @Entry(category = "advanced", min = 0)
    @Condition(requiredOption = "advancedConfig")
    public static float grammarPenalty = 100f;

    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static WhisperVad vad = WhisperVad.DISABLED;

    @Entry(category = "advanced", selectionMode = JFileChooser.FILES_ONLY, width = 4096, fileExtensions = {"bin", "ggml", "gguf"})
    @Condition(requiredOption = "advancedConfig")
    @Condition(requiredOption = "vad", requiredValue = "CUSTOM")
    public static String vad_model_path = "";

    @Entry(category = "advanced", min = 0f, max = 1f, isSlider = true, precision = 200)
    @Condition(requiredOption = "advancedConfig")
    @Condition(requiredOption = "vad", requiredValue = {"BUILT_IN", "CUSTOM"})
    public static float vad__threshold = 0.5f;

    @Entry(category = "advanced", min = 0)
    @Condition(requiredOption = "advancedConfig")
    @Condition(requiredOption = "vad", requiredValue = {"BUILT_IN", "CUSTOM"})
    public static int vad__min_speech_duration_ms = 250;

    @Entry(category = "advanced", min = 0)
    @Condition(requiredOption = "advancedConfig")
    @Condition(requiredOption = "vad", requiredValue = {"BUILT_IN", "CUSTOM"})
    public static int vad__min_silence_duration_ms = 100;

    @Entry(category = "advanced", min = 0f)
    @Condition(requiredOption = "advancedConfig")
    @Condition(requiredOption = "vad", requiredValue = {"BUILT_IN", "CUSTOM"})
    public static float vad__max_speech_duration_s = Float.MAX_VALUE;

    @Entry(category = "advanced", min = 0)
    @Condition(requiredOption = "advancedConfig")
    @Condition(requiredOption = "vad", requiredValue = {"BUILT_IN", "CUSTOM"})
    public static int vad__speech_pad_ms = 30;

    @Entry(category = "advanced", min = 0f)
    @Condition(requiredOption = "advancedConfig")
    @Condition(requiredOption = "vad", requiredValue = {"BUILT_IN", "CUSTOM"})
    public static float vad__samples_overlap = 0.1f;

    /**
     * Get whisper full params
     * @return WhisperFullParams
     * @see WhisperFullParams
     */
    public static @NotNull WhisperFullParams getParams() {
        WhisperFullParams params;
        if (advancedConfig) {
            params = new WhisperFullParams(whisperSamplingStrategy.ordinal());
            params.nThreads = nThreads;
            params.audioCtx = audioCtx;
            params.nMaxTextCtx = nMaxTextCtx;
            params.offsetMs = offsetMs;
            params.temperature = temperature;
            params.maxInitialTs = maxInitialTs;
            params.lengthPenalty = lengthPenalty;
            params.temperatureInc = temperatureInc;
            params.entropyThold = entropyThold;
            params.logprobThold = logprobThold;
            params.noSpeechThold = noSpeechThold;
            params.greedyBestOf = greedyBestOf;
            params.beamSearchBeamSize = beamSearchBeamSize;
            params.beamSearchPatience = beamSearchPatience;
            try {
                params.grammar = WhisperSpeechRecognizer.WHISPER.parseGrammar(Files.readString(Path.of(McmtiConfig.grammar)));
            } catch (IOException | UnsatisfiedLinkError ignored) {}
            params.grammarPenalty = grammarPenalty;
            params.suppressNonSpeechTokens = suppressNonSpeechTokens;
            params.suppressBlank = suppressBlank;
            params.printSpecial = printSpecial;
            params.singleSegment = singleSegment;
            params.initialPrompt = initialPrompt.isBlank() ? null : initialPrompt;
            params.noContext = noContext;
            params.translate = translate;
            params.noTimestamps = noTimestamps;
            params.durationMs = durationMs;
            params.vad = vad != WhisperVad.DISABLED;
            if (params.vad) {
                if (vad == WhisperVad.BUILT_IN) {
                    params.vad_model_path = extractBuiltinVad();
                } else if (vad == WhisperVad.CUSTOM && !vad_model_path.isBlank() && new File(vad_model_path).isFile()) {
                    params.vad_model_path = vad_model_path;
                } else {
                    params.vad = false;           // Disable VAD if no valid model path
                    params.vad_model_path = null;
                }
                params.vadParams.threshold = vad__threshold;
                params.vadParams.min_speech_duration_ms = vad__min_speech_duration_ms;
                params.vadParams.min_silence_duration_ms = vad__min_silence_duration_ms;
                params.vadParams.max_speech_duration_s = vad__max_speech_duration_s;
                params.vadParams.speech_pad_ms = vad__speech_pad_ms;
                params.vadParams.samples_overlap = vad__samples_overlap;
            }
        } else {
            params = new WhisperFullParams();
            params.suppressBlank = true;
            params.suppressNonSpeechTokens = true;
        }
        params.language = language;
        return params;
    }

    private static boolean builtinVadExtracted = false;

    /**
     * Extract builtin vad model if not exists
     * <a href="https://huggingface.co/ggml-org/whisper-vad/blob/main/ggml-silero-v6.2.0.bin"><br>ggml-silero-v6.2.0.bin<br></a>
     */
    private static @NotNull String extractBuiltinVad() {
        Path vadPath = PlatformFunctions.getConfigDirectory().resolve("ggml-silero-v6.2.0.bin");
        if (!builtinVadExtracted) {
            boolean valid = vadPath.toFile().exists() && vadPath.toFile().isFile();
            if (valid) {    // get sha256
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                    messageDigest.update(FileUtils.readFileToByteArray(vadPath.toFile()));
                    byte[] sha256b = messageDigest.digest();
                    StringBuilder sha256Str = new StringBuilder();
                    for (byte b : sha256b) {
                        sha256Str.append(String.format("%02x", 0xff & b));
                    }
                    valid = "2aa269b785eeb53a82983a20501ddf7c1d9c48e33ab63a41391ac6c9f7fb6987".contentEquals(sha256Str);
                } catch (NoSuchAlgorithmException | IOException ignored) {
                    valid = false;
                }
            }
            if (!valid) {
                try {
                    FileUtils.deleteQuietly(vadPath.toFile());
                    LibraryUtils.exportVADModel(MicrophoneTextInput.LOGGER, vadPath);
                } catch (IOException ignored) {}
            }
            builtinVadExtracted = true;
        }
        return vadPath.toString();
    }
}
