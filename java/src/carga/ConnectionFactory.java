/**
 * 
 */
package carga;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.regex.*;

import carga.log.*;
import carga.string.*;
import carga.system.*;

/**
 * @author edgardleal
 *
 */
public class ConnectionFactory {

	private static Pattern connectionNamePattern = Pattern
			.compile(Constants.CONNECTION_PATTERN);

	private ConcurrentHashMap<String, Connection> connections;
	private List<Statement> statements;
	private List<ResultSet> resultSets;
	private static HashSet<String> loadedDrivers = new HashSet<String>();

	private Context context;

	public void setup(Context context) {
		this.context = context;
	}

	private synchronized static void loadDriver(final String driver) {
		if (loadedDrivers.contains(driver)) {
			return;
		} else {
			try {
				Class.forName(driver).newInstance();
				loadedDrivers.add(driver);
			} catch (Exception e) {
				Logger.error(e);
			}
		}
	}

	public Connection getConnection(String name) throws IOException,
			SQLException {

		Connection con = getConnections().get(name);
		String connectionString = null;
		String user = null;
		String pass = null;

		if (con != null) {
			return con;
		}

		File file = new File(StringUtil.concat(context
				.getInstance(Config.class).getConnectionsPath(),
				File.separator, name));

		if (!file.exists()) {
			con = DriverManager.getConnection(name);
		} else {

			String line = new FileUtil().readFile(file);

			checkDriver(line);
			try {
				if ((line.indexOf("/") < 0 || (line
						.indexOf(Constants.POSTGRESQL) > -1 || line
						.indexOf(Constants.MYSQL) > -1))
						&& line.indexOf(Constants.DERBY) < 0) {
					String[] list = line.split(";");
					connectionString = list[0];
					user = list[1];
					pass = list[2].replaceAll(Constants.NEW_LINE,
							Constants.EMPTY_STRING).trim();
					if (list.length > 3) { // isso foi necessario porque um cara
											// criou uma senha com ;
						pass = String.format("%s;%s", pass, list[3]);
						System.out.println("Senha: " + pass);
					}
					con = DriverManager.getConnection(connectionString, user,
							pass);
				} else {
					con = DriverManager.getConnection(line);
				}
			} catch (SQLException sqlex) {
				Logger.error("Erro ao estabelecer a conexao: [%s]", line);
				Logger.error("String de conexao: [%s]", connectionString);
				Logger.error("Usuario: [%s]", user);
				throw sqlex;
			}

		}

		Logger.debug("Nova conexao criada: [%s]", name);
		getConnections().put(name, con);

		return con;
	}

	private static void checkDriver(String line) {
		if (line.indexOf(Constants.MYSQL) > -1) {
			loadDriver(Constants.MYSQL_DRIVER);
		} else if (line.indexOf(Constants.ORACLE) > -1) {
			loadDriver(Constants.ORACLE_DRIVER);
		} else if (line.indexOf(Constants.DERBY) > -1) {
			loadDriver(Constants.DERBY_DRIVER);
		} else if (line.indexOf(Constants.POSTGRESQL) > -1) {
			loadDriver(Constants.POSTGRESQL_DRIVER);
		}
	}

	@Override
	public void finalize() throws Throwable {
		closeAll();
		super.finalize();
	}

	public void closeAll() {
		java.util.Iterator<Entry<String, Connection>> iterator = getConnections()
				.entrySet().iterator();

		Logger.debug("Fechando os resultSets: [");
		for (ResultSet rs : getResultSets()) {
			try {
				rs.close();
				Logger.debug("*");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Logger.debug(']');

		resultSets.clear();
		resultSets = null;

		Logger.debug("Fechando os statements: [");
		for (Statement stm : getStatements()) {
			try {
				stm.close();
				Logger.debug('*');
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Logger.debug(']');

		statements.clear();
		statements = null;

		Logger.debug("Fechando as conexoes: [");
		while (iterator.hasNext()) {
			Entry<String, Connection> entry = iterator.next();
			try {
				entry.getValue().close();
				getConnections().remove(entry.getKey());
				Logger.debug('*');
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Logger.debug(']');
	}

	public PreparedStatement getPreparedStatement(String sql, Connection con)
			throws SQLException {
		PreparedStatement st = con.prepareStatement(sql);
		getStatements().add(st);

		return st;
	}

	public void execute(String sql) throws SQLException, IOException {
		String connectionName = sql.split(Constants.NEW_LINE)[0].split(";")[1];
		try {
			getPreparedStatement(sql, getConnection(connectionName)).execute();
		} catch (SQLException ex) {
			System.out.println(String.format(
					"Erro ao executar o comando: [%s]", sql));
			System.out.println(ex.getMessage());
			throw ex;
		}
	}

	public ResultSet getResultSet(String sql) throws SQLException, IOException {

		String[] lines = sql.split(Constants.NEW_LINE);
		String connectionName = null;

		for (String string : lines) {
			if (connectionNamePattern.matcher(string).find()) {
				connectionName = string.split(";")[1];
				break;
			}
		}

		sql = sql.replace(sql.split(Constants.NEW_LINE)[0],
				Constants.EMPTY_STRING);

		try {
			return getResultSet(sql, getConnection(connectionName));
		} catch (Exception ex) {
			Logger.error("Erro ao executar a consultar: [%s]", sql);
			throw ex;
		}
	}

	public ResultSet getResultSet(String sql, Connection con)
			throws SQLException {
		ResultSet rs = getPreparedStatement(sql, con).executeQuery();
		getResultSets().add(rs);
		return rs;
	}

	public ResultSet getResultSet(String sql, Statement stm)
			throws SQLException {
		ResultSet rs = stm.executeQuery(sql);
		getResultSets().add(rs);

		return rs;
	}

	private List<ResultSet> getResultSets() {
		if (resultSets == null) {
			resultSets = new ArrayList<ResultSet>();
		}
		return resultSets;
	}

	private List<Statement> getStatements() {
		if (statements == null) {
			statements = new ArrayList<Statement>();
		}
		return statements;
	}

	private ConcurrentHashMap<String, Connection> getConnections() {
		if (connections == null) {
			connections = new ConcurrentHashMap<String, Connection>();
		}

		return connections;

	}
}