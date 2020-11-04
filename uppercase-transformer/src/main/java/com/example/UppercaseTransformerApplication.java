package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.StreamRetryTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.retry.support.RetryTemplate;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
public class UppercaseTransformerApplication {

    private static Logger logger = LoggerFactory.getLogger(UppercaseTransformerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(UppercaseTransformerApplication.class, args);
    }

    // by default the `input` and `output` binding names will be `transform-in-0` and `transform-out-0`
    @Bean
    public Function<String, String> transform() {
        return String::toUpperCase;
    }

    // source semantics exposed as java.util.function.Supplier

    static class TestSource {

        private AtomicBoolean semaphore = new AtomicBoolean(true);
        private AtomicInteger counter = new AtomicInteger(0);

        // will send: foo,bar,foo,bar,..
        // the source (the origin) of the data, it does not subscribe to any in-bound destination and,
        // therefore, has to be triggered by some other mechanism
        // can be imperative or reactive - which relates how are they triggered
        @Bean
        public Supplier<String> sendTestData() {
            // produces a string whenever its get() method is called
            // the framework will call this supplier by default every 1 second
            return () -> {
                //String value =  this.semaphore.getAndSet(!this.semaphore.get()) ? "foo" : "bar";
                String value = "message" + counter.incrementAndGet();
                sleep(1);
                logger.info("Creating: " + value);
                return value;
            };

        }

    }

    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) { }
    }

    // sink semantics exposed as java.util.function.Consumer
    static class TestSink {

        @Bean
        public Consumer<String> receive() {

            return payload -> {
                sleep(2);
                logger.info("Data received: " + payload);
                throw new RuntimeException("Katakroker");
            };
        }

        @Bean
        public Consumer<String> receive2() {

            return payload -> {
                logger.info("Data received from DLX: " + payload);
            };
        }


    }

    /*
    @StreamRetryTemplate
    public RetryTemplate myRetryTemplate() {
        return new RetryTemplate();
    }

     */

    /*
    @ServiceActivator(inputChannel = "transformed.consumer.errors")
    public void handleError(Message<?> message) {
        logger.info("headers: " + message.getHeaders());
        logger.info("headers: " + message.getPayload());

    }
    */

}
