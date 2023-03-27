package github.jaffe2718.mcmti;

import eu.midnightdust.lib.config.MidnightConfig;
import github.jaffe2718.mcmti.config.ConfigUI;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricMicrophoneTextInputMain implements ModInitializer {

    public static final String MOD_ID = "mcmti";

    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        LOGGER.info("Fabric Microphone Text Input Mod is Initializing...");
        MidnightConfig.init(MOD_ID, ConfigUI.class);
    }
}
