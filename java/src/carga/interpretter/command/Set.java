package carga.interpretter.command;

import carga.interpretter.*;

public class Set extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		scoop = checkScoope(scoop);
		StringBuilder builder = new StringBuilder();

		for (int i = 2; i < arg.length; i++) {
			builder.append(this.arg[i]);
		}

		this.scoope.put(this.scoope.parse(this.arg[0]),
				this.scoope.parse(builder.toString()));
		return null;
	}

}
