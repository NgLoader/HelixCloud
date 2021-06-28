package de.ngloader.helixcloud.api.plugin.error;

public class InvalidPluginException extends Exception {

	private static final long serialVersionUID = 1355156972206522161L;

	public InvalidPluginException() { }

	public InvalidPluginException(Throwable error) {
		super(error);
	}

	public InvalidPluginException(String message, Throwable error) {
		super(message, error);
	}

	public InvalidPluginException(String message) {
		super(message);
	}
}
