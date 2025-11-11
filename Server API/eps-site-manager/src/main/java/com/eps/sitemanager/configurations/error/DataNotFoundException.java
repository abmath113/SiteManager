package com.eps.sitemanager.configurations.error;

public class DataNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 9131422325480571420L;

	public DataNotFoundException() {
        super();
    }

    public DataNotFoundException(String message) {
        super(message);
    }
    
}
