/**
 * 
 */
package carga.interpretter.command;

import carga.*;
import carga.interpretter.*;

/**
 * @author edgardleal
 *
 */
public class CloseAll extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		this.context.getInstance(Queryes.class).closeAll();
		this.context.getInstance(ConnectionFactory.class).closeAll();
		return scoop;
	}

}
