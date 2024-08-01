package io.github.empee.lightwire.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

@UtilityClass
public class PackageScanner {

  @SneakyThrows
  private List<String> findAllClassesFromJar(String basePackage, File jarFile) {
    try(var jar = new JarFile(jarFile)) {
      String fileFormat = ".class";

      return JarUtils.getContentFromJar(jar, basePackage.replace(".", "/")).stream()
          .map(ZipEntry::getName)
          .filter(c -> c.endsWith(fileFormat))
          .map(c -> c.substring(0, c.length() - fileFormat.length()))
          .map(c -> c.replace("/", "."))
          .collect(Collectors.toList());
    }
  }

  @SneakyThrows
  private List<String> findAllClassesFromDirectory(String basePackage, File sourceDir) {
    try(var content = Files.walk(sourceDir.toPath())) {
      String fileFormat = ".class";

      Path basePackagePath = Path.of(sourceDir.getPath(), basePackage.split("\\."));
      int sourceDirLength = sourceDir.getPath().length() + 1; //1 cause need to account for the '/' between the path and the package

      return content.filter(Files::isRegularFile)
          .filter(f -> f.startsWith(basePackagePath))
          .map(Path::toString)
          .filter(f -> f.endsWith(fileFormat))
          .map(c -> c.substring(sourceDirLength, c.length() - fileFormat.length()))
          .map(c -> c.replace("/", "."))
          .collect(Collectors.toList());
    }
  }

  @SneakyThrows
  public List<? extends Class<?>> findAll(String basePackage, Class<?> baseClass) {
    File file = JarUtils.getSourcePath(baseClass);
    Collection<String> jarContent;
    if (file.isFile()) {
      jarContent = findAllClassesFromJar(basePackage, file);
    } else {
      jarContent = findAllClassesFromDirectory(basePackage, file);
    }

    return jarContent.stream()
        .map(c -> getClassWithoutLoadingIt(c, baseClass.getClassLoader()))
        .collect(Collectors.toList());
  }

  public List<? extends Class<?>> findAllWithAnnotation(
      String basePackage, Class<?> baseClass, Class<? extends Annotation> annotation
  ) {
    return findAll(basePackage, baseClass).stream()
        .filter(c -> c.isAnnotationPresent(annotation))
        .collect(Collectors.toList());
  }

  @SneakyThrows
  private static Class<?> getClassWithoutLoadingIt(String clazz, ClassLoader classLoader) {
    return Class.forName(clazz, false, classLoader);
  }


}
