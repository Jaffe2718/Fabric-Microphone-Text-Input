package me.jaffe2718.mcmti.forge;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import me.jaffe2718.mcmti.MicrophoneTextInput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;


@Mod(value = MicrophoneTextInput.MOD_ID)
@Mod.EventBusSubscriber(modid = MicrophoneTextInput.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class MicrophoneTextInputForge {

    public MicrophoneTextInputForge() {
        KeyMappingRegistry.register(MicrophoneTextInput.RECOGNIZE_KEY);
        MicrophoneTextInput.init();
    }
}
