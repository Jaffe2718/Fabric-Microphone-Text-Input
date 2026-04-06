package io.github.jaffe2718.mcmti.neoforge.event;

import io.github.jaffe2718.mcmti.MicrophoneTextInput;
import io.github.jaffe2718.mcmti.event.EventSystem;
import io.github.jaffe2718.mcmti.util.AudioRecorder;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import net.minecraft.client.Minecraft;
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

    @SuppressWarnings("ConstantValue")
    @SubscribeEvent
    public static void onClientTick(@NotNull ClientTickEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        if (client == null) return;
        EventSystem.onConfigAltered(client);
        if (client.level != null) {
            EventSystem.showRecognizeStatus(client.level);
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
