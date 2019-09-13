package com.bridgelabz.usermngmt.config;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
@Component
public class Utility {

	public LocalDate getDate() {
		return LocalDate.now();
	}
}
