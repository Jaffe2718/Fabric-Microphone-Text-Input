package io.github.jaffe2718.vosk_neoforge.config;

import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import io.github.jaffe2718.vosk_neoforge.VoskNeoForge;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public final class VoskConfig {

    public static final VoskConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    public final ModConfigSpec.ConfigValue<Integer> priority;
    public final ModConfigSpec.ConfigValue<Boolean> enabled;
    public final ModConfigSpec.ConfigValue<String> voskModelDir;

    private VoskConfig(@NotNull ModConfigSpec.Builder builder) {
        this.priority = builder.define("priority", 1);
        this.enabled = builder.define("enabled", true);
        this.voskModelDir = builder.define("vosk_model_dir", "");
    }

    static {
        Pair<VoskConfig, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(VoskConfig::new);

        //Store the resulting values
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    public static void onCongigAlter(@NotNull ModConfigEvent.Reloading event) {
        if (VoskNeoForge.MOD_ID.equals(event.getConfig().getModId())) {
            SpeechRecognizer.init();
        }
    }
}
