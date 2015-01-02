package carga.interpretter.command;

import carga.interpretter.*;
import carga.system.*;

public class AppendToFile extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		this.context.getInstance(FileUtil.class).append(this.arg[1],
				this.arg[0]);
		return scoop;
	}

}
