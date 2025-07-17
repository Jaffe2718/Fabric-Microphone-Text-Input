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

| Keybinding Name | Recognize              |
|-----------------|------------------------|
| Category        | `key.categories.mcmti` |
| Translation Key | `mcmti.key.recognize`  |
| Default Key     | `V`                    |

### General Settings

| Setting                   | Translation Key                         | Field                                                        | Type                                             | Default Value                            | Description                                                                |
|---------------------------|-----------------------------------------|--------------------------------------------------------------|--------------------------------------------------|------------------------------------------|----------------------------------------------------------------------------|
| GGML Whisper Model        | `mcmti.midnightconfig.model`            | `github.jaffe2718.mcmti.config.McmtiConfig.model`            | `String`                                         | `""`                                     | Path to the GGML Whisper model.                                            |
| Language                  | `mcmti.midnightconfig.language`         | `github.jaffe2718.mcmti.config.McmtiConfig.language`         | `String`                                         | `"en"`                                   | Language for speech recognition.                                           |
| Mode                      | `mcmti.midnightconfig.mode`             | `github.jaffe2718.mcmti.config.McmtiConfig.mode`             | `github.jaffe2718.mcmti.config.McmtiConfig.Mode` | `"RELEASE_KEY_TO_SEND"`                  | Mod's work mode.                                                           |
| Record Cycle (ms)         | `mcmti.midnightconfig.recordCycleMs`    | `github.jaffe2718.mcmti.config.McmtiConfig.recordCycleMs`    | `int`                                            | `5000`                                   | Record cycle in milliseconds.                                              |
| Record Buffer Size (byte) | `mcmti.midnightconfig.recordBufferSize` | `github.jaffe2718.mcmti.config.McmtiConfig.recordBufferSize` | `int`                                            | `1024`                                   | Record buffer size in bytes.                                               |
| Prefix                    | `mcmti.midnightconfig.prefix`           | `github.jaffe2718.mcmti.config.McmtiConfig.prefix`           | `String`                                         | `"⌈Speech Input⌋"`                       | Prefix added to the recognized text.                                       |
| Encoding Repair           | `mcmti.midnightconfig.encodingRepair`   | `github.jaffe2718.mcmti.config.McmtiConfig.encodingRepair`   | `boolean`                                        | `false`                                  | Enable encoding repair.                                                    |
| Source Encoding           | `mcmti.midnightconfig.srcEncoding`      | `github.jaffe2718.mcmti.config.McmtiConfig.srcEncoding`      | `String`                                         | `Charset.defaultCharset().displayName()` | Source encoding for text. Applies only if encoding repair is enabled.      |
| Destination Encoding      | `mcmti.midnightconfig.dstEncoding`      | `github.jaffe2718.mcmti.config.McmtiConfig.dstEncoding`      | `String`                                         | `Charset.defaultCharset().displayName()` | Destination encoding for text. Applies only if encoding repair is enabled. |

### Advanced Settings

| Setting                      | Translation Key                                     | Field                                                                    | Type                                                           | Default Value | Description                                                                         |
|------------------------------|-----------------------------------------------------|--------------------------------------------------------------------------|----------------------------------------------------------------|---------------|-------------------------------------------------------------------------------------|
| Enable Advanced Config       | `mcmti.midnightconfig.advancedConfig`               | `github.jaffe2718.mcmti.config.McmtiConfig.advancedConfig`               | `boolean`                                                      | `false`       | Enable advanced configuration.                                                      |
| useVulkan                    | `mcmti.midnightconfig.useVulkan`                    | `github.jaffe2718.mcmti.config.McmtiConfig.useVulkan`                    | `boolean`                                                      | `false`       | Use Vulkan for GPU acceleration.                                                    |
| nThreads                     | `mcmti.midnightconfig.nThreads`                     | `github.jaffe2718.mcmti.config.McmtiConfig.nThreads`                     | `int`                                                          | `0`           | Number of threads to use for the operation of the Whisper model. `0` for max cores. |
| audioCtx                     | `mcmti.midnightconfig.audioCtx`                     | `github.jaffe2718.mcmti.config.McmtiConfig.audioCtx`                     | `int`                                                          | `0`           | Audio context size for the Whisper model. `0` means use default.                    |
| nMaxTextCtx                  | `mcmti.midnightconfig.nMaxTextCtx`                  | `github.jaffe2718.mcmti.config.McmtiConfig.nMaxTextCtx`                  | `int`                                                          | `16384`       | Max tokens to use from past text as prompt for the decoder.                         |
| offsetMs                     | `mcmti.midnightconfig.offsetMs`                     | `github.jaffe2718.mcmti.config.McmtiConfig.offsetMs`                     | `int`                                                          | `0`           | Offset in ms to start recording from.                                               |
| durationMs                   | `mcmti.midnightconfig.durationMs`                   | `github.jaffe2718.mcmti.config.McmtiConfig.durationMs`                   | `int`                                                          | `0`           | Audio duration to process in ms. `0` means use default.                             |
| translate                    | `mcmti.midnightconfig.translate`                    | `github.jaffe2718.mcmti.config.McmtiConfig.translate`                    | `boolean`                                                      | `false`       | Translate the text to the default language.                                         |
| noTimestamps                 | `mcmti.midnightconfig.noTimestamps`                 | `github.jaffe2718.mcmti.config.McmtiConfig.noTimestamps`                 | `boolean`                                                      | `false`       | Do not generate timestamps.                                                         |
| detectLanguage               | `mcmti.midnightconfig.detectLanguage`               | `github.jaffe2718.mcmti.config.McmtiConfig.detectLanguage`               | `boolean`                                                      | `false`       | Detect the language of the input audio.                                             |
| initialPrompt                | `mcmti.midnightconfig.initialPrompt`                | `github.jaffe2718.mcmti.config.McmtiConfig.initialPrompt`                | `String`                                                       | `""`          | Initial text to use as a prompt for the whisper.                                    |
| noContext                    | `mcmti.midnightconfig.noContext`                    | `github.jaffe2718.mcmti.config.McmtiConfig.noContext`                    | `boolean`                                                      | `true`        | Do not use past transcription (if any) as initial prompt for the decoder.           |
| singleSegment                | `mcmti.midnightconfig.singleSegment`                | `github.jaffe2718.mcmti.config.McmtiConfig.singleSegment`                | `boolean`                                                      | `false`       | Force single segment output (useful for streaming).                                 |
| printSpecial                 | `mcmti.midnightconfig.printSpecial`                 | `github.jaffe2718.mcmti.config.McmtiConfig.printSpecial`                 | `boolean`                                                      | `false`       | Print special tokens.                                                               |
| printProgress                | `mcmti.midnightconfig.printProgress`                | `github.jaffe2718.mcmti.config.McmtiConfig.printProgress`                | `boolean`                                                      | `true`        | Print progress information.                                                         |
| printRealtime                | `mcmti.midnightconfig.printRealtime`                | `github.jaffe2718.mcmti.config.McmtiConfig.printRealtime`                | `boolean`                                                      | `false`       | Print results from within whisper.cpp (avoid it, use callback instead).             |
| printTimestamps              | `mcmti.midnightconfig.printTimestamps`              | `github.jaffe2718.mcmti.config.McmtiConfig.printTimestamps`              | `boolean`                                                      | `true`        | Print timestamps for each text segment when printing realtime.                      |
| suppressBlank                | `mcmti.midnightconfig.suppressBlank`                | `github.jaffe2718.mcmti.config.McmtiConfig.suppressBlank`                | `boolean`                                                      | `true`        | Decoder option.                                                                     |
| suppressNonSpeechTokens      | `mcmti.midnightconfig.suppressNonSpeechTokens`      | `github.jaffe2718.mcmti.config.McmtiConfig.suppressNonSpeechTokens`      | `boolean`                                                      | `true`        | Tokenizer option.                                                                   |
| temperature                  | `mcmti.midnightconfig.temperature`                  | `github.jaffe2718.mcmti.config.McmtiConfig.temperature`                  | `float`                                                        | `0.0f`        | Initial decoding temperature.                                                       |
| maxInitialTs                 | `mcmti.midnightconfig.maxInitialTs`                 | `github.jaffe2718.mcmti.config.McmtiConfig.maxInitialTs`                 | `float`                                                        | `1.0f`        | Maximum initial timestamp.                                                          |
| lengthPenalty                | `mcmti.midnightconfig.lengthPenalty`                | `github.jaffe2718.mcmti.config.McmtiConfig.lengthPenalty`                | `float`                                                        | `-1.0f`       | Length penalty.                                                                     |
| temperatureInc               | `mcmti.midnightconfig.temperatureInc`               | `github.jaffe2718.mcmti.config.McmtiConfig.temperatureInc`               | `float`                                                        | `0.4f`        | Temperature increment.                                                              |
| entropyThold                 | `mcmti.midnightconfig.entropyThold`                 | `github.jaffe2718.mcmti.config.McmtiConfig.entropyThold`                 | `float`                                                        | `2.4f`        | Entropy threshold (similar to OpenAI's "compression_ratio_threshold").              |
| logprobThold                 | `mcmti.midnightconfig.logprobThold`                 | `github.jaffe2718.mcmti.config.McmtiConfig.logprobThold`                 | `float`                                                        | `-1.0f`       | Log probability threshold.                                                          |
| noSpeechThold                | `mcmti.midnightconfig.noSpeechThold`                | `github.jaffe2718.mcmti.config.McmtiConfig.noSpeechThold`                | `float`                                                        | `0.6f`        | No speech threshold.                                                                |
| greedyBestOf                 | `mcmti.midnightconfig.greedyBestOf`                 | `github.jaffe2718.mcmti.config.McmtiConfig.greedyBestOf`                 | `int`                                                          | `-1`          | Specific to greedy sampling strategy.                                               |
| beamSearchBeamSize           | `mcmti.midnightconfig.beamSearchBeamSize`           | `github.jaffe2718.mcmti.config.McmtiConfig.beamSearchBeamSize`           | `int`                                                          | `2`           | Specific to bean search sampling strategy.                                          |
| beamSearchPatience           | `mcmti.midnightconfig.beamSearchPatience`           | `github.jaffe2718.mcmti.config.McmtiConfig.beamSearchPatience`           | `float`                                                        | `-1.0f`       | Specific to bean search sampling strategy.                                          |
| grammar                      | `mcmti.midnightconfig.grammar`                      | `github.jaffe2718.mcmti.config.McmtiConfig.grammar`                      | `String`                                                       | `""`          | Grammar file path. Empty means no grammar.                                          |
| grammarPenalty               | `mcmti.midnightconfig.grammarPenalty`               | `github.jaffe2718.mcmti.config.McmtiConfig.grammarPenalty`               | `float`                                                        | `100.0f`      | Penalty for non grammar tokens.                                                     |
| whisperSamplingStrategy      | `mcmti.midnightconfig.whisperSamplingStrategy`      | `github.jaffe2718.mcmti.config.McmtiConfig.whisperSamplingStrategy`      | `io.github.freshsupasulley.whisperjni.WhisperSamplingStrategy` | `BEAM_SEARCH` | The `WhisperContext` enum to configure whisper's sampling strategy.                 |
| vad                          | `mcmti.midnightconfig.vad`                          | `github.jaffe2718.mcmti.config.McmtiConfig.vad`                          | `boolean`                                                      | `false`       | Enable VAD (Voice Activity Detection).                                              |
| vad__max_speech_duration_s   | `mcmti.midnightconfig.vad__max_speech_duration_s`   | `github.jaffe2718.mcmti.config.McmtiConfig.vad__max_speech_duration_s`   | `float`                                                        | `0f`          | Max duration of a speech segment before forcing a new segment.                      |
| vad__min_silence_duration_ms | `mcmti.midnightconfig.vad__min_silence_duration_ms` | `github.jaffe2718.mcmti.config.McmtiConfig.vad__min_silence_duration_ms` | `int`                                                          | `0`           | Min silence duration to consider speech as ended.                                   |
| vad__min_speech_duration_ms  | `mcmti.midnightconfig.vad__min_speech_duration_ms`  | `github.jaffe2718.mcmti.config.McmtiConfig.vad__min_speech_duration_ms`  | `int`                                                          | `0`           | Min duration for a valid speech segment.                                            |
| vad__samples_overlap         | `mcmti.midnightconfig.vad__samples_overlap`         | `github.jaffe2718.mcmti.config.McmtiConfig.vad__samples_overlap`         | `float`                                                        | `0f`          | Overlap in seconds when copying audio samples from speech segment.                  |
| vad__speech_pad_ms           | `mcmti.midnightconfig.vad__speech_pad_ms`           | `github.jaffe2718.mcmti.config.McmtiConfig.vad__speech_pad_ms`           | `int`                                                          | `0`           | Padding added before and after speech segments.                                     |
| vad__threshold               | `mcmti.midnightconfig.vad__threshold`               | `github.jaffe2718.mcmti.config.McmtiConfig.vad__threshold`               | `float`                                                        | `0f`          | Probability threshold to consider as speech.                                        |
| vad_model_path               | `mcmti.midnightconfig.vad_model_path`               | `github.jaffe2718.mcmti.config.McmtiConfig.vad_model_path`               | `String`                                                       | `""`          | Path to the VAD model. Empty means use default.                                     |

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

### Use Vulkan

Only support vulkan on Windows x64 (Advanced Settings), see [WhisperJNI#canUseVulkan()](https://github.com/FreshSupaSulley/whisper-jni/blob/main/src/main/java/io/github/freshsupasulley/whisperjni/WhisperJNI.java#L534).
You need to enable the advanced configuration and set the `useVulkan` to `true` in the configuration menu.

## Troubleshooting

- **Audio Input Device Load Failed**: Please check if Java has access to the audio input device.
- **Whisper Model Load Failed**: Make sure the path to the GGML Whisper model is correct.

## Contributing

If you'd like to contribute to this project, please feel free to submit issues or pull requests
on [GitHub](https://github.com/Jaffe2718/Fabric-Microphone-Text-Input).

## License

This mod is released under the [MIT License](LICENSE).