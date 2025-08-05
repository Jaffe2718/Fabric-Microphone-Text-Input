[//]: # (Mincrophone Text Input 2.1.3-rc.2-1.21.x)

## Changelog

- fix: addressed the issue where the default dynamic link library extraction failed by using `io.github.freshsupasulley:whisper-jni:0.5.5` as whisper library instead of `0.5.0`
- adjust: support custom dynamic link library loading, download from https://github.com/FreshSupaSulley/whisper-jni/releases/tag/v0.5.5.

## Dependencies

| Minecraft | Fabric                                                                                                                                                                                    | NeoForge                                                                                                                                                                                                    |
|-----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1.21.4    | [fabric-api 0.119.3+1.21.4](https://modrinth.com/mod/fabric-api/version/0.119.3+1.21.4) <br> [midnightlib 1.7.3-fabric](https://modrinth.com/mod/midnightlib/version/1.7.3+1.21.4-fabric) | [architechury-api 15.0.3+neoforge](https://modrinth.com/mod/architectury-api/version/15.0.3+neoforge) <br> [midnightlib 1.7.3-neoforge](https://modrinth.com/mod/midnightlib/version/1.7.3+1.21.4-neoforge) |
| 1.21.5    | [fabric-api 0.128.1+1.21.5](https://modrinth.com/mod/fabric-api/version/0.128.1+1.21.5) <br> [midnightlib 1.7.3-fabric](https://modrinth.com/mod/midnightlib/version/1.7.3+1.21.4-fabric) | [architechury-api 16.1.4+neoforge](https://modrinth.com/mod/architectury-api/version/16.1.4+neoforge) <br> [midnightlib 1.7.3-neoforge](https://modrinth.com/mod/midnightlib/version/1.7.3+1.21.4-neoforge) |
| 1.21.6    | [fabric-api 0.128.1+1.21.6](https://modrinth.com/mod/fabric-api/version/0.128.1+1.21.6) <br> [midnightlib 1.7.5-fabric](https://modrinth.com/mod/midnightlib/version/1.7.5+1.21.6-fabric) | [architechury-api 17.0.6+neoforge](https://modrinth.com/mod/architectury-api/version/17.0.6+neoforge) <br> [midnightlib 1.7.5-neoforge](https://modrinth.com/mod/midnightlib/version/1.7.5+1.21.6-neoforge) |
| 1.21.7    | [fabric-api 0.128.1+1.21.7](https://modrinth.com/mod/fabric-api/version/0.128.1+1.21.7) <br> [midnightlib 1.7.5-fabric](https://modrinth.com/mod/midnightlib/version/1.7.5+1.21.6-fabric) | [architechury-api 17.0.8+neoforge](https://modrinth.com/mod/architectury-api/version/17.0.8+neoforge) <br> [midnightlib 1.7.5-neoforge](https://modrinth.com/mod/midnightlib/version/1.7.5+1.21.6-neoforge) |
| 1.21.8    | [fabric-api 0.129.0+1.21.8](https://modrinth.com/mod/fabric-api/version/0.129.0+1.21.8) <br> [midnightlib 1.7.5-fabric](https://modrinth.com/mod/midnightlib/version/1.7.5+1.21.6-fabric) | [architechury-api 17.0.8+neoforge](https://modrinth.com/mod/architectury-api/version/17.0.8+neoforge) <br> [midnightlib 1.7.5-neoforge](https://modrinth.com/mod/midnightlib/version/1.7.5+1.21.6-neoforge) |


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
   
## Download Whisper Model

- [Whisper GGML Model](https://huggingface.co/ggerganov/whisper.cpp/tree/main)
- [VAD Model](https://huggingface.co/ggml-org/whisper-vad)
