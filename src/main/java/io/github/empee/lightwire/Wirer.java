package io.github.empee.lightwire;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(staticName = "of")
public class Wirer {

  private final LightWire lightWire;

  @SneakyThrows
  public Object execute(Object instance, Executable executable) {
    Parameter[] parameters = executable.getParameters();
    Object[] arguments = new Object[parameters.length];

    for (int i=0; i<arguments.length; i++) {
      arguments[i] = getDependency(parameters[i]);
    }

    if (executable instanceof Constructor<?>) {
      return ((Constructor<?>) executable).newInstance(arguments);
    } else {
      return ((Method) executable).invoke(instance, arguments);
    }
  }

  private Object getDependency(Parameter parameter) {
    if (parameter.getType() == List.class) {
      var collectionType = getCollectionType(parameter.getParameterizedType());
      return findComponentsByClass(collectionType).stream()
          .map(d -> d.getInstance(lightWire))
          .collect(Collectors.toList());
    }

    var components = findComponentsByClass(parameter.getType());
    if (components.isEmpty()) {
      throw new IllegalArgumentException("Unable to find dependency " + parameter);
    }

    if (components.size() > 1) {
      throw new IllegalArgumentException("Found multiple matches for dependency " + parameter);
    }

    return components.get(0).getInstance(lightWire);
  }

  private List<Component> findComponentsByClass(Class<?> clazz) {
    return lightWire.getComponents().stream()
        .filter(c -> clazz.isAssignableFrom(c.getClazz()))
        .collect(Collectors.toList());
  }

  private Class<?> getCollectionType(Type type) {
    var generics = ((ParameterizedType) type).getActualTypeArguments();
    return getRawType(generics[0]);
  }

  private Class<?> getRawType(Type type) {
    if (type instanceof ParameterizedType) {
      return getRawType(((ParameterizedType) type).getRawType());
    }

    return (Class<?>) type;
  }

}
