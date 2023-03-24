package github.jaffe2718.mcmti.client.event;


import github.jaffe2718.mcmti.client.MicrophoneTextInputClient;
import github.jaffe2718.mcmti.util.MicrophoneHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.text.Text;

public class MicrophoneEventRegister {

    public static MicrophoneHandler microphoneHandler = new MicrophoneHandler();

    public static Thread sendThread;

    /**
     * Registers the events.
     * When the key v is pressed, the voice input will be automatically sent to the server as a player chat message.
     * This process will create a new thread to avoid blocking the main thread, which can ensure the game's smoothness.
     * The thread will be stopped when the key v is released.
     * @see MicrophoneTextInputClient#vKeyBinding
     * @see MicrophoneHandler#getResult()
     */
    public static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(
            (client) -> {
                if (client.player!=null) {
                    //MicrophoneTextInput.LOGGER.info("v:"+MicrophoneTextInputClient.vKeyBinding.isPressed());
                    if (MicrophoneTextInputClient.vKeyBinding.isPressed()                                // If the key v is pressed.
                            && (sendThread == null || !sendThread.isAlive())) {                          // to avoid creating redundant threads.
                        client.player.sendMessage(Text.of("Recognizing audio..."), true);  // Send a message to the client to notify the player that the voice input is being recognized.
                        sendThread = new Thread(() -> {
                            while (MicrophoneTextInputClient.vKeyBinding.isPressed()) {                 // When User press the key v
                                String voiceInput = microphoneHandler.getResult();
                                if (voiceInput != null && voiceInput.length() > 0) {                    // If the voice input is not empty.
                                    client.player.sendChatMessage(voiceInput, Text.of(voiceInput));
                                }
                            }
                            String lastMessage = microphoneHandler.getResult();
                            if (lastMessage.length() > 0) {
                                client.player.sendChatMessage(lastMessage, Text.of(lastMessage));
                            }
                        });
                        sendThread.start();
                    }
                }
            });

        ClientLifecycleEvents.CLIENT_STOPPING.register(
                (client) -> microphoneHandler.stopRecognize()
        );
    }
}
