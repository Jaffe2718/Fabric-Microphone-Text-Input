package io.github.jaffe2718.mcmti.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.jaffe2718.mcmti.event.EventType;

public abstract class EventUtil {

    @SuppressWarnings("unused")
    @ExpectPlatform
    public static void triggerEvent(EventType event, Object... args) {
        throw new RuntimeException();
    }
}
