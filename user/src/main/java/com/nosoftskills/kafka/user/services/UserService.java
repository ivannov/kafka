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
package com.nosoftskills.kafka.user.services;

import com.nosoftskills.kafka.user.model.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.net.URI;
import java.util.List;

@RequestScoped
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class UserService {

    @Inject
    private EntityManager em;

    @GET
    public Response getAllUsers() {
        List<User> users = em.createQuery("SELECT users FROM User users", User.class)
                .getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        users.stream()
                .map(this::toJson)
                .forEach(arrayBuilder::add);
        return Response.ok(arrayBuilder.build()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createUser(String userJson) {
        User user = fromJsonString(userJson);
        em.persist(user);
        return Response
                .created(URI.create("/" + user.getId()))
                .entity(toJson(user))
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateUser(String userJson) {
        User user = fromJsonString(userJson);
        if (user.getId() != null) {
            User found = em.find(User.class, user.getId());
            if (found != null) {
                em.merge(user);
                return Response
                        .ok()
                        .entity(toJson(user))
                        .build();
            }
            user.setId(null);
        }
        return createUser(userJson);
    }

    @GET
    @Path("/reset")
    @Transactional
    public Response reset() {
        try {
            em.createQuery("DELETE FROM User u").executeUpdate();
        } catch (PersistenceException pe) {
            System.out.println("Tables don't exist");
        }
        em.persist(new User("ivko3", "ivan", "Ivan", "Ivanov", "ivan.ivanov@example.org"));
        em.persist(new User("kocko07", "koko", "Konstantin", "Stefanov", "konstantin.stefanov@example.org"));
        em.persist(new User("boboran", "boyan", "Boyan", "Stefanov", "boyan.stefanov@example.org"));
        return Response.noContent().build();
    }

    private JsonObject toJson(User user) {
        return Json.createObjectBuilder()
                    .add("id", user.getId())
                    .add("userName", user.getUserName())
                    .add("firstName", user.getFirstName())
                    .add("lastName", user.getLastName())
                    .add("email", user.getEmail())
                    .build();
    }

    private User fromJsonString(String userJson) {
        JsonObject jsonObject = Json.createReader(new StringReader(userJson))
                .readObject();
        User user = new User();
        JsonNumber idNumber = jsonObject.getJsonNumber("id");
        if (idNumber != null) {
            user.setId(idNumber.longValue());
        }
        user.setUserName(jsonObject.getString("userName"));
        user.setPassword(jsonObject.getString("password"));
        user.setFirstName(jsonObject.getString("firstName"));
        user.setLastName(jsonObject.getString("lastName"));
        user.setEmail(jsonObject.getString("email"));
        return user;
    }
}
