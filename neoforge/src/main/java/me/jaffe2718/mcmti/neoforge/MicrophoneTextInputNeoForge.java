package me.jaffe2718.mcmti.neoforge;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import me.jaffe2718.mcmti.MicrophoneTextInput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = MicrophoneTextInput.MOD_ID, dist = Dist.CLIENT)
public final class MicrophoneTextInputNeoForge {

    public MicrophoneTextInputNeoForge() {
        KeyMappingRegistry.register(MicrophoneTextInput.RECOGNIZE_KEY);
        MicrophoneTextInput.init();
    }
}
