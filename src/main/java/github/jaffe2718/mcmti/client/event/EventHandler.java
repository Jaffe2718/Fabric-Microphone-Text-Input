package github.jaffe2718.mcmti.client.event;

import github.jaffe2718.mcmti.MicrophoneTextInputMain;
import github.jaffe2718.mcmti.client.MicrophoneTextInputClient;
import github.jaffe2718.mcmti.config.ConfigUI;
import github.jaffe2718.mcmti.unit.MicrophoneHandler;
import github.jaffe2718.mcmti.unit.SpeechRecognizer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
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

        ClientLifecycleEvents.CLIENT_STARTED.register(EventHandler::handelClientStartEvent);

        ClientTickEvents.END_CLIENT_TICK.register(EventHandler::handleEndClientTickEvent);

        ClientTickEvents.START_CLIENT_TICK.register(EventHandler::handleStartClientTickEvent);

        ClientLifecycleEvents.CLIENT_STOPPING.register(EventHandler::handleClientStopEvent);
    }

    private static void listenThreadTask() {
        while (true) {
            try {
                if (speechRecognizer == null) {         // wait 10 seconds and try to initialize the speech recognizer again
                    if (MinecraftClient.getInstance().player != null) {
                        MinecraftClient.getInstance().player.sendMessage(Text.of("§cAcoustic Model Load Failed"), true);
                    }
                    // listenThread.wait(10000);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ie) {
                        continue;
                    }
                    speechRecognizer = new SpeechRecognizer(new Model(ConfigUI.acousticModelPath), ConfigUI.sampleRate);
                } else if (microphoneHandler == null) {  // wait 10 seconds and try to initialize the microphone handler again
                    listenThread.wait(10000);
                    microphoneHandler = new MicrophoneHandler(new AudioFormat(ConfigUI.sampleRate, 16, 1, true, false));
                    microphoneHandler.startListening();  // Try to restart microphone
                } else {                                 // If the speech recognizer and the microphone handler are initialized successfully
                    String tmp = speechRecognizer.getStringMsg(microphoneHandler.readData());
                    if (!tmp.isEmpty() && !tmp.equals(lastResult) &&
                            MicrophoneTextInputClient.vKeyBinding.isPressed()) {   // Read audio data from the microphone and send it to the speech recognizer for recognition
                        if (ConfigUI.encoding_repair) {
                            lastResult = SpeechRecognizer.repairEncoding(tmp, ConfigUI.srcEncoding, ConfigUI.dstEncoding);
                        } else {                                        // default configuration without encoding repair
                            lastResult = tmp;                           // restore the recognized text
                        }
                    }
                }
            } catch (Exception e) {
                MicrophoneTextInputMain.LOGGER.error(e.getMessage());
            }
        }
    }

    private static void handelClientStartEvent(MinecraftClient client) {     // when the client launch
        MicrophoneTextInputMain.LOGGER.info("Loading acoustic model from " + ConfigUI.acousticModelPath + "   ..."); // Log the path of the acoustic model
        try {                                  // Initialize the speech recognizer
            speechRecognizer = new SpeechRecognizer(new Model(ConfigUI.acousticModelPath), ConfigUI.sampleRate);
            MicrophoneTextInputMain.LOGGER.info("Acoustic model loaded successfully!");
        }catch (Exception e1) {
            MicrophoneTextInputMain.LOGGER.error(e1.getMessage());
        }
        try {                                   // Initialize the microphone handler, single channel, 16 bits per sample, signed, little endian
            microphoneHandler = new MicrophoneHandler(new AudioFormat(ConfigUI.sampleRate, 16, 1, true, false));
            microphoneHandler.startListening();
            MicrophoneTextInputMain.LOGGER.info("Microphone handler initialized successfully!");
        } catch (Exception e2) {
            MicrophoneTextInputMain.LOGGER.error(e2.getMessage());
        }
        if (ConfigUI.encoding_repair) {         // If the encoding repair function is enabled, log a warning
            MicrophoneTextInputMain.LOGGER.warn(
                    String.format("(test function) Trt to resolve error encoding from %s to %s...", ConfigUI.srcEncoding, ConfigUI.dstEncoding));
        }
        listenThread = new Thread(EventHandler::listenThreadTask);
        listenThread.start();
    }

    private static void handleClientStopEvent(MinecraftClient client) {
        listenThread.interrupt();                 // Stop the thread that listens to the microphone
        microphoneHandler.stopListening();        // Stop listening to the microphone
        speechRecognizer = null;
        microphoneHandler = null;
        listenThread = null;                      // Clear the thread
    }

    private static void handleEndClientTickEvent(MinecraftClient client) {     // When the client ticks, check if the user presses the key V
        if (client.player!=null &&                                             // If the player is not null
                MicrophoneTextInputClient.vKeyBinding.isPressed() &&           // If the user presses the key V
                microphoneHandler != null &&                                   // If the microphone initialization is successful
                !lastResult.isEmpty()) {                                      // If the recognized text is not empty
            // Send the recognized text to the server as a chat message automatically
            if (ConfigUI.autoSend) {
                client.player.networkHandler.sendChatMessage(ConfigUI.prefix + " " + lastResult);
                client.player.sendMessage(Text.of("§aMessage Sent"), true);
            } else {
                client.setScreen(new ChatScreen(ConfigUI.prefix + " " + lastResult));
                if (client.currentScreen!=null) client.currentScreen.applyKeyPressNarratorDelay();
            }
            lastResult = "";                                                   // Clear the recognized text
        }
    }

    private static void handleStartClientTickEvent(MinecraftClient client) {  // handle another client tick event to notify the user that the speech recognition is in progress and the game is not frozen
        if (client.player!=null && MicrophoneTextInputClient.vKeyBinding.isPressed()) {  // If the user presses the key V
            client.player.sendMessage(Text.of("§eRecording & Recognizing..."), true);
        } else if (!lastResult.isEmpty()) {
            lastResult = "";
        }
    }

}
