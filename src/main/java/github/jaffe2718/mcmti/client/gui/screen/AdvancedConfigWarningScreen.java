package github.jaffe2718.mcmti.client.gui.screen;

import eu.midnightdust.lib.config.MidnightConfig;
import github.jaffe2718.mcmti.client.MicrophoneTextInput;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class AdvancedConfigWarningScreen extends ConfirmScreen {

    private final Screen parent;

    public AdvancedConfigWarningScreen(Screen parent) {
        super(AdvancedConfigWarningScreen::checkConfirmed,
                Text.translatable("mcmti.gui.config.advanced.warn.title"),
                Text.translatable("mcmti.gui.config.advanced.warn"),
                Text.translatable("gui.proceed").withColor(0xFF5555),
                Text.translatable("gui.cancel"));
        this.parent = parent;
    }

    private static void checkConfirmed(boolean confirmed) {
        if (!confirmed) {
            MidnightConfig.loadValuesFromJson(MicrophoneTextInput.MOD_ID);
        } else {
            MicrophoneTextInput.LOGGER.warn("Advanced config enabled");
        }
        if (MinecraftClient.getInstance().currentScreen instanceof AdvancedConfigWarningScreen screen) {
            screen.close();
        }
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        } else {
            super.close();
        }
    }
}
