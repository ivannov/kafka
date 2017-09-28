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

import bg.jug.cdi.kafka.Consumes;
import com.nosoftskills.kafka.forum.model.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.io.StringReader;

@ApplicationScoped
public class UserModificationKafkaConsumer {

    @PersistenceContext
    private EntityManager em;

    @Consumes(topic = "user")
    @Transactional
    public void listenForUsers(ConsumerRecord<String, String> record) {
        String userJson = record.value();
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
