package com.redhat.openshift.knative.showcase.view;

import com.redhat.openshift.knative.showcase.domain.contract.HelloService;
import com.redhat.openshift.knative.showcase.domain.entity.Hello;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Path;

@ApplicationScoped
@Path("")
public class HelloResourceBean implements HelloResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(HelloResourceBean.class);

  private final HelloService helloService;

  @Inject
  HelloResourceBean(HelloService helloService) {
    this.helloService = helloService;
  }

  @Override
  @Valid
  public Hello hello(String who) {
    try {
      var hello = helloService.greet(who);
      LOGGER.info("Received hello({}) for: {}", hello.getNumber(), who);
      LOGGER.debug("Responding with: {}", hello);
      return hello;
    } catch (RuntimeException ex) {
      LOGGER.info("Received hello for: {}", who);
      throw ex;
    }
  }
}
