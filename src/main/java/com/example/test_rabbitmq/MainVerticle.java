package com.example.test_rabbitmq;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQOptions;

public class MainVerticle extends AbstractVerticle {


    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        var clientConfig = new RabbitMQOptions();
        clientConfig.setUri("amqp://guest:guest@localhost");

        var client = RabbitMQClient.create(vertx, clientConfig);

        client.start(connectionResult -> {
            if (connectionResult.succeeded()) {
                vertx.setPeriodic(1000, id -> {
                    var message = Buffer.buffer("Test message");

                    client.basicPublish("non-existent-exchange", "", message, publishResult -> {
                        if (publishResult.succeeded()) {
                            System.out.println("Message sent");
                        } else {
                            System.out.println("Failed to send message");
                        }
                    });
                });
            } else {
                System.out.println("Failed to connect to RabbitMQ");
            }
        });
    }
}
