package com.bridgelabz.usermngmt.services;

import org.springframework.stereotype.Service;

import com.bridgelabz.usermngmt.config.Response;

@Service
public interface IDashboardServices {

	Response getLocation();

	Response getGender();

	Response getLatest();

	Response getAge();

	Response getHistory();
}
