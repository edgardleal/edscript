/**
 *
 */
package carga.string;

/**
 * @author Edgard Leal
 * 
 */
public class StringUtil {

	public static String capitalize(final String value) {
		if (isNullOrEmpty(value)) {
			return null;
		}
		return concat(value.substring(0, 1).toUpperCase(),
				value.substring(1, value.length()));
	}

	public static boolean isNullOrEmpty(final String value) {
		return value == null || value.trim().equals(Constants.EMPTY_STRING);
	}

	public static String ifNullOrEmpty(final String value,
			final String alternative) {
		return isNullOrEmpty(value) ? alternative : value;
	}

	public static String fillString(String original, String filler,
			String string3, int finalSize) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < finalSize; i++) {
			builder.append(filler);
		}
		return builder.toString();
	}

	public static String concat(String... strings) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < strings.length; i++) { // java <= 6 compatibility
			builder.append(strings[i]);
		}
		return builder.toString();
	}

	public static StringAppender appender(final String value) {
		return new StringAppender(value);
	}

}
