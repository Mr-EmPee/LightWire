plugins {
  id("java-library")
  id("io.freefair.lombok") version "8.4"
  id("com.vanniktech.maven.publish") version "0.28.0"
}

version = findProperty("tag") ?: "0.0.4-SNAPSHOT"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(11))

mavenPublishing {
  coordinates("io.github.mr-empee", "lightwire", version.toString())

  pom {
    name.set("LightWire")
    description.set("Simple IoC container")
    inceptionYear.set("2024")
    url.set("https://github.com/Mr-EmPee/LightWire")
    licenses {
      license {
        name.set("The Apache License, Version 2.0")
        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
        distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
      }
    }

    developers {
      developer {
        id.set("mr-empee")
        name.set("Mr. EmPee")
        url.set("https://github.com/mr-empee/")
      }
    }

    scm {
      url.set("https://github.com/Mr-EmPee/LightWire")
      connection.set("scm:git:git://github.com/Mr-EmPee/LightWire.git")
      developerConnection.set("scm:git:ssh://git@github.com:Mr-EmPee/LightWire.git")
    }
  }
}