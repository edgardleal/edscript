package carga.cron;

import java.util.regex.*;

import carga.log.*;
import carga.string.*;

public class ExpressionParser {

	private static int[] MINUTES = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
			27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43,
			44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60 };

	private static int[] HOURS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
			11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };

	Pattern module = Pattern.compile("(\\d+|\\*)\\/(\\d+)");
	Pattern sequence = Pattern.compile("(\\d{1,2}|\\*)(,\\d{1,2})*");
	private String[] parts;

	public ExpressionParser() {

	}

	public CronExpression parser(final String expression) {
		this.parts = expression.split(Constants.SPACE);
		CronExpression result = new CronExpression();

		int[] minutes = null;
		int[] hours = null;

		if (!module.matcher(parts[0]).find()) {
			minutes = generateMinuteSequence(parts[0]);
		} else if (module.matcher(parts[0]).find()) {
			minutes = generateMinuteModule(parts[0]);
		}

		if (!module.matcher(parts[1]).find()) {
			hours = generateHourSequence(parts[1]);
		} else if (module.matcher(parts[1]).find()) {
			hours = generateHourModule(parts[1]);
		}

		result.setStringExpression(expression);
		result.setMinutes(minutes);
		result.setHours(hours);

		Logger.debug("Expressao compilada: [%s]", result.toString());
		return result;
	}

	private int[] generateMinuteModule(String string) {
		String parts[] = string.split("\\/");
		if (parts[0].equals("*")) {
			parts[0] = "0";
		}
		int start = Integer.valueOf(parts[0]), step = Integer.valueOf(parts[1]);

		int[] result = new int[Math.round(60 / step)];
		for (int i = 0; i < result.length; i++) {
			result[i] = start;
			start += step;
		}

		return result;
	}

	private int[] generateHourModule(String string) {
		String parts[] = string.split("\\/");
		if (parts[0].equals("*")) {
			parts[0] = "0";
		}
		int start = Integer.valueOf(parts[0]), step = Integer.valueOf(parts[1]);

		int[] result = new int[Math.round(24 / step)];
		for (int i = 0; i < result.length; i++) {
			result[i] = start;
			start += step;
		}

		return result;
	}

	private int[] generateMinuteSequence(String string) {
		if (string.equals("*")) {
			return MINUTES;
		}
		String[] parts = string.split(",");
		int[] result = new int[parts.length];
		for (int i = 0; i < parts.length; i++) {
			result[i] = Integer.valueOf(parts[i]);
		}
		return result;
	}

	private int[] generateHourSequence(String string) {
		if (string.equals("*")) {
			return HOURS;
		}
		String[] parts = string.split(",");
		int[] result = new int[parts.length];
		for (int i = 0; i < parts.length; i++) {
			result[i] = Integer.valueOf(parts[i]);
		}
		return result;
	}

	public static void main(String[] args) {
		ExpressionParser parser = new ExpressionParser();

		System.out.println(parser.parser("* * * "));
		System.out.println(parser.parser("3 * * "));
		System.out.println(parser.parser("1,2,5 * * "));
		System.out.println(parser.parser("2/5 * * "));
		System.out.println(parser.parser("10/15 * * "));
		System.out.println(parser.parser("1 3/5 * "));
	}
}
