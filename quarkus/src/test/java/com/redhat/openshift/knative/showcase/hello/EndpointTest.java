package com.redhat.openshift.knative.showcase.hello;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.redhat.openshift.knative.showcase.support.EventSinkWiremock;
import com.redhat.openshift.knative.showcase.support.HasWiremockServer;
import com.redhat.openshift.knative.showcase.support.Testing;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@QuarkusTest
@QuarkusTestResource(EventSinkWiremock.class)
class EndpointTest implements HasWiremockServer {

  protected String invalidHelloMessage = "hello.arg0: must match \"^[A-Z][a-z]+$\"";
  private final Client client;

  private WireMockServer wireMockServer;

  @Inject
  EndpointTest(@RestClient Client client) {
    this.client = client;
  }

  @Override
  public final void setWireMockServer(WireMockServer wireMockServer) {
    this.wireMockServer = wireMockServer;
  }

  @Test
  void hello() {
    var names = List.of(
      "Alice", "Bob", "Charlie", "Doug", "Emily", "Frances", "Gregory"
    );
    var rand = new Random();
    var idx = rand.nextInt(names.size());
    var name = names.get(idx);
    wireMockServer.stubFor(post(urlEqualTo("/"))
      .withHeader("Ce-Specversion", equalTo("1.0"))
      .withHeader("Ce-Source", matching(".+"))
      .withHeader("Ce-Type", matching(".+"))
      .willReturn(aResponse()
        .withStatus(Response.Status.ACCEPTED.getStatusCode()))
    );

    var hello = client.hello(name);

    assertThat(hello)
      .extracting(Hello::getGreeting, Hello::getWho, Hello::getNumber)
      .containsExactly(Testing.greeting(), name, 1);

    wireMockServer.verify(1,
      postRequestedFor(new UrlPattern(equalTo("/"), false))
        .withHeader("Ce-Specversion", equalTo("1.0"))
        .withHeader("Ce-Source", equalTo("//events/showcase"))
        .withHeader("Ce-Type", equalTo(Hello.class.getName()))
    );
  }

  @Test
  void invalidHello() {
    ThrowingCallable throwingCallable = () -> client.hello("small-caps");

    assertThatThrownBy(throwingCallable)
      .hasMessage(invalidHelloMessage);
  }
}
