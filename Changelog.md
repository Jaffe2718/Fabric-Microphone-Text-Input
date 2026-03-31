## Changelog

1. feature:Support developers to extend custom speech recognition mod (need to refer to this mod as a dependency to develop new mod);
2. adjust: optimize lifecycle of the recognizers
3. fix: resolved an issue where there was no warning when advanced config were turned on

## Download Whisper Model

- [Official Whisper GGML Model](https://huggingface.co/ggerganov/whisper.cpp/tree/main)
- [Unofficial Whisper GGML Model](https://huggingface.co/Jaffe2718/ggml-whisper-unofficial/tree/main)
- [VAD Model](https://huggingface.co/ggml-org/whisper-vad)

## Dependencies

| Minecraft | Fabric                                                                                                                                                                                       | NeoForge                                                                                                                                                                                                     |
|-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1.21.11   | [fabric-api 0.141.3+1.21.11](https://modrinth.com/mod/fabric-api/version/0.141.3+1.21.11) <br> [midnightlib 1.9.2-fabric](https://modrinth.com/mod/midnightlib/version/1.9.2+1.21.11-fabric) | [architechury-api 19.0.1+neoforge](https://modrinth.com/mod/architectury-api/version/19.0.1+neoforge) <br> [midnightlib 1.9.2-neoforge](https://modrinth.com/mod/midnightlib/version/1.9.2+1.21.11-neoforge) |

## Compatibility

|                   | Windows                         | Linux                           | MacOS                           |
|-------------------|---------------------------------|---------------------------------|---------------------------------|
| x86_64            | Compatible                      | Compatible                      | Compatible                      |
| arm64             | Not compatible                  | Compatible                      | Compatible                      |
| x86_64 + Vulkan   | External dynamic library needed | External dynamic library needed | External dynamic library needed |
| arm64 + Vulkan    | Not compatible                  | External dynamic library needed | External dynamic library needed |
| x86_64 + openBLAS | External dynamic library needed | Not compatible                  | Not compatible                  |
| x86_64 + CUDA     | External dynamic library needed | External dynamic library needed | Not compatible                  |

### NOTE

The provided dynamic link libraries of `Vulkan` and `CUDA`
in [GitHub Releases](https://github.com/Jaffe2718/whisper-jni/releases/tag/v1.0.1) are compiled on `ubuntu-22.04` with
`GLIBC 2.35`, and may not be compatible with all Linux distributions. There are some solutions that may work, if your
game crashes and it is confirmed in the crash log that it is incompatible between GLIBC versions, you can try any of the
following:

1. Use a different version of GLIBC (recommended)
    1. Check the environment variables for `Vulkan` or `CUDA Toolkit 12.4`
    2. Download [glibc-2.35.tar.gz](https://ftp.gnu.org/pub/gnu/glibc/glibc-2.35.tar.gz)
    3. Compile & install at `/path/to/your/glibc-2.35`, **DO NOT** replace the system `glibc` and **DO NOT** set up any
       global environment variables for it
    4. Use some launcher that supports custom environment variables, such
       as [Prism Launcher](https://prismlauncher.org/wiki/help-pages/environment-variables/), set `LD_LIBRARY_PATH` to
       `/path/to/your/glibc-2.35/lib64`
2. Compile the dynamic link library yourself and edit the config file of the mod
    1. Clone the [Jaffe2718/whisper-jni](https://github.com/Jaffe2718/whisper-jni) repository:
       `git clone https://github.com/Jaffe2718/whisper-jni.git`
    2. Switch to the tag `v1.0.1`: `cd whisper-jni && git checkout v1.0.1`
    3. Install `Vulkan SDK` and `ShaderC` for building `Vulkan` dynamic link library, or `CUDA Toolkit 12.4` for
       building `CUDA` dynamic link library
    4. Build

## Custom Dynamic Library

1. Download the custom dynamic library
   from [Jaffe2718/whisper-jni](https://github.com/Jaffe2718/whisper-jni/releases/tag/v1.0.1) and extract the files, *
   *DO NOT USE OTHER VERSIONS**
2. Enable the advanced configuration and set the `useCustomDynamicLib` to `true` in the configuration menu.
3. Set the `customDynamicLibDir` to the directory where the custom dynamic library is located in the configuration menu.
4. If you want to use the dynamic library which is supported vulkan, check your check that your computer has drivers and
   libraries running Vulkan installed.
   ```shell
   vulkaninfo
   ```
5. If you want to use CUDA, check that your computer has drivers and libraries running CUDA installed.
   ```shell
   nvidia-smi
   ```
   For Linux, you need to install `CUDA Toolkit >= 12.4.0` and configure the environment variables.
   For Windows, if the game crashes, you have to force the game to use the `Java >= 25`,
   see [Jaffe2718/whisper-jni/v1.0.1](https://github.com/Jaffe2718/whisper-jni/releases/tag/v1.0.1)
