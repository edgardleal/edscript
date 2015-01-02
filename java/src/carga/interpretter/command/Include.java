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
public class Include extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see carga.interpretter.command.Command#execute(carga.interpretter.Scoop,
	 * java.lang.String[])
	 */
	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {

		for (String string : this.arg) {
			context.getInstance(Interpreter.class)
					.execute(string, getNewMain());
		}
		return scoop;
	}

	private Main getNewMain() {
		Main main;
		main = new Main();
		main.setScoope(scoope);
		main.setContext(context);
		Queryes queryes = context.getInstance(Queryes.class);
		queryes.setup(scoope, context);
		context.getInstance(ConnectionFactory.class).setup(context);

		return main;
	}
}
