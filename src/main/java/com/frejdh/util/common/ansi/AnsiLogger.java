package com.frejdh.util.common.ansi;

import com.frejdh.util.common.ansi.annotation.AnsiProperties;
import com.frejdh.util.common.ansi.builder.AnsiColorBuilder;
import com.frejdh.util.common.ansi.models.AnsiColor;
import com.frejdh.util.common.ansi.models.AnsiElement;
import com.frejdh.util.common.ansi.models.AnsiStyle;
import com.frejdh.util.common.ansi.models.LogLevel;
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
@Service
public class AnsiLogger {

	@FunctionalInterface
	private interface AnsiLoggingProcedure {
		void log();
	}

	// Output stream
	private static PrintStream printStream;

	// Ansi properties
	@Autowired private AnsiProperties autowiredProperties;
	private static AnsiProperties properties;
	private static SimpleDateFormat timestampFormat;

	// Init
	@PostConstruct
	public void initProperties() { // If spring-boot is used, load the defined properties
		properties = autowiredProperties;
		timestampFormat = new SimpleDateFormat(properties.getTimestamp().getFormat());
	}

	static {
		properties = new AnsiProperties();
		timestampFormat = new SimpleDateFormat(properties.getTimestamp().getFormat());
		printStream = System.out;
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
	 * Use {@link #info(Object...)} instead. Method will be removed in a future version.
	 * @param objects Text, colors or styles to add to the log
	 */
	@Deprecated
	public static void information(Object... objects) {
		info(objects);
	}

	/**
	 * Logging for INFORMATION tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 */
	public static void info(Object... objects) {
		final Object[] logObjects = ArrayUtils.addAll(new Object[]{getTimestamp(), new AnsiElement(LogLevel.INFO.getText(), AnsiColor.GREEN), AnsiColor.DEFAULT, " "}, objects);

		executeLoggingProcedure(LogLevel.INFO, () -> AnsiOutput.synchronizedPrint(printStream, logObjects));
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
	public static void debug(Throwable exceptionStacktrace, Object... objects) {
		final Object[] logObjects = ArrayUtils.addAll(new Object[]{getTimestamp(), new AnsiElement(LogLevel.DEBUG.getText(), AnsiColor.CYAN), AnsiColor.DEFAULT, " "}, objects,
				exceptionStacktrace != null ? new Object[]{AnsiColor.RED, CommonUtils.stacktraceToString(exceptionStacktrace)} : "");
		executeLoggingProcedure(LogLevel.DEBUG, () -> AnsiOutput.synchronizedPrint(printStream, logObjects));
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
	public static void trace(Throwable exceptionStacktrace, Object... objects) {
		final Object[] logObjects = ArrayUtils.addAll(new Object[]{getTimestamp(), new AnsiElement(LogLevel.TRACE.getText(), AnsiColor.GRAY), AnsiColor.DEFAULT, " "}, objects,
				exceptionStacktrace != null ? new Object[]{AnsiColor.RED, CommonUtils.stacktraceToString(exceptionStacktrace)} : "");
		executeLoggingProcedure(LogLevel.TRACE, () -> AnsiOutput.synchronizedPrint(printStream, logObjects));
	}

	/**
	 * Use {@link #warn(Object...)} instead. Method will be removed in a future version.
	 */
	@Deprecated
	public static void warning(Object... objects) {
		warning(null, objects);
	}

	/**
	 * Use {@link #warn(Throwable, Object...)} instead. Method will be removed in a future version.
	 * @param objects Text, colors or styles to add to the log
	 * @param exceptionStacktrace Optional stacktrace
	 */
	@Deprecated
	public static void warning(Throwable exceptionStacktrace, Object... objects) {
		warn(exceptionStacktrace, objects);
	}

	/**
	 * Logging for WARNING tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 */
	public static void warn(Object... objects) {
		warning(null, objects);
	}

	/**
	 * Logging for WARNING tags.
	 *
	 * @param objects Text, colors or styles to add to the log
	 * @param exceptionStacktrace Optional stacktrace
	 */
	public static void warn(Throwable exceptionStacktrace, Object... objects) {
		final Object[] logObjects = ArrayUtils.addAll(new Object[]{getTimestamp(), new AnsiElement(LogLevel.WARN.getText(), AnsiColor.YELLOW), AnsiColor.DEFAULT, " "}, objects,
				exceptionStacktrace != null ? new Object[]{AnsiColor.RED, CommonUtils.stacktraceToString(exceptionStacktrace)} : "");
		executeLoggingProcedure(LogLevel.WARN, () -> AnsiOutput.synchronizedPrint(printStream, logObjects));
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
	public static void error(Throwable exceptionStacktrace, Object... objects) {
		final Object[] logObjects = ArrayUtils.addAll(new Object[]{getTimestamp(), new AnsiElement(LogLevel.ERROR.getText(), AnsiColor.RED), AnsiColor.DEFAULT, " "}, objects,
				exceptionStacktrace != null ? new Object[]{AnsiColor.RED, CommonUtils.stacktraceToString(exceptionStacktrace)} : "");
		executeLoggingProcedure(LogLevel.ERROR, () -> AnsiOutput.synchronizedPrint(printStream, logObjects));
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
	public static void major(Throwable exceptionStacktrace, Object... objects) {
		final Object[] logObjects = ArrayUtils.addAll(new Object[]{getTimestamp(), new AnsiElement(LogLevel.MAJOR.getText(), AnsiColor.RED, new AnsiColorBuilder.Background(AnsiColor.BRIGHT_YELLOW), AnsiStyle.BOLD), AnsiColor.DEFAULT, " "}, objects,
				exceptionStacktrace != null ? new Object[]{AnsiColor.RED, CommonUtils.stacktraceToString(exceptionStacktrace)} : "");
		executeLoggingProcedure(LogLevel.MAJOR, () -> AnsiOutput.synchronizedPrint(printStream, logObjects));
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
	public static void critical(Throwable exceptionStacktrace, Object... objects) {
		final Object[] logObjects = ArrayUtils.addAll(new Object[]{getTimestamp(), new AnsiElement(LogLevel.CRITICAL.getText(), AnsiColor.WHITE, new AnsiColorBuilder.Background(AnsiColor.RED), AnsiStyle.BOLD), AnsiColor.DEFAULT, " "}, objects,
				exceptionStacktrace != null ? new Object[]{AnsiColor.RED, CommonUtils.stacktraceToString(exceptionStacktrace)} : "");
		executeLoggingProcedure(LogLevel.CRITICAL, () -> AnsiOutput.synchronizedPrint(printStream, logObjects));
	}

	private static boolean shouldLog(LogLevel logLevel) {
		String currentPath = Arrays.stream(new Throwable().getStackTrace())
				.map(StackTraceElement::getClassName)
				.filter(classpath -> !classpath.equals(AnsiLogger.class.getCanonicalName()))
				.findFirst().orElse(null);

		while (currentPath != null) {
			LogLevel definedLoggingLevel = properties.getPaths().get(currentPath);
			if (definedLoggingLevel != null) {
				return LogLevel.isHigherOrEqualLevel(definedLoggingLevel, logLevel);
			}
			currentPath = currentPath.contains(".") ? currentPath.substring(0, currentPath.lastIndexOf(".")) : null;
		}
		return LogLevel.isHigherOrEqualLevel(properties.getDefaultLevel(), logLevel);
	}

	private static void executeLoggingProcedure(LogLevel logLevel, AnsiLoggingProcedure loggingProcedure) {
		if (shouldLog(logLevel)) {
			loggingProcedure.log();
		}
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
