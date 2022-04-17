package com.frejdh.util.common.ansi.annotation;

import com.frejdh.util.common.ansi.models.LogLevel;
import com.frejdh.util.environment.Config;
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
@ConfigurationProperties(prefix = "ansi.logging")
public class AnsiProperties {

	private static transient LogLevel DEFAULT_LOGGING_LEVEL = LogLevel.toLogLevel(Config.getString("ansi.logging.default-level"));

	private final Timestamp timestamp = new Timestamp();

	private final Map<String, LogLevel> paths = new HashMap<>();

	private final List<String> pathss = new ArrayList<>();


	private LogLevel defaultLevel = (DEFAULT_LOGGING_LEVEL != null) ? DEFAULT_LOGGING_LEVEL : LogLevel.INFO;

	public Map<String, LogLevel> getPaths() {
		return paths;
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

}
