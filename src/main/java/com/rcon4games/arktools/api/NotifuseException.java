package com.rcon4games.arktools.api;

/**
 * Created by Anthony Denaud on 27/04/17.
 * Copyright Personalized-Software Ltd
 */
public class NotifuseException extends Exception {

	private int statusCode;
	private String error;
	private String message;

	public NotifuseException(int statusCode, String error, String message) {
		this.statusCode = statusCode;
		this.error = error;
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
