package carga.interpretter.command;

import java.io.*;

import carga.interpretter.*;
import carga.system.*;

public class Rm extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		for (String string : args) {
			new File(FileUtil.getFullPath(null, string)).delete();
		}
		return scoop;
	}

}
