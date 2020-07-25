package com.frejdh.util.common;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Replacement of the Spring implementation of AnsiColor. Primary usage is with {@link AnsiOutput} but provides some additional methods
 */
public enum AnsiColor implements AnsiCodeInterface {

	DEFAULT(39),

	BLACK(30),

	RED(31),

	GREEN(32),

	YELLOW(33),

	BLUE(34),

	MAGENTA(35),

	CYAN(36),

	WHITE(37),

	BRIGHT_BLACK(90),

	BRIGHT_RED(91),

	BRIGHT_GREEN(92),

	BRIGHT_YELLOW(93),

	BRIGHT_BLUE(94),

	BRIGHT_MAGENTA(95),

	BRIGHT_CYAN(96),

	BRIGHT_WHITE(97);

	private final int colorCode;

	AnsiColor(int colorCode) {
		this.colorCode = colorCode;
	}

	/**
	 * Get the ANSI color code
	 * @return An integer with the code
	 */
	@Override
	public int getCode() {
		return colorCode;
	}

	@SuppressWarnings("OptionalGetWithoutIsPresent")
	public static AnsiColor colorCodeToEnum(int colorCode) {
		try {
			return Arrays.stream(AnsiColor.values()).filter(e -> e.colorCode == colorCode).findFirst().get();
		}
		catch (NoSuchElementException e) {
			return null;
		}
	}

	public static AnsiColor colorCodeToEnum(String colorCode) {
		try {
			return colorCodeToEnum(Integer.parseInt(colorCode));
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
