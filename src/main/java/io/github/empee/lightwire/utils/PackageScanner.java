package io.github.empee.lightwire.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

@UtilityClass
public class PackageScanner {

  @SneakyThrows
  public List<? extends Class<?>> findAll(String basePackage, ClassLoader classLoader) {
    try(var jar = new JarFile(JarUtils.getJar())) {
      var fileFormat = ".class";

      return JarUtils.getContentFromJar(jar, basePackage.replace(".", "/")).stream()
          .map(ZipEntry::getName)
          .filter(c -> c.endsWith(fileFormat))
          .map(c -> c.substring(0, c.length() - fileFormat.length()))
          .map(c -> c.replace("/", "."))
          .map(c -> getClassWithoutLoadingIt(c, classLoader))
          .collect(Collectors.toList());
    }
  }

  public List<? extends Class<?>> findAllWithAnnotation(
      String basePackage, ClassLoader classLoader, Class<? extends Annotation> annotation
  ) {
    return findAll(basePackage, classLoader).stream()
        .filter(c -> c.isAnnotationPresent(annotation))
        .collect(Collectors.toList());
  }

  @SneakyThrows
  private static Class<?> getClassWithoutLoadingIt(String clazz, ClassLoader classLoader) {
    return Class.forName(clazz, false, classLoader);
  }


}
