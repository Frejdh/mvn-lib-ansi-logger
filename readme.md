Ansi converter
-
This module is meant as a utility for logging and creating ANSI outputs.
Doesn't work with windows at this moment.

All of the available classes can be found [here](https://github.com/frejdh/mvn-lib-ansi-logger/tree/master/src/main/java/com/frejdh/util/common).

## Configurations
In order to turn on/off timestamps, please add the following to your `application.properties` file:
```
ansi.logging.timestamp.enabled=[true|false]
```

## Adding the dependency
```
<dependencies>
    <dependency>
        <groupId>com.frejdh.util.common</groupId>
        <artifactId>ansi-logger</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>

<repositories> <!-- Required in order to resolve this package -->
    <repository>
        <id>mvn-lib-common-ansi-logger</id>
        <url>https://raw.github.com/Frejdh/mvn-lib-ansi-logger/mvn-repo/</url>
    </repository>
</repositories>
```

## Other libraries
[Search for my other public libraries here](https://github.com/search?q=Frejdh%2Fmvn-lib-).