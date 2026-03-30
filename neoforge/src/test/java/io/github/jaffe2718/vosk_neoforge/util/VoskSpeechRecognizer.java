package io.github.jaffe2718.vosk_neoforge.util;

import com.google.gson.JsonParser;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import io.github.jaffe2718.vosk_neoforge.VoskNeoForge;
import io.github.jaffe2718.vosk_neoforge.config.VoskConfig;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vosk.Model;
import org.vosk.Recognizer;

import java.io.IOException;

public class VoskSpeechRecognizer extends SpeechRecognizer {

    private @Nullable Recognizer ctx;

    @Override
    public boolean enabled() {
        return VoskConfig.CONFIG.enabled.get();
    }

    @Override
    protected @NotNull Text availableToast() {
        return Text.literal("Vosk Speech Recognizer is loaded.").withColor(0x55FF55);
    }

    @Override
    protected @NotNull Text unavailableToast() {
        return Text.literal("Vosk Speech Recognizer is not loaded.").withColor(0xFF5555);
    }

    @Override
    public @NotNull String transcribe(float[] audio) {
        if (this.ctx != null) {
            short[] shortAudio = new short[audio.length];
            for (int i = 0; i < audio.length; i++) {
                shortAudio[i] = (short) (audio[i] * Short.MAX_VALUE);
            }
            this.ctx.acceptWaveForm(shortAudio, shortAudio.length);
            return JsonParser.parseString(this.ctx.getFinalResult()).getAsJsonObject().get("text").getAsString();
        }
        return "";
    }

    @Override
    protected boolean available() {
        return super.available() && this.ctx != null;
    }

    @Override
    protected void activate() throws IOException {
        this.deactivate();
        this.ctx = new Recognizer(new Model(VoskConfig.CONFIG.voskModelDir.get()), 16000);
        super.activate();
        VoskNeoForge.LOGGER.info("Vosk Speech Recognizer is activated.");
    }

    protected void deactivate() {
        if (this.ctx != null) {
            this.ctx.close();
        }
        this.ctx = null;
        super.deactivate();
    }
}
