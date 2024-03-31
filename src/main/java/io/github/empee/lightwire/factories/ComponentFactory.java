package io.github.empee.lightwire.factories;

import io.github.empee.lightwire.LightWire;

public interface ComponentFactory {
  Object get(LightWire lightWire);
}
