package me.jaffe2718.mcmti.mixin;

import io.github.freshsupasulley.whisperjni.WhisperJNI;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static me.jaffe2718.mcmti.config.McmtiConfig.advancedConfig;
import static me.jaffe2718.mcmti.config.McmtiConfig.customDynamicLibDir;
import static me.jaffe2718.mcmti.config.McmtiConfig.useCustomDynamicLib;

@Mixin(WhisperJNI.class)
public abstract class WhisperJNIMixin {

    @Unique private static final List<String> mcmti$loadOrder = Arrays.asList("ggml-base", "ggml-cpu", "ggml-vulkan", "ggml", "whisper", "whisper-jni");
    @Unique private static final String[] LIB_NAMES = {".so", ".dylib", ".dll"};

    @Shadow(remap = false) private static boolean libraryLoaded;

    @Unique
    private static void mcmti$loadInOrder(Logger logger, Path tempDir) throws IOException
    {
        // Now load everything in the correct order
        List<String> natives = Stream.of(Objects.requireNonNull(tempDir.toFile().listFiles())).sorted(Comparator.comparing(file ->
        {
            for(int i = 0; i < mcmti$loadOrder.size(); i++)
            {
                // adding the . differentiates between files like 'ggml' and 'ggml-base', ensuring its at the suffix
                if(file.getName().contains(mcmti$loadOrder.get(i) + "."))
                {
                    return i; // return index of match as priority
                }
            }

            logger.warn("File not handled in load order: {}", file);
            return Integer.MAX_VALUE; // unknown files go last
        })).map(File::getAbsolutePath).filter(file -> Stream.of(LIB_NAMES).anyMatch(suffix -> file.matches(new String(".*\\" + suffix + "(\\.\\d+)*$")))).toList();

        if(natives.isEmpty())
        {
            logger.error("Failed to find any natives. If you're running in an IDE, make sure you build the natives for your platform before testing using the build scripts");
        }
        else
        {
            // ^ collecting into a list because the consumer doesn't declare IOException
            for(String path : natives)
            {
                logger.info("Loading {}", path);

                try
                {
                    System.load(path);
                } catch(Exception e)
                {
                    // Pass into parent
                    logger.error("Failed to load {}. Is the loading order incorrect?", path, e);
                    throw new IOException(e);
                }
            }

            logger.info("Done loading natives");
        }
    }


    /**
     * @author Jaffe2718
     * @reason load custom library
     */
    @Inject(method = "loadLibrary(Lorg/slf4j/Logger;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private static void loadLibrary(@NotNull Logger logger, CallbackInfo ci) throws IOException {
        if (advancedConfig && useCustomDynamicLib) {
            ci.cancel();
            logger.warn("Loading custom natives for whisper-jni from {}", customDynamicLibDir);
            mcmti$loadInOrder(logger, Path.of(customDynamicLibDir));
            libraryLoaded = true;
        }
    }
}
