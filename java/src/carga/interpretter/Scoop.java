package carga.interpretter;

import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import carga.interpretter.command.Sysdate;
import carga.log.*;
import carga.string.Constants;

/**
 * 
 * @author edgardleal
 *
 */
public class Scoop implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MathematicalParser mathematicalParser = new MathematicalParser();
	private HashMap<String, Object> map = new HashMap<String, Object>();

	private static Pattern variablePattern = Pattern
			.compile(Constants.REGEX_VARIABLE);

	private static Pattern evaluationPattern = Pattern
			.compile(Constants.REGEX_EVALUATION);
	private static Pattern mathematicalEvaluationPattern = Pattern
			.compile(Constants.REGEX_MAHEMATICAL_EXPRESSION);

	public void dispose() {
		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			Object value = entry.getValue();
			if (value instanceof PrintWriter) {
				PrintWriter p = (PrintWriter) value;
				p.flush();
				p.close();
			}
		}
	}

	public Scoop() {
		map.put(Constants.DATE_FORMAT_LABEL, Constants.DATE_FORMAT);
		map.put(Constants.SYSDATE, new Sysdate());
		map.put(Constants.SPACE_LABEL, Constants.SPACE);
		map.put(Constants.SYS_WEB_COOKIE, new HashMap<String, String>());
		map.put("empty", Constants.EMPTY_STRING);
	}

	public Object get(String name) {
		Object value = map.get(name);
		value = value == null ? map.get(name.toUpperCase()) : value;
		value = value == null ? map.get(name.toLowerCase()) : value;

		if (value instanceof Function) {
			value = ((Function) value).execute(new String[0]);
		} else if (value instanceof Procedure) {
			try {
				((Procedure) value).execute(null, new String[0]);
			} catch (Exception e) {
				Logger.error(e);
			}
		}
		return value;
	}

	public void put(String name, Object value) {
		Object old = map.put(name, value);
		if (old != null) {
			if (old instanceof Flushable) {
				try {
					((Flushable) old).flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (old instanceof Closeable) {
				try {
					((Closeable) old).close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String parse3(final String sql) {
		String variablePattern = "\\$\\{%s\\}";
		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		String result = sql;

		// TODO: nao disparar mas todas as procedures
		while (iterator.hasNext()) {

			Entry<String, Object> entry = iterator.next();
			String value = get(entry.getKey()).toString();
			result = result.replaceAll(
					String.format(variablePattern, entry.getKey()), value);
		}

		if (result.indexOf('$') > 0) {
			// throw new RuntimeException(result);
		}

		return result;
	}

	public String parse(final String sql) {
		Matcher matcher = variablePattern.matcher(sql);
		List<String> parts = new ArrayList<String>();
		if (sql.indexOf('$') == -1) {
			return sql;
		}

		int lastMatch = 0;
		while (matcher.find()) {
			for (int i = 0; i < matcher.groupCount(); i++) {
				parts.add(sql.substring(lastMatch, matcher.start(i)));
				parts.add(matcher.group(i));
				lastMatch = matcher.end(i);
			}
		}
		parts.add(sql.substring(lastMatch, sql.length()));

		StringBuilder builder = new StringBuilder();

		for (String item : parts) {
			if (item.startsWith("$((")) {
				builder.append(mathematicalParser(item));
			} else if (item.startsWith("${")) {
				builder.append(get(fieldName(item)));
			} else {
				builder.append(item);
			}
		}
		return builder.toString();
	}

	private String mathematicalParser(String item) {
		System.out.println("Examinando a expressao: " + item);
		String expression = item;
		if (item.startsWith("$((")) {
			expression = item.substring(3, item.length() - 2);
		}
		System.out.println("Executando a expressao: " + expression);
		return String.valueOf(this.mathematicalParser.eval(expression));
	}

	public String fieldName(String field) {
		return field.substring(2, field.length() - 1);
	}

	public void readResultSet(ResultSet rs, ResultSetMetaData meta)
			throws SQLException {
		int columnCount = meta.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			String columnName = meta.getColumnLabel(i + 1).toUpperCase();
			map.put(columnName, rs.getObject(i + 1));
		}
	}

	public void readResultSet(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		readResultSet(rs, meta);
	}

	public static void main(String[] args) {
		System.out.println(new Scoop().parse("$((5+8))"));
	}
}
