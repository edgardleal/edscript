package carga.interpretter.command;

import carga.interpretter.*;
import carga.string.*;
import carga.system.shell.*;

/**
 * Executa um comando no SO
 * 
 * @author edgardleal
 *
 */
public class System extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		scoop = checkScoope(scoop);
		BashExecuteResult result = BashExecutor.executeCommand(this.arg);

		scoop.put(Constants.RESULT_FIELD, result.getOutput());
		scoop.put(Constants.STATUS_VARIABLE, result.getExitCode());
		return null;
	}

}
