package github.jaffe2718.mcmti.config;

import eu.midnightdust.lib.config.MidnightConfig;

/**
 * The configuration file of the mod.
 * for more information, please visit the wiki page
 * @see <a href="https://github.com/TeamMidnightDust/MidnightLib/wiki/Using-MidnightConfig">MidnightConfig Wiki</a>
 */
public class ConfigUI extends MidnightConfig{
    @Comment(centered = true) public static Comment labelConfig;

    /**
     * The path of the acoustic model.
     * You can download the acoustic model from the following link:
     * @see <a href="https://alphacephei.com/vosk/models">Vosk Models</a>
     */
    @Entry(width = 300) public static String acousticModelPath = "absolute/path/to/your/acoustic/model";

    /**
     * Maximum cache of microphone audio in bytes.
     * Also the size of the cache used to store audio data, use bytes as the unit, the default is 4096 bytes.*/
    @Entry(min = 1024, max = 32768) public static int cacheSize = 4096;    // The size of the cache used to store audio data, use bytes as the unit, the default is 4096 bytes

    /**
     * The sample rate of the microphone, the default is 16000.
     * The default value is 16000, which is the recommended value.
     * If you want to change the value, you must make sure that the value is supported by the microphone.
     */
    @Entry(min = 8000, max = 48000) public static int sampleRate = 16000;  // The sample rate of the microphone, the default is 16000

    /**
     * This configuration determines whether to automatically send voice recognition messages.<br>
     * Default to true<br>
     * If true, the identified message will be automatically sent out<br>
     * If it is false, the message will be entered into the message sending column for users to edit, requiring users to manually send it
     * */
    @Entry public static boolean autoSend = true;

    /**
     * The prefix to prepend in front of chat messages.
     * The default value is "⌈Speech Input⌋".
     * You can change it to any string you want.
     */
    @Entry(width = 300) public static String prefix = "⌈Speech Input⌋";    // The prefix to prepend in front of chat messages

    /**
     * This is a beta feature, it may cause errors.
     * Enable it to fix the encoding error, set this value to true.
     */
    @Entry public static boolean encoding_repair = false;                  // beta feature, may cause errors

    /**
     * This is a beta feature, it may cause errors.
     * The encoding of the source file.
     * The default value is the system encoding.
     * You can change it as the encoding of your source file.
     */
    @Entry public static String srcEncoding = System.getProperty("file.encoding");   // use system encoding as default

    /**
     * This is a beta feature, it may cause errors.
     * The encoding of the destination file.
     * The default value is the system encoding.
     * You can change it as the encoding of your destination file.
     */
    @Entry public static String dstEncoding = System.getProperty("file.encoding");   // use system encoding as default
    @Comment(centered = true) public static Comment about;
    @Comment public static Comment mod_name;
    @Comment public static Comment license;
    @Comment public static Comment author;
    @Comment public static Comment version;
}
