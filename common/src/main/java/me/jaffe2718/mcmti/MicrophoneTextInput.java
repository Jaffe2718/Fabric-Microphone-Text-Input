package me.jaffe2718.mcmti;


import io.github.givimad.whisperjni.WhisperJNI;
import me.jaffe2718.mcmti.config.McmtiConfig;
import me.jaffe2718.mcmti.util.AudioRecorder;
import me.jaffe2718.mcmti.util.SpeechRecognizer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class MicrophoneTextInput {
    public static final String MOD_ID = "mcmti";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final KeyBinding RECOGNIZE_KEY = new KeyBinding("key.mcmti.recognize", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "key.categories.mcmti");

    /**
     * listen to config change
     * @see McmtiConfig#advancedConfig
     * */
    public static volatile boolean advancedConfig = false;

    public static void init() {
        McmtiConfig.init(MOD_ID, McmtiConfig.class);
        advancedConfig = McmtiConfig.advancedConfig;
        try {
            if (McmtiConfig.advancedConfig && !McmtiConfig.whisperjniLibdir.isBlank()) {
                System.setProperty("io.github.givimad.whisperjni.libdir", McmtiConfig.whisperjniLibdir);
            }
            WhisperJNI.loadLibrary(McmtiConfig.whisperLogLevel::log);
            WhisperJNI.setLibraryLogger(McmtiConfig.whisperLogLevel::log);
        } catch (IOException ignored) {}
        AudioRecorder.init();
        SpeechRecognizer.init();
    }
}
