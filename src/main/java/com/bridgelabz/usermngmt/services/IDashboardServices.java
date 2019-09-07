package com.bridgelabz.usermngmt.services;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.bridgelabz.usermngmt.config.Response;
import com.bridgelabz.usermngmt.exception.UserException;

@Service
public interface IDashboardServices {

	HashMap<String, Long> getLocation(String token)throws UserException;

	HashMap<String, Long> getGender(String token) throws UserException;

	HashMap<String, Long> getLatest(String token)throws UserException;

	HashMap<String, Integer> getAge(String token)throws UserException;

	Response getHistory(String token)throws UserException;
}
