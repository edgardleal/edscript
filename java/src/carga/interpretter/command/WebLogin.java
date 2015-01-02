/**
 * 
 */
package carga.interpretter.command;

import java.util.*;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.*;
import org.jsoup.nodes.*;

import carga.interpretter.*;
import carga.string.*;

/**
 * @author edgardleal
 *
 */
public class WebLogin extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public WebLogin() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see carga.interpretter.command.Command#execute(carga.interpretter.Scoop,
	 * java.lang.String[])
	 */
	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {

		String url = this.arg[0], loginField = this.arg[1], login = this.arg[2], passField = this.arg[3], pass = this.arg[4];

		Response res = Jsoup.connect(url)
				.data(loginField, login, passField, pass).method(Method.POST)
				.execute();

		this.scoope.put(Constants.SYS_WEB_COOKIE, res.cookies());
		return null;
	}

}
