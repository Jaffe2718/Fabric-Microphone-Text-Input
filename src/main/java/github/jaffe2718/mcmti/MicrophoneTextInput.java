package github.jaffe2718.mcmti;

import github.jaffe2718.mcmti.client.event.MicrophoneEventRegister;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MicrophoneTextInput implements ModInitializer {

    public static final String MOD_ID = "mcmti";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**Runs the mod initializer.*/
    @Override
    public void onInitialize() {
        MicrophoneEventRegister.microphoneHandler.startRecognize();
        MicrophoneEventRegister.registerEvents();
    }
}
