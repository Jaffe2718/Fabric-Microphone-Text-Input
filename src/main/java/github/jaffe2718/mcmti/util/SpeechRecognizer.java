package github.jaffe2718.mcmti.util;

import github.jaffe2718.mcmti.client.MicrophoneTextInputClient;
import github.jaffe2718.mcmti.config.MicrophoneTextInputConfig;
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
    public final String modelPath;
    public final String grammarPath;

    public static SpeechRecognizer instance() {
        return INSTANCE;
    }

    public static void init() {
        destroy();
        if (INSTANCE == null) {
            try {
                INSTANCE = new SpeechRecognizer();
            } catch (IOException e) {
                MicrophoneTextInputClient.LOGGER.error("Failed to initialize speech recognizer", e);
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
            MicrophoneTextInputClient.LOGGER.error("Couldn't repair encoding, using default", uee);
            return str;
        }
    }

    public static @NotNull String recognize(float[] audio) {
        WhisperFullParams params = MicrophoneTextInputConfig.getParams();
        params.grammar = INSTANCE.grammar;
        int flag = WHISPER.full(INSTANCE.ctx, params, audio, audio.length);
        if (flag == 0) {
            String result = WHISPER.fullGetSegmentText(INSTANCE.ctx, 0);
            if (MicrophoneTextInputConfig.encodingRepair) {
                return repairEncoding(result, MicrophoneTextInputConfig.srcEncoding, MicrophoneTextInputConfig.dstEncoding);
            } else {
                return result;
            }
        }
        return "";
    }

    protected SpeechRecognizer() throws IOException {
        this.modelPath = MicrophoneTextInputConfig.model;
        this.grammarPath = MicrophoneTextInputConfig.advancedConfig ? MicrophoneTextInputConfig.grammar : "";
        this.ctx = WHISPER.init(Path.of(this.modelPath));
        if (!grammarPath.isEmpty()) {
            this.grammar = WHISPER.parseGrammar(grammarPath);
        } else {
            this.grammar = null;
        }
    }
}
