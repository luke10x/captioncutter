package dev.luke10x.captioncutter.transcriptionapi.order.aws.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

@Configuration
public class AsyncS3ClientConfig {
    @Value("${cloud.aws.endpoint}")
    private String endpoint;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Bean
    public S3AsyncClient amazonS3Async(
            AwsCredentialsProvider credentialsProvider
    ) {
        SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
                .writeTimeout(Duration.ofSeconds(3))
                .maxConcurrency(64)
                .build();
        S3Configuration serviceConfiguration = S3Configuration.builder()
                .checksumValidationEnabled(false)
                .chunkedEncodingEnabled(true)
                .build();
        S3AsyncClientBuilder b = S3AsyncClient.builder().httpClient(httpClient)
                .region(Region.of(region))
                .credentialsProvider(awsCredentialsProvider())
                .serviceConfiguration(serviceConfiguration);

        try {
            URI configuredEndpointUri = new URI(endpoint);

            if (configuredEndpointUri != null) {
                b = b.endpointOverride(configuredEndpointUri);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return b.build();
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {

//        return DefaultCredentialsProvider.create();

//        if (StringUtils.isBlank()) {
//            // Return default provider
//            return DefaultCredentialsProvider.create();
//        }
//        else {
//            // Return custom credentials provider
            return () -> {
                AwsCredentials creds = AwsBasicCredentials.create(
                        "dummy",
                        "dummy"
                );
                return creds;
            };
//        }
    }
}
