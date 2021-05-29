package com.frejdh.util.common.ansi.models;

/**
 * Interface for returning the code for the color/style etc.
 */
public interface AnsiColorInterface extends AnsiCodeInterface {

	int getBitDepth();

	boolean isForeground();

	boolean isBackground();
}
