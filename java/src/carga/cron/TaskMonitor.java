package carga.cron;

import java.util.*;

public class TaskMonitor {
	private List<CronTask> tasks;
	private static TaskMonitor innerInstance;

	private TaskMonitor() {
		tasks = new ArrayList<>();
	}

	public static TaskMonitor getInstance() {
		if (innerInstance == null) {
			innerInstance = new TaskMonitor();
		}
		return innerInstance;
	}

	public static void add(CronTask cronTask) {
		getInstance().tasks.add(cronTask);
	}

	public void finalize(CronTask cronTask) {
		tasks.remove(cronTask);
	}

	public void check(int minute, int hour) {
		for (CronTask task : tasks) {
			if (task.check(minute, hour)) {
				task.execute();
			}
		}
	}

}
