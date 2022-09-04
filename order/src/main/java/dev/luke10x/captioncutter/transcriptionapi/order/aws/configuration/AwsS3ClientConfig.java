package dev.luke10x.captioncutter.transcriptionapi.order.aws.configuration;

import com.amazonaws.auth.*;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import dev.luke10x.captioncutter.transcriptionapi.order.aws.configuration.credentials.ApplicationAWSCredentialsProviderChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3ClientConfig {
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3 amazonS3(final AwsClientBuilder.EndpointConfiguration endpointConfiguration) {

        BasicAWSCredentials credentials = new BasicAWSCredentials("dummy", "dummy");

        AmazonS3 amazonS3Client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(ApplicationAWSCredentialsProviderChain.getInstance())
                .withPathStyleAccessEnabled(true)
                .build();

        return amazonS3Client;
    }


}
