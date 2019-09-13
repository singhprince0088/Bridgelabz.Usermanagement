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

/**
 * Controller class for dashBoard APIs.
 * 
 * @since september-2019
 * @author Prince Singh
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

	@Autowired
	private IDashboardServices dashboardServices;

	/**
	 * Get Status of User(Active/InActive).
	 * 
	 * @param token
	 * @return HashMap of users status.
	 * @throws UserException
	 */
	@GetMapping("/status")
	public HashMap<String, List<User>> getStatus(@RequestHeader String token) throws UserException {
		return dashboardServices.getStatus(token);
	}

	/**
	 * Location API
	 * 
	 * @param Token
	 * @return Location with number of users
	 * @throws UserException
	 */
	@GetMapping("/location")
	public HashMap<String, Long> getLocation(@RequestHeader String token) throws UserException {
		return dashboardServices.getLocation(token);
	}

	/**
	 * Gender API
	 * 
	 * @param Token
	 * @return Number of users in genders
	 * @throws UserException
	 */
	@GetMapping("/gender")
	public HashMap<String, Long> getGender(@RequestHeader String token) throws UserException {
		return dashboardServices.getGender(token);
	}

	/**
	 * AgeGroup API
	 * 
	 * @param Token
	 * @return Number of users in age groups
	 * @throws UserException
	 */
	@GetMapping("/age")
	public HashMap<String, Integer> getAge(@RequestHeader String token) throws UserException {
		return dashboardServices.getAge(token);
	}

	/**
	 * LatestUser API
	 * 
	 * @param Token
	 * @return Latest users list
	 * @throws UserException
	 */
	@GetMapping("/latest")
	public List<User> getLatest(@RequestHeader String token) throws UserException {
		return dashboardServices.getLatest(token);
	}

	/**
	 * History API
	 * 
	 * @param Token
	 * @return History of registration
	 * @throws UserException
	 */
	@GetMapping("/history")
	public HashMap<String, Integer> getHistory(@RequestHeader String token) throws UserException {
		return dashboardServices.getHistory(token);
	}
}
