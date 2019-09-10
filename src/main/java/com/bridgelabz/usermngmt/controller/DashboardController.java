package com.bridgelabz.usermngmt.controller;

import java.util.List;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.usermngmt.exception.UserException;
import com.bridgelabz.usermngmt.model.User;
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
	public List<User> getLatest(@RequestHeader String token) throws UserException {
		return dashboardServices.getLatest(token);
	}

	@GetMapping("/history")
	public HashMap<String, Integer> getHistory(@RequestHeader String token) throws UserException {
		return dashboardServices.getHistory(token);
	}
}
