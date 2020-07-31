package com.frejdh.util.common.ansi;

import org.springframework.lang.Nullable;
import java.util.Arrays;
import java.util.List;

public class AnsiElement {
	private String text;
	private List<AnsiCodeInterface> colorsAndStyles;

	public AnsiElement(@Nullable String text, AnsiCodeInterface... colorsAndStyles) {
		this.text = text != null ? text : "";
		this.colorsAndStyles = Arrays.asList(colorsAndStyles);
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

	public AnsiColorInterface getForegroundColor() {
		return (AnsiColorInterface) colorsAndStyles.stream().filter(el -> el instanceof AnsiColorInterface && ((AnsiColorInterface) el).isForeground()).findFirst().orElse(null);
	}

	public AnsiColorInterface getBackgroundColor() {
		return (AnsiColorInterface) colorsAndStyles.stream().filter(el -> el instanceof AnsiColorInterface && ((AnsiColorInterface) el).isBackground()).findFirst().orElse(null);
	}

	public AnsiStyle getStyle() {
		return (AnsiStyle) colorsAndStyles.stream().filter(el -> el instanceof AnsiStyle).findFirst().orElse(null);
	}

	public boolean hasForegroundColor() {
		return getForegroundColor() != null;
	}

	public boolean hasBackgroundColor() {
		return getBackgroundColor() != null;
	}

	public boolean hasStyle() {
		return getStyle() != null;
	}

	@Override
	public String toString() {
		return "AnsiElement{" +
				"text='" + text + '\'' +
				", foreground color=" + getForegroundColor() +
				", foreground color=" + getBackgroundColor() +
				", style=" + getStyle() +
				'}';
	}
}
