[//]: # (Mincrophone Text Input 2.1.7-1.21.9)

## Changelog

- fix: avoid result missing while multiple segments

## Download Whisper Model

- [Whisper GGML Model](https://huggingface.co/ggerganov/whisper.cpp/tree/main)
- [VAD Model](https://huggingface.co/ggml-org/whisper-vad)

## Dependencies

| Minecraft | Fabric                                                                                                                                                                                      | NeoForge                                                                                                                                                                                                    |
|-----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1.21.9    | [fabric-api 0.134.0+1.21.9](https://modrinth.com/mod/fabric-api/version/0.134.0+1.21.9) <br> [midnightlib 1.8.3-fabric](https://modrinth.com/mod/midnightlib/version/1.8.3+1.21.9-fabric)   | [architechury-api 18.0.5+neoforge](https://modrinth.com/mod/architectury-api/version/18.0.5+neoforge) <br> [midnightlib 1.8.3-neoforge](https://modrinth.com/mod/midnightlib/version/1.8.3+1.21.9-neoforge) |
| 1.21.10   | [fabric-api 0.135.0+1.21.10](https://modrinth.com/mod/fabric-api/version/0.135.0+1.21.10) <br> [midnightlib 1.8.3-fabric](https://modrinth.com/mod/midnightlib/version/1.8.3+1.21.9-fabric) | [architechury-api 18.0.5+neoforge](https://modrinth.com/mod/architectury-api/version/18.0.5+neoforge) <br> [midnightlib 1.8.3-neoforge](https://modrinth.com/mod/midnightlib/version/1.8.3+1.21.9-neoforge) |

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

