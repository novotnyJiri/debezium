package io.debezium.resource;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.debezium.entity.DatabaseEntry;
import io.debezium.service.MainService;

@Path("Main")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MainResource {

    @Inject
    MainService mainService;

    @Path("Insert")
    @POST
    public Response insert(JsonObject inputJsonObj) {
        try {
            DatabaseEntry dbEntity = new DatabaseEntry(inputJsonObj);
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
            DatabaseEntry dbEntity = new DatabaseEntry(inputJsonObj);
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
            DatabaseEntry dbEntity = new DatabaseEntry(inputJsonObj);
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
            DatabaseEntry dbEntity = new DatabaseEntry(inputJsonObj);
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
        try {
            DatabaseEntry dbEntity = new DatabaseEntry(inputJsonObj);
            mainService.CreateTableAndUpsert(dbEntity);
            return Response.ok().build();
        }
        catch (Exception ex) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
