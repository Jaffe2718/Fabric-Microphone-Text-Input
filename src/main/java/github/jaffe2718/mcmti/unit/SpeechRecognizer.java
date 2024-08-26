package github.jaffe2718.mcmti.unit;

import com.google.gson.JsonParser;
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
    public Recognizer recognizer;
    public final int sampleRate;
    public final String acousticModelPath;

    public SpeechRecognizer(String acousticModelPath, int sampleRate) throws IOException {
        this.sampleRate = sampleRate;
        this.acousticModelPath = acousticModelPath;
        Model model = new Model(acousticModelPath);
        recognizer = new Recognizer(model, sampleRate);
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
     * @throws UnsupportedEncodingException Unsupported encoding
     */
    @Contract("_, _, _ -> new")
    public static @NotNull String repairEncoding(@NotNull String str, String srcEncoding, String dstEncoding) throws UnsupportedEncodingException {
        return new String(str.getBytes(srcEncoding), dstEncoding);
    }
}
