package io.github.jaffe2718.mcmti;

import io.github.jaffe2718.mcmti.config.McmtiConfig;
import io.github.jaffe2718.mcmti.util.AudioRecorder;
import io.github.jaffe2718.mcmti.util.EventUtil;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import io.github.jaffe2718.mcmti.util.whisper.WhisperSpeechRecognizer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MicrophoneTextInput {
    public static final String MOD_ID = "mcmti";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final KeyBinding.Category MICROPHONE_TEXT_INPUT_CATEGORY = new KeyBinding.Category(Identifier.ofVanilla(MOD_ID));
    public static final KeyBinding RECOGNIZE_KEY = new KeyBinding("key.mcmti.recognize", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, MICROPHONE_TEXT_INPUT_CATEGORY);

    public static boolean advancedConfig = false;

    public static void init() {
        McmtiConfig.init(MOD_ID, McmtiConfig.class);
        AudioRecorder.init();
        if (WhisperSpeechRecognizer.loadLibrary()) {
            SpeechRecognizer.register(Integer.MAX_VALUE, new WhisperSpeechRecognizer());
        }
        SpeechRecognizer.init();
        EventUtil.register();
        advancedConfig = McmtiConfig.advancedConfig;
    }
}
