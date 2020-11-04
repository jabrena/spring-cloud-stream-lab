# spring-cloud-stream-lab

== Related presentation

https://speakerdeck.com/mkheck/drinking-from-the-stream-how-to-use-messaging-platforms-for-scalability-and-performance["Drinking from the Stream: How to use messaging platforms for scalability & performance" presentation]

== Build and Run

=== Build

You can build .jar files like so:

```bash
mvn clean package -f scst-source/pom.xml
mvn clean package -f scst-processor/pom.xml
mvn clean package -f scst-sink/pom.xml
```

=== Run

Due to dependencies on Kafka and RabbitMQ it is
recommended that you use docker-compose to run the
apps:

```bash
docker-compose up -d
```

It will take a few minutes to start and then you can
view the docker logs to see the coffee get processed:

```bash
docker-compose logs -f source processor sink
```

http://localhost:15672/
guest/guest

```
spring:
  cloud:
    stream:
      function:
        definition: sendMessage;receiveMessageFeedback
      bindings:
        sendMessage-out-0:         
          destination: messageInput
        receiveMessageFeedback-in-0:
          group: eventProcessor
          destination: messageFeedback
```