package carga.cron;

import carga.log.*;

public class CronTask {
	private static long instanceCount = 0L;
	private Runnable runnable;
	private CronExpression expression;
	private String name;

	public CronTask(String expression, Runnable runnable) {
		this.runnable = runnable;
		this.expression = new ExpressionParser().parser(expression);
		instanceCount++;
		name = String.format("%d-%f-%d", ++instanceCount, Math.random(),
				System.nanoTime());
		Logger.log("Tarefa agendada: [%s]", expression);
		TaskMonitor.add(this);
	}

	public boolean check(int minute, int hour) {
		return this.expression.check(minute, hour);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof CronTask))
			return false;
		return ((CronTask) obj).getName().equals(this.getName());
	}

	public String getName() {
		return name;
	}

	private boolean isRunning = false;

	public synchronized void execute() {
		if (isRunning) {
			return;
		}
		isRunning = true;
		try {
			if (runnable != null) {
				Thread thread = new Thread(runnable);
				thread.setName(this.name);
				thread.start();
			}
		} catch (Exception e) {
			Logger.error(e);
		}
		isRunning = false;
		TaskMonitor.getInstance().finalize(this);
	}

}
