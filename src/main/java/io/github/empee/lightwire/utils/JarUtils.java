package io.github.empee.lightwire.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Utility class to perform actions on a JAR
 */

@UtilityClass
public class JarUtils {

  @SneakyThrows
  public File getSourcePath(Class<?> clazz) {
    return new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
  }

  public List<JarEntry> getContentFromJar(JarFile jarFile, String path) {
    return Collections.list(jarFile.entries()).stream()
        .filter(entry -> entry.getName().startsWith(path))
        .filter(entry -> !entry.isDirectory())
        .collect(Collectors.toList());
  }

}
