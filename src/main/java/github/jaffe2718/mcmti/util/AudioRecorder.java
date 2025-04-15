package github.jaffe2718.mcmti.util;

import github.jaffe2718.mcmti.client.MicrophoneTextInput;
import github.jaffe2718.mcmti.config.McmtiConfig;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public final class AudioRecorder {
    private static final AudioFormat AUDIO_FORMAT = new AudioFormat(16000, 16, 1, true, false);
    private static AudioRecorder INSTANCE;
    private final TargetDataLine line;

    public static void destroy() {
        if (INSTANCE != null) {
            INSTANCE.line.close();
            INSTANCE = null;
        }
    }

    public static void init() {
        destroy();
        try {
            INSTANCE = new AudioRecorder();
        } catch (LineUnavailableException e) {
            MicrophoneTextInput.LOGGER.error("Failed to initialize audio recorder", e);
        }
    }

    public static AudioRecorder instance() {
        return INSTANCE;
    }

    private AudioRecorder() throws LineUnavailableException {
        line = AudioSystem.getTargetDataLine(AUDIO_FORMAT);
        line.open(AUDIO_FORMAT);
    }

    private static float @NotNull [] toFloatArray(byte @NotNull [] data) {
        float[] result = new float[data.length / 2];
        ShortBuffer shortBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        for (int i = 0; i < result.length; i++) {
            result[i] = Float.max(-1f, Float.min(((float) shortBuffer.get()) / (float) Short.MAX_VALUE, 1f));
        }
        return result;
    }

    public static float @NotNull [] recordCycle() {
        INSTANCE.line.start();
        byte[] buf = new byte[McmtiConfig.recordCycleMs * 32];
        int read = INSTANCE.line.read(buf, 0, buf.length);
        INSTANCE.line.stop();
        INSTANCE.line.flush();
        if (read > 0) {
            return toFloatArray(buf);
        }
        return new float[0];
    }

    public static float @NotNull [] record() {
        assert McmtiConfig.mode != McmtiConfig.Mode.AUTO_SEND;
        ByteArrayOutputStream dynamicBuffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[McmtiConfig.recordBufferSize];
        INSTANCE.line.start();
        while (MicrophoneTextInput.RECOGNIZE_KEY.isPressed()) {
            int read = INSTANCE.line.read(chunk, 0, chunk.length);
            if (read > 0) {
                dynamicBuffer.write(chunk, 0, read);
            }
        }
        INSTANCE.line.stop();
        INSTANCE.line.flush();
        return toFloatArray(dynamicBuffer.toByteArray());
    }

}
