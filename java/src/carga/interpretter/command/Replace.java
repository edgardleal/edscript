/**
 * 
 */
package carga.interpretter.command;

import java.util.*;

import carga.*;
import carga.interpretter.*;
import carga.string.*;

/**
 * Substitui todas ocorrencias do segundo parametro encontrados no primeiro
 * argumetno pelo terceiro argumento
 * 
 * @author edgardleal
 *
 */
public class Replace extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop _scoop, String... _args) throws Exception {

		if (arg.length != 3) {
			throw new SyntaxException("The number of arguments is wrong");
		}
		String text = scoope.parse(this.arg[0]);
		String regex = scoope.parse(this.arg[1]);
		String replacement = scoope.parse(this.arg[2]);

		scoope = checkScoope(scoope);
		String value = text.replaceAll(regex, replacement);

		scoope.put(Constants.RESULT_FIELD, value);
		return scoope;
	}

	public static void main(String[] args) {
		java.lang.System.out
				.println("teste de substituicao java com outras palavras"
						.replaceAll("^.*(java|JAVA|Java).*$", "1"));

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("A", "15");
		map.put("A", "16");
		map.put("A", "17");
		java.lang.System.out.println(map.get("A"));
	}

}
