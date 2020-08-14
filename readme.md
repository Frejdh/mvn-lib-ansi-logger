Ansi converter
-
This module is meant as a utility for logging and creating ANSI outputs.
Doesn't work with windows at this moment.

All of the available classes can be found [here](https://github.com/frejdh/mvn-lib-ansi-logger/tree/master/src/main/java/com/frejdh/util/common).

## Configurations
By default, an attempt to load `application.properties` and `config.json` from the resource directory is made. <br>
Additional files to load can be set with the environment variable or configuration property: `ansi-logger.property-files=[File1, File2, ...]`

### Available properties
Defined with their default values
```
ansi-logger.enabled=true # Enable logging or not
ansi-logger.timestamp.enabled=false # If timestamps should be enabled

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
