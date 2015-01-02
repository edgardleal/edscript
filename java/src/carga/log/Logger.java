/**
 * 
 */
package carga.log;

import org.slf4j.*;

/**
 * @author edgardleal
 *
 */
public class Logger {

	public static boolean isDebug = System.getenv("DEBUG") != null;

	private static org.slf4j.Logger l = LoggerFactory.getLogger(Logger.class);

	public static void debug(char text) {
		debug(String.valueOf(text));
	}

	public static void debug(String text) {
		if (isDebug) {
			log(text);
		}
	}

	public static void debug(String pattern, Object... args) {
		debug(String.format(pattern, args));
	}

	public static void log(String pattern, Object... args) {
		log(String.format(pattern, args));
	}

	public static void log(String message) {
		l.info(message);
	}

	public static void error(Exception ex) {
		ex.printStackTrace();
	}

	public static void error(String value) {
		error("%s", value);
	}

	public static void error(String pattern, Object... value) {
		l.error(String.format(pattern, value));
	}
}
