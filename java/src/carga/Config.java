/**
 *
 */
package carga;

import java.io.*;
import java.util.*;

import carga.log.*;
import carga.string.*;
import carga.system.*;

/**
 * 
 * @author Edgard Leal
 * @since 11/02/2014 10:19:15
 * 
 */
public class Config {

	private Properties properties;
	private ResourceBundle bundle;
	private String home = null;
	private static String apphome = null;
	private File tmpDir;
	private SystemProfile profile;
	private String CON_PATH;

	private static String SQL_PATH;
	private static String SQL_BIN_PATH;

	public Config() {
		setup();
		String _profile = System.getProperty(Constants.LABEL_PROFILE);
		if (_profile == null) {
			_profile = System.getenv(Constants.LABEL_PROFILE);
		}
		profile = SystemProfile.getByDescription(_profile);
		System.out.println(String.format(
				"Inicializando o sistema como o profile: [%s]",
				profile.toString()));

		CON_PATH = FileUtil.getFullPath(null, Constants.CONNECTIONS_FOLDER);

		FileUtil fileUtil = new FileUtil();

		fileUtil.checkParent(FileUtil.getFile(null,
				Constants.CONNECTIONS_FOLDER));

		SQL_PATH = fileUtil.checkParent(
				FileUtil.getFile(null, Constants.SQL_FOLDER)).getAbsolutePath();

		SQL_BIN_PATH = fileUtil.checkParent(
				FileUtil.getFile(Constants.BIN_FOLDER, Constants.SQL_FOLDER))
				.getAbsolutePath();
	}

	private void setup() {
		if (apphome != null) {
			return;
		}

		apphome = System.getenv(Constants.APP_PATH);

		if (apphome == null) {
			apphome = Constants.DOT;
		}

		SQL_PATH = StringUtil.concat(apphome, File.separator,
				Constants.SQL_FOLDER);
		SQL_BIN_PATH = StringUtil.concat(apphome, File.separator,
				Constants.BIN_FOLDER, File.separator, Constants.SQL_FOLDER);

		File sqlDir = new File(SQL_PATH);
		File sqlBinDir = new File(SQL_BIN_PATH);

		if (!sqlDir.exists()) {
			sqlDir.mkdir();
			Logger.log("Pasta sql criada em: [%s]", sqlDir.getAbsolutePath());
		} else {
			Logger.debug("A pasta ja existe [%s]", sqlDir.getAbsoluteFile());
		}
		if (!sqlBinDir.exists()) {
			sqlDir.mkdirs();
		}
	}

	static {
	}

	public File getCacheFolder() {
		File result = new File(new File(getHome()), "cache");
		if (!result.exists()) {
			result.mkdir();
		}

		return result;
	}

	private Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(new FileInputStream(new File(apphome,
						"config.properties")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}

	/**
	 * Retorna o host , utilizado pelo jetty para filtrar as requisicoes
	 * recebidas. Normalmente é definido como localhost para que aceite apenas
	 * requisicoes locais, para aceitar requisicoes externas, use 0.0.0.0
	 * 
	 * @return
	 */
	public String getHOST() {
		return getProperties().getProperty("host", "localhost");
	}

	/**
	 * Porta na qual o jetty irá receber as requisicoes
	 * 
	 * @return
	 */
	public int getPORT() {
		return getInteger("PORT", 8079);
	}

	private String getString(String key, String def) {
		return getProperties().getProperty(key, def);
	}

	private Integer getInteger(final String key, Integer def) {
		String temp = getProperties().getProperty(key);
		if (temp == null || temp.isEmpty()
				|| !temp.matches(Constants.REGEX_INTEGER)) {
			return def;
		} else {
			return Integer.valueOf(temp);
		}
	}

	/**
	 * Retorna a pasta de deploy
	 * 
	 * @return
	 */
	public File getDeployFolder() {
		File result = new File(apphome, "deploy");
		if (!result.exists()) {
			result.mkdir();
		}
		return result;
	}

	private ResourceBundle getResourcebouBundle() {
		if (bundle == null) {
			bundle = ResourceBundle.getBundle("messages");
		}
		return bundle;
	}

	/**
	 * Retorna a pasta atual onde a aplicacao esta instalada
	 * 
	 * @return
	 */
	public String getHome() {
		if (home == null || home.isEmpty()) {
			home = System.getProperty(Constants.APP_PATH);
			if (home == null || home.isEmpty()) {
				home = System.getenv(Constants.APP_PATH);
			}
			if (home == null || home.isEmpty()) {
				throw new RuntimeException(StringUtil.concat(
						"Informe a variavel de ambiente ", Constants.APP_PATH));
			}
		}
		return home;
	}

	public File getTmpDir() {
		if (tmpDir == null) {
			tmpDir = new File(getString("tmp.dir", "tmp"));
			if (!tmpDir.exists()) {
				tmpDir.mkdirs();
			}
		}

		return tmpDir;
	}

	public String getConnectionsPath() {
		return CON_PATH;
	}

	public boolean isDebug() {
		return profile.toString().equals(SystemProfile.DESE.toString())
				|| profile.toString().equals(SystemProfile.HOMOL.toString());
	}

	public String getAppPath() {
		return apphome;
	}

	public String getSQLPath() {
		return SQL_PATH;
	}

	public String getSQLbinPath() {
		return SQL_BIN_PATH;
	}
}
