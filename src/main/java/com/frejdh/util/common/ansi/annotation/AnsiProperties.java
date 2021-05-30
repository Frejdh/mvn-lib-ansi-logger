package com.frejdh.util.common.ansi.annotation;

import com.frejdh.util.common.ansi.models.LogLevel;
import com.frejdh.util.environment.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties for the Ansi dependency.
 * It's highly recommended to turn on the annotation processor for this.
 */
@lombok.Data
@lombok.ToString
@Component
@ConfigurationProperties(prefix = "ansi.logging")
public class AnsiProperties {

	public AnsiProperties() {
		LogLevel defaultLevelFromProperties = LogLevel.toLogLevel(Config.getString("ansi.logging.default-level"));
		if (defaultLevelFromProperties != null) {
			this.defaultLevel = defaultLevelFromProperties;
		}
		this.timestamp = new Timestamp();
		this.timestamp.enabled = Config.getBoolean("ansi.logging.timestamp.enabled", timestamp.enabled);
		this.timestamp.format = Config.getString("ansi.logging.timestamp.format", timestamp.format);
	}

	private Timestamp timestamp;

	private Map<String, LogLevel> paths = new HashMap<>();

	private LogLevel defaultLevel = LogLevel.INFO;

	//
	// Nested property classes
	//
	@lombok.Data
	@lombok.ToString
	public static class Timestamp {
		/**
		 * If timestamps should be enabled or not for the ANSI output dependency
		 */
		private Boolean enabled = false;

		/**
		 * The used format for the timestamps (if enabled)
		 */
		private String format = "yyyy-MM-dd HH:mm:ss.SSS";
	}

}
