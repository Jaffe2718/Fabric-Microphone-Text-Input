package me.jaffe2718.mcmti.util;

import dev.architectury.injectables.annotations.ExpectPlatform;

public abstract class EventUtil {

    @ExpectPlatform
    public static void register() {
        throw new RuntimeException();
    }
}
