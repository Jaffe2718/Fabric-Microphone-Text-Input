package io.github.jaffe2718.vosk_neoforge;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import io.github.jaffe2718.vosk_neoforge.config.VoskConfig;
import io.github.jaffe2718.vosk_neoforge.util.VoskSpeechRecognizer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(value = VoskNeoForge.MOD_ID, dist = Dist.CLIENT)
public final class VoskNeoForge {

    public static final String MOD_ID = "vosk_neoforge";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public VoskNeoForge(@NotNull IEventBus modBus, @NotNull ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, VoskConfig.CONFIG_SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        ClientLifecycleEvent.CLIENT_STARTED.register(client -> SpeechRecognizer.register(
                VoskConfig.CONFIG.priority.get(),    // config is unavailable before client started
                VoskSpeechRecognizer::new
        ));
        modBus.addListener(VoskConfig::onCongigAlter);
    }
}
