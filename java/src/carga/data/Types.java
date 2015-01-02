package carga.data;

import java.util.regex.*;

import carga.string.*;

public enum Types {
	NUMBER("number"), FLOAT("float"), STRING("string"), BOOLEAN("boolean"), DATE(
			"date"), FILE("file"), VARIANT("variant"), NULL(Constants.NULL);

	private static Pattern numberPattern = Pattern.compile("^[0-9]+$");
	private static Pattern floatPattern = Pattern
			.compile("^[0-9]+(\\.[0-9]+)$");

	private String label;

	Types(String label) {
		this.label = label;
	}

	public static Types identify(Object obj) {
		if (obj == null) {
			return NULL;
		} else if (floatPattern.matcher(obj.toString()).find()) {
			return FLOAT;
		} else if (numberPattern.matcher(obj.toString()).find()) {
			return NUMBER;
		}

		return STRING;
	}

	public String toString() {
		return this.label;
	}
}
