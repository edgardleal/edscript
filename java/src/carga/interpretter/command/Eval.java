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
public class Eval extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public Eval() {
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

		StringBuilder builder = new StringBuilder();

		for (String string : this.arg) {
			builder.append(this.scoope.parse(string));
		}
		double result = this.context.getInstance(MathematicalParser.class)
				.eval(builder.toString());
		this.scoope.put(Constants.RESULT_FIELD, result);
		return scoop;
	}

}
