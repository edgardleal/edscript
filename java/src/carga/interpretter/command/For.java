/**
 * 
 */
package carga.interpretter.command;

import carga.interpretter.*;
import carga.string.*;

/**
 * @author edgardleal
 *
 */
public class For extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public For() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see carga.interpretter.command.Command#execute(carga.interpretter.Scoop,
	 * java.lang.String[])
	 */
	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		int start = Integer.valueOf(this.arg[0]);
		int end = Integer.valueOf(this.arg[2]);
		int i = start;
		int increment = start <= end ? 1 : -1;

		while (i != end) {
			i = i + increment;
			this.scoope.put(Constants.INCREMENT_VARIABLE, i);
			this.executeCommands();
		}

		return scoop;
	}

}
