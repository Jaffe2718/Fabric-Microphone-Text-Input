package github.jaffe2718.mcmti.client.event;

import github.jaffe2718.mcmti.FabricMicrophoneTextInputMain;
import github.jaffe2718.mcmti.client.FabricMicrophoneTextInputClient;
import github.jaffe2718.mcmti.config.ConfigUI;
import github.jaffe2718.mcmti.unit.MicrophoneHandler;
import github.jaffe2718.mcmti.unit.SpeechRecognizer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.text.Text;
import org.vosk.Model;

import javax.sound.sampled.AudioFormat;

/**
 * This class is used to register response processing for game events.
 * This includes the function of initializing the speech recognizer when the game starts and the task of detecting that the user presses the key V to initiate speech recognition and send a message.
 * @author Jaffe2718*/
public class EventHandler {

    /** The following variables are used to store the speech recognizer*/
    private static MicrophoneHandler microphoneHandler;

    /** The following variables are used to store the microphone handler*/
    private static SpeechRecognizer speechRecognizer;

    /** The following variables are used to store the last recognized result*/
    private static String lastResult = "";

    /** The following variables are used to store the thread that listens to the microphone*/
    private static Thread listenThread;

    /** This method is used to register the response processing for the game start event*/
    public static void register() {

        ClientLifecycleEvents.CLIENT_STARTED.register(     // when the client launch
                (client) -> {
                    FabricMicrophoneTextInputMain.LOGGER.info("Loading acoustic model from " + ConfigUI.acousticModelPath + "   ..."); // Log the path of the acoustic model
                    try {                                  // Initialize the speech recognizer
                        speechRecognizer = new SpeechRecognizer(new Model(ConfigUI.acousticModelPath), ConfigUI.sampleRate);
                        FabricMicrophoneTextInputMain.LOGGER.info("Acoustic model loaded successfully!");
                    }catch (Exception e1) {
                        FabricMicrophoneTextInputMain.LOGGER.error(e1.getMessage());
                    }
                    try {                                   // Initialize the microphone handler, single channel, 16 bits per sample, signed, little endian
                        microphoneHandler = new MicrophoneHandler(new AudioFormat(ConfigUI.sampleRate, 16, 1, true, false));
                        microphoneHandler.startListening();
                        FabricMicrophoneTextInputMain.LOGGER.info("Microphone handler initialized successfully!");
                    } catch (Exception e2) {
                        FabricMicrophoneTextInputMain.LOGGER.error(e2.getMessage());
                    }
                    if (ConfigUI.encoding_repair) {         // If the encoding repair function is enabled, log a warning
                        FabricMicrophoneTextInputMain.LOGGER.warn(
                                String.format("(test function) Trt to resolve error encoding from %s to %s...", ConfigUI.srcEncoding, ConfigUI.dstEncoding));
                    }
                    listenThread = new Thread(() -> {
                        while (true) {
                            try {
                                if (speechRecognizer == null) {         // wait 10 seconds and try to initialize the speech recognizer again
                                    listenThread.wait(10000);
                                    speechRecognizer = new SpeechRecognizer(new Model(ConfigUI.acousticModelPath), ConfigUI.sampleRate);
                                } else if (microphoneHandler == null) {  // wait 10 seconds and try to initialize the microphone handler again
                                    listenThread.wait(10000);
                                    microphoneHandler = new MicrophoneHandler(new AudioFormat(ConfigUI.sampleRate, 16, 1, true, false));
                                } else {                                 // If the speech recognizer and the microphone handler are initialized successfully
                                    String tmp = speechRecognizer.getStringMsg(microphoneHandler.readData());
                                    if (!tmp.equals("") && !tmp.equals(lastResult) &&
                                            FabricMicrophoneTextInputClient.vKeyBinding.isPressed()) {   // Read audio data from the microphone and send it to the speech recognizer for recognition
                                        if (ConfigUI.encoding_repair) {
                                            lastResult = SpeechRecognizer.repairEncoding(tmp, ConfigUI.srcEncoding, ConfigUI.dstEncoding);
                                        } else {                                        // default configuration without encoding repair
                                            lastResult = tmp;                           // restore the recognized text
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                FabricMicrophoneTextInputMain.LOGGER.error(e.getMessage());
                            }
                        }
                    });
                    listenThread.start();
                }
        );

        ClientTickEvents.END_CLIENT_TICK.register(        // When the client ticks, check if the user presses the key V
            client -> {
                if (client.player!=null &&                                             // If the player is not null
                        FabricMicrophoneTextInputClient.vKeyBinding.isPressed() &&     // If the user presses the key V
                        microphoneHandler != null &&                                   // If the microphone initialization is successful
                        !lastResult.equals("")) {                                      // If the recognized text is not empty
                    // Send the recognized text to the server as a chat message automatically
                    client.player.sendChatMessage(lastResult, Text.of(lastResult));
                    lastResult = "";                                                   // Clear the recognized text
                }
            }
        );

        ClientTickEvents.START_CLIENT_TICK.register(         // register another client tick event to notify the user that the speech recognition is in progress and the game is not frozen
                client -> {
                    if (client.player!=null && FabricMicrophoneTextInputClient.vKeyBinding.isPressed()) {  // If the user presses the key V
                        client.player.sendMessage(Text.of("§eRecording & Recognizing..."), true);
                    }
                }
        );

        ClientLifecycleEvents.CLIENT_STOPPING.register(   // when the game is ready to close
            client -> {
                microphoneHandler.stopListening();        // Stop listening to the microphone
                speechRecognizer = null;
                microphoneHandler = null;
                listenThread.stop();                      // Stop the thread that listens to the microphone
                listenThread = null;                      // Clear the thread
            }
        );
    }
}
