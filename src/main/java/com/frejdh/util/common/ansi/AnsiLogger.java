package com.frejdh.util.common.ansi;

import com.frejdh.util.common.ansi.annotation.LoggingProperties;
import com.frejdh.util.common.ansi.builder.AnsiColorBuilder;
import com.frejdh.util.common.toolbox.CommonUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utilizes {@link AnsiOutput} methods for different logging actions. All methods are synchronized.
 */
@SuppressWarnings("unused")
@EnableConfigurationProperties(LoggingProperties.class)
@Service
public class AnsiLogger {

	public enum Level {
		TRACE, DEBUG, INFORMATION, WARNING, ERROR, MAJOR, CRITICAL
	}

	// Map of enabled logging levels
	private static Map<Level, Boolean> enabledLoggingLevels = new HashMap<>();

	// Output stream
	private static PrintStream printStream;

	// Ansi properties
	@Autowired private LoggingProperties autowiredProperties;
	private static LoggingProperties properties;
	private static SimpleDateFormat timestampFormat;

	// Init
	@PostConstruct
	public void initSpringProperties() { // If spring-boot is used, load the defined properties (again)
		properties = autowiredProperties;
		initProperties();
	}

	static {
		properties = new LoggingProperties();
		printStream = System.out;
		initProperties();
	}

	/**
	 * Init/reload the properties
	 */
	private static void initProperties() {
		timestampFormat = new SimpleDateFormat(properties.getTimestamp().getFormat());
		parseAndSetLoggingLevels();
	}

	/**
	 * Parse the logging levels to be used and set them in the corresponding variable
	 */
	private static void parseAndSetLoggingLevels() {
		List<String> excludedFields = new ArrayList<>(Arrays.asList(properties.getLevels().getExclude().split("\\s*[,]\\s*"))); // Mutable
		for (Level level : Level.values()) {
			enabledLoggingLevels.put(level, excludedFields.stream().noneMatch(field -> field.equalsIgnoreCase(level.toString())));
		}
	}

	/**
	 * Get the PrintStream used for by this logger class. Default is 'System.out'.
	 *
	 * @return The used PrintStream instance
	 */
	public static PrintStream getPrintStream() {
		return printStream;
	}

	/**
	 * Set the Print- or OutputStream to use for the logger. Default is 'System.out'.
	 *
	 * @param outputStream Stream in which logging should occur.
	 */
	public static void setPrintStream(@Nullable PrintStream outputStream) {
		AnsiLogger.printStream = outputStream != null ? outputStream : new PrintStream(nullOutputStream());
	}

	/**
	 * {@link #setPrintStream(PrintStream)}
	 */
	public static void setPrintStream(@Nullable OutputStream outputStream) {
		setPrintStream(outputStream != null ? new PrintStream(outputStream) : new PrintStream(nullOutputStream()));
	}

	/**
	 * Logging for TRACE tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 */
	public static void trace(Object... objects) {
		trace(null, objects);
	}

	/**
	 * Logging for TRACE tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 * @param exceptionStacktrace Optional stacktrace
	 */
	public static void trace(Exception exceptionStacktrace, Object... objects) {
		if (!enabledLoggingLevels.get(Level.TRACE))
			return;

		objects = ArrayUtils.addAll(new Object[]{getTimestamp(), new AnsiElement("TRACE ", AnsiColor.GRAY), AnsiColor.DEFAULT}, objects,
				exceptionStacktrace != null ? new Object[]{AnsiColor.GRAY, CommonUtils.exceptionStacktraceToString(exceptionStacktrace)} : "");
		AnsiOutput.synchronizedPrint(printStream, objects);
	}

	/**
	 * Logging for DEBUG tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 */
	public static void debug(Object... objects) {
		debug(null, objects);
	}

	/**
	 * Logging for DEBUG tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 * @param exceptionStacktrace Optional stacktrace
	 */
	public static void debug(Exception exceptionStacktrace, Object... objects) {
		if (!enabledLoggingLevels.get(Level.DEBUG))
			return;

		objects = ArrayUtils.addAll(new Object[]{getTimestamp(), new AnsiElement("DEBUG ", AnsiColor.CYAN), AnsiColor.DEFAULT}, objects,
				exceptionStacktrace != null ? new Object[]{AnsiColor.RED, CommonUtils.exceptionStacktraceToString(exceptionStacktrace)} : "");
		AnsiOutput.synchronizedPrint(printStream, objects);
	}

	/**
	 * Logging for INFORMATION tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 */
	public static void information(Object... objects) {
		if (!enabledLoggingLevels.get(Level.INFORMATION))
			return;

		objects = ArrayUtils.addAll(new Object[]{getTimestamp(), new AnsiElement("INFORMATION ", AnsiColor.GREEN), AnsiColor.DEFAULT}, objects);
		AnsiOutput.synchronizedPrint(printStream, objects);
	}

	/**
	 * Logging for WARNING tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 */
	public static void warning(Object... objects) {
		warning(null, objects);
	}

	/**
	 * Logging for WARNING tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 * @param exceptionStacktrace Optional stacktrace
	 */
	public static void warning(Exception exceptionStacktrace, Object... objects) {
		if (!enabledLoggingLevels.get(Level.WARNING))
			return;

		objects = ArrayUtils.addAll(new Object[]{getTimestamp(), new AnsiElement("WARNING ", AnsiColor.YELLOW), AnsiColor.DEFAULT}, objects,
				exceptionStacktrace != null ? new Object[]{AnsiColor.RED, CommonUtils.exceptionStacktraceToString(exceptionStacktrace)} : "");
		AnsiOutput.synchronizedPrint(printStream, objects);
	}

	/**
	 * Logging for ERROR tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 */
	public static void error(Object... objects) {
		error(null, objects);
	}

	/**
	 * Logging for ERROR tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 * @param exceptionStacktrace Optional stacktrace
	 */
	public static void error(Exception exceptionStacktrace, Object... objects) {
		if (!enabledLoggingLevels.get(Level.ERROR))
			return;

		objects = ArrayUtils.addAll(new Object[]{getTimestamp(), new AnsiElement("ERROR ", AnsiColor.RED), AnsiColor.DEFAULT}, objects,
				exceptionStacktrace != null ? new Object[]{AnsiColor.RED, CommonUtils.exceptionStacktraceToString(exceptionStacktrace)} : "");
		AnsiOutput.synchronizedPrint(printStream, objects);
	}

	/**
	 * Logging for MAJOR error tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 */
	public static void major(Object... objects) {
		major(null, objects);
	}

	/**
	 * Logging for MAJOR error tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 * @param exceptionStacktrace Optional stacktrace
	 */
	public static void major(Exception exceptionStacktrace, Object... objects) {
		if (!enabledLoggingLevels.get(Level.MAJOR))
			return;

		objects = ArrayUtils.addAll(new Object[]{getTimestamp(), new AnsiElement("MAJOR", AnsiColor.RED, new AnsiColorBuilder.Background(AnsiColor.BRIGHT_YELLOW), AnsiStyle.BOLD), AnsiColor.DEFAULT, " "}, objects,
				exceptionStacktrace != null ? new Object[]{AnsiColor.RED, CommonUtils.exceptionStacktraceToString(exceptionStacktrace)} : "");
		AnsiOutput.synchronizedPrint(printStream, objects);
	}

	/**
	 * Logging for CRITICAL error tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 */
	public static void critical(Object... objects) {
		critical(null, objects);
	}

	/**
	 * Logging for CRITICAL error tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 */
	public static void critical(Exception exceptionStacktrace, Object... objects) {
		if (!enabledLoggingLevels.get(Level.CRITICAL))
			return;

		objects = ArrayUtils.addAll(new Object[]{getTimestamp(), new AnsiElement("CRITICAL", AnsiColor.WHITE, new AnsiColorBuilder.Background(AnsiColor.RED), AnsiStyle.BOLD), AnsiColor.DEFAULT, " "}, objects,
				exceptionStacktrace != null ? new Object[]{AnsiColor.RED, CommonUtils.exceptionStacktraceToString(exceptionStacktrace)} : "");
		AnsiOutput.synchronizedPrint(printStream, objects);
	}

	private static String getTimestamp() {
		return properties.getTimestamp().getEnabled() ? timestampFormat.format(Calendar.getInstance().getTime()) + "  " : "";
	}

	// Modified version of the JDK11 method (works on JDKs below that too!)
	private static OutputStream nullOutputStream() {
		return new OutputStream() {
			private volatile boolean closed;

			private void ensureOpen() throws IOException {
				if (closed) {
					throw new IOException("Stream closed");
				}
			}

			@Override
			public void write(int b) throws IOException {
				ensureOpen();
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				if ((b.length | off | b.length) < 0 || b.length > b.length - off)
					throw new IOException();
				ensureOpen();
			}

			@Override
			public void close() {
				closed = true;
			}
		};
	}
}
