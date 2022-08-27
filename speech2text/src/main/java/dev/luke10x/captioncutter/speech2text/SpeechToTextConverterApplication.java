package dev.luke10x.captioncutter.speech2text;

import dev.luke10x.captioncutter.speech2text.caption.Example;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpeechToTextConverterApplication {

	public static void main(String[] args) throws Exception {
//		Example.doit();
		SpringApplication.run(SpeechToTextConverterApplication.class, args);
	}

}
