package com.bridgelabz.usermngmt.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.usermngmt.config.Response;
import com.bridgelabz.usermngmt.config.TokenGenerator;
import com.bridgelabz.usermngmt.exception.UserException;
import com.bridgelabz.usermngmt.model.User;
import com.bridgelabz.usermngmt.repository.IUserRepository;

@Service
@PropertySource("classpath:response.properties")
public class DashboardServicesImp implements IDashboardServices {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private TokenGenerator tokenGenerator;

	@Autowired
	private Environment environment;

	@Override
	public HashMap<String, Long> getLocation(String token) throws UserException {
		if (!validateAdmin(token)) {
			throw new UserException(environment.getProperty("user.role.admin"));
		}
		List<User> allUser = userRepository.findAll();
		long usa = allUser.stream().filter(gen -> gen.getCountry().equalsIgnoreCase("usa")).count();
		long india = allUser.stream().filter(gen -> gen.getCountry().equalsIgnoreCase("india")).count();
		long canada = allUser.stream().filter(gen -> gen.getCountry().equalsIgnoreCase("canada")).count();
		long other = allUser.stream().filter(gen -> gen.getCountry().equalsIgnoreCase("other")).count();
		HashMap<String, Long> map = new HashMap<>();
		map.put("USA", usa);
		map.put("India", india);
		map.put("Canada", canada);
		map.put("Other", other);
		return map;
	}

	@Override
	public HashMap<String, Long> getGender(String token) throws UserException {
		if (!validateAdmin(token)) {
			throw new UserException(environment.getProperty("user.role.admin"));
		}
		List<User> allUser = userRepository.findAll();
		long male = allUser.stream().filter(gen -> gen.getGender().equalsIgnoreCase("male")).count();
		long female = allUser.stream().filter(gen -> gen.getGender().equalsIgnoreCase("female")).count();
		HashMap<String, Long> map = new HashMap<>();
		map.put("Male", male);
		map.put("Female", female);
		return map;
	}

	@Override
	public HashMap<String, Long> getLatest(String token) throws UserException {
		if (!validateAdmin(token)) {
			throw new UserException(environment.getProperty("user.role.admin"));
		}
		
		
		return null;
	}

	@Override
	public HashMap<String, Integer> getAge(String token) throws UserException {
		if (!validateAdmin(token)) {
			throw new UserException(environment.getProperty("user.role.admin"));
		}
		List<User> allUser = userRepository.findAll();
		int below18 = 0;
		int between18_25 = 0;
		int between25_32 = 0;
		int between32_40 = 0;
		int over40 = 0;

		for (User user : allUser) {
			Period period = Period.between(user.getRegDate(), getDate());
			if (period.getYears() < 18) {
				below18++;
			} else if (period.getYears() >= 18 || period.getYears() < 25) {
				between18_25++;
			} else if (period.getYears() >= 25 || period.getYears() < 32) {
				between25_32++;
			} else if (period.getYears() >= 32 || period.getYears() < 40) {
				between32_40++;
			} else if (period.getYears() > 40) {
				over40++;
			}
		}
		HashMap<String, Integer> map = new HashMap<>();
		map.put("Under 18", below18);
		map.put("18-25", between18_25);
		map.put("25-32", between25_32);
		map.put("32-40", between32_40);
		map.put("over 40", over40);

		return map;
	}

	@Override
	public Response getHistory(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	public LocalDate getDate() {
		return LocalDate.now();
	}

	public boolean validateAdmin(String token) {
		Long adminId = tokenGenerator.decodeToken(token);
		Optional<User> admin = userRepository.findById(adminId);
		if (admin.get().isUserRole() == false) {
			return false;
		}
		return true;
	}
}
