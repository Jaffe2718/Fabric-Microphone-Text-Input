package github.jaffe2718.mcmti.unit;

import com.google.gson.JsonParser;
import github.jaffe2718.mcmti.client.MicrophoneTextInputClient;
import github.jaffe2718.mcmti.config.ConfigUI;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.vosk.Model;
import org.vosk.Recognizer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Speech recognition class.
 * This class is used to recognize the speech in the game thread.
 * @author Jaffe2718
 */
public class SpeechRecognizer {

    private static volatile SpeechRecognizer INSTANCE;
    private static volatile String MODEL_PATH = "";

    public final Recognizer recognizer;
    public final int sampleRate;
    public final String acousticModelPath;

    public static void init() {
        MODEL_PATH = ConfigUI.acousticModelPath;
        close();
        try {
            INSTANCE = new SpeechRecognizer(ConfigUI.acousticModelPath, ConfigUI.sampleRate);
            MicrophoneTextInputClient.LOGGER.info("Acoustic model loaded successfully! (path: " + MODEL_PATH + ", sample-rate: " + INSTANCE.sampleRate + ")");
        } catch (IOException ie) {
            MicrophoneTextInputClient.LOGGER.error("Failed to load acoustic model, please check the path and try again.", ie);
        }
    }

    public static void close() {
        if (INSTANCE != null) {
            INSTANCE.recognizer.close();
            INSTANCE = null;
        }
    }

    public static boolean statusChanged() {
        if (!ConfigUI.acousticModelPath.equals(MODEL_PATH)) return true;
        return MicrophoneHandler.statusChanged();
    }

    public static SpeechRecognizer instance() {
        return INSTANCE;
    }

    protected SpeechRecognizer(String acousticModelPath, int sampleRate) throws IOException {
        this.sampleRate = sampleRate;
        this.acousticModelPath = acousticModelPath;
        Model model = new Model(acousticModelPath);
        this.recognizer = new Recognizer(model, sampleRate);

    }

    /**
     * Get the text from the audio data
     * @param data Audio data
     * @return {@link java.lang.String} message
     */
    public String getStringMsg(byte[] data) {
        return recognizer.acceptWaveForm(data, data.length) ?
                JsonParser.parseString(recognizer.getResult()).getAsJsonObject().get("text").getAsString() : "";
    }

    /**
     * Beta function, it may cause some unpredictable errors
     * Repair the error encoding of the string
     * @param str String to be repaired
     * @param srcEncoding Original encoding
     * @param dstEncoding Encoding to be repaired
     * @return {@link java.lang.String} message
     */
    @Contract("_, _, _ -> new")
    public static @NotNull String repairEncoding(@NotNull String str, String srcEncoding, String dstEncoding) {
        try {
            return new String(str.getBytes(srcEncoding), dstEncoding);
        } catch (UnsupportedEncodingException uee) {
            MicrophoneTextInputClient.LOGGER.error("Couldn't repair encoding, using default", uee);
            return str;
        }
    }
}
