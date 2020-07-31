package com.frejdh.util.common;

import com.frejdh.util.common.ansi.AnsiColor;
import com.frejdh.util.common.ansi.AnsiElement;
import com.frejdh.util.common.ansi.AnsiOutput;
import com.frejdh.util.common.ansi.builder.AnsiColorBuilder;
import com.frejdh.util.common.ansi.AnsiLogger;
import com.frejdh.util.common.ansi.builder.BitDepth;
import com.frejdh.util.common.ansi.builder.ColorCodeOutOfRange;
import com.frejdh.util.common.ansi.builder.ColorType;
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * TODO: Add more tests
 */
public class SimpleTests {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;

	private void setupCustomStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	private void restoreStreams() {
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	/**
	 * Manual test for some color codes. Dunno how to make a "good" test for visual stuff...
	 */
	@Test
	public void colorTest() throws Exception {
		String prependString = "Test of color: ";

		AnsiColorBuilder custom1 = new AnsiColorBuilder.Foreground(BitDepth.EIGHT, 214);
		AnsiColorBuilder custom2 = new AnsiColorBuilder.Background(196);
		AnsiColorBuilder custom3 = new AnsiColorBuilder.Foreground(BitDepth.FOUR, 32);

		// Test 'color code out of range' exception
		setupCustomStreams();
		String warningMessage = AnsiOutput.toString(new AnsiElement("WARNING ", AnsiColor.YELLOW), AnsiColor.DEFAULT, "Color code was out of range. Must be 0-255, was ", AnsiColor.BLUE, "%s");

		int colorcode = 256; new AnsiColorBuilder.Foreground(BitDepth.EIGHT, colorcode);
		Assert.assertEquals("Warning 1 - Test", String.format(warningMessage, colorcode), outContent.toString().replaceAll("[\n\r]", ""));
		outContent.reset();

		colorcode = -1; new AnsiColorBuilder.Foreground(BitDepth.EIGHT, colorcode);
		Assert.assertEquals("Warning 2 - Test", String.format(warningMessage, colorcode), outContent.toString().replaceAll("[\n\r]", ""));
		outContent.reset();

		colorcode = 27; new AnsiColorBuilder.Foreground(BitDepth.FOUR, colorcode);
		Assert.assertEquals("Warning 3 - Test", String.format(warningMessage, colorcode), outContent.toString().replaceAll("[\n\r]", ""));
		outContent.reset();

		colorcode = 62; new AnsiColorBuilder.Foreground(BitDepth.FOUR, colorcode);
		Assert.assertEquals("Warning 4 - Test", String.format(warningMessage, colorcode), outContent.toString().replaceAll("[\n\r]", ""));
		outContent.reset();
		restoreStreams();

		// 4-bit
		AnsiLogger.debug(prependString, AnsiColor.GREEN, AnsiColor.GREEN.toString());
		AnsiLogger.debug(prependString, AnsiColor.BRIGHT_BLUE, AnsiColor.BRIGHT_BLUE.toString());
		AnsiLogger.debug(prependString, AnsiColor.BRIGHT_RED, AnsiColor.BRIGHT_RED.toString());
		AnsiLogger.debug(prependString, AnsiColor.GRAY, AnsiColor.GRAY.toString());
		AnsiLogger.debug(prependString, AnsiColor.WHITE, AnsiColor.WHITE.toString());

		// 8-bit
		AnsiLogger.debug(prependString, AnsiColor.BRIGHT_GRAY, AnsiColor.BRIGHT_GRAY.toString());
		AnsiLogger.debug(prependString, AnsiColor.LIME, AnsiColor.LIME.toString());

		// Builder
		AnsiLogger.debug(prependString, custom1, custom1.toString());
		AnsiLogger.debug(prependString, custom2, custom2.toString());
		AnsiLogger.debug(prependString, custom3, custom3.toString());
	}
}
