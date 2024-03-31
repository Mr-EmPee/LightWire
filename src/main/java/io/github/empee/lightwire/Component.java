package io.github.empee.lightwire;

import io.github.empee.lightwire.annotations.LightWired;
import io.github.empee.lightwire.factories.ComponentFactory;
import io.github.empee.lightwire.factories.StaticFactory;
import io.github.empee.lightwire.factories.WiredFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Component implements Comparable<Component> {

  private Class<?> clazz;
  private LightWired meta;

  private ComponentFactory factory;

  public static Component lightWired(Class<?> clazz) {
    var component = new Component();

    component.meta = clazz.getAnnotation(LightWired.class);
    if (component.meta == null) {
      throw new IllegalArgumentException("Component " + clazz + " isn't lightwired");
    }

    component.clazz = clazz;
    component.factory = new WiredFactory(clazz);

    return component;
  }

  public static Component ofInstance(Object instance) {
    var component = new Component();

    component.clazz = instance.getClass();
    component.factory = new StaticFactory(instance);

    return component;
  }

  public Object getInstance(LightWire lightWire) {
    try {
      return factory.get(lightWire);
    } catch (Exception e) {
      throw new RuntimeException("Error while loading of " + this, e);
    }
  }

  @Override
  public int compareTo(Component component) {
    if (meta == null) {
      return 1;
    }

    if (component.meta == null) {
      return -1;
    }

    var priority = Integer.compare(meta.priority(), component.meta.priority());
    if (priority != 0) {
      return priority;
    }

    return clazz.getName().compareTo(component.getClazz().getName());
  }

  @Override
  public String toString() {
    return clazz.getName();
  }

}
