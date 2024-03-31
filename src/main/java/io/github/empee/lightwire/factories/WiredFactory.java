package io.github.empee.lightwire.factories;

import io.github.empee.lightwire.LightWire;

import java.lang.reflect.Executable;

public class WiredFactory implements ComponentFactory {

  private final Executable provider;
  private Object instance;

  public WiredFactory(Class<?> clazz) {
    this.provider = findProvider(clazz);
  }

  private static Executable findProvider(Class<?> wiredClazz) {
    var constructors = wiredClazz.getDeclaredConstructors();
    if (constructors.length > 1) {
      throw new IllegalArgumentException("Component " + wiredClazz + " must have only one constructor");
    }

    return constructors[0];
  }

  @Override
  public Object get(LightWire lightWire) {
    if (instance == null) {
      instance = lightWire.getWirer().execute(null, provider);
    }

    return instance;
  }

}
