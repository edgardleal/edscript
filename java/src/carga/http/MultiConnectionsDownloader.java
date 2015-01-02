/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package carga.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Edgard Leal
 */
public class MultiConnectionsDownloader implements IDownloader {

	// int[] buffer = new int[100];
	final Object THREAD_MONITOR = new Object();

	ByteBuffer buffer = ByteBuffer.allocateDirect(50);

	public void callback() {

	}

	public void download() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		System.out.println("Inicializando as threads...");
		new AsyncronousDownload(this, 0, 9);
		new AsyncronousDownload(this, 10, 19);
		new AsyncronousDownload(this, 20, 29);
		new AsyncronousDownload(this, 30, 39);
		new AsyncronousDownload(this, 40, 49);

		synchronized (this.THREAD_MONITOR) {
			THREAD_MONITOR.notifyAll();
		}
	}

	@Override
	public long download(String url, String file) throws IOException {
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	void print() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		for (int i = 0; i < buffer.limit(); i++) {
			builder.append(buffer.get(i)).append(' ');
		}
		System.out.println(builder.append(']').toString());
	}
}

// ---------------------------------------------------------------------------
class AsyncronousDownload extends Thread {

	private static long count = 0;
	private final MultiConnectionsDownloader connectionsDownloader;
	private final int index;
	private final int offset;
	private long fileSize;
	private int returnCode;
	private String lastDateModified;
	private byte[] innerBuffer = new byte[1024];

	public AsyncronousDownload(
			MultiConnectionsDownloader connectionsDownloader, int index,
			int offset) {
		this.index = index;
		this.offset = offset;
		this.connectionsDownloader = connectionsDownloader;
		synchronized (this) {
			this.setName(String.format("ThreadMultiConnection%d", ++count));
		}

		this.start();
	}

	private void connect(final String url, String date)
			throws MalformedURLException, IOException {
		URL _url = new URL(url);
		HttpURLConnection urlConn = (HttpURLConnection) _url.openConnection();

		urlConn.addRequestProperty("Accept-Encoding", "gzip");
		urlConn.addRequestProperty("Accept",
				"image/jpeg+jpg+png+gif;q=0.9,image/webp,*/*;q=0.8");
		urlConn.setConnectTimeout(800);
		urlConn.setInstanceFollowRedirects(true);
		urlConn.setRequestProperty("User-Agent", RandomUserAgent.generate());
		if (date != null) {
			urlConn.setRequestProperty("If-Modified-Since", date);
		}

		fileSize = urlConn.getContentLengthLong();

		returnCode = urlConn.getResponseCode();

		lastDateModified = urlConn.getHeaderField("Last-Modified");

		if (returnCode == HttpURLConnection.HTTP_OK) {
			readData(urlConn, new File(""));
		}
	}

	private void readData(HttpURLConnection connection, File file) {
		InputStream in = null;
		FileOutputStream out = null;
		try {
			in = connection.getInputStream();
			out = new FileOutputStream(file);
			int c;
			while ((c = in.read(innerBuffer)) != -1) {
				// Thread.sleep(readBufferDelay);
				out.write(innerBuffer, 0, c);
			}
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			closeInputStream(in);
			closeOutputStream(out);
		}
	}

	@Override
	public void run() {
		try {
			System.out.println("Thread aguardamdo: " + this.getName());
			synchronized (this.connectionsDownloader) {
				this.connectionsDownloader.wait(1000);
			}
			for (int i = index; i < offset; i++) {
				this.connectionsDownloader.buffer.put(i,
						((byte) (Math.round(Math.random() * 255))));
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			Logger.getLogger(AsyncronousDownload.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		System.out.println("Thread finalizada: " + this.getName());
	}

	private void closeOutputStream(FileOutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void closeInputStream(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
