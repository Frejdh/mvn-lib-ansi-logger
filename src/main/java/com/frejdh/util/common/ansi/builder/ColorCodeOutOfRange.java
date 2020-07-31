package com.frejdh.util.common.ansi.builder;

public class ColorCodeOutOfRange extends Exception {

	public ColorCodeOutOfRange() { }

	public ColorCodeOutOfRange(String message) {
		super(message);
	}

	public ColorCodeOutOfRange(String message, Throwable cause) {
		super(message, cause);
	}

	public ColorCodeOutOfRange(Throwable cause) {
		super(cause);
	}
}
