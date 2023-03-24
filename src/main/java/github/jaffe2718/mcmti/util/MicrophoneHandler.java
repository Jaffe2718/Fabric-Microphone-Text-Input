package github.jaffe2718.mcmti.util;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

import java.io.IOException;

public class MicrophoneHandler {

    private LiveSpeechRecognizer recognizer;

    public MicrophoneHandler() {
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        try {
            recognizer = new LiveSpeechRecognizer(configuration);
        } catch (IOException e) {
            recognizer = null;
        }
    }

    public void startRecognize() {
        if (recognizer != null) {
            recognizer.startRecognition(true);
        }
    }

    public void stopRecognize() {
        if (this.recognizer != null) {
            this.recognizer.stopRecognition();
        }
    }

    public String getResult() {
        if (this.recognizer != null) {
            return recognizer.getResult().getHypothesis();
        } else {
            return "";
        }
    }
}
