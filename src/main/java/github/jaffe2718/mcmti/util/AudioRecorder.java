package github.jaffe2718.mcmti.util;

import github.jaffe2718.mcmti.client.MicrophoneTextInputClient;
import github.jaffe2718.mcmti.config.MicrophoneTextInputConfig;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class AudioRecorder {
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
            MicrophoneTextInputClient.LOGGER.error("Failed to initialize audio recorder", e);
        }
    }

    protected AudioRecorder() throws LineUnavailableException {
        line = AudioSystem.getTargetDataLine(AUDIO_FORMAT);
        line.open(AUDIO_FORMAT);
    }

    private static float[] toFloatArray(byte[] data) {
        float[] result = new float[data.length / 2];
        ShortBuffer shortBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        for (int i = 0; i < result.length; i++) {
            result[i] = Float.max(-1f, Float.min(((float) shortBuffer.get()) / (float) Short.MAX_VALUE, 1f));
        }
        return result;
    }

    public static float @NotNull [] recordCycle() {
        INSTANCE.line.start();
        byte[] buf = new byte[MicrophoneTextInputConfig.recordCycleMs * 32];
        int read = INSTANCE.line.read(buf, 0, buf.length);
        INSTANCE.line.stop();
        INSTANCE.line.flush();
        if (read > 0) {
            return toFloatArray(buf);
        }
        return new float[0];
    }

    public static float @NotNull [] record() {
        ByteArrayOutputStream dynamicBuffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[0];
        switch (MicrophoneTextInputConfig.mode) {
            case AUTO_SEND -> throw new AssertionError("cannot record in AUTO_SEND mode");
            case RELEASE_KEY_TO_SEND -> chunk = new byte[MicrophoneTextInputConfig.recordCacheSizeSend];
            case RELEASE_KEY_TO_INPUT -> chunk = new byte[MicrophoneTextInputConfig.recordCacheSizeInput];
        }

        INSTANCE.line.start();
        while (MicrophoneTextInputClient.RECOGNIZE_KEY.isPressed()) {
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
