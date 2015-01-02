/**
 * 
 */
package carga.cron;

import carga.Interpreter;

/**
 * @author edgardleal
 *
 */
public class CronTaskScriptExecute extends CronTask {

	public CronTaskScriptExecute(String expression, String script) {
		super(expression, new RunnableExecutor(script));
	}

	@Deprecated
	public CronTaskScriptExecute(String expression, Runnable runnable) {
		super(expression, runnable);
		// TODO Auto-generated constructor stub
	}
}

class RunnableExecutor implements Runnable {
	private String file;

	public RunnableExecutor(String file) {
		this.file = file;
	}

	@Override
	public void run() {
		try {
			new Interpreter().execute(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}