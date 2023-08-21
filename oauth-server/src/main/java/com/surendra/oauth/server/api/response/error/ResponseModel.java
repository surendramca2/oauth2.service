package com.surendra.oauth.server.api.response.error;


public class ResponseModel {
    private final String message;
    private final int status;
    
	public ResponseModel(String message, int status) {
		super();
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return status;
	}
}
