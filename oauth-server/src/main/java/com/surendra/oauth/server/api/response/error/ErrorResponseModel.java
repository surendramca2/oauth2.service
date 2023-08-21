package com.surendra.oauth.server.api.response.error;


public class ErrorResponseModel {

    private ResponseModel error;

    public ErrorResponseModel(String message, int status) {
        error = new ResponseModel(message, status);
    }

	public ResponseModel getError() {
		return error;
	}

	public void setError(ResponseModel error) {
		this.error = error;
	}
}
