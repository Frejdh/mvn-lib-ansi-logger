package com.frejdh.util.common;

import org.springframework.lang.Nullable;

public class AnsiElement {
	private String text;
	private AnsiColor color;
	private AnsiStyle style;

	public AnsiElement(@Nullable String text, AnsiColor color, AnsiStyle style) {
		this.text = text != null ? text : "";
		this.color = color;
		this.style = style;
	}

	public AnsiElement(String text, AnsiColor color) {
		this(text, color, null);
	}


	public AnsiElement(String text, AnsiStyle style) {
		this(text, null, style);
	}

	public AnsiElement(@Nullable String text) {
		this(text, null, null);
	}

	public String getText() {
		return text;
	}

	public AnsiColor getColor() {
		return color;
	}

	public AnsiStyle getStyle() {
		return style;
	}

	public boolean hasColor() {
		return color != null;
	}

	public boolean hasStyle() {
		return style != null;
	}

	@Override
	public String toString() {
		return "AnsiElement{" +
				"text='" + text + '\'' +
				", color=" + color +
				", style=" + style +
				'}';
	}
}
