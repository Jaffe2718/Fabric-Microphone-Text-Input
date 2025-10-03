# **Microphone Text Input Mod Developer Documentation**

<div style="text-align: center;">
<p style="font-size: large;">Architectury 2.x</p>
</div>

<div style="text-align: center;">

<img alt="image" src="common/src/main/resources/assets/mcmti/icon.png"/>
</div>

## Introduction

The Microphone Text Input Mod is a Fabric mod designed for Minecraft clients. It provides speech recognition input
functionality and automatically converts spoken words into text chat messages, enhancing the in - game communication
experience.

## Features

- **Multiple Modes**: Supports different operation modes, including `AUTO_SEND`, `RELEASE_KEY_TO_SEND`, and
  `RELEASE_KEY_TO_INPUT`.
- **Advanced Configuration**: Allows users to adjust advanced parameters related to the Whisper library, such as the
  number of threads, audio context size, and sampling strategy.

## Dependencies

| Dependency Name  | Fabric                                                           | NeoForge                                                                          |
|------------------|------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| Java             | 21                                                               | 21                                                                                |
| Fabric API       | see [fabric.mod.json](fabric/src/main/resources/fabric.mod.json) | ❌                                                                                 |
| Architectury API | ❌                                                                | see [neoforge.mods.toml](neoforge/src/main/resources/META-INF/neoforge.mods.toml) |
| MidnightLib      | see [fabric.mod.json](fabric/src/main/resources/fabric.mod.json) | see [neoforge.mods.toml](neoforge/src/main/resources/META-INF/neoforge.mods.toml) |

## Configuration

### Keybinding

| Keybinding Name | Recognize                        |
|-----------------|----------------------------------|
| Category        | `key.category.minecraft.mcmti`   |
| Translation Key | `key.mcmti.recognize`            |
| Default Key     | `V`                              |

### General Settings

| Setting                   | Translation Key                         | Field                                                    | Type                                         | Default Value                            | Description                                                                                 |
|---------------------------|-----------------------------------------|----------------------------------------------------------|----------------------------------------------|------------------------------------------|---------------------------------------------------------------------------------------------|
| GGML Whisper Model        | `mcmti.midnightconfig.model`            | `me.jaffe2718.mcmti.config.McmtiConfig.model`            | `String`                                     | `""`                                     | Path to the GGML Whisper model.                                                             |
| Language                  | `mcmti.midnightconfig.language`         | `me.jaffe2718.mcmti.config.McmtiConfig.language`         | `String`                                     | `"en"`                                   | Language for speech recognition.                                                            |
| Mode                      | `mcmti.midnightconfig.mode`             | `me.jaffe2718.mcmti.config.McmtiConfig.mode`             | `me.jaffe2718.mcmti.config.McmtiConfig.Mode` | `"RELEASE_KEY_TO_SEND"`                  | Mod's work mode.                                                                            |
| Record Cycle (ms)         | `mcmti.midnightconfig.recordCycleMs`    | `me.jaffe2718.mcmti.config.McmtiConfig.recordCycleMs`    | `int`                                        | `5000`                                   | Record cycle in milliseconds.                                                               |
| Record Buffer Size (byte) | `mcmti.midnightconfig.recordBufferSize` | `me.jaffe2718.mcmti.config.McmtiConfig.recordBufferSize` | `int`                                        | `1024`                                   | Record buffer size in bytes.                                                                |
| Prefix                    | `mcmti.midnightconfig.prefix`           | `me.jaffe2718.mcmti.config.McmtiConfig.prefix`           | `String`                                     | `"⌈Speech Input⌋"`                       | Prefix added to the recognized text.                                                        |
| Draft Input               | `mcmti.midnightconfig.draftInput`       | `me.jaffe2718.mcmti.config.McmtiConfig.draftInput`       | `boolean`                                    | `false`                                  | Enable draft input. If enabled, the recognized text will be shown in the entry as a draft.  |
| Encoding Repair           | `mcmti.midnightconfig.encodingRepair`   | `me.jaffe2718.mcmti.config.McmtiConfig.encodingRepair`   | `boolean`                                    | `false`                                  | Enable encoding repair.                                                                     |
| Source Encoding           | `mcmti.midnightconfig.srcEncoding`      | `me.jaffe2718.mcmti.config.McmtiConfig.srcEncoding`      | `String`                                     | `Charset.defaultCharset().displayName()` | Source encoding for text. Applies only if encoding repair is enabled.                       |
| Destination Encoding      | `mcmti.midnightconfig.dstEncoding`      | `me.jaffe2718.mcmti.config.McmtiConfig.dstEncoding`      | `String`                                     | `Charset.defaultCharset().displayName()` | Destination encoding for text. Applies only if encoding repair is enabled.                  |

### Advanced Settings

| Setting                      | Translation Key                                     | Field                                                                | Type                                                           | Default Value | Description                                                                         |
|------------------------------|-----------------------------------------------------|----------------------------------------------------------------------|----------------------------------------------------------------|---------------|-------------------------------------------------------------------------------------|
| Enable Advanced Config       | `mcmti.midnightconfig.advancedConfig`               | `me.jaffe2718.mcmti.config.McmtiConfig.advancedConfig`               | `boolean`                                                      | `false`       | Enable advanced configuration.                                                      |
| nThreads                     | `mcmti.midnightconfig.nThreads`                     | `me.jaffe2718.mcmti.config.McmtiConfig.nThreads`                     | `int`                                                          | `0`           | Number of threads to use for the operation of the Whisper model. `0` for max cores. |
| audioCtx                     | `mcmti.midnightconfig.audioCtx`                     | `me.jaffe2718.mcmti.config.McmtiConfig.audioCtx`                     | `int`                                                          | `0`           | Audio context size for the Whisper model. `0` means use default.                    |
| nMaxTextCtx                  | `mcmti.midnightconfig.nMaxTextCtx`                  | `me.jaffe2718.mcmti.config.McmtiConfig.nMaxTextCtx`                  | `int`                                                          | `16384`       | Max tokens to use from past text as prompt for the decoder.                         |
| offsetMs                     | `mcmti.midnightconfig.offsetMs`                     | `me.jaffe2718.mcmti.config.McmtiConfig.offsetMs`                     | `int`                                                          | `0`           | Offset in ms to start recording from.                                               |
| durationMs                   | `mcmti.midnightconfig.durationMs`                   | `me.jaffe2718.mcmti.config.McmtiConfig.durationMs`                   | `int`                                                          | `0`           | Audio duration to process in ms. `0` means use default.                             |
| translate                    | `mcmti.midnightconfig.translate`                    | `me.jaffe2718.mcmti.config.McmtiConfig.translate`                    | `boolean`                                                      | `false`       | Translate the text to the default language.                                         |
| noTimestamps                 | `mcmti.midnightconfig.noTimestamps`                 | `me.jaffe2718.mcmti.config.McmtiConfig.noTimestamps`                 | `boolean`                                                      | `false`       | Do not generate timestamps.                                                         |
| detectLanguage               | `mcmti.midnightconfig.detectLanguage`               | `me.jaffe2718.mcmti.config.McmtiConfig.detectLanguage`               | `boolean`                                                      | `false`       | Detect the language of the input audio.                                             |
| initialPrompt                | `mcmti.midnightconfig.initialPrompt`                | `me.jaffe2718.mcmti.config.McmtiConfig.initialPrompt`                | `String`                                                       | `""`          | Initial text to use as a prompt for the whisper.                                    |
| noContext                    | `mcmti.midnightconfig.noContext`                    | `me.jaffe2718.mcmti.config.McmtiConfig.noContext`                    | `boolean`                                                      | `true`        | Do not use past transcription (if any) as initial prompt for the decoder.           |
| singleSegment                | `mcmti.midnightconfig.singleSegment`                | `me.jaffe2718.mcmti.config.McmtiConfig.singleSegment`                | `boolean`                                                      | `false`       | Force single segment output (useful for streaming).                                 |
| printSpecial                 | `mcmti.midnightconfig.printSpecial`                 | `me.jaffe2718.mcmti.config.McmtiConfig.printSpecial`                 | `boolean`                                                      | `false`       | Print special tokens.                                                               |
| printProgress                | `mcmti.midnightconfig.printProgress`                | `me.jaffe2718.mcmti.config.McmtiConfig.printProgress`                | `boolean`                                                      | `true`        | Print progress information.                                                         |
| printRealtime                | `mcmti.midnightconfig.printRealtime`                | `me.jaffe2718.mcmti.config.McmtiConfig.printRealtime`                | `boolean`                                                      | `false`       | Print results from within whisper.cpp (avoid it, use callback instead).             |
| printTimestamps              | `mcmti.midnightconfig.printTimestamps`              | `me.jaffe2718.mcmti.config.McmtiConfig.printTimestamps`              | `boolean`                                                      | `true`        | Print timestamps for each text segment when printing realtime.                      |
| suppressBlank                | `mcmti.midnightconfig.suppressBlank`                | `me.jaffe2718.mcmti.config.McmtiConfig.suppressBlank`                | `boolean`                                                      | `true`        | Decoder option.                                                                     |
| suppressNonSpeechTokens      | `mcmti.midnightconfig.suppressNonSpeechTokens`      | `me.jaffe2718.mcmti.config.McmtiConfig.suppressNonSpeechTokens`      | `boolean`                                                      | `true`        | Tokenizer option.                                                                   |
| temperature                  | `mcmti.midnightconfig.temperature`                  | `me.jaffe2718.mcmti.config.McmtiConfig.temperature`                  | `float`                                                        | `0.0f`        | Initial decoding temperature.                                                       |
| maxInitialTs                 | `mcmti.midnightconfig.maxInitialTs`                 | `me.jaffe2718.mcmti.config.McmtiConfig.maxInitialTs`                 | `float`                                                        | `1.0f`        | Maximum initial timestamp.                                                          |
| lengthPenalty                | `mcmti.midnightconfig.lengthPenalty`                | `me.jaffe2718.mcmti.config.McmtiConfig.lengthPenalty`                | `float`                                                        | `-1.0f`       | Length penalty.                                                                     |
| temperatureInc               | `mcmti.midnightconfig.temperatureInc`               | `me.jaffe2718.mcmti.config.McmtiConfig.temperatureInc`               | `float`                                                        | `0.4f`        | Temperature increment.                                                              |
| entropyThold                 | `mcmti.midnightconfig.entropyThold`                 | `me.jaffe2718.mcmti.config.McmtiConfig.entropyThold`                 | `float`                                                        | `2.4f`        | Entropy threshold (similar to OpenAI's "compression_ratio_threshold").              |
| logprobThold                 | `mcmti.midnightconfig.logprobThold`                 | `me.jaffe2718.mcmti.config.McmtiConfig.logprobThold`                 | `float`                                                        | `-1.0f`       | Log probability threshold.                                                          |
| noSpeechThold                | `mcmti.midnightconfig.noSpeechThold`                | `me.jaffe2718.mcmti.config.McmtiConfig.noSpeechThold`                | `float`                                                        | `0.6f`        | No speech threshold.                                                                |
| greedyBestOf                 | `mcmti.midnightconfig.greedyBestOf`                 | `me.jaffe2718.mcmti.config.McmtiConfig.greedyBestOf`                 | `int`                                                          | `-1`          | Specific to greedy sampling strategy.                                               |
| beamSearchBeamSize           | `mcmti.midnightconfig.beamSearchBeamSize`           | `me.jaffe2718.mcmti.config.McmtiConfig.beamSearchBeamSize`           | `int`                                                          | `2`           | Specific to bean search sampling strategy.                                          |
| beamSearchPatience           | `mcmti.midnightconfig.beamSearchPatience`           | `me.jaffe2718.mcmti.config.McmtiConfig.beamSearchPatience`           | `float`                                                        | `-1.0f`       | Specific to bean search sampling strategy.                                          |
| grammar                      | `mcmti.midnightconfig.grammar`                      | `me.jaffe2718.mcmti.config.McmtiConfig.grammar`                      | `String`                                                       | `""`          | Grammar file path. Empty means no grammar.                                          |
| grammarPenalty               | `mcmti.midnightconfig.grammarPenalty`               | `me.jaffe2718.mcmti.config.McmtiConfig.grammarPenalty`               | `float`                                                        | `100.0f`      | Penalty for non grammar tokens.                                                     |
| whisperSamplingStrategy      | `mcmti.midnightconfig.whisperSamplingStrategy`      | `me.jaffe2718.mcmti.config.McmtiConfig.whisperSamplingStrategy`      | `io.github.freshsupasulley.whisperjni.WhisperSamplingStrategy` | `BEAM_SEARCH` | The `WhisperContext` enum to configure whisper's sampling strategy.                 |
| vad                          | `mcmti.midnightconfig.vad`                          | `me.jaffe2718.mcmti.config.McmtiConfig.vad`                          | `boolean`                                                      | `false`       | Enable VAD (Voice Activity Detection).                                              |
| vad__max_speech_duration_s   | `mcmti.midnightconfig.vad__max_speech_duration_s`   | `me.jaffe2718.mcmti.config.McmtiConfig.vad__max_speech_duration_s`   | `float`                                                        | `0f`          | Max duration of a speech segment before forcing a new segment.                      |
| vad__min_silence_duration_ms | `mcmti.midnightconfig.vad__min_silence_duration_ms` | `me.jaffe2718.mcmti.config.McmtiConfig.vad__min_silence_duration_ms` | `int`                                                          | `0`           | Min silence duration to consider speech as ended.                                   |
| vad__min_speech_duration_ms  | `mcmti.midnightconfig.vad__min_speech_duration_ms`  | `me.jaffe2718.mcmti.config.McmtiConfig.vad__min_speech_duration_ms`  | `int`                                                          | `0`           | Min duration for a valid speech segment.                                            |
| vad__samples_overlap         | `mcmti.midnightconfig.vad__samples_overlap`         | `me.jaffe2718.mcmti.config.McmtiConfig.vad__samples_overlap`         | `float`                                                        | `0f`          | Overlap in seconds when copying audio samples from speech segment.                  |
| vad__speech_pad_ms           | `mcmti.midnightconfig.vad__speech_pad_ms`           | `me.jaffe2718.mcmti.config.McmtiConfig.vad__speech_pad_ms`           | `int`                                                          | `0`           | Padding added before and after speech segments.                                     |
| vad__threshold               | `mcmti.midnightconfig.vad__threshold`               | `me.jaffe2718.mcmti.config.McmtiConfig.vad__threshold`               | `float`                                                        | `0f`          | Probability threshold to consider as speech.                                        |
| vad_model_path               | `mcmti.midnightconfig.vad_model_path`               | `me.jaffe2718.mcmti.config.McmtiConfig.vad_model_path`               | `String`                                                       | `""`          | Path to the VAD model. Empty means use default.                                     |
| useCustomDynamicLib          | `mcmti.midnightconfig.useCustomDynamicLib`          | `me.jaffe2718.mcmti.config.McmtiConfig.useCustomDynamicLib`          | `boolean`                                                      | `false`       | Enable using custom dynamic library for Whisper.                                    |
| customDynamicLibDir          | `mcmti.midnightconfig.customDynamicLibDir`          | `me.jaffe2718.mcmti.config.McmtiConfig.customDynamicLibDir`          | `String`                                                       | `""`          | Custom dynamic link library directory for Whisper.                                  |

- For more info,
  see [WhisperFullParams.java](https://github.com/FreshSupaSulley/whisper-jni/blob/main/src/main/java/io/github/freshsupasulley/whisperjni/WhisperFullParams.java)

> WARNING: Activating the advanced configuration will change the default parameters of the Whisper model configuration,
> which will have a critical impact on the speech recognition results. Inappropriate configuration of advanced
> parameters can lead to problems such as speech recognition failure, high computer resource usage, and program crashes.
> Please use with caution.

## Usage

1. Install the mod and download the [GGML Whisper model](https://huggingface.co/ggerganov/whisper.cpp/tree/main).
2. Configure the mod according to your needs in the configuration menu.
3. Use the keybinding you set (default is `V`) to start recording and speech recognition.
4. If the mode is set to `AUTO_SEND`, the recognized text will be automatically sent as a chat message. If set to
   `RELEASE_KEY_TO_SEND` or `RELEASE_KEY_TO_INPUT`, follow the corresponding key - release actions.

### Use VAD
If you want to use VAD (Voice Activity Detection), you need to download [VAD](https://huggingface.co/ggml-org/whisper-vad) model,
enable the advanced configuration and set the `vad_model_path` to the path of the VAD model in the configuration menu.

### Custom Dynamic Library

1. Download the custom dynamic library from [FreshSupaSulley/whisper-jni/releases](https://github.com/FreshSupaSulley/whisper-jni/releases/tag/v0.4.0) and extract the files.
2. Enable the advanced configuration and set the `useCustomDynamicLib` to `true` in the configuration menu.
3. Set the `customDynamicLibDir` to the directory where the custom dynamic library is located in the configuration menu.
4. If you want to use the dynamic library which is supported vulkan, check your check that your computer has drivers and libraries running Vulkan installed.
   ```shell
   vulkaninfo
   ```

## Troubleshooting

- **Audio Input Device Load Failed**: Please check if Java has access to the audio input device.
- **Whisper Model Load Failed**: Make sure the path to the GGML Whisper model is correct.

## Contributing

If you'd like to contribute to this project, please feel free to submit issues or pull requests
on [GitHub](https://github.com/Jaffe2718/Fabric-Microphone-Text-Input).

## License

This mod is released under the [MIT License](LICENSE).