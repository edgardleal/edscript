package carga.interpretter.command;

import carga.interpretter.*;
import carga.string.*;

public class Lower extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {

		if (scoop == null) {
			scoop = this.scoope;
		}

		for (String c : this.arg) {
			Object value = scoop.get(c);
			scoop.put(c, value == null ? Constants.NULL : value.toString()
					.toLowerCase());
		}
		return null;
	}

}
