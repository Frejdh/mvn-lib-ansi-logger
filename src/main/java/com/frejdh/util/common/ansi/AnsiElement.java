package com.frejdh.util.common.ansi;

import org.springframework.lang.Nullable;
import java.util.Arrays;
import java.util.List;

public class AnsiElement {
	private String text;
	private List<AnsiCodeInterface> colorsAndStyles;

	public AnsiElement(@Nullable Object element, AnsiCodeInterface... colorsAndStyles) {
		this.text = element != null ? element.toString() : "";
		this.colorsAndStyles = Arrays.asList(colorsAndStyles);
	}

	public AnsiElement(Object element, AnsiColor color) {
		this(element, color, null);
	}


	public AnsiElement(Object element, AnsiStyle style) {
		this(element, null, style);
	}

	public AnsiElement(@Nullable Object element) {
		this(element, null, null);
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
