package io.github.jaffe2718.mcmti.neoforge;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import io.github.jaffe2718.mcmti.MicrophoneTextInput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

@Mod(value = MicrophoneTextInput.MOD_ID, dist = Dist.CLIENT)
public final class MicrophoneTextInputNeoForge {

    public MicrophoneTextInputNeoForge() {
        KeyMappingRegistry.register(MicrophoneTextInput.RECOGNIZE_KEY);
        MicrophoneTextInput.init();
    }

    /**
     * Get the event bus of `mcmti`.
     * @return the event bus of `mcmti`.
     * @throws IllegalStateException if the event bus is null.
     */
    @SuppressWarnings("unused")
    public static IEventBus getEventBus() throws IllegalStateException {
        ModContainer container = ModList.get().getModContainerById(MicrophoneTextInput.MOD_ID).orElse(null);
        if (container == null || container.getEventBus() == null) {
            throw new IllegalStateException(String.format("Mod %s container or event bus is null", MicrophoneTextInput.MOD_ID));
        }

        return container.getEventBus();
    }
}
