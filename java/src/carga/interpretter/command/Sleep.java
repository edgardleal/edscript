/**
 * 
 */
package carga.interpretter.command;

import carga.interpretter.*;

/**
 * @author edgardleal
 *
 */
public class Sleep extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public Sleep() {
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

		int interval = this.arg != null && this.arg.length > 0 ? Integer
				.valueOf(this.arg[0]) : 1000;

		try {
			Thread.sleep(interval);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this.scoope;
	}

}
