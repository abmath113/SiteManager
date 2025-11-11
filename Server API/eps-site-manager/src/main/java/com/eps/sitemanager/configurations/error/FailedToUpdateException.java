
package com.eps.sitemanager.configurations.error;

public class FailedToUpdateException extends RuntimeException {

	private static final long serialVersionUID = 2200357384858952481L;

	public FailedToUpdateException() {
        super();
    }

    public FailedToUpdateException(String message) {
        super(message);
    }
	
}

