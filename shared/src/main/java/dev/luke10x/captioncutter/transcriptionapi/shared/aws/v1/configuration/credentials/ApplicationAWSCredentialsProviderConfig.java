package dev.luke10x.captioncutter.transcriptionapi.shared.aws.v1.configuration.credentials;

import com.amazonaws.auth.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class ApplicationAWSCredentialsProviderConfig {
    @Value("${module.aws.accessKey}")
    String accessKey;

    @Value("${module.aws.secretKey}")
    String secretKey;

    @Primary
    @Bean
    AWSCredentialsProvider applicationAWSCredentialsProvider() {
        return new AWSCredentialsProviderChain(
                DefaultAWSCredentialsProviderChain.getInstance(),
                new AWSCredentialsProvider() {
                    @Override
                    public AWSCredentials getCredentials() {
                        return new BasicAWSCredentials(accessKey, secretKey);
                    }

                    @Override
                    public void refresh() {

                    }
                });
    }
}
