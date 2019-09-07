package com.bridgelabz.usermngmt.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.usermngmt.config.Response;
import com.bridgelabz.usermngmt.exception.UserException;
import com.bridgelabz.usermngmt.services.IDashboardServices;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

	@Autowired
	private IDashboardServices dashboardServices;

	@GetMapping("/location")
	public HashMap<String, Long> getLocation(@RequestHeader String token) throws UserException {
		return dashboardServices.getLocation(token);
	}

	@GetMapping("/gender")
	public HashMap<String, Long> getGender(@RequestHeader String token) throws UserException {
		return dashboardServices.getGender(token);
	}

	@GetMapping("/age")
	public HashMap<String, Integer> getAge(@RequestHeader String token) throws UserException {
		return dashboardServices.getAge(token);
	}

	@GetMapping("/latest")
	public HashMap<String, Long> getLatest(@RequestHeader String token) throws UserException {
		return dashboardServices.getLatest(token);
	}

	@GetMapping("/history")
	public ResponseEntity<Response> getHistory(@RequestHeader String token) throws UserException {
		return new ResponseEntity<>(dashboardServices.getHistory(token), HttpStatus.OK);
	}
}
