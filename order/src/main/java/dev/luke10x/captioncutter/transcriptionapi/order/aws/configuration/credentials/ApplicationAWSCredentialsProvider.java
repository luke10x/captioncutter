package dev.luke10x.captioncutter.transcriptionapi.order.aws.configuration.credentials;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;

public class ApplicationAWSCredentialsProvider implements AWSCredentialsProvider {

    @Override
    public AWSCredentials getCredentials() {
        return new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return "dummy";
            }

            @Override
            public String getAWSSecretKey() {
                return "dummy";
            }
        };
    }

    @Override
    public void refresh() {
        // no-op
    }
}
