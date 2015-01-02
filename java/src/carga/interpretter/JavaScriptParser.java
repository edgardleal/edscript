package carga.interpretter;

import javax.script.*;

import carga.string.*;

/**
 * 
 * @author edgardleal
 *
 */
public class JavaScriptParser {
	private static ScriptEngineManager mgr = new ScriptEngineManager();
	private static ScriptEngine engine = mgr
			.getEngineByName(Constants.DEFAULT_SCRIPT_LANGUAGE);

	public static String parser(final String expressions)
			throws ScriptException {
		return engine.eval(expressions).toString();
	}
}
