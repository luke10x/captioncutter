package dev.luke10x.captioncutter.speech2text.caption;

import java.io.FileInputStream;
import java.io.InputStream;

import dev.luke10x.captioncutter.speech2text.aws.service.S3BucketStorageService;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Component
public class TranscriberDemo {
    @Autowired
    S3BucketStorageService service;

    public void go() throws Exception {

        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
//        InputStream stream = new FileInputStream(
//                ResourceUtils.getFile("classpath:monorepo5.wav")
//        );

        String fileName = "test11.wav";
        InputStream stream = service.downloadFile(fileName);

        recognizer.startRecognition(stream);
        SpeechResult result;
        while ((result = recognizer.getResult()) != null) {
            System.out.format("😇 Hypo: %s\n", result.getHypothesis());
        }
        recognizer.stopRecognition();


    }
}