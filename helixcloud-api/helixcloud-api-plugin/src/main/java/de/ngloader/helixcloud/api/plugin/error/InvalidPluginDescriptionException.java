package de.ngloader.helixcloud.api.plugin.error;

public class InvalidPluginDescriptionException extends Exception {

	private static final long serialVersionUID = 1355156972206522161L;

	public InvalidPluginDescriptionException() { }

	public InvalidPluginDescriptionException(Throwable error) {
		super(error);
	}

	public InvalidPluginDescriptionException(String message, Throwable error) {
		super(message, error);
	}

	public InvalidPluginDescriptionException(String message) {
		super(message);
	}
}
