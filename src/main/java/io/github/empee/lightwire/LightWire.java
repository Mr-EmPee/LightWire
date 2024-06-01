package io.github.empee.lightwire;

import io.github.empee.lightwire.annotations.LightWired;
import io.github.empee.lightwire.utils.PackageScanner;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class LightWire {

  private final SortedSet<Component> components = new TreeSet<>();

  @Getter
  private final Wirer wirer = Wirer.of(this);

  /**
   * Helper method that creates an instance of LightWire and also register all the components
   * class that it is able to find inside the package
   */
  public static LightWire of(String scan) {
    var ioc = new LightWire();

    var providers = PackageScanner.findAllWithAnnotation(scan, ioc.getClass().getClassLoader(), LightWired.class);
    for (Class<?> provider : providers) {
      ioc.registerComponents(provider);
    }

    return ioc;
  }

  public static LightWire of(Package scan) {
    return of(scan.getName());
  }

  /**
   * Mark a class that is used by LightWire
   */
  public void registerComponents(Class<?> clazz) {
    components.add(Component.lightWired(clazz));
  }

  public void addComponent(Object instance) {
    components.add(Component.ofInstance(instance));
  }

  public Set<Component> getComponents() {
    return Collections.unmodifiableSet(components);
  }

  public <T> List<T> getInstances(Class<T> clazz) {
    return (List<T>) components.stream()
        .filter(c -> clazz.isAssignableFrom(c.getClazz()))
        .map(c -> c.getInstance(this))
        .collect(Collectors.toList());
  }

  public void load() {
    components.forEach(c -> c.getInstance(this));
  }

}
