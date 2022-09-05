package dev.luke10x.captioncutter.transcriptionapi.shared.aws.v2.configuration;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.core.retry.conditions.RetryCondition;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;
import java.time.Duration;

@Configuration
public class AsyncS3Config {

    @Value("${module.aws.endpoint}")
    private String endpoint;

    @Value("${module.aws.region}")
    private String region;

    @SneakyThrows
    @Bean
    public S3AsyncClient s3AsyncClient(AwsCredentialsProvider applicationAwsCredentialsProvider) {
        SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
                .writeTimeout(Duration.ZERO)
                .maxConcurrency(64)
                .build();
        S3Configuration serviceConfiguration = S3Configuration.builder()
                .checksumValidationEnabled(false)
                .chunkedEncodingEnabled(true)
                .pathStyleAccessEnabled(true)
                .build();
        S3AsyncClientBuilder b = S3AsyncClient.builder().httpClient(httpClient)
                .region(Region.of(region))
                .credentialsProvider(applicationAwsCredentialsProvider)
//                .overrideConfiguration(config ->
//                        config.retryPolicy(
//                                RetryPolicy.builder()
//                                        .retryCondition(RetryCondition.none())
//                                        .build()
//                        ).build()
//                )
                .serviceConfiguration(serviceConfiguration);

        if (endpoint != null) {
            b = b.endpointOverride(new URI(endpoint));
        }
        return b.build();
    }
}
