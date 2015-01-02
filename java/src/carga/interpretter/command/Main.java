package carga.interpretter.command;

import carga.*;
import carga.interpretter.*;

public class Main extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Main() {
		this.level = -1;
	}

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		executeCommands();
		this.context.getInstance(Queryes.class).closeAll();
		this.context.getInstance(ConnectionFactory.class).closeAll();
		this.scoope.dispose();
		return scoop;
	}

}
