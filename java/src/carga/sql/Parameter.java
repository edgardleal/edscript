/**
 * 
 */
package carga.sql;

import carga.data.*;
import carga.string.*;

/**
 * @author edgardleal
 *
 */
public class Parameter {

	private String name;
	private Object value;
	private Types type;

	/**
	 * 
	 */
	public Parameter() {
		// TODO Auto-generated constructor stub
	}

	public Parameter(String name, Object value, Types type) {
		super();
		this.name = name;
		this.value = value;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Types getType() {
		return type;
	}

	public void setType(Types type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return StringUtil.concat("Parameter [name=", name, ", value=",
				(value == null ? Constants.NULL : value.toString()), ", type=",
				type.toString(), "]");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parameter other = (Parameter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public Integer asInteger() {
		return Integer.valueOf(value.toString());
	}

	public String asString() {
		return value.toString();
	}

	public Float asFloat() {
		return Float.valueOf(value.toString());
	}

	public Boolean asBoolean() {
		return Boolean.parseBoolean(value.toString());
	}
}
