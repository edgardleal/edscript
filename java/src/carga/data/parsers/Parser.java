package carga.data.parsers;

/**
 * 
 * @author edgardleal
 *
 */
public interface Parser<T> {

	public T parse(Object obj);
}
