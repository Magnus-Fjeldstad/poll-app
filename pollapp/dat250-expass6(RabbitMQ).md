# DAT250 Experiment Assignment 6

## Introduction

In this assignment, I worked with **RabbitMQ**, a message broker that implements the AMQP protocol, to understand its
role in enabling event-driven architecture in distributed systems.  
The goal was to extend the existing PollApp backend with asynchronous communication, where polls and votes are published
and consumed as messages instead of being handled only through REST requests.

Each poll now corresponds to a topic in RabbitMQ.  
When a new poll is created, a `PollCreated` event is published, and when a user votes, a `VoteCast` event is sent to
RabbitMQ.  
The application also subscribes to vote events to update poll data automatically.

---

## Installation

I installed RabbitMQ locally using **Homebrew** on macOS.

```bash
brew install rabbitmq
brew services start rabbitmq
```

I verified that RabbitMQ was running using:

```bash
rabbitmqctl status
```

The management console was available at [http://localhost:15672](http://localhost:15672),  
and I logged in with the default credentials `guest` / `guest`.

---

## Configuration

The Spring Boot application was configured through the `application.properties` file:

```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

A configuration class `RabbitMQConfig` was added to declare:

- A **topic exchange** (`pollsExchange`)
- A **durable queue** (`pollApp.votes`)
- A **binding** between them using the routing key `poll.*`
- A **Jackson2JsonMessageConverter** to ensure messages were serialized as JSON instead of Java objects

---

## Implementation

### PollManager

The `PollManager` was updated to publish events using Spring’s `RabbitTemplate`.

- When a poll is created, a `PollCreated` event is sent to RabbitMQ.
- When a vote is cast, a `VoteCast` event is published.
- Each event includes data such as `pollId`, `optionId`, `voter`, and timestamps.

Example:

```java
rabbitTemplate.convertAndSend("pollsExchange","poll."+poll.getId(),event);
```

### VoteEventListener

A new class `VoteEventListener` was added with an `@RabbitListener` that subscribes to the `pollApp.votes` queue.  
Whenever a `VoteCast` event is received, the listener prints a log message and updates the application’s in-memory data
model through `PollManager`.

This allows the app to respond automatically to external events in real-time.

---

## Testing

### REST Integration Tests

I used an `.http` test file to verify the full flow:

1. Create users
2. Create a poll
3. Cast votes via the REST API

Each action triggered a corresponding message (`PollCreated` or `VoteCast`) in RabbitMQ, which I verified in the
RabbitMQ management console under the `pollsExchange`.

### Python Test Script

I also created a small Python script `rabbit_check.py` to verify that RabbitMQ was running correctly.  
It connects via AMQP, sends a test message, consumes it back, and prints a success message.

Example output:

```
✅ Connected to RabbitMQ
📤 Sent message: {'status': 'ping'}
📥 Received message: {'status': 'ping'}
✅ RabbitMQ OK – message roundtrip successful!
```

This confirmed that RabbitMQ was fully operational and correctly configured.

---

## Technical Problems

- Initially, the Spring Boot application failed to connect (`Connection refused`) because RabbitMQ was not running.  
  This was solved by starting the RabbitMQ service through Homebrew.

- A later issue occurred where messages were serialized as Java objects instead of JSON (
  `application/x-java-serialized-object`),  
  causing deserialization errors in the listener (`Cannot convert from [[B] to [Map]`).  
  The fix was to register a `Jackson2JsonMessageConverter` in `RabbitMQConfig`.

After these fixes, message publishing and consumption worked as expected.

---

## Conclusion

This exercise provided hands-on experience with **RabbitMQ** and **event-driven architecture** in a Java Spring Boot
application.  
I successfully integrated RabbitMQ into the PollApp, allowing it to broadcast and process poll and vote events
asynchronously.  
I verified the setup both through the REST API and an independent Python test script.

RabbitMQ proved to be a reliable and efficient message broker, enabling scalable, decoupled communication between
services.  
This experiment deepened my understanding of asynchronous messaging and how it can be integrated into modern backend
systems.
