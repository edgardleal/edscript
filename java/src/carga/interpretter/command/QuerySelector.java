/**
 * 
 */
package carga.interpretter.command;

import java.util.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import carga.*;
import carga.interpretter.*;
import carga.string.*;

/**
 * Busca o valor de uma tag html de acordo com o seletor informado
 * 
 * @author edgardleal
 *
 */
public class QuerySelector extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QuerySelector() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public Scoop execute(Scoop se, String... args) throws Exception {
		se = checkScoope(se);

		if (this.arg == null || this.arg.length != 3) {
			throw new SyntaxException(
					"Informe tres parametros. \nEx: querySelector ${html} #idDaTag atributoDaTag");
		}

		String url = this.scoope.parse(this.arg[0]);

		Document doc = null;
		if (url.startsWith(Constants.HTTP)) {
			doc = Jsoup
					.connect(url)
					.cookies(
							(HashMap<String, String>) this.scoope
									.get(Constants.SYS_WEB_COOKIE)).get();
		} else {
			doc = Jsoup.parse(url);
		}

		String selector = this.arg[1];
		if (selector.indexOf('$') > -1) {
			selector = scoope.parse(selector);
		}
		Elements elements = doc.select(selector);

		int i = 0;
		for (Element el : elements) {
			if (arg[2].equals("html")) {
				se.put(Constants.VALUE_LABEL, el.html());
			} else {
				se.put(Constants.VALUE_LABEL, el.attr(this.arg[2]));
			}
			se.put(Constants.INCREMENT_VARIABLE, i++);
			this.executeCommands();
		}

		return se;
	}
}
