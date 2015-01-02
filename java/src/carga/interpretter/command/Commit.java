package carga.interpretter.command;

import java.sql.*;

import carga.*;
import carga.interpretter.*;

/**
 * 
 * @author edgardleal
 *
 */
public class Commit extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		Connection con = this.context.getInstance(ConnectionFactory.class)
				.getConnection(this.arg[0]);
		con.commit();
		con.setAutoCommit(true);

		return null;
	}

}
