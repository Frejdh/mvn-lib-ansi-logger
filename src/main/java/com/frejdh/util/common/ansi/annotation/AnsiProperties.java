package com.frejdh.util.common.ansi.annotation;

import com.frejdh.util.common.ansi.models.LogLevel;
import com.frejdh.util.environment.Config;
import com.frejdh.util.environment.ConversionUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration properties for the Ansi dependency.
 * It's highly recommended to turn on the annotation processor for this.
 */
@Data
@ToString
@Component
@NoArgsConstructor
@AllArgsConstructor
@EnableConfigurationProperties
@ConfigurationProperties(prefix = AnsiPropertyKeys.PREFIX)
public class AnsiProperties {

	private static transient LogLevel DEFAULT_LOGGING_LEVEL = LogLevel.toLogLevel(Config.getString(AnsiPropertyKeys.DEFAULT_LOGGING_LEVEL));

	private final Timestamp timestamp = new Timestamp();

	// Paths are stored in kebab-case, fix?
	private final Map<String, LogLevel> paths = convertMultiMapToSimpleMap(Config.getFlattenedPathMultiMap(AnsiPropertyKeys.CLASS_LOGGING_BASE));

	private LogLevel defaultLevel = (DEFAULT_LOGGING_LEVEL != null) ? DEFAULT_LOGGING_LEVEL : LogLevel.INFO;

	public LogLevel getPathLogLevel(String path) {
		return paths.get(ConversionUtils.toKebabCase(path));
	}

	//
	// Nested property classes
	//
	@lombok.Data
	@lombok.ToString
	public static class Timestamp {
		/**
		 * If timestamps should be enabled or not for the ANSI output dependency
		 */
		private Boolean enabled = Config.getBoolean("ansi.logging.timestamp.enabled", false);

		/**
		 * The used format for the timestamps (if enabled)
		 */
		private String format = Config.getString("ansi.logging.timestamp.format", "yyyy-MM-dd HH:mm:ss.SSS");
	}

	private Map<String, LogLevel> convertMultiMapToSimpleMap(Map<String, List<Object>> map) {
		Map<String, LogLevel> simpleMap = new HashMap<>();
		map.forEach((key, value) -> {
			if (value != null && !value.isEmpty()) {
				simpleMap.put(key, LogLevel.toLogLevel(value.get(0).toString()));
			}
		});

		return simpleMap;
	}

}
