/**
 *
 */
package carga;

import java.io.*;
import java.util.*;

/**
 * Gerenciador de dependencias da aplicacao
 * 
 * @author Edgard Leal
 * @since 12/02/2014 09:38:25
 * 
 */
public class Context implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Context innerInstance;
	private static HashMap<Class<?>, Object> objectCache;

	private synchronized HashMap<Class<?>, Object> getApplicationContext() {
		if (objectCache == null) {
			objectCache = new HashMap<Class<?>, Object>();
		}
		return objectCache;
	}

	public Context() {

	}

	@SuppressWarnings("unused")
	@Deprecated
	private synchronized static Context getGlobalContext() {
		if (innerInstance == null) {
			innerInstance = new Context();
		}
		return innerInstance;
	}

	public synchronized <T> void put(Class<T> c, T obj) {
		getApplicationContext().put(c, obj);
	}

	@SuppressWarnings("unchecked")
	public synchronized <T> T getInstance(Class<T> c) {
		T result = (T) getApplicationContext().get(c);
		if (result == null) {
			try {
				result = (T) c.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
			getApplicationContext().put(c, result);
		}
		return result;
	}
}
