package carga.interpretter.command;

import carga.interpretter.*;
import carga.string.*;

/**
 * Atribui a variavel result o valor para a substring quando encontrado
 * 
 * @author edgardleal
 *
 */
public class IndexOf extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {

		if (scoop == null) {
			scoop = this.scoope;
		}
		this.parseVariablesInArgs();

		scoop.put(Constants.RESULT_FIELD, this.arg[0].indexOf(this.arg[1]));
		return null;
	}

}
