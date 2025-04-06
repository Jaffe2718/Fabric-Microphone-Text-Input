package github.jaffe2718.mcmti.client.event;

import github.jaffe2718.mcmti.client.MicrophoneTextInputClient;
import github.jaffe2718.mcmti.config.ConfigUI;
import github.jaffe2718.mcmti.unit.MicrophoneHandler;
import github.jaffe2718.mcmti.unit.SpeechRecognizer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;


/**
 * This class is used to register response processing for game events.
 * This includes the function of initializing the speech recognizer when the game starts and the task of detecting that the user presses the key V to initiate speech recognition and send a message.
 * @author Jaffe2718*/
public class EventHandler {

    /** The following variables are used to store the last recognized result*/
    private static volatile String lastResult = "";

    /** The following variables are used to store the thread that listens to the microphone*/
    private static Thread listenThread;

    /** This method is used to register the response processing for the game start event*/
    public static void register() {

        ClientLifecycleEvents.CLIENT_STARTED.register(EventHandler::handelClientStartEvent);

        ClientTickEvents.END_CLIENT_TICK.register(EventHandler::handleEndClientTickEvent);

        ClientTickEvents.START_CLIENT_TICK.register(EventHandler::handleStartClientTickEvent);

        ClientLifecycleEvents.CLIENT_STOPPING.register(EventHandler::handleClientStopEvent);
    }

    /**
     * This method is the task of the thread that listens to the microphone.
     * It is used to read audio data from the microphone and send it to the speech recognizer for recognition.
     * @see EventHandler#handelClientStartEvent(MinecraftClient)
     */
    private static void listenThreadTask() {
        while (true) {
            if (MicrophoneHandler.statusChanged()) {
                MicrophoneHandler.init();
                SpeechRecognizer.init();
            } else if (SpeechRecognizer.statusChanged()) {
                SpeechRecognizer.init();
            }
            if (SpeechRecognizer.instance() != null && MicrophoneHandler.instance() != null) { // If the speech recognizer and the microphone handler are initialized successfully
                String tmp = SpeechRecognizer.instance().getStringMsg(MicrophoneHandler.instance().readData());
                if (!tmp.isEmpty() && !tmp.equals(lastResult) &&
                        MicrophoneTextInputClient.micKeyBinding.isPressed()) {   // Read audio data from the microphone and send it to the speech recognizer for recognition
                    if (ConfigUI.encodingRepair) {
                        lastResult = SpeechRecognizer.repairEncoding(tmp, ConfigUI.srcEncoding, ConfigUI.dstEncoding);
                    } else {                                        // default configuration without encoding repair
                        lastResult = tmp;                           // restore the recognized text
                    }
                }
            }
        }
    }

    /**
     * This method is used to handle the game start event.
     * It is used to initialize the speech recognizer and the microphone handler.
     * @param client The Minecraft client
     */
    private static void handelClientStartEvent(MinecraftClient client) {     // when the client launches, initialize the speech recognizer and the microphone handler
        SpeechRecognizer.init();
        MicrophoneHandler.init();
        if (ConfigUI.encodingRepair) {         // If the encoding repair function is enabled, log a warning
            MicrophoneTextInputClient.LOGGER.warn(
                    String.format("(test function) Trt to resolve error encoding from %s to %s...", ConfigUI.srcEncoding, ConfigUI.dstEncoding));
        }
        listenThread = new Thread(EventHandler::listenThreadTask, "Speech Recognizer Thread");
        listenThread.start();
    }

    /**
     * This method is used to handle the game stop event.
     * It is used to stop the thread that listens to the microphone and stop listening to the microphone.
     * @param client The Minecraft client
     */
    private static void handleClientStopEvent(MinecraftClient client) {
        listenThread.interrupt();                 // Stop the thread that listens to the microphone
        MicrophoneHandler.close();
        SpeechRecognizer.close();
        listenThread = null;                      // Clear the thread
    }

    /**
     * This method is used to handle the game end tick event.
     * It is used to detect that the user presses the key V to initiate speech recognition and send a message.
     * @param client The Minecraft client
     */
    private static void handleEndClientTickEvent(@NotNull MinecraftClient client) {     // When the client ticks, check if the user presses the key V
        if (client.player != null &&                                             // If the player is not null
                MicrophoneTextInputClient.micKeyBinding.isPressed() &&           // If the user presses the key V
                SpeechRecognizer.instance() != null &&                           // If the speech recognizer initialization is successful
                MicrophoneHandler.instance() != null &&                          // If the microphone initialization is successful
                !lastResult.isEmpty()) {                                         // If the recognized text is not empty
            if (ConfigUI.autoSend) {                                             // Send the recognized text to the server as a chat message automatically
                client.player.networkHandler.sendChatMessage(ConfigUI.prefix + " " + lastResult);
                client.player.sendMessage(Text.translatable("message.mcmti.messageSent"), true);
            } else {                                                             // If the auto send function is disabled, open the chat screen and insert the recognized text
                client.setScreen(new ChatScreen(ConfigUI.prefix + " " + lastResult));
                if (client.currentScreen != null) client.currentScreen.applyKeyPressNarratorDelay();
            }
            lastResult = "";                                                     // Clear the recognized text
        }
    }

    /**
     * This method is used to handle the game start tick event.
     * It is used to notify the user that the speech recognition is in progress and the game is not frozen.
     * @param client The Minecraft client
     */
    private static void handleStartClientTickEvent(@NotNull MinecraftClient client) {        // handle another client tick event to notify the user that the speech recognition is in progress and the game is not frozen
        if (client.player != null) {
            if (SpeechRecognizer.instance() == null) {
                client.player.sendMessage(Text.translatable("message.mcmti.acousticModelLoadFailed"), true);
                return;
            } else if (MicrophoneHandler.instance() == null) {
                client.player.sendMessage(Text.translatable("message.mcmti.microphoneInitFailed"), true);
                return;
            }
            if (MicrophoneTextInputClient.micKeyBinding.isPressed()) {  // If the user presses the key V
                client.player.sendMessage(Text.translatable("message.mcmti.recordingAndRecognizing"), true);
            }
        }
    }

}
