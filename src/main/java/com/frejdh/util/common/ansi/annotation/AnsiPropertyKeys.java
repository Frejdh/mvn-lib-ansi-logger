package com.frejdh.util.common.ansi.annotation;

public class AnsiPropertyKeys {

	private AnsiPropertyKeys() {

	}

	public static final String PREFIX = "ansi.logging";

	private static final String PREFIX_WITH_DOT = PREFIX + ".";

	public static final String DEFAULT_LOGGING_LEVEL = PREFIX_WITH_DOT + "default-level";

	public static final String CLASS_LOGGING_BASE = PREFIX_WITH_DOT + "paths";

}
