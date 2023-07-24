package com.redhat.openshift.knative.showcase.events;

import io.cloudevents.CloudEvent;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("")
@ApplicationScoped
class Rest implements Endpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(Rest.class);
  private final EventStore events = new EventStore();
  private final Presenter presenter;

  @Inject
  Rest(Presenter presenter) {
    this.presenter = presenter;
  }

  @Override
  public Multi<CloudEvent> events() {
    return events.stream();
  }

  @Override
  public void receive(CloudEvent event) {
    var he = presenter.asHumanReadable(event);
    LOGGER.info("Received event:\n{}", he);
    events.add(event);
  }

  @Override
  public void receiveOnIndex(CloudEvent event) {
    receive(event);
  }

}
