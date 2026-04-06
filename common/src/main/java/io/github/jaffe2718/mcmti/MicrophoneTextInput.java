package io.github.jaffe2718.mcmti;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.jaffe2718.mcmti.config.McmtiConfig;
import io.github.jaffe2718.mcmti.event.EventSystem;
import io.github.jaffe2718.mcmti.util.AudioRecorder;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import io.github.jaffe2718.mcmti.util.whisper.WhisperSpeechRecognizer;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MicrophoneTextInput {
    public static final String MOD_ID = "mcmti";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final KeyMapping.Category MICROPHONE_TEXT_INPUT_CATEGORY = new KeyMapping.Category(Identifier.withDefaultNamespace(MOD_ID));
    public static final KeyMapping RECOGNIZE_KEY = new KeyMapping("key.mcmti.recognize", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, MICROPHONE_TEXT_INPUT_CATEGORY);

    public static boolean advancedConfig = false;

    public static void init() {
        McmtiConfig.init(MOD_ID, McmtiConfig.class);
        AudioRecorder.init();
        if (WhisperSpeechRecognizer.loadLibrary()) {
            SpeechRecognizer.register(Integer.MAX_VALUE, Identifier.fromNamespaceAndPath(MOD_ID, "whisper"), WhisperSpeechRecognizer::new);
        }
        advancedConfig = McmtiConfig.advancedConfig;
        Thread.ofVirtual().start(EventSystem::recognizeTask).setName("thread.mcmti.recognizer.loop");
    }
}
