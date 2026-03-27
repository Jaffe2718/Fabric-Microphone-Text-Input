package io.github.jaffe2718.mcmti.fabric;

import io.github.jaffe2718.mcmti.MicrophoneTextInput;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public final class MicrophoneTextInputFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(MicrophoneTextInput.RECOGNIZE_KEY);
        MicrophoneTextInput.init();
    }
}
