# Mincrophone Text Input 2.0.0-1.21.5

## Whats New
- OpenAI Whisper model support.
- Support custom dynamic link libraries, the users can compile custom libraries for CUDA support.

## Installation

1. Install `minecraft 1.21.5`, `fabric loader >=0.16.0`, `fabric-api` and [MidnightLib 1.7.2-fabric](https://modrinth.com/mod/midnightlib/version/1.7.2+1.21.4-fabric).
2. Install the mod and download the [GGML Whisper model](https://huggingface.co/ggerganov/whisper.cpp/tree/main).
3. [Configure the mod](https://github.com/Jaffe2718/Fabric-Microphone-Text-Input/tree/2.x#configuration) according to your needs in the configuration menu.
4. Use the keybinding you set (default is `V`) to start recording and speech recognition.
5. If the mode is set to `AUTO_SEND`, the recognized text will be automatically sent as a chat message. If set to
   `RELEASE_KEY_TO_SEND` or `RELEASE_KEY_TO_INPUT`, follow the corresponding key - release actions.

> Publish by GitHub Action.
> For more info, see [README: 2.x](https://github.com/Jaffe2718/Fabric-Microphone-Text-Input/blob/2.x/README.md)