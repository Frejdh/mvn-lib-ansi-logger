package com.frejdh.util.common.annotation.ansi;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for the Ansi dependency.
 * It's highly recommended to turn on the annotation processor for this.
 */
@lombok.Data
@lombok.ToString
@Component
@ConfigurationProperties(prefix = "ansi.logging")
public class AnsiProperties {

	//
	// Normal properties and nested properties
	//
	private Timestamp timestamp;

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
