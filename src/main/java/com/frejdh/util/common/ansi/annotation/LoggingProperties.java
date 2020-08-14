package com.frejdh.util.common.ansi.annotation;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for the Ansi dependency.
 * It's highly recommended to turn on the annotation processor for this.
 */
@lombok.Setter
@lombok.ToString
@ConfigurationProperties(prefix = "ansi-logger")
public class LoggingProperties {

	private static transient PropertiesConfiguration config;

	static {

	}

	private static void loadPropertyFile(String filename) {

	}

	/**
	 * If logging should be enabled or not
	 */
	private Boolean enabled = true;

	//
	// Nested properties
	//
	private ColoredOutput coloredOutput;
	private Timestamp timestamp;
	private Levels levels;

	//
	// Getters
	//
	public Boolean getEnabled() {
		return enabled;
	}

	public ColoredOutput getColoredOutput() {
		if (coloredOutput == null) { // If not using spring-boot...
			coloredOutput = new ColoredOutput();
		}
		return coloredOutput;
	}

	public Timestamp getTimestamp() {
		if (timestamp == null) { // If not using spring-boot...
			timestamp = new Timestamp();
		}
		return timestamp;
	}

	public Levels getLevels() {
		if (levels == null) { // If not using spring-boot...
			levels = new Levels();
		}
		return levels;
	}

	//
	// Nested property classes
	//
	@lombok.Data
	@lombok.ToString
	public static class ColoredOutput {
		/**
		 * If colored output should be enabled or not
		 */
		private Boolean enabled = true;
	}

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

	@lombok.Data
	@lombok.ToString
	public static class Levels {
		/**
		 * Select logging levels to exclude. If empty, nothing is excluded. <br>
		 * Separate the fields by comma. For instance: 'trace, debug' disables both 'trace' and 'debug' level logging. <br> <br>
		 * Available levels: <br>
		 * - trace <br>
		 * - debug <br>
		 * - information <br>
		 * - warning <br>
		 * - error <br>
		 * - major <br>
		 * - critical <br>
		 */
		private String exclude = "trace, debug";

	}
}
