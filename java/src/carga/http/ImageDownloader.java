/**
 *
 */
package carga.http;

import java.io.*;
import java.net.*;

import org.slf4j.*;

import carga.system.*;

/**
 * @author Edgard Leal TODO: Implementar multiplas conexoes por download. usar
 *         Object.wait e nao Thread.sleep
 */
public class ImageDownloader implements IDownloader {

	private static final String ERRO_AO_LER_OS_DADOS_DA_CONEXAO = "Erro ao ler os dados da conexao http";
	private static final String ERRO_AO_COPIAR_A_IMAGEM_DA_PASTA_TEMPORAR = "Erro ao copiar a imagem da pasta temporaria";
	private static final String URL_INFORMADA_E_INVALIDA_ = "A url informada e invalida: [%s]";
	private static final String ACCEPT = null;
	private static final String USER_AGENT = null;
	private static final String IF_MODIFIED_SINCE = null;
	private static final String IMAGEM_NAO_MODIFICADA_ = null;
	private static final Marker DOWNLOAD_DA_IMAGEM_PNG_FINALIZADO = null;
	private static final int ONE_K_BYTE = 0;
	private final byte[] innerBuffer;
	private int returnCode;
	private long fileSize;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final FileUtil fileUtil = new FileUtil();

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.socialtime.carga.http.IDownloader#download(java.lang.String,
	 * java.lang.String)
	 */
	public ImageDownloader() {
		super();
		innerBuffer = new byte[ONE_K_BYTE];
	}

	/**
	 * Este metodo modifica o objeto imageModel
	 * 
	 * @param imageModel
	 * @return
	 * @throws java.io.IOException
	 */
	public int download(String url) throws IOException {

		long start = System.currentTimeMillis();
		String id = UUID.generate();
		download(url, new File(String.format("%s.png", id)), null);

		logger.debug(DOWNLOAD_DA_IMAGEM_PNG_FINALIZADO, id,
				System.currentTimeMillis() - start);

		return 200;
	}

	private void readData(HttpURLConnection connection, File file) {
		InputStream in = null;
		FileOutputStream out = null;

		File limbo = new File("tmp", file.getName());

		try {
			in = connection.getInputStream();
			out = new FileOutputStream(limbo);
			int c;
			while ((c = in.read(innerBuffer)) != -1) {
				// Thread.sleep(readBufferDelay);
				out.write(innerBuffer, 0, c);
			}
			// out.flush();
		} catch (IOException ex) {
			logger.error(ERRO_AO_LER_OS_DADOS_DA_CONEXAO, ex);
		} finally {

			fileUtil.closeStream(in);

			fileUtil.closeStream(out);
			try {
				fileUtil.moveFile(limbo, file);
			} catch (IOException ex) {
				logger.error(ERRO_AO_COPIAR_A_IMAGEM_DA_PASTA_TEMPORAR, ex);
			}
		}
	}

	public long download(final String url, final File file, final String date)
			throws IOException {
		URL _url = null;
		try {
			_url = new URL(url);
		} catch (MalformedURLException ex) {
			logger.error(URL_INFORMADA_E_INVALIDA_, url);
			throw new MalformedURLException(URL_INFORMADA_E_INVALIDA_);
		}

		HttpURLConnection urlConn = (HttpURLConnection) _url.openConnection();

		// urlConn.addRequestProperty(ACCEPT_ENCODING, GZIP);
		urlConn.addRequestProperty(ACCEPT,
				"image/jpeg+jpg+png+gif;q=0.9,image/webp,*/*;q=0.8");
		urlConn.setConnectTimeout(10000);
		urlConn.setInstanceFollowRedirects(true);
		urlConn.setRequestProperty(USER_AGENT, RandomUserAgent.generate());
		if (date != null) {
			urlConn.setRequestProperty(IF_MODIFIED_SINCE, date);
		}

		fileSize = urlConn.getContentLengthLong();

		returnCode = urlConn.getResponseCode();

		// lastDateModified = urlConn.getHeaderField(LAST_MODIFIED);
		if (returnCode == HttpURLConnection.HTTP_OK) {
			readData(urlConn, file);
		} else {
			logger.debug(IMAGEM_NAO_MODIFICADA_, url);
		}
		urlConn.disconnect();

		return fileSize;
	}

	@Override
	public long download(final String url, final String file)
			throws IOException {
		return download(url, new File(file), null);
	}

	public static void main(String[] args) throws IOException {
		ImageDownloader downloader = new ImageDownloader();
		long start = System.currentTimeMillis();
		downloader
				.download(
						"http://parceiroimg.maquinadevendas.com.br/produto/32345_1399486_20120823174419.jpg",
						// "http://parceiroimg.maquinadevendas.com.br/produto/35712_87341_20101007162938.jpg",
						"/Users/edgardleal/teste.jpg");

		System.out.println(String.format("Download finalizado em : [%d ms]",
				System.currentTimeMillis() - start));
	}

}
