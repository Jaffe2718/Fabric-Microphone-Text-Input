package me.jaffe2718.mcmti.util;

import me.jaffe2718.mcmti.MicrophoneTextInput;
import me.jaffe2718.mcmti.config.McmtiConfig;
import io.github.freshsupasulley.whisperjni.WhisperContext;
import io.github.freshsupasulley.whisperjni.WhisperFullParams;
import io.github.freshsupasulley.whisperjni.WhisperGrammar;
import io.github.freshsupasulley.whisperjni.WhisperJNI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SpeechRecognizer {
    public static final WhisperJNI WHISPER = new WhisperJNI();
    private static volatile SpeechRecognizer INSTANCE;

    final @NotNull WhisperContext ctx;
    final @Nullable WhisperGrammar grammar;

    public static SpeechRecognizer instance() {
        return INSTANCE;
    }

    public synchronized static void init() {
        destroy();
        try {
            INSTANCE = new SpeechRecognizer();
        } catch (IOException e) {
            MicrophoneTextInput.LOGGER.error("Failed to initialize speech recognizer", e);
        }
    }


    @SuppressWarnings("ConstantValue")
    public synchronized static void destroy() {
        if (INSTANCE != null) {
            if (INSTANCE.ctx != null) {
                INSTANCE.ctx.close();
            }
            if (INSTANCE.grammar != null) {
                WHISPER.free(INSTANCE.grammar);
            }
            INSTANCE = null;
        }
    }

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
        if (flag == 0 && WHISPER.fullNSegments(INSTANCE.ctx) > 0) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < WHISPER.fullNSegments(INSTANCE.ctx); i++) {
                result.append(WHISPER.fullGetSegmentText(INSTANCE.ctx, i));
            }
            if (McmtiConfig.encodingRepair) {
                return repairEncoding(result.toString(), McmtiConfig.srcEncoding, McmtiConfig.dstEncoding);
            } else {
                return result.toString();
            }
        }
        return "";
    }

    private SpeechRecognizer() throws IOException {
        this.ctx = WHISPER.init(Path.of(McmtiConfig.model));
        if (Path.of(McmtiConfig.grammar).toFile().isFile()) {
            this.grammar = WHISPER.parseGrammar(Files.readString(Path.of(McmtiConfig.grammar)));
        } else {
            this.grammar = null;
        }
    }
}
