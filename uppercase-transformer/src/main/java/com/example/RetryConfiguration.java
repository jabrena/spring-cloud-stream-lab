package com.example;

import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.cloud.stream.annotation.StreamRetryTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/*
@Configuration
public class RetryConfiguration {

    @StreamRetryTemplate
    public RetryTemplate sinkConsumerRetryTemplate() {
        final RetryTemplate retryTemplate = new RetryTemplate();
        //retryTemplate.setRetryPolicy(retryPolicy());
        //retryTemplate.setBackOffPolicy(backOffPolicy());

        return retryTemplate;
    }

    private ExceptionClassifierRetryPolicy retryPolicy() {

        final SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3);
        final AlwaysRetryPolicy alwaysRetryPolicy = new AlwaysRetryPolicy();

        ExceptionClassifierRetryPolicy retryPolicy = new ExceptionClassifierRetryPolicy();

        return retryPolicy;
    }

    private FixedBackOffPolicy backOffPolicy() {
        final FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();

        return backOffPolicy;
    }
}

 */