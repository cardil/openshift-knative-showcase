package com.redhat.openshift.knative.showcase.hello;

import org.eclipse.microprofile.openapi.annotations.Operation;

import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

public interface Endpoint {
  @GET
  @Operation(
    summary = "Basic hello operation",
    description = "Greeting can be changed by setting environment variable GREET"
  )
  @Path("hello")
  @Produces(MediaType.APPLICATION_JSON)
  Hello hello(
    @QueryParam("who")
    @DefaultValue("Person")
    @Pattern(regexp = "^[A-Z][a-z]+$")
      String who
  );
}
