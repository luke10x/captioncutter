package dev.luke10x.captioncutter.transcriptionapi.shared.aws.v2.configuration.credentials;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.utils.StringUtils;

@Slf4j
@Configuration
public class ApplicationAwsCredentialsProviderConfig {
    @Value("cloud.aws.credentials.access-key")
    private String accessKey;

    @Value("cloud.aws.credentials.secret-key")
    private String secretKey;

    @Bean
    public AwsCredentialsProvider applicationAwsCredentialsProvider() {
        var builder = AwsCredentialsProviderChain.builder()
                .addCredentialsProvider(DefaultCredentialsProvider.create());

        if (StringUtils.isBlank(accessKey)) {
            log.info("Application AWS access key is blank");
        }

        if (StringUtils.isBlank(secretKey)) {
            log.info("Application AWS secret key is blank");
        }

        builder.addCredentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey));

        return builder.build();
    }
}
