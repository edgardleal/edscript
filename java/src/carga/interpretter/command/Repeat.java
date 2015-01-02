package carga.interpretter.command;

import carga.interpretter.*;
import carga.string.*;

public class Repeat extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		scoop = checkScoope(scoop);

		int total = isNumber(this.arg[0]) ? Integer.valueOf(this.arg[0]) : 0;

		for (int i = 0; i < total; i++) {
			scoop.put(Constants.INCREMENT_VARIABLE, i);
			executeCommands();
		}

		scoop.put(Constants.INCREMENT_VARIABLE, 0);

		return scoop;
	}

	private boolean isNumber(String value) {
		return value != null && value.matches(Constants.REGEX_INTEGER);
	}

}
