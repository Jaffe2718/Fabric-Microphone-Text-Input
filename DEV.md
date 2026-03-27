# Development Documentation

## Introduction
Since version 3.0.0, the Microphone Text Input (`mcmti`) mod has been redesigned to support custom ASR (Automatic Speech Recognition) extensions. This modular architecture allows developers to create their own speech recognition implementations that integrate seamlessly with the mod's core functionality.

`mcmti` provides a unified interface for voice-to-text input in Minecraft, enabling players to use voice commands and dictation without typing. The mod handles microphone input, audio processing, and text input into Minecraft's chat and other input fields.

Key features of `mcmti` include:

- Modular ASR system with support for custom extensions
- Real-time speech recognition
- Configurable input settings
- Support for multiple languages (depending on the ASR implementation)
- Integration with Minecraft's native input system
- User-friendly configuration interface

This documentation provides guidelines for developers who want to create custom ASR extensions for `mcmti`, as well as information on how to integrate the mod into their own projects.

## Configuration

```groovy
// build.gradle

repositories {
    // ...
    maven {
        url = "https://api.modrinth.com/maven"
    }
}

dependencies {
    modImplementation "maven.modrinth:mcmti:${project.mcmti_3_x}"  // check your version at https://modrinth.com/mod/mcmti/versions
    // ...
}
```

## ASR Extension Development

You can create another mod that support other ASR APIs based on `mcmti`

### 1. Implement your custom SpeechRecognizer class
In your project, you should implement your custom [SpeechRecognizer](common/src/main/java/io/github/jaffe2718/mcmti/util/SpeechRecognizer.java)
to manage the lifecycle of speech recognizers, including initialization, activation, speech recognition, and deactivation.

```java
public class MySpeechRecognizer extends SpeechRecognizer {
    
    // private MyASR ctx = ...;
    
    @Override
    public boolean enabled() {
        // depends on your config or other logic, only the first enabled instance will be used
    }

    @Override
    protected @NotNull Text availableToast() {
        // show a toast message when the recognizer is available
        return Text.literal("My SpeechRecognizer is available");
    }

    @Override
    protected @NotNull Text unavailableToast() {
        // show a toast message when the recognizer is enabled but not available
        return Text.literal("My SpeechRecognizer is enabled but not available");
    }
    
    @Override
    protected boolean available() {
        // check if the model is loaded and available
        return super.available(); // && this.ctx != null && otherConditions
    }
    
    @Override
    public void activate() throws IOException {
        // load the model
        // ...
        super.activate();  // remember to call super.activate() to set the active flag to true
    }
    
    @Override
    public void deactivate() {
        // release the model to save memory
        // ...
        super.deactivate();  // remember to call super.deactivate() to set the active flag to false
    }
    
    @Override
    public String transcribe(float[] audio) {
        // use the model to recognize the audio
        // result = ...
        return result;
    }
}
```

### 2. Register your custom SpeechRecognizer

- Fabric version
```java

// register your custom SpeechRecognizer in the client side
public class MyModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // register your custom SpeechRecognizer
        int priority = 1;  // smaller value means higher priority
        SpeechRecognizer.register(priority, new MySpeechRecognizer());
        // ...
    }
}
```

- NeoForge version
```java
@Mod("my_mod")
public final class MyModNeoForge {
    public MyModNeoForge() {
        // register your custom SpeechRecognizer
        int priority = 1;  // smaller value means higher priority
        SpeechRecognizer.register(priority, new MySpeechRecognizer());
        // ...
    }
    // ...
}
```

### 3. Declare dependencies

- Fabric version: `fabric.mod.json`
```javascript
{
    // other metadata
    
    "depends": {
        "mcmti": ">=3.0.0"  // at least version 3.0.0
        // ...
    }
}
```

- NeoForge version: `neoforge.mods.toml`
```toml
# neoforge.mods.toml
[[mods]]
modId = "my_mod"
# ...

[[dependencies.my_mod]]
modId = "mcmti"
type = "required"
versionRange = "[3.x,)"
ordering = "AFTER"
side = "CLIENT"
```

## Tutorial: Implementing Qwen3 ASR Extension

We use [Qwen3 ASR](https://central.sonatype.com/artifact/io.github.jaffe2718/qwen3asr4j) as an example and 
[MidnightLib](https://www.midnightdust.eu/wiki/midnightlib/) as a configuration library to implement the ASR extension. 
This tutorial will guide you through the complete implementation process based on the example in `fabric/src/test`.

### 1. Project Structure

First, let's look at the project structure for the Qwen3 ASR extension:

```
qwen3asr_fabric/
├── src/
│   ├── main/
│   │   ├── java/io/github/jaffe2718/qwen3asr_fabric/
│   │   │   ├── config/
│   │   │   │   └── Qwen3ASRConfig.java            # Configuration class
│   │   │   ├── util/
│   │   │   │   └── Qwen3ASRSpeechRecognizer.java  # SpeechRecognizer implementation
│   │   │   └── Qwen3ASRFabricClient.java          # Client initializer
│   │   └── resources/
│   │       ├── assets/qwen3asr_fabric
│   │       │   └── icon.png
│   │       └── fabric.mod.json                    # Fabric mod metadata
└── build.gradle  # Build configuration
```

### 2. Configuration Management

Create a configuration class to manage settings for your ASR extension. The example uses MidnightLib for configuration [Qwen3ASRConfig.java](fabric/src/test/java/io/github/jaffe2718/qwen3asr_fabric/config/Qwen3ASRConfig.java):

```java
// Qwen3ASRConfig.java
public class Qwen3ASRConfig extends MidnightConfig {

    @Override
    public void writeChanges() {
        super.writeChanges();
        Thread.ofVirtual().start(SpeechRecognizer::init);
    }

    @Entry
    public static boolean enabled = true;

    @Entry(selectionMode = JFileChooser.FILES_ONLY, fileExtensions = {"gguf"})
    public static String modelPath = "";

    @Entry
    public static boolean customLibrary = false;

    @Entry(selectionMode = JFileChooser.DIRECTORIES_ONLY)
    @Condition(requiredOption = "customLibrary", requiredValue = "true")
    public static String customLibraryDir = "";

    // Other configuration fields...

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
```

### 3. SpeechRecognizer Implementation

Implement the `SpeechRecognizer` class with the following key methods in [Qwen3ASRSpeechRecognizer.java](fabric/src/test/java/io/github/jaffe2718/qwen3asr_fabric/util/Qwen3ASRSpeechRecognizer.java):

```java
// Qwen3ASRSpeechRecognizer.java
public class Qwen3ASRSpeechRecognizer extends SpeechRecognizer {

    private @Nullable Qwen3ASR ctx;

    @Override
    public boolean enabled() {
        return Qwen3ASRConfig.enabled;
    }

    @Override
    protected @NotNull Text availableToast() {
        return Text.literal("Qwen3 ASR is loaded!").setStyle(Style.EMPTY.withColor(0x55FF55));
    }

    @Override
    protected @NotNull Text unavailableToast() {
        return Text.literal("Failed to load Qwen3 ASR model!").setStyle(Style.EMPTY.withColor(0xFF5555));
    }

    @Override
    protected boolean available() {
        return super.available() && this.ctx != null && this.ctx.isLoaded();
    }

    @Override
    public void activate() throws IOException {
        this.deactivate();
        this.ctx = new Qwen3ASR(Qwen3ASRConfig.modelPath, Qwen3ASRFabricClient.LOGGER);
        super.activate();
    }

    @Override
    public void deactivate() {
        if (this.ctx != null) {
            this.ctx.close();
        }
        this.ctx = null;
        super.deactivate();
    }

    @Override
    public @NotNull String transcribe(float[] audio) {
        if (this.ctx != null && this.ctx.isLoaded()) {
            TranscribeResult result = this.ctx.transcribe(audio, Qwen3ASRConfig.getQwenParams());
            if (result.errorMsg().isBlank()) {
                return result.text();
            } else {
                Qwen3ASRFabricClient.LOGGER.error("Qwen3 ASR error: {}", result.errorMsg());
            }
        }
        return "";
    }

    public static boolean loadNativeLibrary() {
        try {
            if (Qwen3ASRConfig.customLibrary) {
                NativeManager.loadLibrary(Paths.get(Qwen3ASRConfig.customLibraryDir), Qwen3ASRFabricClient.LOGGER);
            } else {
                NativeManager.loadLibrary(Qwen3ASRFabricClient.LOGGER);
            }
        } catch (Exception ignored) {
            return false;
        }
        GGUFModelWrapper.setGGMLGlobalLogger(Qwen3ASRFabricClient.LOGGER);
        return true;
    }
}
```

### 4. Client Initialization and Registration

Register your custom `SpeechRecognizer` in the client initializer [Qwen3ASRFabricClient.java](fabric/src/test/java/io/github/jaffe2718/qwen3asr_fabric/Qwen3ASRFabricClient.java):

```java
// Qwen3ASRFabricClient.java
public class Qwen3ASRFabricClient implements ClientModInitializer {

    public static final String MOD_ID = "qwen3asr_fabric";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        MidnightConfig.init(MOD_ID, Qwen3ASRConfig.class);
        if (Qwen3ASRSpeechRecognizer.loadNativeLibrary()) {
            SpeechRecognizer.register(1024, new Qwen3ASRSpeechRecognizer());
        }
    }
}
```

### 5. Dependency Declaration

Add the necessary dependencies in your [fabric.mod.json](fabric/src/test/resources/fabric.mod.json):

```javascript
{
    "schemaVersion": 1,
    "id": "qwen3asr_fabric",
    "version": "0.0.1-alpha",
    "name": "Qwen3 ASR",
    "description": "Qwen3 ASR extension for Microphone Text Input.",
    "environment": "client",
    "entrypoints": {
        "client": [
            "io.github.jaffe2718.qwen3asr_fabric.Qwen3ASRFabricClient"
        ]
    },
    "depends": {
        "fabricloader": ">=0.15.0",
        "minecraft": "*",
        "midnightlib": "*",
        "mcmti": ">=3.0.0"
    },
   // other metadata...
}
```

### 6. Build Configuration

Add the necessary dependencies in your `build.gradle` file:

```groovy
repositories {
    // ...
    maven {
        url = "https://api.modrinth.com/maven"
    }
    // Add any other repositories needed for your ASR library
}

dependencies {
    modImplementation "maven.modrinth:mcmti:${project.mcmti_3_x}"
    modImplementation "eu.midnightdust:midnightlib:${project.midnightlib_version}"
    // Add your ASR library dependencies
    // Add your ASR library dependencies
    // implementation include "your.asr.library:version"
    // ...
}
```

### 7. Best Practices

1. **Error Handling**: Always include proper error handling in your `transcribe` method to ensure the mod doesn't crash if the ASR service fails.

2. **Resource Management**: Implement `deactivate()` properly to release resources when they're not needed, which helps save memory.

3. **Configuration Reloading**: Add a mechanism to reload the SpeechRecognizer when configuration changes, as shown in the `Qwen3ASRConfig.writeChanges()` method.

4. **Logging**: Use a logger to record important events and errors, which helps with debugging.

5. **User Feedback**: Provide clear toast messages to inform users about the status of your ASR extension.

### 8. Testing Your Extension

1. **Model Preparation**: Download the required model files (e.g., Qwen3 ASR GGUF model) and configure the model path in your mod's settings.

2. **Testing Workflow**:
   - Start Minecraft with your mod installed
   - Open the mod settings and ensure your ASR extension is enabled
   - Configure the model path and other settings
   - Use the microphone input feature to test speech recognition
   - Check the logs for any errors or warnings

3. **Troubleshooting**:
   - Ensure the native library is loaded correctly
   - Verify the model path is set correctly
   - Check the logs for any error messages
   - Ensure your mod has the necessary permissions to access the microphone

By following this tutorial and the example code, you can successfully implement your own ASR extension for the Microphone Text Input mod.