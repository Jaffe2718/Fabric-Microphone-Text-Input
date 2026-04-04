package io.github.jaffe2718.mcmti.neoforge.event;

import io.github.jaffe2718.mcmti.MicrophoneTextInput;
import io.github.jaffe2718.mcmti.event.EventSystem;
import io.github.jaffe2718.mcmti.util.AudioRecorder;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.minecraft.client.MinecraftClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.lifecycle.ClientStartedEvent;
import net.neoforged.neoforge.client.event.lifecycle.ClientStoppingEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(value = Dist.CLIENT, modid = MicrophoneTextInput.MOD_ID)
public class EventHandler {

    @SubscribeEvent
    public static void registerBindings(@NotNull RegisterKeyMappingsEvent event) {
        event.register(MicrophoneTextInput.RECOGNIZE_KEY);
    }

    @SubscribeEvent
    public static void onClientTick(@NotNull ClientTickEvent.Post event) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        EventSystem.onConfigAltered(client);
        if (client.world != null) {
            EventSystem.showRecognizeStatus(client.world);
        }
    }

    @SubscribeEvent
    public static void initSpeechRecognizer(@NotNull ClientStartedEvent event) {
        Thread.ofVirtual().start(SpeechRecognizer::init);
    }

    @SubscribeEvent
    public static void deregisterSpeechRecognizer(@NotNull ClientStoppingEvent event) {
        SpeechRecognizer.deregister();
        AudioRecorder.destroy();
    }

}
