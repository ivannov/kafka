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

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserService {

    @Inject
    private EntityManager em;

    @GET
    public Response getUsers() {
        List<User> users = em.createQuery("SELECT users FROM User users", User.class)
                .getResultList();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        users.stream()
                .map(User::toJson)
                .forEach(arrayBuilder::add);
        return Response.ok(arrayBuilder.build()).build();
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
        return Response.noContent().build();
    }
}
