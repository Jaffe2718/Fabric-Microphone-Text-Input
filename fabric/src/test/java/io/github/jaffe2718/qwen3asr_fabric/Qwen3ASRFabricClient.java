package io.github.jaffe2718.qwen3asr_fabric;

import eu.midnightdust.lib.config.MidnightConfig;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import io.github.jaffe2718.qwen3asr_fabric.config.Qwen3ASRConfig;
import io.github.jaffe2718.qwen3asr_fabric.util.Qwen3ASRSpeechRecognizer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Qwen3ASRFabricClient implements ClientModInitializer {

    public static final String MOD_ID = "qwen3asr_fabric";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**
     * Initialize the Qwen3 AS client side.
     */
    @Override
    public void onInitializeClient() {
        MidnightConfig.init(MOD_ID, Qwen3ASRConfig.class);
        if (Qwen3ASRSpeechRecognizer.loadNativeLibrary()) {
            SpeechRecognizer.register(Qwen3ASRConfig.priority, Identifier.of(MOD_ID, "qwen3asr"), Qwen3ASRSpeechRecognizer::new);
        }
    }
}
