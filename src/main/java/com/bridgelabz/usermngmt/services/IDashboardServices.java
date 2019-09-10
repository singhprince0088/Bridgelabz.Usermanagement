package com.bridgelabz.usermngmt.services;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import com.bridgelabz.usermngmt.exception.UserException;
import com.bridgelabz.usermngmt.model.User;

@Service
public interface IDashboardServices {

	HashMap<String, Long> getLocation(String token) throws UserException;

	HashMap<String, Long> getGender(String token) throws UserException;

	List<User> getLatest(String token) throws UserException;

	HashMap<String, Integer> getAge(String token) throws UserException;

	HashMap<String, Integer> getHistory(String token) throws UserException;
}
