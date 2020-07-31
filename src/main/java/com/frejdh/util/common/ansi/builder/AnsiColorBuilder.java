package com.frejdh.util.common.ansi.builder;

import com.frejdh.util.common.ansi.AnsiColor;
import com.frejdh.util.common.ansi.AnsiColorInterface;
import com.frejdh.util.common.ansi.AnsiLogger;
import javax.validation.constraints.NotNull;

/**
 * Create and customize your own colors
 */
public class AnsiColorBuilder implements AnsiColorInterface {

	// Variables
	protected final int colorCode;
	protected final ColorType colorType;
	protected final BitDepth bitDepth;

	/**
	 * Alternative constructor. Automatically sets four bit color to foreground/background.
	 * Eight bit is always set to foreground.
	 * @param bitDepth Bit depth of the color
	 * @param colorCode Integer code of the color
	 * @throws ColorCodeOutOfRange If invalid range of the color code
	 */
	protected AnsiColorBuilder(BitDepth bitDepth, int colorCode) {
		this.bitDepth = bitDepth;
		this.colorCode = colorCode;
		if (bitDepth.equals(BitDepth.FOUR)) {
			this.colorType = isFourBitBackground(colorCode) ? ColorType.BACKGROUND : ColorType.FOREGROUND;
		}
		else {
			this.colorType = ColorType.FOREGROUND;
		}
	}

	/**
	 * Build a color from the ground up. Set foreground/background, bit depth and color code.
	 * @param colorType Foreground or background enum
	 * @param bitDepth Bit depth of the color
	 * @param colorCode Integer code of the color
	 * @throws ColorCodeOutOfRange If invalid range of the color code
	 */
	protected AnsiColorBuilder(ColorType colorType, BitDepth bitDepth, int colorCode) {
		this.colorType = colorType;
		this.bitDepth = bitDepth;
		this.colorCode = colorCode;
	}

	public static class Foreground extends AnsiColorBuilder {
		public Foreground(BitDepth bitDepth, int colorCode) {
			super(ColorType.FOREGROUND, bitDepth, colorCode);

			try {
				validateColorRange();
			} catch (Exception e) {
				AnsiLogger.warning("Color code was out of range. Must be 0-255, was ", AnsiColor.BLUE, colorCode);
			}
		}

		public Foreground(int colorCode) {
			this(BitDepth.EIGHT, colorCode);
		}
	}

	public static class Background extends AnsiColorBuilder {
		public Background(BitDepth bitDepth, int colorCode) {
			super(ColorType.BACKGROUND, bitDepth, colorCode);

			try {
				validateColorRange();
			} catch (Exception e) {
				AnsiLogger.warning("Color code was out of range. Must be 0-255, was ", AnsiColor.BLUE, colorCode);
			}
		}

		public Background(int colorCode) {
			this(BitDepth.EIGHT, colorCode);
		}

		public Background(@NotNull AnsiColor ansiColor) {
			this(ansiColor.bitDepth, ansiColor.code + 10);
		}
	}

	protected boolean isFourBitForeground(int colorCode) {
		return (colorCode >= 30 && colorCode <= 37) || (colorCode == 39) || (colorCode >= 90 && colorCode <= 97);
	}

	protected boolean isFourBitBackground(int colorCode) {
		return (colorCode >= 40 && colorCode <= 47) || (colorCode == 49) || (colorCode >= 100 && colorCode <= 107);
	}

	protected void validateColorRange() throws ColorCodeOutOfRange {
		if (this.bitDepth.equals(BitDepth.FOUR)) {
			if (!(isFourBitBackground(this.colorCode) || isFourBitForeground(this.colorCode))) {
				throw new ColorCodeOutOfRange("Invalid color code range. Four bit color must be between 30-37, 40-47, 90-97 or 100-107");
			}
		}
		else {
			if (this.colorCode < 0 || this.colorCode > 255)
				throw new ColorCodeOutOfRange("Invalid color code range. Eight bit color must be between 0-255");
		}
	}

	/**
	 * Get the ANSI color code
	 * @return An integer with the code
	 */
	@Override
	public int getCode() {
		return colorCode;
	}

	@Override
	public int getBitDepth() {
		return bitDepth.getValue();
	}

	@Override
	public boolean isForeground() {
		return colorType.equals(ColorType.FOREGROUND);
	}

	@Override
	public boolean isBackground() {
		return colorType.equals(ColorType.BACKGROUND);
	}

	public ColorType getColorType() {
		return colorType;
	}

	@Override
	public String toString() {
		return "AnsiColorBuilder{" +
				"colorCode=" + colorCode +
				", colorType=" + colorType +
				", bitDepth=" + bitDepth +
				'}';
	}
}
