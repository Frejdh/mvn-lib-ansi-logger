package com.frejdh.util.common;

import com.frejdh.util.common.toolbox.CommonUtils;
import com.frejdh.util.common.toolbox.OperatingSystemUtils;
import com.frejdh.util.common.toolbox.ReflectionUtils;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Logging with ANSI color codes.
 */
@SuppressWarnings("WeakerAccess")
public class AnsiOutput {

	private static final char ANSI_START_1 = 27; // 001B
	private static final char ANSI_START_2 = '[';
	protected static final String ANSI_START = String.valueOf(ANSI_START_1) + ANSI_START_2;
	; // 001B
	protected static final char ANSI_END = 'm';

	static {
		if (OperatingSystemUtils.isWindows()) {
			AnsiConsole.systemInstall();

			// If the mode 'STRIP_ANSI' is active, disable Jansi permanently
			try {
				Field field = ReflectionUtils.setFieldToAccessible(AnsiConsole.class, "JANSI_STDOUT_TYPE");
				String enumValue = field.get(ReflectionUtils.getInnerClassOrEnum(AnsiConsole.class, "JansiOutputType")).toString();
				if (enumValue.equalsIgnoreCase("STRIP_ANSI"))
					AnsiConsole.systemUninstall();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
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
		Ansi block = ansi();
		AnsiColor prevColor = AnsiColor.DEFAULT;
		AnsiStyle prevStyle = AnsiStyle.NORMAL;

		for (Object el : elements) {
			if (el instanceof AnsiCodeInterface) {// Style or color
				block.a(toAnsi((AnsiCodeInterface) el));

				// Save the color/style
				if (isAnsiColor((AnsiCodeInterface) el))
					prevColor = (AnsiColor) el;
				else if (isAnsiStyle((AnsiCodeInterface) el))
					prevStyle = (AnsiStyle) el;
			}
			else if (el instanceof org.springframework.boot.ansi.AnsiElement) { // Spring's ANSI element class. One color or style.
				org.springframework.boot.ansi.AnsiElement recasted = (org.springframework.boot.ansi.AnsiElement) el;
				block.a(toAnsi(recasted));

				// Save the color/style
				AnsiColor foundColor = AnsiColor.colorCodeToEnum(recasted.toString());
				AnsiStyle foundStyle = AnsiStyle.styleCodeToEnum(recasted.toString());
				if (isAnsiColor(foundColor))
					prevColor = foundColor;
				if (isAnsiStyle(foundStyle))
					prevStyle = foundStyle;
			}
			else if (el instanceof AnsiElement) { // My ANSI element class. Text with optional color and style
				// TODO: RESTORE TO PREVIOUS COLOR!!!!!!
				boolean hasColor = ((AnsiElement) el).hasColor();
				boolean hasStyle = ((AnsiElement) el).hasStyle();

				if (hasColor) {
					block.a(toAnsi((AnsiCodeInterface) ((AnsiElement) el).getColor()));
				}
				if (hasStyle) {
					block.a(toAnsi((AnsiCodeInterface) ((AnsiElement) el).getStyle()));
				}
				block.a(((AnsiElement) el).getText());

				// Text added, return to the previous state
				if (hasColor)
					block.a(toAnsi(prevColor));
				if (hasStyle)
					block.a(toAnsi(prevStyle));
			}
			else { // Normal object, use toString()
				block.a(el);
			}
		}

		return block.reset().toString();
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
		String sout = elements.length == 1 && elements[0] instanceof String
				? elements[0].toString() : toString(elements);

		synchronized (System.out) {
			System.out.flush();
			System.out.println(sout);
		}
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
		ansi().eraseScreen();
	}

	private static boolean isAnsiColor(AnsiCodeInterface ansiCodeInstance) {
		return (ansiCodeInstance instanceof AnsiColor);
	}

	private static boolean isAnsiStyle(AnsiCodeInterface ansiCodeInstance) {
		return (ansiCodeInstance instanceof AnsiStyle);
	}

	private static String toAnsi(AnsiCodeInterface ansiCodeInstance) {
		return ANSI_START + ansiCodeInstance.getCode() + ANSI_END;
	}

	private static String toAnsi(org.springframework.boot.ansi.AnsiElement ansiElement) {
		return ANSI_START + ansiElement.toString() + ANSI_END;
	}

}
