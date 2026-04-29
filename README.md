# **Microphone Text Input Mod Developer Documentation**

<div style="text-align: center;">
<p style="font-size: large;">3.x</p>
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
- **ASR Extension**: Supports custom ASR models, enabling developers to integrate their own ASR models into the mod, see [DEV.md](DEV.md).

## Dependencies

| Dependency Name  | Fabric                                                           | NeoForge                                                                          |
|------------------|------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| Java             | 21                                                               | 21                                                                                |
| Fabric API       | see [fabric.mod.json](fabric/src/main/resources/fabric.mod.json) | ❌                                                                                 |
| MidnightLib      | see [fabric.mod.json](fabric/src/main/resources/fabric.mod.json) | see [neoforge.mods.toml](neoforge/src/main/resources/META-INF/neoforge.mods.toml) |

## Configuration

### Keybinding

| Keybinding Name | Recognize                        |
|-----------------|----------------------------------|
| Category        | `key.category.minecraft.mcmti`   |
| Translation Key | `key.mcmti.recognize`            |
| Default Key     | `V`                              |

### General Settings

| Setting                   | Translation Key                         | Field                                                           | Type                                                | Default Value                                                               | Description                                                                                |
|---------------------------|-----------------------------------------|-----------------------------------------------------------------|-----------------------------------------------------|-----------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| GGML Whisper Model        | `mcmti.midnightconfig.model`            | `io.github.jaffe2718.mcmti.config.McmtiConfig#model`            | `String`                                            | `"https://huggingface.co/ggerganov/whisper.cpp/resolve/main/ggml-base.bin"` | Path or URL to the GGML Whisper model.                                                     |
| Language                  | `mcmti.midnightconfig.language`         | `io.github.jaffe2718.mcmti.config.McmtiConfig#language`         | `String`                                            | `"en"`                                                                      | Language for speech recognition.                                                           |
| Mode                      | `mcmti.midnightconfig.mode`             | `io.github.jaffe2718.mcmti.config.McmtiConfig#mode`             | `io.github.jaffe2718.mcmti.config.McmtiConfig.Mode` | `"RELEASE_KEY_TO_SEND"`                                                     | Mod's work mode.                                                                           |
| Record Cycle (ms)         | `mcmti.midnightconfig.recordCycleMs`    | `io.github.jaffe2718.mcmti.config.McmtiConfig#recordCycleMs`    | `int`                                               | `5000`                                                                      | Record cycle in milliseconds.                                                              |
| Record Buffer Size (byte) | `mcmti.midnightconfig.recordBufferSize` | `io.github.jaffe2718.mcmti.config.McmtiConfig#recordBufferSize` | `int`                                               | `1024`                                                                      | Record buffer size in bytes.                                                               |
| Prefix                    | `mcmti.midnightconfig.prefix`           | `io.github.jaffe2718.mcmti.config.McmtiConfig#prefix`           | `String`                                            | `"⌈Speech Input⌋"`                                                          | Prefix added to the recognized text.                                                       |
| Draft Input               | `mcmti.midnightconfig.draftInput`       | `io.github.jaffe2718.mcmti.config.McmtiConfig#draftInput`       | `boolean`                                           | `false`                                                                     | Enable draft input. If enabled, the recognized text will be shown in the entry as a draft. |
| Encoding Repair           | `mcmti.midnightconfig.encodingRepair`   | `io.github.jaffe2718.mcmti.config.McmtiConfig#encodingRepair`   | `boolean`                                           | `false`                                                                     | Enable encoding repair.                                                                    |
| Source Encoding           | `mcmti.midnightconfig.srcEncoding`      | `io.github.jaffe2718.mcmti.config.McmtiConfig#srcEncoding`      | `String`                                            | `Charset.defaultCharset().displayName()`                                    | Source encoding for text. Applies only if encoding repair is enabled.                      |
| Destination Encoding      | `mcmti.midnightconfig.dstEncoding`      | `io.github.jaffe2718.mcmti.config.McmtiConfig#dstEncoding`      | `String`                                            | `Charset.defaultCharset().displayName()`                                    | Destination encoding for text. Applies only if encoding repair is enabled.                 |

### Advanced Settings

| Setting                      | Translation Key                                     | Field                                                                       | Type                                                            | Default Value | Description                                                                         |
|------------------------------|-----------------------------------------------------|-----------------------------------------------------------------------------|-----------------------------------------------------------------|---------------|-------------------------------------------------------------------------------------|
| Enable Advanced Config       | `mcmti.midnightconfig.advancedConfig`               | `io.github.jaffe2718.mcmti.config.McmtiConfig#advancedConfig`               | `boolean`                                                       | `false`       | Enable advanced configuration.                                                      |
| nThreads                     | `mcmti.midnightconfig.nThreads`                     | `io.github.jaffe2718.mcmti.config.McmtiConfig#nThreads`                     | `int`                                                           | `0`           | Number of threads to use for the operation of the Whisper model. `0` for max cores. |
| audioCtx                     | `mcmti.midnightconfig.audioCtx`                     | `io.github.jaffe2718.mcmti.config.McmtiConfig#audioCtx`                     | `int`                                                           | `0`           | Audio context size for the Whisper model. `0` means use default.                    |
| nMaxTextCtx                  | `mcmti.midnightconfig.nMaxTextCtx`                  | `io.github.jaffe2718.mcmti.config.McmtiConfig#nMaxTextCtx`                  | `int`                                                           | `16384`       | Max tokens to use from past text as prompt for the decoder.                         |
| offsetMs                     | `mcmti.midnightconfig.offsetMs`                     | `io.github.jaffe2718.mcmti.config.McmtiConfig#offsetMs`                     | `int`                                                           | `0`           | Offset in ms to start recording from.                                               |
| durationMs                   | `mcmti.midnightconfig.durationMs`                   | `io.github.jaffe2718.mcmti.config.McmtiConfig#durationMs`                   | `int`                                                           | `0`           | Audio duration to process in ms. `0` means use default.                             |
| translate                    | `mcmti.midnightconfig.translate`                    | `io.github.jaffe2718.mcmti.config.McmtiConfig#translate`                    | `boolean`                                                       | `false`       | Translate the text to the default language.                                         |
| noTimestamps                 | `mcmti.midnightconfig.noTimestamps`                 | `io.github.jaffe2718.mcmti.config.McmtiConfig#noTimestamps`                 | `boolean`                                                       | `false`       | Do not generate timestamps.                                                         |
| initialPrompt                | `mcmti.midnightconfig.initialPrompt`                | `io.github.jaffe2718.mcmti.config.McmtiConfig#initialPrompt`                | `String`                                                        | `""`          | Initial text to use as a prompt for the whisper.                                    |
| noContext                    | `mcmti.midnightconfig.noContext`                    | `io.github.jaffe2718.mcmti.config.McmtiConfig#noContext`                    | `boolean`                                                       | `true`        | Do not use past transcription (if any) as initial prompt for the decoder.           |
| singleSegment                | `mcmti.midnightconfig.singleSegment`                | `io.github.jaffe2718.mcmti.config.McmtiConfig#singleSegment`                | `boolean`                                                       | `false`       | Force single segment output (useful for streaming).                                 |
| printSpecial                 | `mcmti.midnightconfig.printSpecial`                 | `io.github.jaffe2718.mcmti.config.McmtiConfig#printSpecial`                 | `boolean`                                                       | `false`       | Print special tokens.                                                               |
| suppressBlank                | `mcmti.midnightconfig.suppressBlank`                | `io.github.jaffe2718.mcmti.config.McmtiConfig#suppressBlank`                | `boolean`                                                       | `true`        | Decoder option.                                                                     |
| suppressNonSpeechTokens      | `mcmti.midnightconfig.suppressNonSpeechTokens`      | `io.github.jaffe2718.mcmti.config.McmtiConfig#suppressNonSpeechTokens`      | `boolean`                                                       | `true`        | Tokenizer option.                                                                   |
| temperature                  | `mcmti.midnightconfig.temperature`                  | `io.github.jaffe2718.mcmti.config.McmtiConfig#temperature`                  | `float`                                                         | `0.0f`        | Initial decoding temperature.                                                       |
| maxInitialTs                 | `mcmti.midnightconfig.maxInitialTs`                 | `io.github.jaffe2718.mcmti.config.McmtiConfig#maxInitialTs`                 | `float`                                                         | `1.0f`        | Maximum initial timestamp.                                                          |
| lengthPenalty                | `mcmti.midnightconfig.lengthPenalty`                | `io.github.jaffe2718.mcmti.config.McmtiConfig#lengthPenalty`                | `float`                                                         | `-1.0f`       | Length penalty.                                                                     |
| temperatureInc               | `mcmti.midnightconfig.temperatureInc`               | `io.github.jaffe2718.mcmti.config.McmtiConfig#temperatureInc`               | `float`                                                         | `0.4f`        | Temperature increment.                                                              |
| entropyThold                 | `mcmti.midnightconfig.entropyThold`                 | `io.github.jaffe2718.mcmti.config.McmtiConfig#entropyThold`                 | `float`                                                         | `2.4f`        | Entropy threshold (similar to OpenAI's "compression_ratio_threshold").              |
| logprobThold                 | `mcmti.midnightconfig.logprobThold`                 | `io.github.jaffe2718.mcmti.config.McmtiConfig#logprobThold`                 | `float`                                                         | `-1.0f`       | Log probability threshold.                                                          |
| noSpeechThold                | `mcmti.midnightconfig.noSpeechThold`                | `io.github.jaffe2718.mcmti.config.McmtiConfig#noSpeechThold`                | `float`                                                         | `0.6f`        | No speech threshold.                                                                |
| greedyBestOf                 | `mcmti.midnightconfig.greedyBestOf`                 | `io.github.jaffe2718.mcmti.config.McmtiConfig#greedyBestOf`                 | `int`                                                           | `-1`          | Specific to greedy sampling strategy.                                               |
| beamSearchBeamSize           | `mcmti.midnightconfig.beamSearchBeamSize`           | `io.github.jaffe2718.mcmti.config.McmtiConfig#beamSearchBeamSize`           | `int`                                                           | `2`           | Specific to bean search sampling strategy.                                          |
| beamSearchPatience           | `mcmti.midnightconfig.beamSearchPatience`           | `io.github.jaffe2718.mcmti.config.McmtiConfig#beamSearchPatience`           | `float`                                                         | `-1.0f`       | Specific to bean search sampling strategy.                                          |
| grammar                      | `mcmti.midnightconfig.grammar`                      | `io.github.jaffe2718.mcmti.config.McmtiConfig#grammar`                      | `String`                                                        | `""`          | Grammar file path. Empty means no grammar.                                          |
| grammarPenalty               | `mcmti.midnightconfig.grammarPenalty`               | `io.github.jaffe2718.mcmti.config.McmtiConfig#grammarPenalty`               | `float`                                                         | `100.0f`      | Penalty for non grammar tokens.                                                     |
| whisperSamplingStrategy      | `mcmti.midnightconfig.whisperSamplingStrategy`      | `io.github.jaffe2718.mcmti.config.McmtiConfig#whisperSamplingStrategy`      | `io.github.jaffe2718.mcmti.config.McmtiConfig.SamplingStrategy` | `BEAM_SEARCH` | The `SamplingStrategy` enum to configure whisper's sampling strategy.               |
| vad                          | `mcmti.midnightconfig.vad`                          | `io.github.jaffe2718.mcmti.config.McmtiConfig#vad`                          | `io.github.jaffe2718.mcmti.config.McmtiConfig.WhisperVad`       | `DISABLED`    | Enable VAD (Voice Activity Detection).                                              |
| vad_model_path               | `mcmti.midnightconfig.vad_model_path`               | `io.github.jaffe2718.mcmti.config.McmtiConfig#vad_model_path`               | `String`                                                        | `""`          | Path to the VAD model. Empty means use default.                                     |
| vad__max_speech_duration_s   | `mcmti.midnightconfig.vad__max_speech_duration_s`   | `io.github.jaffe2718.mcmti.config.McmtiConfig#vad__max_speech_duration_s`   | `float`                                                         | `0f`          | Max duration of a speech segment before forcing a new segment.                      |
| vad__min_silence_duration_ms | `mcmti.midnightconfig.vad__min_silence_duration_ms` | `io.github.jaffe2718.mcmti.config.McmtiConfig#vad__min_silence_duration_ms` | `int`                                                           | `0`           | Min silence duration to consider speech as ended.                                   |
| vad__min_speech_duration_ms  | `mcmti.midnightconfig.vad__min_speech_duration_ms`  | `io.github.jaffe2718.mcmti.config.McmtiConfig#vad__min_speech_duration_ms`  | `int`                                                           | `0`           | Min duration for a valid speech segment.                                            |
| vad__samples_overlap         | `mcmti.midnightconfig.vad__samples_overlap`         | `io.github.jaffe2718.mcmti.config.McmtiConfig#vad__samples_overlap`         | `float`                                                         | `0f`          | Overlap in seconds when copying audio samples from speech segment.                  |
| vad__speech_pad_ms           | `mcmti.midnightconfig.vad__speech_pad_ms`           | `io.github.jaffe2718.mcmti.config.McmtiConfig#vad__speech_pad_ms`           | `int`                                                           | `0`           | Padding added before and after speech segments.                                     |
| vad__threshold               | `mcmti.midnightconfig.vad__threshold`               | `io.github.jaffe2718.mcmti.config.McmtiConfig#vad__threshold`               | `float`                                                         | `0f`          | Probability threshold to consider as speech.                                        |
| useCustomDynamicLib          | `mcmti.midnightconfig.useCustomDynamicLib`          | `io.github.jaffe2718.mcmti.config.McmtiConfig#useCustomDynamicLib`          | `boolean`                                                       | `false`       | Enable using custom dynamic library for Whisper.                                    |
| customDynamicLibDir          | `mcmti.midnightconfig.customDynamicLibDir`          | `io.github.jaffe2718.mcmti.config.McmtiConfig#customDynamicLibDir`          | `String`                                                        | `""`          | Custom dynamic link library directory for Whisper.                                  |

- For more info,
  see [WhisperFullParams.java](https://github.com/Jaffe2718/whisper-jni/blob/v1.0.1/src/main/java/io/github/jaffe2718/whisperjni/WhisperFullParams.java)

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

1. Download the custom dynamic library from [Jaffe2718/whisper-jni/releases](https://github.com/Jaffe2718/whisper-jni/releases/tag/v0.5.6) and extract the files.
2. Enable the advanced configuration and set the `useCustomDynamicLib` to `true` in the configuration menu.
3. Set the `customDynamicLibDir` to the directory where the custom dynamic library is located in the configuration menu.
4. If you want to use the dynamic library which is supported vulkan, check your check that your computer has drivers and libraries running Vulkan installed.
   ```shell
   vulkaninfo
   ```
5. If you want to use the dynamic library which is supported CUDA, check that your computer has drivers and libraries running `CUDA >= 12.4` installed.
   ```shell
   nvidia-smi
   ```
   And for Linux, you must install `CUDA Toolkit >= 12.4` and set the `PATH` environment variable.

## Troubleshooting

- **Crash on Startup**: 
    1. Please check the compatibility and version of your use of the dynamic link library. You can disable the advanced configuration and set the `useCustomDynamicLib` to `false` to use the default dynamic library (CPU version).
    2. For Linux users, please check the `GLIBC` compatibility and version. The default dynamic library is compiled with `musl`, but the CUDA/Vulkan libraries are compiled on `ubuntu-22.04` with `GLIBC 2.35`.
- **Audio Input Device Load Failed**: Please check if Java has access to the audio input device.
- **Whisper Model Load Failed**: Make sure the path to the GGML Whisper model is correct.

## ASR Extension (Development)

See [DEV.md](DEV.md) for details.

## Contributing

If you'd like to contribute to this project, please feel free to submit issues or pull requests
on [GitHub](https://github.com/Jaffe2718/Fabric-Microphone-Text-Input).

## License

This mod is released under the [MIT License](LICENSE).