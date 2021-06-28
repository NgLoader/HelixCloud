package de.ngloader.helixcloud.api.common;

import java.util.Map;

public interface IProperties {

	public Object set(String key, Object value);

	public Object get(String key);

	public <T> T get(String key, T defaultValue, Class<T> clazz);

	public String getString(String key);

	public boolean getBoolean(String key);

	public int getInt(String key);

	public float getFloat(String key);

	public double getDouble(String key);

	public short getShort(String key);

	public byte getByte(String key);

	public boolean has(String key);

	public Map<String, Object> getMetadata();
}
