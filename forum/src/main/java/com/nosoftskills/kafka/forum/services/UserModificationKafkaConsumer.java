/*
 * Copyright 2016 Microprofile.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nosoftskills.kafka.forum.services;

import com.nosoftskills.kafka.forum.model.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.StringReader;
import java.util.Collections;
import java.util.Properties;

@Singleton
@Startup
public class UserModificationKafkaConsumer {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private TimerService timerService;

    private KafkaConsumer<String, String> consumer;


    @PostConstruct
    public void consumeKafkaEvents() {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "test");

        this.consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("user"));

        timerService.createIntervalTimer(200L, 500L, new TimerConfig("userConsumer", false));
    }

    private void cancelTimer(Timer timer) {
        System.out.println("Cancelling timer " + timer.getInfo());
        timer.cancel();
    }

    @Timeout
    public void listenForUsers() {
        ConsumerRecords<String, String> records = consumer.poll(100);
        records.forEach(this::storeUser);
    }

    private void storeUser(ConsumerRecord<String, String> userRecord) {
        String userJson = userRecord.value();
        JsonObject jsonObject = Json.createReader(new StringReader(userJson))
                .readObject();

        String userName = jsonObject.getString("userName");
        String displayName = jsonObject.getString("firstName") + " " +
                             jsonObject.getString("lastName");

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.userName = :userName", User.class);
        query.setParameter("userName", userName);
        try {
            User user = query.getSingleResult();
            System.out.println("Found user, setting display name");
            user.setDisplayName(displayName);
        } catch (NoResultException nre) {
            System.out.println("Creating new user");
            User newUser = new User(userName, displayName);
            em.persist(newUser);
            System.out.println("New user persisted");
        }
    }
}
