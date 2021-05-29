package com.frejdh.util.common.ansi.models;

import java.util.Arrays;

public enum LogLevel {
	TRACE("TRACE", 0),
	DEBUG("DEBUG", 1),
	INFO("INFORMATION", 2),
	WARN("WARNING", 3),
	ERROR("ERROR", 4),
	MAJOR("MAJOR", 5),
	CRITICAL("CRITICAL", 6);

	private final String text;
	private final int precedence;
	LogLevel(String text, int precedence) {
		this.text = text;
		this.precedence = precedence;
	}

	public String getText() {
		return text;
	}

	public static LogLevel toLogLevel(String level) {
		return Arrays.stream(LogLevel.values())
				.filter(logLevel -> logLevel.name().equalsIgnoreCase(level) || logLevel.getText().equalsIgnoreCase(level))
				.findFirst().orElse(null);
	}

	public static boolean isEqualLevel(LogLevel level1, LogLevel level2) {
		return level1.precedence == level2.precedence;
	}

	public static boolean isHigherOrEqualLevel(LogLevel expectedLowerLevel, LogLevel expectedHigherLevel) {
		return expectedLowerLevel.precedence <= expectedHigherLevel.precedence;
	}

	public static boolean isHigherLevel(LogLevel expectedLowerLevel, LogLevel expectedHigherLevel) {
		return expectedLowerLevel.precedence < expectedHigherLevel.precedence;
	}

}
