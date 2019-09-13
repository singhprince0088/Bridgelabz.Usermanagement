package com.bridgelabz.usermngmt.exception;

/**
 * Class for handling exceptions by custom
 * 
 * @author Prince Singh
 *
 */
public class UserException extends Exception {

	private static final long serialVersionUID = -4868809895408514510L;

	int code;
	String msg;

	public UserException(int code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}

	public UserException(String msg) {
		super(msg);
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "UserException [code=" + code + ", msg=" + msg + "]";
	}

}
