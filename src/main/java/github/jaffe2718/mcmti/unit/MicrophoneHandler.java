package github.jaffe2718.mcmti.unit;

import github.jaffe2718.mcmti.config.ConfigUI;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

/**
 * This class is used to handle the recording of audio data from the microphone.
 * @author Jaffe2718*/
public class MicrophoneHandler {
    private final TargetDataLine line;             // The line that reads the audio data from the microphone
    public final int sampleRate;                   // The sample rate of the microphone

    public MicrophoneHandler(int sampleRate) throws Exception {
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
