package com.redhat.openshift.knative.showcase.events;

import com.redhat.openshift.knative.showcase.support.Testing;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@QuarkusIntegrationTest
class EndpointIT extends EndpointTest {
  EndpointIT() {
    super(Testing.buildRestClient(Client.class));
  }

  @Test
  @Override
  @Disabled("Using the microprofile rest client test hangs")
  void events() {
    /*
    The tests hangs, which is kinda expected as the tests is using the SSE to
    stream the events. The SSE is a blocking operation, so the test will never
    finish. The Microprofile Rest Client does not support async SSE, so the
    test is disabled to avoid the hang.
     */
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
