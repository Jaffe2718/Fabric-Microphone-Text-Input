package github.jaffe2718.mcmti.unit;

import github.jaffe2718.mcmti.client.MicrophoneTextInputClient;
import github.jaffe2718.mcmti.config.ConfigUI;

import javax.sound.sampled.*;

/**
 * This class is used to handle the recording of audio data from the microphone.
 * @author Jaffe2718
 * */
public class MicrophoneHandler {

    private static volatile MicrophoneHandler INSTANCE;
    private static volatile int SAMPLE_RATE = 0;

    private final TargetDataLine line;             // The line that reads the audio data from the microphone
    public final int sampleRate;                   // The sample rate of the microphone

    public static void init() {
        SAMPLE_RATE = ConfigUI.sampleRate;
        close();
        try {
            INSTANCE = new MicrophoneHandler(ConfigUI.sampleRate);
            INSTANCE.startListening();
            MicrophoneTextInputClient.LOGGER.info("Microphone handler initialized successfully! (sample-rate: " + SAMPLE_RATE + ")");
        } catch (LineUnavailableException lue) {
            MicrophoneTextInputClient.LOGGER.error("Failed to initialize microphone handler.", lue);
        }
    }

    public static void close() {
        if (INSTANCE != null) {
            INSTANCE.stopListening();
            INSTANCE.line.close();
            INSTANCE = null;
        }
    }

    public static boolean statusChanged() {
        return SAMPLE_RATE != ConfigUI.sampleRate;
    }

    public static MicrophoneHandler instance() {
        return INSTANCE;
    }

    protected MicrophoneHandler(int sampleRate) throws LineUnavailableException {
        this.sampleRate = sampleRate;
        AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
        this.line = (TargetDataLine) AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, format));
        line.open(format);
    }

    public void startListening() {         // Start recording audio
        line.start();
    }

    public void stopListening() {         // Stop recording audio
        line.stop();
        line.close();
    }

    public byte[] readData() {           // Read audio data
        byte[] buffer = new byte[ConfigUI.cacheSize];        // The size of the cache used to store audio data, use bytes as the unit, the default is 4096 bytes
        int count = line.read(buffer, 0, buffer.length);
        if (count > 0) {
            return buffer;
        } else {
            return new byte[0];
        }
    }

}
