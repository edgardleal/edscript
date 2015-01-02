package carga.sql;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.*;

import carga.*;
import carga.interpretter.*;
import carga.log.*;
import carga.string.*;

/**
 * 
 * @author edgardleal
 *
 */
public class Query implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Context context;
	private Scoop scoop;
	private String fileName;
	private String sqlOriginal;
	private String sqlParsed;
	private PreparedStatement statement;
	private List<Parameter> parameters = new ArrayList<Parameter>();
	private static Pattern connectionNamePattern = Pattern
			.compile(Constants.CONNECTION_PATTERN);

	private ResultSet rs;

	public Query(Scoop scoop, Context context) {
		this.scoop = scoop;
		this.context = context;
	}

	public Scoop getScoop() {
		return scoop;
	}

	public void setScoop(Scoop scoop) {
		this.scoop = scoop;
	}

	public void loadFromFile(final String fileName) throws IOException {
		this.fileName = fileName;
		this.sqlOriginal = context.getInstance(Queryes.class).readSQL(fileName);
		parseOriginalSQL();
	}

	private void parseOriginalSQL() {
		SQLParameterParser parser = context
				.getInstance(SQLParameterParser.class);
		this.parameters = parser.parseParameters(this.sqlOriginal);

		this.sqlParsed = parser.parseSQLParameters(this.sqlOriginal);
	}

	public String getFileName() {
		return fileName;
	}

	public void close() {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			statement = null;
		}
	}

	private PreparedStatement getStatement() throws IOException, SQLException {
		if (statement == null) {
			Connection con = context.getInstance(ConnectionFactory.class)
					.getConnection(getConnectionName());

			statement = context.getInstance(ConnectionFactory.class)
					.getPreparedStatement(sqlParsed, con);

		}
		return statement;
	}

	public void execute() throws SQLException, IOException {
		loadParameters();
		getStatement().execute();
	}

	public ResultSet getResultSet() throws Exception {
		if (rs == null) {
			try {
				getStatement();
				loadParameters();
				rs = getStatement().executeQuery();
			} catch (Exception ex) {
				Logger.error("Erro ao executar a consultar: [%s]", sqlOriginal);
				throw ex;
			}
		}
		return rs;
	}

	private void loadParameters() throws SQLException, IOException {
		
		//TODO: implementar todos os tipos de dados inclusive file
		int i = 1;
		for (Parameter parameter : parameters) {
			parameter.setValue(scoop.get(parameter.getName()));

			switch (parameter.getType()) {
			case NUMBER:
				getStatement().setInt(i++, parameter.asInteger());
				break;
			case STRING:
				getStatement().setString(i++, parameter.asString());
				break;
			case FLOAT:
				getStatement().setFloat(i++, parameter.asFloat());
				break;

			case VARIANT:
				getStatement().setObject(i++, parameter.getValue());
				break;

			default:
				break;
			}
		}
	}

	private String getConnectionName() {
		String[] lines = sqlParsed.split(Constants.NEW_LINE);
		String connectionName = null;
		for (String string : lines) {
			if (connectionNamePattern.matcher(string).find()) {
				connectionName = string.split(";")[1];
				break;
			}
		}
		return connectionName;
	}

	@Override
	public String toString() {
		return "Query [fileName=" + fileName + ", \nsqlOriginal=" + sqlOriginal
				+ ", \nsqlParsed=" + sqlParsed + ", \nparameters=" + parameters
				+ "]";
	}

}
