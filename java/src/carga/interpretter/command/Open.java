package carga.interpretter.command;

import java.io.*;

import carga.interpretter.*;
import carga.system.*;

/**
 * Abre um arquivo para escrita ou leitura
 * 
 * @author edgardleal
 *
 */
public class Open extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {

		boolean append = arg.length > 1 && arg[1].equals("a");
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				FileUtil.getFullPath(null, arg[0]), append)));

		scoope.put(arg[0], out);
		return scoop;
	}

}
