package github.jaffe2718.mcmti.client;

import eu.midnightdust.lib.config.MidnightConfig;
import github.jaffe2718.mcmti.config.McmtiConfig;
import github.jaffe2718.mcmti.util.AudioRecorder;
import github.jaffe2718.mcmti.util.EventSystem;
import github.jaffe2718.mcmti.util.SpeechRecognizer;
import io.github.givimad.whisperjni.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MicrophoneTextInput implements ClientModInitializer {

    public static final String MOD_ID = "mcmti";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final KeyBinding RECOGNIZE_KEY = new KeyBinding("key.mcmti.recognize", InputUtil.Type.KEYSYM, InputUtil.GLFW_KEY_V, "key.categories.mcmti");

    /**
     * listen to config change
     * @see McmtiConfig#advancedConfig
     * */
    public static volatile boolean advancedConfig = false;

    @Override
    public void onInitializeClient() {
        MidnightConfig.init(MOD_ID, McmtiConfig.class);
        advancedConfig = McmtiConfig.advancedConfig;
        try {
            if (McmtiConfig.advancedConfig && !McmtiConfig.whisperjniLibdir.isBlank()) {
                System.setProperty("io.github.givimad.whisperjni.libdir", McmtiConfig.whisperjniLibdir);
            }
            WhisperJNI.loadLibrary(McmtiConfig.whisperLogLevel::log);
            WhisperJNI.setLibraryLogger(McmtiConfig.whisperLogLevel::log);
        } catch (IOException ignored) {}
        KeyBindingHelper.registerKeyBinding(RECOGNIZE_KEY);
        AudioRecorder.init();
        SpeechRecognizer.init();
        EventSystem.register();
    }
}
