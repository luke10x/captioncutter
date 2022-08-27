package dev.luke10x.captioncutter.speech2text.caption;

import edu.cmu.sphinx.api.AbstractSpeechRecognizer;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import lombok.SneakyThrows;

import java.util.logging.Logger;

public class RecognizerFactory {
    private static final String ACOUSTIC_MODEL = "resource:/edu/cmu/sphinx/models/en-us/en-us";
    private static final String DICTIONARY_PATH = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
    private static final String LANGUAGE_MODEL = "resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin";

    @SneakyThrows
    private static Configuration createConfiguration() {
        Logger cmRootLogger = Logger.getLogger("default.config");
        cmRootLogger.setLevel(java.util.logging.Level.OFF);
        String conFile = System.getProperty("java.util.logging.config.file");
        if (conFile == null) {
            System.setProperty("java.util.logging.config.file", "ignoreAllSphinx4LoggingOutput");
        }

        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath(ACOUSTIC_MODEL);
        configuration.setDictionaryPath(DICTIONARY_PATH);
        configuration.setLanguageModelPath(LANGUAGE_MODEL);

        return configuration;
    }

    @SneakyThrows
    public static LiveSpeechRecognizer createLiveSpeechRecognizer() {
        return new LiveSpeechRecognizer(createConfiguration());
    }

    @SneakyThrows
    public static StreamSpeechRecognizer createStreamSpeechRecognizer() {
        return new StreamSpeechRecognizer(createConfiguration());
    }
}
