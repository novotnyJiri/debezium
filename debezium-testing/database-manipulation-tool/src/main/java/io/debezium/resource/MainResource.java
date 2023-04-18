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
    public Response Insert(JsonObject inputJsonObj) {
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
    public Response CreateTable(JsonObject inputJsonObj) {
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
    public Response Upsert(JsonObject inputJsonObj) {
        try {
            DatabaseEntry dbEntity = new DatabaseEntry(inputJsonObj);
            mainService.upsert(dbEntity);
            return Response.ok().build();
        }
        catch (Exception ex) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("CreateTableAndUpsert")
    @POST
    public Response CreateTableAndUpsert(JsonObject inputJsonObj) {
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
