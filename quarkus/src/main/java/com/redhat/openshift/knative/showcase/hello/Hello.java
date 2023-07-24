package com.redhat.openshift.knative.showcase.hello;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import pl.wavesoftware.utils.stringify.Stringify;

public final class Hello {

  private final String greeting;
  private final String who;
  private final int number;

  @JsonCreator
  public Hello(
    @JsonProperty("greeting") String greeting,
    @JsonProperty("who") String who,
    @JsonProperty("number") int number
  ) {
    this.greeting = greeting;
    this.who = who;
    this.number = number;
  }

  @Pattern(regexp = "^[A-Z][a-z]+$")
  public String getGreeting() {
    return greeting;
  }

  @Pattern(regexp = "^[A-Z][a-z]+$")
  public String getWho() {
    return who;
  }

  @Min(1)
  public int getNumber() {
    return number;
  }

  @Override
  public String toString() {
    return Stringify.of(this).toString();
  }
}
