package com.frejdh.util.common;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Sets the style of the ANSI text.
 * By the way, apparently spring also has this class with a similar structure but that was unintentional on my part.
 */
public enum AnsiStyle implements AnsiCodeInterface {

	NORMAL(0),

	BOLD(1),

	LIGHT(2),

	ITALIC(3),

	UNDERLINE(4),

	STROKE(9);

	private final int styleCode;

	AnsiStyle(int styleCode) {
		this.styleCode = styleCode;
	}


	/**
	 * Get the ANSI style code
	 * @return An integer with the code
	 */
	@Override
	public int getCode() {
		return styleCode;
	}

	@SuppressWarnings("OptionalGetWithoutIsPresent")
	public static AnsiStyle styleCodeToEnum(int styleCode) {
		try {
			return Arrays.stream(AnsiStyle.values()).filter(e -> e.styleCode == styleCode).findFirst().get();
		}
		catch (NoSuchElementException e) {
			return null;
		}
	}

	public static AnsiStyle styleCodeToEnum(String colorCode) {
		try {
			return styleCodeToEnum(Integer.parseInt(colorCode));
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
