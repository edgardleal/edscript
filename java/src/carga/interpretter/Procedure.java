package carga.interpretter;

import carga.interpretter.command.*;

/**
 * 
 * @author edgardleal
 *
 */
public class Procedure extends Command {

	public Procedure() {

	}

	@Override
	public void setArg(String[] args) {
		super.setArg(args);
		this.scoope.put(args[0], this);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		scoop = checkScoope(scoop);
		this.executeCommands();
		return scoop;
	}

}
