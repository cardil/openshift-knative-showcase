package com.redhat.openshift.knative.showcase.index;

import org.eclipse.microprofile.openapi.annotations.Operation;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
public interface Endpoint {
    @GET
    @Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Operation(
      summary = "Displays a home page, or the short project info",
      description = "Displays a index HTML page, or the project info in " +
      "JSON format if the Accept header is set to application/json or called " +
      "not from a browser"
    )
    Response home();

    @GET
    @Path("info")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
      summary = "Retrives info about project",
      description = "Information about project like maven coordinates and versions"
    )
    Info info();
}
