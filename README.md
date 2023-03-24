# Fabric Microphone Text Input Mod (EN-US) Developer Documentation

![icon](src/main/resources/assets/mcmti/icon.png)
## Introduction
### Function Overview
This mod allows you to use your microphone to input text into the game. It is a client-side mod, so it does not require any server-side setup.
### How it Works
The mod uses the Sphnix-4 from [CMU Sphnix](https://cmusphinx.github.io/) to convert your voice into text.
This mod will automatically recognize your voice and convert it into text and send it to the game as a chat message if the user is pressing the `v` key.

## Developer Guide
### Setup
1. Install Java Development Kit 17;
2. Install [IntelliJ IDEA](https://www.jetbrains.com/idea/download);
3. Clone the project from my [GitHub Repository](https://github.com/Jaffe2718/Fabric-Microphone-Text-Input);
4. Open the project in IntelliJ IDEA and wait for it to finish loading.
### Configuration List
| Dependency             | Type                               | Recommended Configuration      | Description                                                           |
|------------------------|------------------------------------|--------------------------------|-----------------------------------------------------------------------|
| JDK17                  | Development Kit                    | Java SE Development Kit 17.0.6 | The Java development environment toolkit on which the project depends |
| IntelliJ IDEA          | Integrated Development Environment | Latest Version                 | The IDE used to develop the project                                   |
| git                    | Version Control System             | Latest Version                 | The version control system used to manage the project                 |
| Minecraft Development  | IntelliJ IDEA Plugin               | Latest Version                 | The plugin used to develop Minecraft mods                             |
### Change Language Model
1. Download the language model you want to use from [CMU Sphnix](https://cmusphinx.github.io/wiki/download/);
2. Extract the language model to the `src/main/resources/assets/microphone_text_input/models` folder;
3. Modify the constructor in the `src/main/java/github/jaffe2718/mcmti/util/MicrophoneHandler.java` file to change the language model used by the mod like this:
```java
public MicrophoneHandler() {
    try {
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath("path/to/acoustic/model");
        configuration.setDictionaryPath("path/to/dictionary");
        configuration.setLanguageModelPath("path/to/language/model");
        recognizer = new StreamSpeechRecognizer(configuration);      // Create a new recognizer
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```
4. Change mod name and description in `src/main/resources/fabric.mod.json` file;
5. Change icon in `src/main/resources/assets/mcmti/icon.png` file;

### Compile Mod as JAR
1. Run `build` task from Gradle panel in IntelliJ IDEA;
2. The compiled mod jar file will be in `build/libs` folder.
3. To use the mod, copy the compiled mod jar file to the `mods` folder in your Minecraft installation directory with Fabric Loader(>=0.14.9) and Fabric API installed.

## User Guide
### Installation
1. Install Minecraft with Fabric Loader(>=0.14.9) and Fabric API;
2. Download the mod jar with appropriate version from [GitHub Releases](https://github.com/Jaffe2718/Fabric-Microphone-Text-Input/releases) or [Modrinth](https://modrinth.com/mod/microphone-text-input);
3. Copy the mod jar file to the `mods` folder in your Minecraft installation directory;
4. Run Minecraft and enjoy the mod.
### How to Use
1. Press the `v` key to start listening to your voice;
2. Say something and the mod will convert your voice into text and send it to the game as a chat message;
3. The massage will be automatically sent.
### Tips
The accuracy of speech recognition is related to the model called by the program, not to the program ontology. The more accurate the model, the more accurate the recognition. The model used by this mod is the default model provided by CMU Sphnix, which is not very accurate. If you want to use a more accurate model, you can download the model you want to use from [CMU Sphnix](https://cmusphinx.github.io/wiki/download/) or train your own model. Then you can change the model used by the mod by following the instructions in the [Developer Guide](#change-language-model).

## About
### License
This mod is licensed under the [MIT License](LICENSE)
### Author
[Jaffe2718](https://github.com/Jaffe2718)
### Issue
If you have any questions or suggestions, please submit an issue on [GitHub](https://github.com/Jaffe2718/Fabric-Microphone-Text-Input/issues).
### Contact
You can also contact me through the following methods:
[Bilibili Jaffe-](https://space.bilibili.com/1671742926)