package io.github.jaffe2718.mcmti.client.gui.screen;

import eu.midnightdust.lib.config.MidnightConfig;
import io.github.jaffe2718.mcmti.MicrophoneTextInput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AdvancedConfigWarningScreen extends ConfirmScreen {

    private final Screen parent;

    public AdvancedConfigWarningScreen(Screen parent) {
        super(AdvancedConfigWarningScreen::checkConfirmed,
                Component.translatable("mcmti.gui.config.advanced.warn.title"),
                Component.translatable("mcmti.gui.config.advanced.warn"),
                Component.translatable("gui.proceed").withColor(0xFF5555),
                Component.translatable("gui.cancel"));
        this.parent = parent;
    }

    private static void checkConfirmed(boolean confirmed) {
        if (confirmed) {
            MicrophoneTextInput.LOGGER.warn("Advanced config enabled");
        } else {
            MidnightConfig.configInstances.get(MicrophoneTextInput.MOD_ID).loadValuesFromJson();
        }
        if (Minecraft.getInstance().screen instanceof AdvancedConfigWarningScreen screen) {
            screen.onClose();
        }
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
