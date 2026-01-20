[//]: # (Mincrophone Text Input 2.1.9-1.21.11)

## Changelog

- adjust: optimize model loading strategy
- misc: optimize UI

## Download Whisper Model

- [Whisper GGML Model](https://huggingface.co/ggerganov/whisper.cpp/tree/main)
- [VAD Model](https://huggingface.co/ggml-org/whisper-vad)

## Dependencies

| Minecraft | Fabric                                                                                                                                                                                       | NeoForge                                                                                                                                                                                                     |
|-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1.21.11   | [fabric-api 0.141.1+1.21.11](https://modrinth.com/mod/fabric-api/version/0.141.1+1.21.11) <br> [midnightlib 1.9.2-fabric](https://modrinth.com/mod/midnightlib/version/1.9.2+1.21.11-fabric) | [architechury-api 19.0.1+neoforge](https://modrinth.com/mod/architectury-api/version/19.0.1+neoforge) <br> [midnightlib 1.9.2-neoforge](https://modrinth.com/mod/midnightlib/version/1.9.2+1.21.11-neoforge) |

## Compatibility

|                 | Windows                         | Linux                           | MacOS                           |
|-----------------|---------------------------------|---------------------------------|---------------------------------|
| x86_64          | Compatible                      | Compatible                      | Compatible                      |
| arm64           | Not compatible                  | Compatible                      | Compatible                      |
| x86_64 + Vulkan | External dynamic library needed | External dynamic library needed | External dynamic library needed |
| arm64 + Vulkan  | Not compatible                  | External library needed         | External library needed         |


## Custom Dynamic Library

1. Download the custom dynamic library from [FreshSupaSulley/whisper-jni/releases](https://github.com/FreshSupaSulley/whisper-jni/releases/tag/v0.5.5) and extract the files.
2. Enable the advanced configuration and set the `useCustomDynamicLib` to `true` in the configuration menu.
3. Set the `customDynamicLibDir` to the directory where the custom dynamic library is located in the configuration menu.
4. If you want to use the dynamic library which is supported vulkan, check your check that your computer has drivers and libraries running Vulkan installed.
   ```shell
   vulkaninfo
   ```

