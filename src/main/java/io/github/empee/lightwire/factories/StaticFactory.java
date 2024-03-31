package io.github.empee.lightwire.factories;

import io.github.empee.lightwire.LightWire;

public class StaticFactory implements ComponentFactory {

  private final Object instance;

  public StaticFactory(Object instance) {
    this.instance = instance;
  }

  @Override
  public Object get(LightWire lightWire) {
    return instance;
  }

}
