/**
 * 
 */
package carga.interpretter.command;

import carga.interpretter.*;
import carga.string.*;

/**
 * @author edgardleal
 *
 */
public class Print extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see carga.interpretter.Command#execute(java.sql.ResultSet,
	 * java.lang.String[])
	 */
	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {

		for (String a : this.arg) {
			switch (a) {
			case "\\t":
				java.lang.System.out.print('\t');
				break;
			case Constants.NEW_LINE_TEXT:
				java.lang.System.out.print('\n');
				break;

			default:
				if (a.indexOf('$') > -1) {
					String fieldName = scoope.fieldName(a);
					a = scoope.get(fieldName) == null ? Constants.NULL : scoope
							.get(fieldName).toString();
				}
				java.lang.System.out.print(a);
				break;
			}
			java.lang.System.out.print(' ');
		}// for
		return null;
	}
}
