package carga.interpretter.command;

import java.net.*;
import java.util.regex.*;

import carga.interpretter.*;
import carga.log.*;
import carga.string.*;

public class Ping extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Pattern ipPattern = Pattern
			.compile("^([0-9]|[1-9][0-9]|1([0-9][0-9])|2([0-4][0-9]|5[0-5]))\\.([0-9]|[1-9][0-9]|1([0-9][0-9])|2([0-4][0-9]|5[0-5]))\\.([0-9]|[1-9][0-9]|1([0-9][0-9])|2([0-4][0-9]|5[0-5]))\\.([0-9]|[1-9][0-9]|1([0-9][0-9])|2([0-4][0-9]|5[0-5]))$");

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		if (scoop == null) {
			scoop = this.scoope;
		}
		long result = -1L;
		long start = java.lang.System.currentTimeMillis();
		try {
			if (ipPattern.matcher(this.arg[0]).find()) {
				byte[] ip = new byte[4];
				String parts[] = this.arg[0].split("\\.");
				ip[0] = Byte.valueOf(parts[0]);
				ip[1] = Byte.valueOf(parts[1]);
				ip[2] = Byte.valueOf(parts[2]);
				ip[3] = Byte.valueOf(parts[3]);

				InetAddress.getByAddress(ip).isReachable(
						Constants.DEFAULT_NETWORK_TIMEOUT);
			} else {
				InetAddress.getByName(this.arg[0]).isReachable(
						Constants.DEFAULT_NETWORK_TIMEOUT);
			}
			result = java.lang.System.currentTimeMillis() - start;
		} catch (UnknownHostException ex) {
			Logger.debug(ex.getMessage());
		}
		scoop.put("result", result);

		return null;
	}

}
