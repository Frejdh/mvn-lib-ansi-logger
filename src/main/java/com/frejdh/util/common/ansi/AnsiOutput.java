package com.frejdh.util.common.ansi;

import com.diogonunes.jcolor.Ansi;
import com.frejdh.util.common.ansi.builder.AnsiColorBuilder;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Logging with ANSI color codes.
 */
@SuppressWarnings("WeakerAccess")
public class AnsiOutput {

	private static final char ANSI_START_1 = 27; // 001B
	private static final char ANSI_START_2 = '[';
	protected static final String ANSI_START = String.valueOf(ANSI_START_1) + ANSI_START_2;
	protected static final char ANSI_END = 'm';

	static {

	}

	/**
	 * Create an ANSI string with the given elements.
	 * Utilize {@link AnsiElement}, {@link AnsiColor} or {@link AnsiStyle} for the parameters.
	 * You may also use the corresponding Spring instead classes if wished. This method will automatically reset the
	 * input color and styling at the end, so there's no need to do it manually through the parameter.
	 *
	 * @param elements Objects to insert
	 * @return A string with ANSI output
	 */
	public static String toString(Object... elements) {
		StringBuilder block = new StringBuilder();
		AnsiColorInterface prevForegroundColor = AnsiColor.DEFAULT;
		AnsiColorInterface prevBackgroundColor = new AnsiColorBuilder.Background(AnsiColor.DEFAULT);
		AnsiStyle prevStyle = AnsiStyle.NORMAL;

		for (Object el : elements) {
			if (el.getClass().isArray() || el instanceof List<?>) { // Array or list
				Object[] internalList = null;
				if (el instanceof List<?>) {
					internalList = ((List<?>) el).toArray();
				}
				else {
					internalList = (Object[]) el;
				}
				block.append(toString(internalList));
			}
			else if (el instanceof AnsiCodeInterface) {// Style or color
				block.append(toAnsi((AnsiCodeInterface) el));

				// Save the color/style
				if (isAnsiColor((AnsiCodeInterface) el))
					prevForegroundColor = (AnsiColorInterface) el;
				else if (isAnsiStyle((AnsiCodeInterface) el))
					prevStyle = (AnsiStyle) el;
			}
			else if (el instanceof org.springframework.boot.ansi.AnsiElement) { // Spring's ANSI element class. One color or style.
				org.springframework.boot.ansi.AnsiElement recasted = (org.springframework.boot.ansi.AnsiElement) el;
				block.append(toAnsi(recasted));

				// Save the color/style
				AnsiColor foundColor = AnsiColor.colorCodeToEnum(recasted.toString());
				AnsiStyle foundStyle = AnsiStyle.styleCodeToEnum(recasted.toString());
				if (isAnsiColor(foundColor))
					prevForegroundColor = foundColor;
				if (isAnsiStyle(foundStyle))
					prevStyle = foundStyle;
			}
			else if (el instanceof AnsiElement) { // My ANSI element class. Text with optional color and style
				boolean hasForegroundColor = ((AnsiElement) el).hasForegroundColor();
				boolean hasBackgroundColor = ((AnsiElement) el).hasBackgroundColor();
				boolean hasStyle = ((AnsiElement) el).hasStyle();

				if (hasForegroundColor) {
					block.append(toAnsi((AnsiColorInterface) ((AnsiElement) el).getForegroundColor()));
				}
				if (hasBackgroundColor) {
					block.append(toAnsi((AnsiColorInterface) ((AnsiElement) el).getBackgroundColor()));
				}
				if (hasStyle) {
					block.append(toAnsi((AnsiCodeInterface) ((AnsiElement) el).getStyle()));
				}
				block.append(((AnsiElement) el).getText());

				// Text added, return to the previous state
				if (hasForegroundColor)
					block.append(toAnsi(prevForegroundColor));
				if (hasBackgroundColor)
					block.append(toAnsi(prevBackgroundColor));
				if (hasStyle)
					block.append(toAnsi(prevStyle));
			}
			else { // Normal object, use toString()
				block.append(el);
			}
		}

		return Ansi.colorize(block.toString());
	}

	/**
	 * Print a string with the given elements with a temporary block of System.out.
	 * Utilize {@link AnsiElement}, {@link AnsiColor} or {@link AnsiStyle} for the parameters.
	 * You may also use the corresponding Spring instead classes if wished. This method will automatically reset the
	 * input color and styling at the end, so there's no need to do it manually through the parameter.<br>
	 *
	 * @param elements Objects to insert
	 * @return A string with ANSI output
	 */
	public static void synchronizedPrint(Object... elements) {
		synchronizedPrint(System.out, elements);
	}

	/**
	 * Print a string with the given elements with a temporary block for a PrintStream instance.
	 * Utilize {@link AnsiElement}, {@link AnsiColor} or {@link AnsiStyle} for the parameters.
	 * You may also use the corresponding Spring instead classes if wished. This method will automatically reset the
	 * input color and styling at the end, so there's no need to do it manually through the parameter.<br>
	 *
	 * @param elements Objects to insert
	 * @return A string with ANSI output
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public static void synchronizedPrint(PrintStream printStream, Object... elements) {
		String sout = elements.length == 1 && elements[0] instanceof String
				? elements[0].toString() : toString(elements);

		if (printStream != null) {
			synchronized (printStream) {
				printStream.flush();
				printStream.println(sout);
			}
		}
	}

	/**
	 * Print a string containing objects toString() along with Ansi elements.
	 * Utilize {@link AnsiElement}, {@link AnsiColor} or {@link AnsiStyle} for the parameters.
	 * You may also use the corresponding Spring instead classes if wished. This method will automatically reset the
	 * input color and styling at the end, so there's no need to do it manually through the parameter.<br>
	 *
	 * @param elements Objects to insert
	 * @return A string with ANSI output
	 */
	public static void print(Object... elements) {
		System.out.flush();
		System.out.println(toString(elements));
	}

	/**
	 * Utilizes Jansi's eraseScreen() method
	 */
	public static void clearScreen() {
		//ansi().eraseScreen();
	}

	private static boolean isAnsiColor(AnsiCodeInterface ansiCodeInstance) {
		return (ansiCodeInstance instanceof AnsiColorInterface);
	}

	private static boolean isAnsiStyle(AnsiCodeInterface ansiCodeInstance) {
		return (ansiCodeInstance instanceof AnsiStyle);
	}

	/**
	 * Implements this module's custom interface
	 */
	private static String toAnsi(AnsiCodeInterface ansiCodeInstance) {
		// Color instance and 8 bit depth
		if (ansiCodeInstance instanceof AnsiColorInterface && ((AnsiColorInterface) ansiCodeInstance).getBitDepth() == 8) {
			return ANSI_START
					+ (((AnsiColorInterface) ansiCodeInstance).isForeground() ? 38 : 48 + ";5;")
					+ ansiCodeInstance.getCode()
					+ ANSI_END;
		}
		return ANSI_START + ansiCodeInstance.getCode() + ANSI_END;
	}

	/**
	 * Implements Spring boot's interface
	 */
	private static String toAnsi(org.springframework.boot.ansi.AnsiElement ansiElement) {
		return ANSI_START + ansiElement.toString() + ANSI_END;
	}

}
