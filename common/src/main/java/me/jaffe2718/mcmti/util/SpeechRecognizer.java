package me.jaffe2718.mcmti.util;

import me.jaffe2718.mcmti.MicrophoneTextInput;
import me.jaffe2718.mcmti.config.McmtiConfig;
import io.github.jaffe2718.whisperjni.WhisperContext;
import io.github.jaffe2718.whisperjni.WhisperFullParams;
import io.github.jaffe2718.whisperjni.WhisperGrammar;
import io.github.jaffe2718.whisperjni.WhisperJNI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
            if (MinecraftClient.getInstance() != null
                    && MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("message.mcmti.whisperModelLoading"), true);
            }
            INSTANCE = new SpeechRecognizer();
            if (MinecraftClient.getInstance() != null
                    && MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("message.mcmti.whisperModelLoaded"), true);
            }
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
        WhisperFullParams params = McmtiConfig.wFullParams;
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

    /**
     * Send a message to the chat, split it into multiple messages if necessary.
     * Due to the limitation of the chat message length in Minecraft,
     * the message will be split into multiple parts with prefix and not longer than 256 characters.
     *
     * @param player  The player to send the message.
     * @param message The message to send.
     */
    public static void sendChatMessage(@NotNull ClientPlayerEntity player, @NotNull String message) {
        final int maxLength = 256 - McmtiConfig.prefix.length();
        while (message.length() > maxLength) {
            player.networkHandler.sendChatMessage(McmtiConfig.prefix + message.substring(0, maxLength));
            message = message.substring(maxLength);
        }
        if (!message.isEmpty()) {            // send the rest
            player.networkHandler.sendChatMessage(McmtiConfig.prefix + message);
        }
    }

    private SpeechRecognizer() throws IOException {
        @NotNull WhisperContext whisperContext;
        try (InputStream modelIn = new URI(McmtiConfig.model).toURL().openStream()) {
            whisperContext = WHISPER.init(modelIn);
        } catch (IllegalArgumentException | MalformedURLException | URISyntaxException e) {
            whisperContext = WHISPER.init(Path.of(McmtiConfig.model));
        }
        this.ctx = whisperContext;
        if (Path.of(McmtiConfig.grammar).toFile().isFile()) {
            this.grammar = WHISPER.parseGrammar(Files.readString(Path.of(McmtiConfig.grammar)));
        } else {
            this.grammar = null;
        }
    }
}
