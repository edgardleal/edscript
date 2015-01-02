package carga.interpretter.command;

import java.sql.*;

import carga.interpretter.*;
import carga.log.*;
import carga.string.*;

/**
 * 
 * Teste uma conexao e atribui o valor de tempo de espera para se conectar a
 * variavel global ${result}
 * 
 * @author edgardleal
 *
 */
public class TestSqlConnection extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		Connection con = null;
		if (scoop == null) {
			scoop = this.scoope;
		}
		try {
			long start = java.lang.System.currentTimeMillis();
			con = DriverManager.getConnection(this.arg[0]);
			scoop.put(Constants.RESULT_FIELD,
					java.lang.System.currentTimeMillis() - start);
			try {
				con.close();
				con = null;
			} catch (Exception e) {
				Logger.error("Erro ao executar o teste de conexao", e);
			}
		} catch (Exception e) {
			scoop.put(Constants.RESULT_FIELD, -1);
			Logger.debug(e.getMessage());
		}

		return null;
	}

}
