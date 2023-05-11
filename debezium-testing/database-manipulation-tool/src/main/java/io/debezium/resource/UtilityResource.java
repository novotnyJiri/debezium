/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.resource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import io.debezium.service.UtilityService;
import io.quarkus.runtime.annotations.RegisterForReflection;

@Path("Utility")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
@RegisterForReflection
public class UtilityResource {

    @Inject
    UtilityService utilityService;

    private static final Logger LOG = Logger.getLogger(UtilityResource.class);

    @Path("GetAll")
    @GET
    public Response getAll() {
        try {
            JsonObject obj = utilityService.getAllEntries();
            return Response.ok().entity(obj.toString()).build();

        }
        catch (Exception ex) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
