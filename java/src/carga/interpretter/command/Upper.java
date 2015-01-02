package carga.interpretter.command;

import carga.interpretter.*;

public class Upper extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		scoop = checkScoope(scoop);
		for (String c : this.arg) {
			Object value = scoop.get(c);
			scoop.put(c, value == null ? "NULL" : value.toString()
					.toUpperCase());
		}
		return scoop;
	}

}
