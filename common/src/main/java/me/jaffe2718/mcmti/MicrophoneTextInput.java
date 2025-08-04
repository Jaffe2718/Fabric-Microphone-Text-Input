package me.jaffe2718.mcmti;


import io.github.freshsupasulley.whisperjni.LibraryUtils;
import me.jaffe2718.mcmti.config.McmtiConfig;
import me.jaffe2718.mcmti.util.AudioRecorder;
import me.jaffe2718.mcmti.util.SpeechRecognizer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

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
            if (McmtiConfig.advancedConfig && McmtiConfig.useCustomDynamicLib) {
                LibraryUtils.findAndLoadVulkanRuntime();
                LibraryUtils.loadLibrary(LOGGER, Path.of(McmtiConfig.customDynamicLibDir));
            } else {
//                URI jarUri = URI.create("jar:file:/C:/Users/lfkex/.gradle/caches/modules-2/files-2.1/io.github.freshsupasulley/whisper-jni/0.5.2/75fe0d4a2bd6e5c39201b8153fc7050a836c0080/whisper-jni-0.5.2.jar!/windows-x64");
//                Path p = FileSystems.newFileSystem(jarUri, Collections.emptyMap()).getPath("windows-x64");
                SpeechRecognizer.WHISPER.loadLibrary(LOGGER);
            }
        } catch (IOException ignored) {}
        AudioRecorder.init();
        SpeechRecognizer.init();
    }
}
