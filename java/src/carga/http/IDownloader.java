/**
 * 
 */
package carga.http;

import java.io.IOException;

/**
 * @author Edgard Leal
 * 
 */
public interface IDownloader {

  public long download(String url, String file) throws IOException;
}
