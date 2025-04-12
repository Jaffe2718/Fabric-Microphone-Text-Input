package github.jaffe2718.mcmti.config;

import eu.midnightdust.lib.config.MidnightConfig;
import io.github.givimad.whisperjni.WhisperFullParams;
import io.github.givimad.whisperjni.WhisperSamplingStrategy;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.nio.charset.Charset;

public class McmtiConfig extends MidnightConfig {

    public enum Mode {
        AUTO_SEND,
        RELEASE_KEY_TO_SEND,
        RELEASE_KEY_TO_INPUT,
    }

    @Entry(category = "general", selectionMode = JFileChooser.FILES_ONLY, width = 4096, fileExtensions = {"bin", "ggml"})
    public static String model = "";

    @Entry(category = "general", width = 15)
    public static String language = "en";

    @Entry(category = "general")
    public static Mode mode = Mode.RELEASE_KEY_TO_SEND;

    @Entry(category = "general", min = 1024, max = 65536)
    @Condition(requiredOption = "mode", requiredValue = "AUTO_SEND")
    public static int recordCycleMs = 5000;    // unit: ms, (sampleRate = 16000Hz), default: 5s

    @Entry(category = "general", min = 64, max = 4096)
//    @Condition(requiredOption = "mode", requiredValue = {"RELEASE_KEY_TO_SEND", "RELEASE_KEY_TO_INPUT"})
    public static int recordBufferSize = 1024;    // unit: byte, default: 1024 bytes

    @Entry(category = "general")
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

    @Entry(category = "advanced", selectionMode = JFileChooser.DIRECTORIES_ONLY, width = 4096)
    @Condition(requiredOption = "advancedConfig")
    public static String whisperjniLibdir = "";               // empty for default

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
     * Detect language
     */
    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static boolean detectLanguage;

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
     * Print progress information
     */
    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static boolean printProgress = true;

    /**
     * Print results from within whisper.cpp (avoid it, use callback instead)
     */
    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static boolean printRealtime;

    /**
     * Print timestamps for each text segment when printing realtime
     */
    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static boolean printTimestamps = true;

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

    /**
     * Specific to greedy sampling strategy
     */
    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
    public static int greedyBestOf = -1;

    /**
     * Specific to bean search sampling strategy
     */
    @Entry(category = "advanced", min = 1)
    @Condition(requiredOption = "advancedConfig")
    public static int beamSearchBeamSize = 2;

    /**
     * Specific to bean search sampling strategy
     */
    @Entry(category = "advanced")
    @Condition(requiredOption = "advancedConfig")
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
    public static WhisperSamplingStrategy whisperSamplingStrategy = WhisperSamplingStrategy.BEAN_SEARCH;


    public static @NotNull WhisperFullParams getParams() {
        WhisperFullParams params;
        if (advancedConfig) {
            params = new WhisperFullParams(whisperSamplingStrategy);
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
            params.grammarPenalty = grammarPenalty;
            params.suppressNonSpeechTokens = suppressNonSpeechTokens;
            params.suppressBlank = suppressBlank;
            params.printTimestamps = printTimestamps;
            params.printProgress = printProgress;
            params.printRealtime = printRealtime;
            params.printSpecial = printSpecial;
            params.singleSegment = singleSegment;
            params.initialPrompt = initialPrompt.isBlank() ? null : initialPrompt;
            params.noContext = noContext;
            params.translate = translate;
            params.noTimestamps = noTimestamps;
            params.detectLanguage = detectLanguage;
            params.durationMs = durationMs;
        } else {
            params = new WhisperFullParams();
            params.suppressBlank = true;
            params.suppressNonSpeechTokens = true;
        }
        params.language = language;
        return params;
    }
}
