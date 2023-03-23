package github.jaffe2718.mcmti.client.event;


import github.jaffe2718.mcmti.client.MicrophoneTextInputClient;
import github.jaffe2718.mcmti.util.MicrophoneHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.message.DecoratedContents;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
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
                if (client.world != null && client.player!=null && client.getServer() != null) {
                    ServerPlayerEntity self = client.getServer().getPlayerManager().getPlayer(client.player.getUuid());
                    if (MicrophoneTextInputClient.vKeyBinding.isPressed()                               // If the key v is pressed.
                            && self != null && (sendThread == null || !sendThread.isAlive())) {         // to avoid creating redundant threads.
                        self.sendMessageToClient(Text.of("Recognizing audio..."), true);  // Send a message to the client to notify the player that the voice input is being recognized.
                        sendThread = new Thread(() -> {
                            while (MicrophoneTextInputClient.vKeyBinding.isPressed()) {
                                String voiceInput = microphoneHandler.getResult();
                                if (voiceInput != null && voiceInput.length() > 0) {                   // If the voice input is not empty.
                                    self.sendChatMessage(new SentMessage.Chat(
                                            SignedMessage.ofUnsigned(
                                                    new DecoratedContents(microphoneHandler.getResult())
                                            )
                                    ), false, MessageType.params(MessageType.CHAT, self));
                                }
                            }
                        });
                        sendThread.start();
                    }
                }
            });

        ClientLifecycleEvents.CLIENT_STOPPING.register(
                (client) -> {
                    microphoneHandler.stopRecognize();
                }
        );
    }
}
