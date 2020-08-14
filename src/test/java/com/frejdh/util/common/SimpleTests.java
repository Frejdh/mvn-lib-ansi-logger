package com.frejdh.util.common;

import com.frejdh.util.common.ansi.AnsiColor;
import com.frejdh.util.common.ansi.AnsiElement;
import com.frejdh.util.common.ansi.AnsiOutput;
import com.frejdh.util.common.ansi.annotation.LoggingProperties;
import com.frejdh.util.common.ansi.builder.AnsiColorBuilder;
import com.frejdh.util.common.ansi.AnsiLogger;
import com.frejdh.util.common.ansi.builder.BitDepth;
import com.frejdh.util.common.toolbox.ReflectionUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * TODO: Add more tests
 */
public class SimpleTests {

	private final ByteArrayOutputStream outStreamCustom = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errStreamCustom = new ByteArrayOutputStream();

	private final PrintStream outStreamOriginal = System.out;
	private final PrintStream errStreamOriginal = System.err;

	private void restoreStreams() {
		System.setOut(outStreamOriginal);
		System.setErr(errStreamOriginal);
	}

	@Before
	public void setLoggingLevels() throws Exception {
		setLoggingLevels("");
	}

	public void setLoggingLevels(String excludedFields) throws Exception {
		LoggingProperties originalProperties = ReflectionUtils.getVariable(AnsiLogger.class, "properties", LoggingProperties.class);
		ReflectionUtils.setVariable(originalProperties.getLevels(), "exclude", excludedFields);
		ReflectionUtils.invokeMethod(new AnsiLogger(), "parseAndSetLoggingLevels");
	}

	/**
	 * Manual test for some color codes. Dunno how to make a "good" test for visual stuff...
	 */
	@Test
	public void colorTest() throws Exception {
		String prependString = "Test of color: ";
		PrintStream customPrint = new PrintStream(outStreamCustom);
		AnsiLogger.setPrintStream(customPrint);

		AnsiColorBuilder custom1 = new AnsiColorBuilder.Foreground(BitDepth.EIGHT, 214);
		AnsiColorBuilder custom2 = new AnsiColorBuilder.Background(196);
		AnsiColorBuilder custom3 = new AnsiColorBuilder.Foreground(BitDepth.FOUR, 32);

		// Test 'color code out of range' exception
		String warningMessage = "Color code was out of range. Must be 0-255, was ";

		int colorcode = 256;
		new AnsiColorBuilder.Foreground(BitDepth.EIGHT, colorcode);
		MatcherAssert.assertThat("Warning 1 - Test", outStreamCustom.toString(), CoreMatchers.containsString(warningMessage));
		outStreamCustom.reset();

		colorcode = -1;
		new AnsiColorBuilder.Foreground(BitDepth.EIGHT, colorcode);
		MatcherAssert.assertThat("Warning 2 - Test", outStreamCustom.toString(), CoreMatchers.containsString(warningMessage));
		outStreamCustom.reset();

		colorcode = 27;
		new AnsiColorBuilder.Foreground(BitDepth.FOUR, colorcode);
		MatcherAssert.assertThat("Warning 3 - Test", outStreamCustom.toString(), CoreMatchers.containsString(warningMessage));
		outStreamCustom.reset();

		colorcode = 62;
		new AnsiColorBuilder.Foreground(BitDepth.FOUR, colorcode);
		MatcherAssert.assertThat("Warning 4 - Test", outStreamCustom.toString(), CoreMatchers.containsString(warningMessage));
		outStreamCustom.reset();

		// Visual tests
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

		restoreStreams();
	}

	@Test
	public void testArrayPrintout() {
		Object[] array = new Object[]{"Test of print. ", AnsiColor.BLUE, "blue", AnsiColor.RED, " red "};
		System.out.println(AnsiOutput.toString("Test: ", array));
	}

	@Test
	public void testExcludeLevels() throws Exception {
		String properties = "trace, debug, critical";
		setLoggingLevels(properties);
		PrintStream customPrint = new PrintStream(outStreamCustom);
		AnsiLogger.setPrintStream(customPrint);

		AnsiLogger.trace("test 1");
		Assert.assertEquals("", outStreamCustom.toString().replaceAll("[\n\r\\s]", ""));
		outStreamCustom.reset();

		AnsiLogger.debug("test 2");
		Assert.assertEquals("", outStreamCustom.toString().replaceAll("[\n\r\\s]", ""));
		outStreamCustom.reset();

		AnsiLogger.information("test 3");
		Assert.assertNotEquals("", outStreamCustom.toString().replaceAll("[\n\r\\s]", ""));
		outStreamCustom.reset();

		AnsiLogger.warning("test 4");
		Assert.assertNotEquals("", outStreamCustom.toString().replaceAll("[\n\r\\s]", ""));
		outStreamCustom.reset();

		AnsiLogger.error("test 5");
		Assert.assertNotEquals("", outStreamCustom.toString().replaceAll("[\n\r\\s]", ""));
		outStreamCustom.reset();

		AnsiLogger.major("test 6");
		Assert.assertNotEquals("", outStreamCustom.toString().replaceAll("[\n\r\\s]", ""));
		outStreamCustom.reset();

		AnsiLogger.critical("test 7");
		Assert.assertEquals("", outStreamCustom.toString().replaceAll("[\n\r\\s]", ""));
		outStreamCustom.reset();

		restoreStreams();
		setLoggingLevels(); // Reset for other tests
	}
}
