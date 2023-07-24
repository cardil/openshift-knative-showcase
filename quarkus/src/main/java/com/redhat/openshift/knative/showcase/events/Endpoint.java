package com.redhat.openshift.knative.showcase.events;

import io.cloudevents.CloudEvent;
import io.cloudevents.jackson.JsonFormat;
import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.reactive.RestStreamElementType;

public interface Endpoint {
  @GET
  @Operation(summary = "Retrieves all registered events as a JSON stream")
  @RestStreamElementType(JsonFormat.CONTENT_TYPE)
  @Produces(MediaType.SERVER_SENT_EVENTS)
  @Path("events")
  Multi<CloudEvent> events();

  @POST
  @Consumes
  @Operation(summary = "Receives a CloudEvent and stores it")
  @Path("events")
  void receive(CloudEvent event);

  @POST
  @Consumes
  @Operation(summary = "Receives a CloudEvent and stores it")
  @Path("")
  void receiveOnIndex(CloudEvent event);
}
