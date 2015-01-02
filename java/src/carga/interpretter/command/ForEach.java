/**
 * 
 */
package carga.interpretter.command;

import carga.interpretter.*;
import carga.string.*;

/**
 * Cria um loop com os parametros informados
 * @author edgardleal
 *
 */
public class ForEach extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		if (scoop == null) {
			scoop = this.scoope;
		}
		for (String v : this.arg) {
			scoop.put(Constants.INCREMENT_VARIABLE, v);
			this.executeCommands();
		}
		return null;
	}

}
