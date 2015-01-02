package carga.interpretter.command;

import carga.*;
import carga.interpretter.*;
import carga.string.*;

/**
 * 
 * @author edgardleal
 *
 */
public class Exec extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Exec() {
	}

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		scoop = checkScoope(scoop);

		for (String string : arg) {
			if (scoop.get(string) == null) {
				throw new SyntaxException(StringUtil.concat("The procedure [",
						string, "] dosent exists."));
			}
		}
		return scoop;
	}

}
