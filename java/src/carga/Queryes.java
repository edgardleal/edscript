/**
 * 
 */
package carga;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import carga.interpretter.*;
import carga.sql.*;
import carga.string.*;
import carga.system.*;

/**
 * @author edgardleal
 *
 */
public class Queryes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static FileUtil fileUtil;
	private Context context;
	private Scoop scoop;
	private Config config;
	private static Pattern patternSelect = Pattern.compile(".*select.*",
			Pattern.CASE_INSENSITIVE);
	private Map<String, Query> querys = new HashMap<String, Query>();

	public Queryes() {
	}

	public Query getQuery(final String fileName) throws IOException {
		Query result = querys.get(fileName);
		if (result == null) {
			result = new Query(scoop, context);
			result.loadFromFile(fileName);
		}

		return result;
	}

	public void setup(Scoop scoop, Context context) {
		this.context = context;
		this.scoop = scoop;
		config = context.getInstance(Config.class);
		fileUtil = context.getInstance(FileUtil.class);
	}

	public String readSQL(String filename) throws IOException {

		if (config == null) {
			throw new RuntimeException("Config esta nulo");
		}
		filename = StringUtil.concat(config.getSQLPath(), File.separator,
				filename);

		if (filename.indexOf(".sql") < 0) {
			filename = StringUtil.concat(filename, ".sql");
		}

		return checkCache(filename).toString();
	}

	private static StringBuilder readFromSqlFile(final String fileName)
			throws IOException {
		return fileUtil.readFileToStringBuilder(fileName);
	}

	private static HashMap<String, StringBuilder> cacheMap;

	@SuppressWarnings("unchecked")
	private HashMap<String, StringBuilder> getCacheMap() {
		if (cacheMap == null) {
			File file = new File(config.getSQLbinPath(), "sql.__");
			File zip = new File(config.getSQLbinPath(),
					Constants.BINARY_SQL_FILE_NAME);
			if (zip.exists()) {
				fileUtil.unZipIt(zip.getAbsolutePath(), config.getSQLbinPath());
				ObjectInputStream in;
				try {
					in = new ObjectInputStream(new FileInputStream(file));
					cacheMap = (HashMap<String, StringBuilder>) in.readObject();
				} catch (Exception e) {
					e.printStackTrace();
					cacheMap = new HashMap<String, StringBuilder>();
				}
				file.delete();
			} else {
				cacheMap = new HashMap<String, StringBuilder>();
			}
		}

		return cacheMap;
	}

	private StringBuilder checkCache(String fileName) {
		StringBuilder result = getCacheMap().get(fileName);

		if (result == null) {
			File sqldata = new File(config.getSQLbinPath(), "sql.__");
			File sqldatazip = new File(config.getSQLbinPath(),
					Constants.BINARY_SQL_FILE_NAME);
			if (sqldatazip.exists()) {
				try {
					result = readFromSqlFile(fileName);
					getCacheMap().put(fileName, result);

					ObjectOutputStream out = new ObjectOutputStream(
							new FileOutputStream(sqldata));

					out.writeObject(getCacheMap());
					out.flush();
					out.close();
					fileUtil.zip(sqldata, sqldatazip);
					sqldata.delete();
				} catch (IOException e) {
					e.printStackTrace();
					try {
						readFromSqlFile(fileName);
					} catch (IOException e1) {
						e1.printStackTrace();
						return null;
					}
				}
			} else {
				try {
					return readFromSqlFile(fileName);
				} catch (IOException e) {
					e.printStackTrace();
					return new StringBuilder();
				}
			}
		}
		return result;
	}

	public void closeAll() {
		Set<java.util.Map.Entry<String, Query>> entry = querys.entrySet();
		Iterator<java.util.Map.Entry<String, Query>> iterator = entry
				.iterator();
		while (iterator.hasNext()) {
			java.util.Map.Entry<String, Query> i = iterator.next();
			i.getValue().close();
		}
	}
}
