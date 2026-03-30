package io.github.jaffe2718.qwen3asr_fabric.config;

import eu.midnightdust.lib.config.MidnightConfig;
import io.github.jaffe2718.mcmti.util.SpeechRecognizer;
import io.github.jaffe2718.qwen3asr4j.param.TranscribeParams;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import javax.swing.*;

/**
 * Qwen3 ASR Extension Configuration for tutorial
 * In your project, you can also use other configuration libs.
 */
public class Qwen3ASRConfig extends MidnightConfig {

    /**
     * If you need to reload the SpeechRecognizer once the config changes,
     * you can call SpeechRecognizer.init() in after the config alteration.
     * @see SpeechRecognizer#init()
     */
    @Override
    public void writeChanges() {
        super.writeChanges();
        Thread.ofVirtual().start(SpeechRecognizer::init);  // async init
    }

    /**
     * This config field controls whether to enable the ASR extension.
     * @see io.github.jaffe2718.qwen3asr_fabric.util.Qwen3ASRSpeechRecognizer#enabled()
     */
    @Entry
    public static boolean enabled = true;

    @Entry
    @Condition(requiredOption = "enabled", requiredValue = "true")
    public static int priority = 0;

    @Entry(selectionMode = JFileChooser.FILES_ONLY, fileExtensions = {"gguf"})
    @Condition(requiredOption = "enabled", requiredValue = "true")
    public static String modelPath = "";

    @Entry
    @Condition(requiredOption = "enabled", requiredValue = "true")
    public static boolean customLibrary = false;

    @Entry(selectionMode = JFileChooser.DIRECTORIES_ONLY)
    @Condition(requiredOption = "enabled", requiredValue = "true")
    @Condition(requiredOption = "customLibrary", requiredValue = "true")
    public static String customLibraryDir = "";

    @Entry(min = 1)
    @Condition(requiredOption = "enabled", requiredValue = "true")
    public static int maxTokens = 1024;

    @Entry
    @Condition(requiredOption = "enabled", requiredValue = "true")
    public static String language = "";

    @Entry(min = 0)
    @Condition(requiredOption = "enabled", requiredValue = "true")
    public static int nThreads = 4;

    @Entry
    @Condition(requiredOption = "enabled", requiredValue = "true")
    public static boolean printProgress = true;

    @Entry
    @Condition(requiredOption = "enabled", requiredValue = "true")
    public static boolean printTiming = false;

    @Contract(value = " -> new", pure = true)
    public static @NonNull TranscribeParams getQwenParams() {
        return new TranscribeParams(
                maxTokens,
                language,
                nThreads,
                printProgress,
                printTiming
        );
    }

}
