package carga.cron;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import carga.log.*;
import carga.string.*;
import carga.system.*;

public class CronTimer extends Thread {
	private static boolean isRunning = false;
	private TaskMonitor monitor = TaskMonitor.getInstance();
	private Pattern cronPattern = Pattern
			.compile("([^\\s]+\\s+[^\\s]+\\s+[^\\s]+\\s+[^\\s]+\\s+[^\\s]+)\\s+(.*)");

	private int lastMinute = 0;
	private int timeToSleep = 1000;
	private boolean stoped = false;
	private boolean finalized = false;

	private static CronTimer innerInstance;

	public CronTimer() {

		this.setPriority(Thread.MAX_PRIORITY);
	}

	public synchronized void init() {
		if (isRunning) {
			return;
		}
		isRunning = true;
		getCronTimer().start();
	}

	public synchronized void finalizeAll() {
		stoped = true;
		while (!finalized) {
			try {
				Thread.sleep(timeToSleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		Logger.debug("Thread inicializada");
		while (!stoped) {
			try {
				Thread.sleep(timeToSleep);
				int minute = Calendar.getInstance().get(Calendar.MINUTE);
				int hour = Calendar.getInstance().get(Calendar.HOUR);
				if (lastMinute != minute) {
					monitor.check(minute, hour);
					lastMinute = minute;
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.finalized = true;
			}
		}
		this.finalized = true;
	}

	private CronTimer getCronTimer() {
		if (innerInstance == null) {
			innerInstance = new CronTimer();
		}
		return innerInstance;
	}

	public void readCronFile(String file) throws IOException {
		String[] lines = new FileUtil().readFile(file).toString()
				.split(Constants.NEW_LINE);

		for (String line : lines) {
			Matcher matcher = cronPattern.matcher(line);
			if (matcher.find()) {
				new CronTaskScriptExecute(matcher.group(1), matcher.group(2));
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		CronTimer cron = new CronTimer();
		new CronTask("* * *", new Runnable() {

			@Override
			public void run() {
				System.out.println("task iniciada");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("taskfinalizada");
			}
		});
		System.out.println("Inicializando a thread");
		cron.init();
		Thread.sleep(12000);
		cron.finalizeAll();
		System.out.println("finalizado");
	}
}
