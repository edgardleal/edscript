/**
 * 
 */
package carga.data.parsers;

import java.util.*;
import java.util.regex.*;

import carga.string.*;

/**
 * @author edgardleal
 *
 */
public class DateParser implements Parser<Date> {

	private Pattern datePattern = Pattern.compile(Constants.PATTERN_DATE);
	
	/**
	 * 
	 */
	public DateParser() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Date parse(Object obj) {
		if (obj==null) {
			return null;
		}
		return null;
	}

}
