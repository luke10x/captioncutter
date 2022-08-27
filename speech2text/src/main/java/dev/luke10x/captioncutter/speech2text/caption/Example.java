package dev.luke10x.captioncutter.speech2text.caption;
import edu.cmu.sphinx.api.SpeechResult;

import static java.text.MessageFormat.format;

public class Example {
    public static void doit() throws Exception {

        var recognizer = RecognizerFactory.createLiveSpeechRecognizer();
        recognizer.startRecognition(true);

        SpeechResult result;
        while ((result = recognizer.getResult()) != null) {

//            System.out.format("💬 %s\n", result.getHypothesis());

//            System.out.println("List of recognized words and their times:");
//            for (WordResult r : result.getWords()) {
//                System.out.println(r);
//            }
//
//            System.out.println("Best 3 hypothesis:");

            var best3 = result.getNbest(3).stream()
                    .reduce("", (acc, each) -> {

                        return format("{0} / {1}\n", acc, each);
                    });
            System.out.println("🍀: "+ best3);

        }
        recognizer.stopRecognition();

    }

}
