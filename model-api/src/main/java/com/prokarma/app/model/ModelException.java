package com.prokarma.app.model;


public class ModelException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ModelException() {
    }

    public ModelException(String message) {
        super(message);
    }

    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelException(Throwable cause) {
        super(cause);
    }

}
