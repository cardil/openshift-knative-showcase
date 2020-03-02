package io.github.cardil.knsvng.domain.entity;

import pl.wavesoftware.utils.stringify.Stringify;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

public final class Hello {

  private final String greeting;
  private final String who;
  private final int number;

  @JsonbCreator
  public Hello(
    @JsonbProperty("greeting") String greeting,
    @JsonbProperty("who") String who,
    @JsonbProperty("number") int number
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
