package carga.interpretter.command;

import carga.interpretter.*;
import carga.string.*;

public class Trim extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		scoop = checkScoope(scoop);

		Object value;
		if (args == null) {
			args = this.arg;
		}

		for (int i = 0; i < args.length; i++) {
			value = scoop.get(args[i]);
			value = value == null ? Constants.NULL : value.toString().trim();
			scoop.put(args[i], value);
		}
		return null;
	}

}
