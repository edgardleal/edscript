package carga.interpretter.command;

import carga.*;
import carga.interpretter.*;

public class Execute extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		checkScoope(scoop);
		checkArgs(args);

		carga.sql.Query query = context.getInstance(Queryes.class).getQuery(
				this.scoope.parse(this.arg[0]));

		query.execute();
		return scoop;
	}
}
