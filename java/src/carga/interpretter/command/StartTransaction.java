package carga.interpretter.command;

import carga.*;
import carga.interpretter.*;

/**
 * 
 * @author edgardleal
 *
 */
public class StartTransaction extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {

		this.context.getInstance(ConnectionFactory.class)
				.getConnection(this.arg[0]).setAutoCommit(false);

		return scoop;
	}

}
