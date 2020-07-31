package com.frejdh.util.common.ansi;

import com.frejdh.util.common.ansi.builder.BitDepth;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Replacement of the Spring implementation of AnsiColor. Primary usage is with {@link AnsiOutput} but provides some additional methods
 */
public enum AnsiColor implements AnsiColorInterface {

	DEFAULT(39),

	BLACK(30),

	RED(31),

	GREEN(32),

	YELLOW(33),

	BLUE(34),

	MAGENTA(35),

	CYAN(36),

	GRAY(37),

	BRIGHT_BLACK(90),

	BRIGHT_RED(91),

	BRIGHT_GREEN(92),

	BRIGHT_YELLOW(93),

	BRIGHT_BLUE(94),

	BRIGHT_MAGENTA(95),

	BRIGHT_CYAN(96),

	WHITE(97),

	// 8-bit colors below
	BRIGHT_GRAY(BitDepth.EIGHT, 244),

	LIME(BitDepth.EIGHT, 118);


	// Variables
	public final int code;
	public final BitDepth bitDepth;

	AnsiColor(int code) {
		this.code = code;
		this.bitDepth = BitDepth.FOUR;
	}

	AnsiColor(BitDepth bitDepth, int code) {
		this.code = code;
		this.bitDepth = bitDepth;
	}

	/**
	 * Get the ANSI color code
	 * @return An integer with the code
	 */
	@Override
	public int getCode() {
		return code;
	}

	@Override
	public int getBitDepth() {
		return bitDepth.getValue();
	}

	@Override
	public boolean isForeground() {
		return true;
	}

	@Override
	public boolean isBackground() {
		return false;
	}


	@SuppressWarnings("OptionalGetWithoutIsPresent")
	public static AnsiColor colorCodeToEnum(int colorCode) {
		try {
			return Arrays.stream(AnsiColor.values()).filter(e -> e.code == colorCode).findFirst().get();
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
