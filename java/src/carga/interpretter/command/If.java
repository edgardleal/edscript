package carga.interpretter.command;

import carga.*;
import carga.data.*;
import carga.interpretter.*;

public class If extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {

		scoop = checkScoope(scoop);

		String a = scoope.parse(this.arg[0]);
		String b = scoope.parse(this.arg[2]);
		String operador = this.arg[1];

		boolean result;

		switch (operador) {
		case "=":
			result = equal(a, b);
			break;
		case "<":
			result = lessThan(a, b);
			break;
		case ">":
			result = moreThan(a, b);
			break;
		case "<=":
			result = lessEqual(a, b);
			break;
		case "=>":
			result = moreEqual(a, b);
			break;
		case "<>":
			result = diferent(a, b);
			break;

		default:
			throw new SyntaxException(String.format(
					"Operador de comparacao invalido: [%s]", operador));
		}

		if (result) {
			this.executeCommands();
		}

		return null;
	}

	private boolean diferent(String a, String b) {
		Types typeA = Types.identify(a);
		Types typeB = Types.identify(b);

		if (typeA == Types.NUMBER && typeB == Types.NUMBER) {
			return Integer.valueOf(a) != Integer.valueOf(b);
		} else {
			return !a.equals(b);
		}
	}

	private boolean equal(String a, String b) {
		return a.equals(b);
	}

	private boolean moreThan(String a, String b) {
		Types typeA = Types.identify(a);
		Types typeB = Types.identify(b);

		if (typeA != typeB) {
			return a.length() > b.length();
		}

		if (typeA == Types.NUMBER) {
			return Integer.valueOf(a) > Integer.valueOf(b);
		} else {
			return Float.valueOf(a) > Float.valueOf(b);
		}

	}

	private boolean moreEqual(String a, String b) {
		Types typeA = Types.identify(a);
		Types typeB = Types.identify(b);

		if (typeA != typeB) {
			return a.length() >= b.length();
		}

		if (typeA == Types.NUMBER) {
			return Integer.valueOf(a) >= Integer.valueOf(b);
		} else {
			return Float.valueOf(a) >= Float.valueOf(b);
		}

	}

	private boolean lessEqual(String a, String b) {
		Types typeA = Types.identify(a);
		Types typeB = Types.identify(b);

		if (typeA != typeB) {
			return a.length() <= b.length();
		}

		if (typeA == Types.NUMBER) {
			return Integer.valueOf(a) <= Integer.valueOf(b);
		} else {
			return Float.valueOf(a) <= Float.valueOf(b);
		}

	}

	private boolean lessThan(String a, String b) {
		Types typeA = Types.identify(a);
		Types typeB = Types.identify(b);

		if (typeA != typeB) {
			return a.length() < b.length();
		}

		if (typeA == Types.NUMBER) {
			return Integer.valueOf(a) < Integer.valueOf(b);
		} else {
			return Float.valueOf(a) < Float.valueOf(b);
		}

	}

}
