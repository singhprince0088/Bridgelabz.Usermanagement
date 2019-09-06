package com.bridgelabz.usermngmt.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.usermngmt.config.Response;
import com.bridgelabz.usermngmt.services.IDashboardServices;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

	@Autowired
	private IDashboardServices dashboardServices;
	
	@GetMapping("/location")
	public ResponseEntity<Response> getLocation() {
		return new ResponseEntity<>(dashboardServices.getLocation(), HttpStatus.OK);
	}

	@GetMapping("/gender")
	public ResponseEntity<Response> getGender() {
		return new ResponseEntity<>(dashboardServices.getGender(), HttpStatus.OK);
	}

	@GetMapping("/age")
	public ResponseEntity<Response> getAge() {
		return new ResponseEntity<>(dashboardServices.getAge(), HttpStatus.OK);
	}

	@GetMapping("/latest")
	public ResponseEntity<Response> getLatest() {
		return new ResponseEntity<>(dashboardServices.getLatest(), HttpStatus.OK);
	}

	@GetMapping("/history")
	public ResponseEntity<Response> getHistory() {
		return new ResponseEntity<>(dashboardServices.getHistory(), HttpStatus.OK);
	}
}
