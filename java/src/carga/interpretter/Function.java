/**
 * 
 */
package carga.interpretter;

import carga.interpretter.command.*;

/**
 * @author edgardleal
 *
 */
public abstract class Function extends Command {

	private Scoop innerScoop;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String name;

	public abstract String execute(String... args);

	public String getName() {
		return name;
	}

	public Scoop getInnerScoop() {
		if (innerScoop == null) {
			innerScoop = new Scoop();
		}
		return innerScoop;
	}
}
