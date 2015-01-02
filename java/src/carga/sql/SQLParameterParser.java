/**
 * 
 */
package carga.sql;

import java.util.*;
import java.util.regex.*;

import carga.data.*;
import carga.interpretter.*;
import carga.regex.*;
import carga.string.*;

/**
 * @author edgardleal
 *
 */
public class SQLParameterParser {

	private Pattern VARIABLE_PATTERN = Pattern
			.compile(Patterns.VARIABLE_PATTERN);
	private Scoop scoop;

	/**
	 * 
	 */
	public SQLParameterParser(Scoop scoop) {
		this.scoop = scoop;
	}

	public List<Parameter> parseParameters(final String sql) {
		List<Parameter> result = new ArrayList<Parameter>();

		Matcher matcher = VARIABLE_PATTERN.matcher(sql);
		if (sql.indexOf('$') == -1) {
			return result;
		}

		while (matcher.find()) {
			for (int i = 0; i < matcher.groupCount(); i++) {
				result.add(extractParameter(matcher.group(i)));
			}
		}

		return result;
	}

	public String parseSQLParameters(final String sql) {
		return VARIABLE_PATTERN.matcher(sql).replaceAll("?");
	}

	private Parameter extractParameter(String group) {
		group = group.replace("${", Constants.EMPTY_STRING).replace("}",
				Constants.EMPTY_STRING);
		String list[] = group.split(Constants.SPACE);

		Object value;
		String name;
		Types type;

		if (list.length > 1) {
			type = Types.identify(list[0]);
			value = list[1];
		} else {
			type = Types.VARIANT;
			value = list[0];
		}

		name = value.toString();
		value = this.scoop.get(name);
		return new Parameter(name, value, type);
	}

	public static void main(String[] args) {
		SQLParameterParser parse = new SQLParameterParser(new Scoop());
		String sql = "update table set nome = ${string nome}, valor = ${VALOR} ";
		List<Parameter> list = parse.parseParameters(sql);
		for (Parameter parameter : list) {
			System.out.println(parameter.toString());
		}

		System.out.println(parse.parseSQLParameters(sql));
		System.out.println("Done");
	}
}
