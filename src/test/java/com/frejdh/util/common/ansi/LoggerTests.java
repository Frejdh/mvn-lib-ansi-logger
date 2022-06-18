package com.frejdh.util.common.ansi;

import com.frejdh.util.common.ansi.annotation.AnsiProperties;
import com.frejdh.util.common.ansi.builder.AnsiColorBuilder;
import com.frejdh.util.common.ansi.builder.BitDepth;
import com.frejdh.util.common.ansi.builder.ColorCodeOutOfRange;
import com.frejdh.util.common.ansi.models.AnsiColor;
import com.frejdh.util.common.ansi.models.LogLevel;
import com.frejdh.util.environment.Config;
import com.frejdh.util.environment.test.helper.TestProperty;
import com.frejdh.util.environment.test.helper.TestPropertyExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith({SpringExtension.class, TestPropertyExtension.class})
@EnableConfigurationProperties(AnsiProperties.class)
public class LoggerTests {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;

	private AnsiProperties ansiProperties;

	@BeforeEach
	public void setupCustomStreams() {
		outContent.reset();
		AnsiLogger.setPrintStream(new PrintStream(outContent));
	}

	@AfterEach
	public void restoreStreams() {
		AnsiLogger.setPrintStream(originalOut);
	}

	@BeforeEach
	public void setupSpies() {
		Config.refresh(true);
		this.ansiProperties = Mockito.spy(new AnsiProperties());
		AnsiLogger.properties = this.ansiProperties;
	}

	/**
	 * Manual test for some color codes.
	 */
	@Test
	public void noExceptionsOnValidColorCodes() throws Exception {
		String prependString = "Test of color: ";

		AnsiColorBuilder custom1 = new AnsiColorBuilder.Foreground(BitDepth.EIGHT, 214);
		AnsiColorBuilder custom2 = new AnsiColorBuilder.Background(196);
		AnsiColorBuilder custom3 = new AnsiColorBuilder.Foreground(BitDepth.FOUR, 32);

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

	/**
	 * Manual test for some color codes.
	 */
	@Test
	public void throwsExceptionOnInvalidColorCodes() throws Exception {
		Assertions.assertThrows(ColorCodeOutOfRange.class, () -> new AnsiColorBuilder.Foreground(BitDepth.EIGHT, 256));
		outContent.reset();

		Assertions.assertThrows(ColorCodeOutOfRange.class, () -> new AnsiColorBuilder.Foreground(BitDepth.EIGHT, -1));
		outContent.reset();

		Assertions.assertThrows(ColorCodeOutOfRange.class, () -> new AnsiColorBuilder.Foreground(BitDepth.FOUR, 27));
		outContent.reset();

		Assertions.assertThrows(ColorCodeOutOfRange.class, () -> new AnsiColorBuilder.Foreground(BitDepth.FOUR, 62));
		outContent.reset();
	}

	@Test
	@TestProperty(key = "ansi.logging.paths.com.frejdh.util.common.ansi.LoggerTests", value = "DEBUG")
	public void classLoggingWorks() {
		AnsiLogger.trace("Test1");
		AnsiLogger.debug("Test2");

		String output = outContent.toString();
		Assertions.assertFalse(output.contains("Test1"), "Was: " + output);
		Assertions.assertTrue(output.contains("Test2"), "Was: " + output);
	}

	@Test
	public void testDefaultLoggingLevelsForTrace() {
		Mockito.when(ansiProperties.getDefaultLevel()).thenReturn(LogLevel.TRACE);

		AnsiLogger.trace("trace");
		AnsiLogger.debug("debug");

		Assertions.assertTrue(outContent.toString().contains("trace"), "Was: " + outContent);
		Assertions.assertTrue(outContent.toString().contains("debug"), "Was: " + outContent);
	}

	@Test
	public void testDefaultLoggingLevelsForCritical() {
		Mockito.when(ansiProperties.getDefaultLevel()).thenReturn(LogLevel.CRITICAL);

		AnsiLogger.error("error");
		AnsiLogger.critical("critical");

		Assertions.assertFalse(outContent.toString().contains("error"), "Was: " + outContent);
		Assertions.assertTrue(outContent.toString().contains("critical"), "Was: " + outContent);
	}

	@Test
	public void testPathLevels() {
		Mockito.when(ansiProperties.getDefaultLevel()).thenReturn(LogLevel.TRACE);
		Mockito.when(ansiProperties.getPathLogLevel(eq("com.frejdh"))).thenReturn(LogLevel.ERROR);

		AnsiLogger.info("info");
		AnsiLogger.error("error");
		AnsiLogger.critical("critical");

		Assertions.assertFalse(outContent.toString().contains("info"), "Was: " + outContent);
		Assertions.assertTrue(outContent.toString().contains("error"), "Was: " + outContent);
		Assertions.assertTrue(outContent.toString().contains("critical"), "Was: " + outContent);
	}

	@Test
	public void doesNotThrowExceptionOnNullElements() {
		Assertions.assertDoesNotThrow(() -> AnsiLogger.info("some ", null, " elements"));
	}

}
