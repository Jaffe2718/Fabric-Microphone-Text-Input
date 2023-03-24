package github.jaffe2718.mcmti.client;


import github.jaffe2718.mcmti.client.event.MicrophoneEventRegister;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MicrophoneTextInputClient implements ClientModInitializer {
    public static KeyBinding vKeyBinding;

    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        vKeyBinding = new KeyBinding("key.mcmti.mic", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, KeyBinding.MISC_CATEGORY);
        KeyBindingHelper.registerKeyBinding(vKeyBinding);
        MicrophoneEventRegister.microphoneHandler.startRecognize();
        MicrophoneEventRegister.registerEvents();
    }
}
