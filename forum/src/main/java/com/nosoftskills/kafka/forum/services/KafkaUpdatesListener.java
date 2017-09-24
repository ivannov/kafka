package com.nosoftskills.kafka.forum.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@ApplicationScoped
public class KafkaUpdatesListener implements Runnable {

    @Inject
    @KafkaConsumerConfig(subscriptions = "user")
    private KafkaConsumer<String, String> consumer;

    @Inject
    private Event<ConsumerRecord<String, String>> event;

    private boolean continuePolling = true;

    @Override
    public void run() {
        System.out.println("Polling started");
        while (continuePolling) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            if (!records.isEmpty()) {
                System.out.println("Found some records");
                records.forEach(record -> event.fire(record));
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @PreDestroy
    public void stopPolling() {
        continuePolling = false;
    }
}
