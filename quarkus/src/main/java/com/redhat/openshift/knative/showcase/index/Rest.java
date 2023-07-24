package com.redhat.openshift.knative.showcase.index;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("")
class Rest implements Endpoint {
  private final Presenter presenter;
  private final Mode mode;

  @Inject
  Rest(Presenter presenter, Mode mode) {
    this.presenter = presenter;
    this.mode = mode;
  }

  @Override
  public Response home() {
    Response.ResponseBuilder builder;
    if (mode.isJson()) {
      builder = presenter.asJson();
    } else {
      builder = presenter.asHtml();
    }
    return builder.build();
  }

  @Override
  @Valid
  public Info info() {
    return presenter.info();
  }

}
