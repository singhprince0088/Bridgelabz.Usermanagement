package com.bridgelabz.usermngmt.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.usermngmt.config.Response;

@ControllerAdvice
public class GlobalException {

	@Autowired
	private Response response;

	@ExceptionHandler(UserException.class)
	ResponseEntity<Response> exceptionHandler(UserException userException) {
		response.setStatus(500);
		response.setMsg(userException.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

}
