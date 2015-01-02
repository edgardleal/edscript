/**
 * 
 */
package carga;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import carga.interpretter.*;
import carga.interpretter.command.*;
import carga.log.*;
import carga.sql.*;
import carga.string.*;
import carga.system.*;

/**
 * @author edgardleal
 *
 */
public class Interpreter {
	private static final String CARGAS = "cargas";

	private static final String COMMANDS_DAT = "commands.dat";

	private static final String COMMANDS_TMP = "commands.__";

	private static final String BIN_COMMANDS = "bin/commands";

	private static FileUtil fileUtil = new FileUtil();

	private HashMap<String, Main> cacheMap;

	@SuppressWarnings("unchecked")
	private HashMap<String, Main> getCacheMap() {
		if (cacheMap == null) {
			fileUtil.checkDir(FileUtil.getFile(null, BIN_COMMANDS));

			File file = FileUtil.getFile(BIN_COMMANDS, COMMANDS_TMP);
			File zip = FileUtil.getFile(BIN_COMMANDS, COMMANDS_DAT);

			if (zip.exists()
					&& FileUtil.newerThan(zip, FileUtil.getFile(null, CARGAS))) {
				fileUtil.unZipIt(zip.getAbsolutePath(),
						FileUtil.getFullPath(null, BIN_COMMANDS));
				ObjectInputStream in;
				try {
					in = new ObjectInputStream(new FileInputStream(file));
					cacheMap = (HashMap<String, Main>) in.readObject();
				} catch (Exception e) {
					e.printStackTrace();
					cacheMap = new HashMap<String, Main>();
				}
				file.delete();

				Logger.debug("Cache de comandos recuperado do arquivo");
			} else {
				cacheMap = new HashMap<String, Main>();
			}
		}

		return cacheMap;
	}

	public boolean commandExists(String fileName) {
		return getCacheMap().get(fileName) != null
				|| FileUtil.exists(String.format("%s%s%s",
						FileUtil.CURRENT_PATH, File.separator, CARGAS),
						fileName) || FileUtil.getFile(null, fileName).exists();
	}

	public void runAll() throws Exception {
		File dir = FileUtil.getFile(null, CARGAS);
		File files[] = dir.listFiles();
		for (File file : files) {
			execute(file.getAbsolutePath());
		}
	}

	public void execute(String fileName, Main main) throws Exception {
		readCommand(fileName, main).execute(null, Constants.EMPTY_STRING);
	}

	public void execute(String fileName) throws Exception {
		execute(fileName, getNewMainCommand());
	}

	private Main readCommand(final String fileName, Main main)
			throws IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		File tmp = FileUtil.getFile(null, fileName);

		File file = null;

		if (tmp.exists()) {
			file = tmp;
		} else {
			fileUtil.checkParent(FileUtil.getFile(CARGAS, fileName));
		}

		LinkedTransferQueue<String> pilha = new LinkedTransferQueue<String>(), pilhaAuxiliar = new LinkedTransferQueue<String>();

		String[] commands = fileUtil.readFile(file.getAbsolutePath()).split(
				Constants.NEW_LINE);

		for (String command : commands) {
			if (command != null && !command.trim().startsWith("--")
					&& !command.trim().startsWith("#")
					&& !command.trim().equals(Constants.EMPTY_STRING)) {
				pilha.add(command);
			}
		}

		String value = null;
		while ((value = pilha.poll()) != null) {
			pilhaAuxiliar.put(value);
		}

		while (!pilhaAuxiliar.isEmpty()) {
			main.parse(pilhaAuxiliar);
		}

		getCacheMap().put(fileName, main);
		saveCache();
		return main;
	}

	private Main getNewMainCommand() {
		Main main;
		main = new Main();
		Scoop scoop = new Scoop();
		main.setScoope(scoop);
		Context context = new Context();
		SQLParameterParser sqlParameterParser = new SQLParameterParser(scoop);
		context.put(SQLParameterParser.class, sqlParameterParser);
		main.setContext(context);
		Queryes queryes = context.getInstance(Queryes.class);
		queryes.setup(scoop, context);
		context.getInstance(ConnectionFactory.class).setup(context);
		return main;
	}

	private void saveCache() {
		try {
			File commandsData = fileUtil.checkParent(FileUtil.getFile(
					BIN_COMMANDS, COMMANDS_TMP));
			File commandsZip = fileUtil.checkParent(FileUtil.getFile(
					BIN_COMMANDS, COMMANDS_DAT));

			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(commandsData));

			out.writeObject(getCacheMap());
			out.flush();
			out.close();
			fileUtil.zip(commandsData, commandsZip);
			commandsData.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
