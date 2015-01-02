package carga.interpretter.command;

import java.sql.*;

import carga.*;
import carga.interpretter.*;
import carga.string.*;

public class Query extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		scoop = checkScoope(scoop);
		checkArgs(args);

		ResultSet resultSet;
		carga.sql.Query query = context.getInstance(Queryes.class).getQuery(
				this.scoope.parse(this.arg[0]));

		try {
		
			resultSet = query.getResultSet();
			int i = 0;
			while (resultSet.next()) {
				scoop.readResultSet(resultSet);
				scoop.put(Constants.INCREMENT_VARIABLE, i);
				executeCommands();
			}
			scoop.put(Constants.INCREMENT_VARIABLE, 0);
		} catch (SQLException ex) {
			java.lang.System.out.println("Erro ao executar a query: "
					+ query.toString());
			throw ex;
		}

		return null;
	}

}
