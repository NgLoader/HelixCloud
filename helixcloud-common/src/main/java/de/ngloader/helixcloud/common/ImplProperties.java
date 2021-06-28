package de.ngloader.helixcloud.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.ngloader.helixcloud.api.common.IProperties;

public class ImplProperties implements IProperties {

	private final Map<String, Object> metadata = new HashMap<>();

	@Override
	public Object set(String key, Object value) {
		return this.metadata.put(key, value);
	}

	@Override
	public Object get(String key) {
		return this.metadata.get(key);
	}

	@Override
	public <T> T get(String key, T defaultValue, Class<T> clazz) {
		Object value = this.metadata.getOrDefault(key, defaultValue);
		if (value != null && clazz.isInstance(value)) {
			return clazz.cast(value);
		}

		return defaultValue;
	}

	@Override
	public String getString(String key) {
		return this.get(key, null, String.class);
	}

	@Override
	public boolean getBoolean(String key) {
		return this.get(key, false, Boolean.class);
	}

	@Override
	public int getInt(String key) {
		return this.get(key, 0, Integer.class);
	}

	@Override
	public float getFloat(String key) {
		return this.get(key, 0f, Float.class);
	}

	@Override
	public double getDouble(String key) {
		return this.get(key, 0d, Double.class);
	}

	@Override
	public short getShort(String key) {
		return this.get(key, (short) 0, Short.class);
	}

	@Override
	public byte getByte(String key) {
		return this.get(key, (byte) 0, Byte.class);
	}

	@Override
	public boolean has(String key) {
		return this.metadata.containsKey(key);
	}

	@Override
	public Map<String, Object> getMetadata() {
		return Collections.unmodifiableMap(this.metadata);
	}
}