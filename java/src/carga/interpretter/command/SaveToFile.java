package carga.interpretter.command;

import carga.interpretter.*;
import carga.system.*;

public class SaveToFile extends Command {

	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		new FileUtil().write(FileUtil.getFullPath(null, this.arg[1]),
				this.arg[0]);
		return null;
	}

}
