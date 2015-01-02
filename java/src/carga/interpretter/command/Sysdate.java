/**
 * 
 */
package carga.interpretter.command;

import java.text.SimpleDateFormat;
import java.util.Date;

import carga.interpretter.Function;
import carga.interpretter.Scoop;
import carga.string.Constants;

/**
 * @author edgardleal
 *
 */
public class Sysdate extends Function {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sysdate() {
		this.name = Constants.SYSDATE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see carga.interpretter.Function#execute(java.lang.String[])
	 */
	@Override
	public String execute(String... args) {
		return new SimpleDateFormat(this.getInnerScoop()
				.get(Constants.DATE_FORMAT_LABEL).toString())
				.format(new Date());
	}

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		scoop = checkScoope(scoop);
		execute(args);
		return scoop;
	}

}
