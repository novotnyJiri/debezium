package io.debezium.resource;

import io.debezium.entity.DatabaseEntry;
import io.debezium.service.InsertService;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("Insert")
@Consumes(MediaType.APPLICATION_JSON)
public class MainResource {

    @Inject
    InsertService insertService;

    @POST
    public Response post(JsonObject inputJsonObj) {
        try {
            DatabaseEntry dbEntity = new DatabaseEntry(inputJsonObj);
            insertService.createTable(dbEntity);
            insertService.insert(dbEntity);
            return Response.ok().build();

        } catch (Exception ex) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
