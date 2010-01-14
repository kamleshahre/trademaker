package org.lifeform.core;

import org.lifeform.util.AppUtil;

public class AppException extends Exception {
	private static final long serialVersionUID = 2847725227830170311L;

	public AppException() {
	}

	public AppException(String message) {
		super(message);
	}

	public AppException(Throwable cause) {
		super(cause);
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String getMessage() {
		final String message = super.getMessage();
		final String app = AppUtil.getAppName();
		return app + ": " + message;
	}
}
