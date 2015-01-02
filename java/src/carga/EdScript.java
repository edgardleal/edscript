package carga;

import java.text.*;
import java.util.*;

import carga.cron.*;
import carga.string.*;
import carga.system.*;

/**
 * 
 * 
 * @author edgardleal
 *
 *
 */
public class EdScript {

	public EdScript() {
	}

	public static void main(String[] args) throws Throwable {
		TimeZone.setDefault(TimeZone.getTimeZone(Constants.DEFAULT_TIME_ZONE));
		FileUtil.CURRENT_PATH = System.getProperty("CURRENT_PATH");
		if (FileUtil.CURRENT_PATH == null) {
			FileUtil.CURRENT_PATH = Constants.DOT;
		}
		System.out.println(FileUtil.CURRENT_PATH);
		// TODO: implementar o lpad e rpad
		long start = System.currentTimeMillis();

		boolean commandInformed = false;
		Interpreter interpreter = new Interpreter();
		for (String file : args) {
			if (interpreter.commandExists(file)) {
				interpreter.execute(file);
				commandInformed = true;
			}
		}

		if (!commandInformed) {
			CronTimer cron = new CronTimer();
			cron.readCronFile(Constants.CRONTAB_FILE);
			cron.init();

			while (true) {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println(String.format("%s - Carga finalizda em %d ms",
				getCurrentDateTime(), System.currentTimeMillis() - start));
		System.out.println("Done");

	}

	private static String getCurrentDateTime() {
		Date date;

		date = new Date();
		return new SimpleDateFormat(Constants.DATE_FORMAT).format(date);
	}

}
