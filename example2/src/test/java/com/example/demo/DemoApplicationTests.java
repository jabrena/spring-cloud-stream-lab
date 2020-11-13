package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DemoApplicationTests {

	@Test
	public void testMultipleFunctions() {

		try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
				TestChannelBinderConfiguration.getCompleteConfiguration(
						DemoApplication.class))
				.run("--spring.cloud.function.definition=uppercase;reverse")) {
			context.getBean(InputDestination.class);

			InputDestination inputDestination = context.getBean(InputDestination.class);
			OutputDestination outputDestination = context.getBean(OutputDestination.class);

			Message<byte[]> inputMessage = MessageBuilder.withPayload("Hello".getBytes()).build();
			inputDestination.send(inputMessage, 0);
			inputDestination.send(inputMessage, 1);

			Message<byte[]> outputMessage = outputDestination.receive(0, 0);
			assertThat(outputMessage.getPayload()).isEqualTo("HELLO".getBytes());

			outputMessage = outputDestination.receive(0, 1);
			assertThat(outputMessage.getPayload()).isEqualTo("olleH".getBytes());
		}
	}

}
