package me.jaffe2718.mcmti;


import io.github.freshsupasulley.whisperjni.LibraryUtils;
import me.jaffe2718.mcmti.config.McmtiConfig;
import me.jaffe2718.mcmti.util.AudioRecorder;
import me.jaffe2718.mcmti.util.SpeechRecognizer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

public final class MicrophoneTextInput {
    public static final String MOD_ID = "mcmti";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final KeyBinding.Category MICROPHONE_TEXT_INPUT_CATEGORY = new KeyBinding.Category(Identifier.of(MOD_ID));
    public static final KeyBinding RECOGNIZE_KEY = new KeyBinding("key.mcmti.recognize", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, MICROPHONE_TEXT_INPUT_CATEGORY);

    /**
     * listen to config change
     * @see McmtiConfig#advancedConfig
     * */
    public static volatile boolean advancedConfig = false;

    public static void init() {
        McmtiConfig.init(MOD_ID, McmtiConfig.class);
        advancedConfig = McmtiConfig.advancedConfig;
        try {
            if (McmtiConfig.advancedConfig && McmtiConfig.useCustomDynamicLib) {
                LibraryUtils.findAndLoadVulkanRuntime();
                LibraryUtils.loadLibrary(LOGGER, Path.of(McmtiConfig.customDynamicLibDir));
            } else {
                SpeechRecognizer.WHISPER.loadLibrary(LOGGER);
            }
        } catch (IOException ignored) {}
        AudioRecorder.init();
        SpeechRecognizer.init();
    }
}
