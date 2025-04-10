package github.jaffe2718.mcmti.client;

import eu.midnightdust.lib.config.MidnightConfig;
import github.jaffe2718.mcmti.config.MicrophoneTextInputConfig;
import github.jaffe2718.mcmti.util.AudioRecorder;
import github.jaffe2718.mcmti.util.EventSystem;
import github.jaffe2718.mcmti.util.SpeechRecognizer;
import io.github.givimad.whisperjni.*;
import io.netty.channel.unix.Errors;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class MicrophoneTextInputClient implements ClientModInitializer {

    public static final String MOD_ID = "mcmti";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final KeyBinding RECOGNIZE_KEY = new KeyBinding("key.mcmti.recognize", GLFW.GLFW_KEY_V, "key.categories.mcmti");

    @Override
    public void onInitializeClient() {
        MidnightConfig.init(MOD_ID, MicrophoneTextInputConfig.class);
        try {
            WhisperJNI.loadLibrary();
            WhisperJNI.setLibraryLogger(null);
        } catch (IOException ignored) {}
        KeyBindingHelper.registerKeyBinding(RECOGNIZE_KEY);
        AudioRecorder.init();
        SpeechRecognizer.init();
        EventSystem.register();
    }
}
