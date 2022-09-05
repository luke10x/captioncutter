package dev.luke10x.captioncutter.transcriptionapi.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"dev.luke10x.captioncutter.transcriptionapi.order",
		"dev.luke10x.captioncutter.transcriptionapi.shared"
})
public class OrderUploadEndpointApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderUploadEndpointApplication.class, args);
	}
}
