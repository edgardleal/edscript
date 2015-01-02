/**
 * 
 */
package carga.interpretter.command;

import java.io.*;

import carga.interpretter.*;
import carga.string.*;

/**
 * @author edgardleal
 *
 */
public class Writeln extends Command {

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
		PrintWriter p = (PrintWriter) scoope.get(arg[0]);

		for (int i = 1; i < arg.length; i++) {
			p.print(arg[i]);
			if (i < arg.length - 1) {
				p.print(Constants.SPACE);
			}
		}
		p.print(Constants.NEW_LINE);
		return scoop;
	}

}
