package github.jaffe2718.mcmti.util;

import github.jaffe2718.mcmti.client.MicrophoneTextInput;
import github.jaffe2718.mcmti.config.McmtiConfig;
import io.github.givimad.whisperjni.WhisperContext;
import io.github.givimad.whisperjni.WhisperFullParams;
import io.github.givimad.whisperjni.WhisperGrammar;
import io.github.givimad.whisperjni.WhisperJNI;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;

public class SpeechRecognizer {
    public static final WhisperJNI WHISPER = new WhisperJNI();
    private static volatile SpeechRecognizer INSTANCE;

    final @NotNull WhisperContext ctx;
    final @Nullable WhisperGrammar grammar;
    public static String modelPath = "";
    public static String grammarPath = "";

    public static SpeechRecognizer instance() {
        return INSTANCE;
    }

    public static void init() {
        destroy();
        modelPath = McmtiConfig.model;
        grammarPath = McmtiConfig.advancedConfig ? McmtiConfig.grammar : "";
        if (INSTANCE == null) {
            try {
                INSTANCE = new SpeechRecognizer();
            } catch (IOException e) {
                MicrophoneTextInput.LOGGER.error("Failed to initialize speech recognizer", e);
            }
        }
    }

    public static void destroy() {
        if (INSTANCE != null) {
            INSTANCE.ctx.close();
            if (INSTANCE.grammar != null) {
                WHISPER.free(INSTANCE.grammar);
            }
            INSTANCE = null;
        }
    }

    @Contract("_, _, _ -> new")
    private static @NotNull String repairEncoding(@NotNull String str, String srcEncoding, String dstEncoding) {
        try {
            return new String(str.getBytes(srcEncoding), dstEncoding);
        } catch (UnsupportedEncodingException uee) {
            MicrophoneTextInput.LOGGER.error("Couldn't repair encoding, using default", uee);
            return str;
        }
    }

    public static @NotNull String recognize(float[] audio) {
        if (INSTANCE == null) return "";
        WhisperFullParams params = McmtiConfig.getParams();
        params.grammar = INSTANCE.grammar;
        int flag = WHISPER.full(INSTANCE.ctx, params, audio, audio.length);
        if (flag == 0) {
            String result = WHISPER.fullGetSegmentText(INSTANCE.ctx, 0);
            if (McmtiConfig.encodingRepair) {
                return repairEncoding(result, McmtiConfig.srcEncoding, McmtiConfig.dstEncoding);
            } else {
                return result;
            }
        }
        return "";
    }

    protected SpeechRecognizer() throws IOException {
        this.ctx = WHISPER.init(Path.of(modelPath));
        if (!grammarPath.isEmpty()) {
            this.grammar = WHISPER.parseGrammar(grammarPath);
        } else {
            this.grammar = null;
        }
    }
}
