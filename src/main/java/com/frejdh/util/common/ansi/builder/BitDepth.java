package com.frejdh.util.common.ansi.builder;

// Additional enum for color bit depth
public enum BitDepth {
	FOUR(4), // 16 colors
	EIGHT(8); // 256 colors

	private final int value;
	BitDepth(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
