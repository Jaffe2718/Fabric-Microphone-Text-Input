package me.jaffe2718.mcmti.neoforge;

import me.jaffe2718.mcmti.MicrophoneTextInput;
import me.jaffe2718.mcmti.util.EventUtil;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.jetbrains.annotations.NotNull;

@Mod(value = MicrophoneTextInput.MOD_ID, dist = Dist.CLIENT)
public final class MicrophoneTextInputNeoForge {

    public MicrophoneTextInputNeoForge() {
        MicrophoneTextInput.init();
        EventUtil.register();
    }

    @SubscribeEvent
    public static void registerBindings(@NotNull RegisterKeyMappingsEvent event) {
        event.register(MicrophoneTextInput.RECOGNIZE_KEY);
    }
}
