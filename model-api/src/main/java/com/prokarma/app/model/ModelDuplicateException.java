package com.prokarma.app.model;


public class ModelDuplicateException extends ModelException {

	private static final long serialVersionUID = 1L;

	public ModelDuplicateException() {
    }

    public ModelDuplicateException(String message) {
        super(message);
    }

    public ModelDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelDuplicateException(Throwable cause) {
        super(cause);
    }

}
