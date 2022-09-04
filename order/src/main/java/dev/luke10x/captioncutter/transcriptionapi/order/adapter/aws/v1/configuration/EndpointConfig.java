package dev.luke10x.captioncutter.transcriptionapi.order.adapter.aws.v1.configuration;
import com.amazonaws.client.builder.AwsClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndpointConfig {
    @Value("${cloud.aws.endpoint}")
    private String endpoint;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AwsClientBuilder.EndpointConfiguration endpointConfiguration() {
        return new AwsClientBuilder.EndpointConfiguration(endpoint, region);
    }
}