package com.example.demo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

import java.util.UUID;

@SpringBootTest
class DemoApplicationTests {

	@Disabled
	@Test
	public void samplePollingTest() {
		ApplicationContext context = new SpringApplicationBuilder(SamplePolledConfiguration.class)
				.web(WebApplicationType.NONE)
				.run("--spring.jmx.enabled=false");
		OutputDestination destination = context.getBean(OutputDestination.class);
		System.out.println("Message 1: " + new String(destination.receive().getPayload()));
		System.out.println("Message 2: " + new String(destination.receive().getPayload()));
		System.out.println("Message 3: " + new String(destination.receive().getPayload()));
	}

	@EnableBinding(SamplePolledConfiguration.PolledConsumer.class)
	@Import(TestChannelBinderConfiguration.class)
	@EnableAutoConfiguration
	public static class SamplePolledConfiguration {

		@Bean
		public MessageSource<?> source() {
			return () -> new GenericMessage<>("My Own Data " + UUID.randomUUID());
		}

		@Bean
		public ApplicationRunner poller(
				PollableMessageSource polledMessageSource,
				MessageChannel output,
				TaskExecutor taskScheduler) {

			return args -> {
				taskScheduler.execute(() -> {
					for (int i = 0; i < 3; i++) {
						try {
							if (!polledMessageSource.poll(m -> {
								String newPayload = ((String) m.getPayload()).toUpperCase();
								output.send(new GenericMessage<>(newPayload));
							})) {
								Thread.sleep(2000);
							}
						}
						catch (Exception e) {
							// handle failure
						}
					}
				});
			};
		}

		public static interface PolledConsumer extends Source {
			@Input
			PollableMessageSource pollableSource();
		}
	}

}
