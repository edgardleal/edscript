/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package carga.system;

import java.io.*;
import java.util.logging.*;
import java.util.zip.*;

import carga.string.*;

/**
 * 
 * @author Edgard Leal
 */
public class FileUtil {

	public static String CURRENT_PATH = Constants.DOT;

	public static String getFullPath(String dir, String file) {
		if (dir == null || dir.length() == 0) {
			return String.format("%s%s%s", CURRENT_PATH, File.separator, file);
		}

		return String.format("%s%s%s%s%s", CURRENT_PATH, File.separator, dir,
				File.separator, file);
	}

	/**
	 * Retorna um arquivo montado com base na propertie CURRENT_PATH
	 * 
	 * @param dir
	 * @param file
	 * @return
	 */
	public static File getFile(String dir, String file) {
		return new File(getFullPath(dir, file));
	}

	/**
	 * Verifica se a pasta parent do arquivo informado existe, se nao existir
	 * tenta cria-la
	 * 
	 * @param file
	 */
	public File checkParent(File file) {
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		return file;
	}

	public String readFile(File file) throws IOException {
		return readFileToStringBuilder(file).toString();
	}

	public String readFile(String file) throws IOException {
		return readFileToStringBuilder(file).toString();
	}

	public StringBuilder readFileToStringBuilder(final String file)
			throws IOException {
		return readFileToStringBuilder(new File(file));
	}

	public StringBuilder readFileToStringBuilder(File file) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			builder.append(line).append('\n');
		}
		br.close();

		return builder;
	}

	public void write(String text, String file) {
		write(text, file, false);
	}

	public void append(String text, String file) {
		write(text, file, true);
	}

	public void write(String text, String file, boolean append) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(file, append)))) {
			out.println(text);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void copy(File origin, File dest) throws FileNotFoundException,
			IOException {
		InputStream is = null;
		OutputStream os = null;

		try {
			is = new FileInputStream(origin);
			os = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			closeStream(is);
			closeStream(os);
		}
	}

	public void moveFile(File origin, File dest) throws IOException {
		copy(origin, dest);
		origin.delete();
	}

	public void closeStream(InputStream s) {
		if (s != null) {
			try {
				s.close();
			} catch (IOException ex) {
				Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
	}

	public void closeStream(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException ex) {
				Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE,
						null, ex);
			}

		}
	}

	public static boolean exists(String parent, String fileName) {
		return new File(parent, fileName).exists();
	}

	public static boolean exists(String fileName) {
		return new File(fileName).exists();
	}

	public void zip(File origin, File out) throws FileNotFoundException,
			IOException {
		byte[] buffer = new byte[1024];

		FileOutputStream fos = new FileOutputStream(out);
		try (ZipOutputStream zos = new ZipOutputStream(fos)) {
			ZipEntry ze = new ZipEntry(origin.getName());
			zos.putNextEntry(ze);
			try (FileInputStream in = new FileInputStream(origin)) {
				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
			}
			zos.closeEntry();
		}
	}

	public void unZipIt(String zipFile, String outputFolder) {

		byte[] buffer = new byte[1024];

		try {
			ZipInputStream zis = new ZipInputStream(
					new FileInputStream(zipFile));
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName();
				File newFile = new File(StringUtil.concat(outputFolder,
						File.separator, fileName));

				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static boolean newerThan(File zip, File file) {
		long time = zip.lastModified();

		if (file.isFile()) {
			return time >= file.lastModified();
		} else {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].lastModified() > time) {
					return false;
				}
			}
		}
		return true;
	}

	public File checkDir(File file) {
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}
}
