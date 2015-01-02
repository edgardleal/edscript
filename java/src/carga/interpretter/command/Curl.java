package carga.interpretter.command;

import java.io.*;
import java.net.*;

import carga.interpretter.*;
import carga.log.*;
import carga.string.*;

public class Curl extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Scoop execute(Scoop scoop, String... args) throws Exception {
		if (scoop == null) {
			scoop = this.scoope;
		}
		scoop.put(Constants.RESULT_FIELD, getContent(this.arg[0]));
		return null;
	}

	private String getContent(String host) {
		URL url;
		InputStream is = null;
		BufferedReader br;
		StringBuilder builder = new StringBuilder();
		String line;

		try {
			url = new URL(host);
			is = url.openStream(); // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));

			while ((line = br.readLine()) != null) {
				builder.append(line).append('\n');
			}
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				Logger.debug(ioe.getMessage());
				return Constants.ERROR;
			}
		}
		return builder.toString();
	}
}
