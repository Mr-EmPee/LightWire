# LightWire
A lightweight InvesrionOfControl container, share and create instances of your classes with ease!

### Maven Installation
```xml
<dependency>
    <groupId>io.github.mr-empee</groupId>
    <artifactId>lightwire</artifactId>
    <version>0.0.5</version>
</dependency>
```

### Gradle Installation
```kotlin
implementation("io.github.mr-empee:lightwire:0.0.5")
```

## Usage

1. Initialize the IoC container inside you main class file
```java
  private final LightWire iocContainer = LightWire.of("my.base.package");
```
2. Invoke the loading method to eagerly load all the classes annotated with `@LightWired`
```java
    iocContainer.load();
```
3. You can look at how it is used on [MysticalBarriers](https://github.com/Mr-EmPee/MysticalBarriers), one of my projects!
