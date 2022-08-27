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

import static java.text.MessageFormat.format;

@Component
public class TranscriberDemo {
    @Autowired
    S3BucketStorageService service;

    public void go() throws Exception {

        var recognizer = RecognizerFactory.createStreamSpeechRecognizer();
        String fileName = "test11.wav";
        InputStream stream = service.downloadFile(fileName);

        recognizer.startRecognition(stream);
        SpeechResult result;

        while ((result = recognizer.getResult()) != null) {

            var best3 = result.getNbest(3).stream()
                    .reduce("", (acc, each) -> format("{0} / {1}\n", acc, each));
            System.out.println("🍀: "+ best3);

        }
        recognizer.stopRecognition();


    }
}