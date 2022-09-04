package dev.luke10x.captioncutter.transcriptionapi.order.aws.v2.configurartion;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.core.retry.conditions.RetryCondition;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.utils.StringUtils;

import java.net.URI;
import java.time.Duration;

@Configuration
public class AwsS3AsyncClientConfig {

    @Value("${cloud.aws.endpoint}")
    private String endpoint;

    @Value("${cloud.aws.region.static}")
    private String region;

    @SneakyThrows
    @Bean
    public S3AsyncClient s3AsyncClient(AwsCredentialsProvider credentialsProvider) {
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
                .credentialsProvider(credentialsProvider)
                .overrideConfiguration(config ->
                        config.retryPolicy(
                                RetryPolicy.builder()
                                        .retryCondition(RetryCondition.none())
//                                        .retryCapacityCondition(null)
                                        .build()
                        ).build()
                )                .serviceConfiguration(serviceConfiguration);

        if (endpoint != null) {
            b = b.endpointOverride(new URI(endpoint));
        }
        return b.build();
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        if (StringUtils.isBlank("dummy")) {
            return DefaultCredentialsProvider.create();
        } else {
            return () -> AwsBasicCredentials.create("dummy", "dummy");
        }
    }
}
