package dev.luke10x.captioncutter.transcriptionapi.order.adapter.aws.v1.configuration.credentials;

import com.amazonaws.auth.*;

public class ApplicationAWSCredentialsProviderChain extends AWSCredentialsProviderChain {
    private static final ApplicationAWSCredentialsProviderChain INSTANCE = new ApplicationAWSCredentialsProviderChain();

    public ApplicationAWSCredentialsProviderChain() {
        super(new AWSCredentialsProvider[]{
                DefaultAWSCredentialsProviderChain.getInstance(),
                new ApplicationAWSCredentialsProvider()
        });
    }

    public static ApplicationAWSCredentialsProviderChain getInstance() {
        return INSTANCE;
    }
}

