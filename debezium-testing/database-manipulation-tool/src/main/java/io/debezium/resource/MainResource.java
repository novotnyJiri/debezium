/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.resource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import io.debezium.model.DatabaseEntry;
import io.debezium.service.MainService;
import io.debezium.utils.DatabaseEntryParser;
import io.quarkus.runtime.annotations.RegisterForReflection;

@Path("Main")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
@RegisterForReflection
public class MainResource {

    @Inject
    MainService mainService;

    @Inject
    DatabaseEntryParser parser;

    private static final Logger LOG = Logger.getLogger(MainResource.class);

    @Path("Insert")
    @POST
    public Response insert(JsonObject inputJsonObj) {
        try {
            DatabaseEntry dbEntity = parser.parse(inputJsonObj);
            mainService.insert(dbEntity);
            return Response.ok().build();

        }
        catch (Exception ex) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("CreateTable")
    @POST
    public Response createTable(JsonObject inputJsonObj) {
        try {
            DatabaseEntry dbEntity = parser.parse(inputJsonObj);
            mainService.createTable(dbEntity);
            return Response.ok().build();

        }
        catch (Exception ex) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("Upsert")
    @POST
    public Response upsert(JsonObject inputJsonObj) {
        try {
            DatabaseEntry dbEntity = parser.parse(inputJsonObj);
            mainService.upsert(dbEntity);
            return Response.ok().build();
        }
        catch (Exception ex) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("Update")
    @POST
    public Response update(JsonObject inputJsonObj) {
        try {
            DatabaseEntry dbEntity = parser.parse(inputJsonObj);
            mainService.update(dbEntity);
            return Response.ok().build();
        }
        catch (Exception ex) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("CreateTableAndUpsert")
    @POST
    public Response createTableAndUpsert(JsonObject inputJsonObj) {
        LOG.debug("Received CREATE TABLE If does not exist and UPSERT request");
        try {
            DatabaseEntry dbEntity = parser.parse(inputJsonObj);
            mainService.upsert(dbEntity);
            return Response.ok().build();
        }
        catch (Exception ex) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
