package com.nosoftskills.kafka.forum.services;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.Arrays;
import java.util.Properties;

@ApplicationScoped
public class KafkaConsumerFactory {

    @Produces
    @RequestScoped
    public <K,V> KafkaConsumer<K, V> createConsumer(InjectionPoint injectionPoint) {
        KafkaConsumerConfig annotation = injectionPoint.getAnnotated().getAnnotation(KafkaConsumerConfig.class);
        System.out.println("Injection point annotations: " + injectionPoint.getAnnotated().getAnnotations());
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty("bootstrap.servers", annotation.bootstrapServers());
        consumerProperties.setProperty("key.deserializer", getSerializerFor(annotation.keyType()));
        consumerProperties.setProperty("value.deserializer", getSerializerFor(annotation.valueType()));
        consumerProperties.setProperty("group.id", annotation.groupId());
        KafkaConsumer<K, V> kafkaConsumer = new KafkaConsumer<>(consumerProperties);
        kafkaConsumer.subscribe(Arrays.asList(annotation.subscriptions()));
        System.out.println("Created Kafka consumer");
        return kafkaConsumer;
    }

    private String getSerializerFor(Class aClass) {
        if (aClass.equals(String.class)) {
            return StringDeserializer.class.getCanonicalName();
        } else if (aClass.equals(Integer.class) || aClass.equals(int.class)) {
            return IntegerDeserializer.class.getCanonicalName();
        }
        throw new IllegalArgumentException();
    }

}
