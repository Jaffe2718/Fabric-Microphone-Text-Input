package github.jaffe2718.mcmti.client;

import eu.midnightdust.lib.config.MidnightConfig;
import github.jaffe2718.mcmti.client.event.EventHandler;
import github.jaffe2718.mcmti.config.ConfigUI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MicrophoneTextInputClient implements ClientModInitializer {

    public static final String MOD_ID = "mcmti";

    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static KeyBinding micKeyBinding = new KeyBinding("key.mcmti.mic", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, KeyBinding.MISC_CATEGORY);      // microphone key binding
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        LOGGER.info("Microphone Text Input Mod is Initializing...");
        MidnightConfig.init(MOD_ID, ConfigUI.class);
        KeyBindingHelper.registerKeyBinding(micKeyBinding);
        EventHandler.register();
    }
}
