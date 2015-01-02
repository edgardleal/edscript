/*
 * Envio de arquivos por upload em formularios
 * @author Edgard Leal
 */
package carga.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 *
 * @author edgardleal
 */
public class FileUpload {

    private final String attachmentName = "file";
    private final String crlf = "\r\n";
    private final String twoHyphens = "--";
    private final String boundary = "*****";
    private final String empty = "";

    public String sendFile(InputStream file, URL url, String id) throws ProtocolException, IOException {
        if (id == null) {
            id = empty;
        }

        HttpURLConnection httpUrlConnection = null;
        httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setUseCaches(false);
        httpUrlConnection.setDoOutput(true);

        httpUrlConnection.setRequestMethod("POST");
        httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
        httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
        httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary);

        DataOutputStream request = new DataOutputStream(httpUrlConnection.getOutputStream());

        request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" + this.attachmentName + "\";filename=\"" + id + "\";id=\"2222\"" + this.crlf);
        request.writeBytes(this.crlf);

        request.write(toByteArray(file));

        request.writeBytes(this.crlf);
        request.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens + this.crlf);

        request.flush();
        request.close();

        return getResponse(httpUrlConnection);
    }

    public String upload(String file, String url) throws FileNotFoundException, IOException {
        FileInputStream in = new FileInputStream(new File(file));

        return sendFile(in, new URL(url), empty);
    }

    private byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[1024];

        while ((nRead = in.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        //buffer.flush();
        return buffer.toByteArray();

    }

    private String getResponse(HttpURLConnection httpUrlConnection) throws IOException {

        InputStream responseStream = new BufferedInputStream(httpUrlConnection.getInputStream());

        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
        String line = empty;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line).append('\n');
        }
        responseStreamReader.close();

        return stringBuilder.toString();
    }
}
