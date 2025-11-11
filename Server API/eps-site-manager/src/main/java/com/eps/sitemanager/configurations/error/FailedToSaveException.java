package com.eps.sitemanager.configurations.error;

public class FailedToSaveException extends RuntimeException {

	private static final long serialVersionUID = 9131422325480571421L;

	public FailedToSaveException() {
        super();
    }

    public FailedToSaveException(String message) {
        super(message);
    }
	
}


