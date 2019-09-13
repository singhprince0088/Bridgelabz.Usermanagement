package com.bridgelabz.usermngmt.config;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {

	int status;
	String msg;

	public <T> Response buildSuccesResponse(T t) {
		return new Response(200, "Success");
	}
	
	public <T> Response buildFailedResponse(T t) {
		return new Response(500, "Failed");
	}
}
