/**
 *
 */
package carga.http;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author Edgard Leal
 * 
 */
public class HttpConnectionHeader {

  private final HashMap<String, String> values = new HashMap<>();

  public void setHeader(String key, String value) {
    values.put(key, value);
  }

  public void setHeader(final String value) {
    String[] items = value.split(";");
    setHeader(items[0], items[1]);
  }

  public void assignToConnection(HttpURLConnection con) {
    if (con == null) {
      return;
    }
    Iterator<Entry<String, String>> iterator = values.entrySet().iterator();
    Entry<String, String> entry = null;

    while (iterator.hasNext()) {
      entry = iterator.next();
      con.addRequestProperty(entry.getKey(), entry.getValue());
    }
  }
}
