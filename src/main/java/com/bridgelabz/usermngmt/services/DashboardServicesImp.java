package com.bridgelabz.usermngmt.services;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

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
	public List<User> getLatest(String token) throws UserException {
		if (!validateAdmin(token)) {
			throw new UserException(environment.getProperty("user.role.admin"));
		}
		List<User> latestSorted = userRepository.findAll().stream()
				.sorted(Comparator.comparing(User::getRegDate).reversed()).collect(Collectors.toList());
		return latestSorted;
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
	public HashMap<String, Integer> getHistory(String token) {
		int july_aug18 = 0, sep_oct18 = 0, nov_dec18 = 0, jan_feb19 = 0, mar_april19 = 0, may_june19 = 0,
				july_aug19 = 0, sep_oct19 = 0, nov_dec19 = 0;
		List<User> users = userRepository.findAll();
		for (User user : users) {
			DateTimeFormatter formmater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String date = user.getRegDate().format(formmater);

			if ((LocalDate.parse(date).isAfter(LocalDate.parse("2018-06-30")))
					&& (LocalDate.parse(date).isBefore(LocalDate.parse("2018-09-01")))) {
				july_aug18++;
			} else if ((LocalDate.parse(date).isAfter(LocalDate.parse("2018-08-31")))
					&& (LocalDate.parse(date).isBefore(LocalDate.parse("2018-11-01")))) {
				sep_oct18++;
			} else if ((LocalDate.parse(date).isAfter(LocalDate.parse("2018-10-31")))
					&& (LocalDate.parse(date).isBefore(LocalDate.parse("2019-01-01")))) {
				nov_dec18++;
			} else if ((LocalDate.parse(date).isAfter(LocalDate.parse("2018-12-31")))
					&& (LocalDate.parse(date).isBefore(LocalDate.parse("2019-03-01")))) {
				jan_feb19++;
			} else if ((LocalDate.parse(date).isAfter(LocalDate.parse("2019-02-28")))
					&& (LocalDate.parse(date).isBefore(LocalDate.parse("2019-05-01")))) {
				mar_april19++;
			} else if ((LocalDate.parse(date).isAfter(LocalDate.parse("2019-04-30")))
					&& (LocalDate.parse(date).isBefore(LocalDate.parse("2019-07-01")))) {
				may_june19++;
			} else if ((LocalDate.parse(date).isAfter(LocalDate.parse("2019-06-30")))
					&& (LocalDate.parse(date).isBefore(LocalDate.parse("2019-09-01")))) {
				july_aug19++;
			} else if ((LocalDate.parse(date).isAfter(LocalDate.parse("2019-08-31")))
					&& (LocalDate.parse(date).isBefore(LocalDate.parse("2019-11-01")))) {
				sep_oct19++;
			} else if ((LocalDate.parse(date).isAfter(LocalDate.parse("2019-10-31")))
					&& (LocalDate.parse(date).isBefore(LocalDate.parse("2020-01-01")))) {
				nov_dec19++;
			}
		}
		LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
		map.put("July-August 2018", july_aug18);
		map.put("September-October 2018", sep_oct18);
		map.put("November-December 2018", nov_dec18);
		map.put("January-February 2019", jan_feb19);
		map.put("March-April 2019", mar_april19);
		map.put("May-June 2019", may_june19);
		map.put("July-August 2019", july_aug19);
		map.put("September-October 2019", sep_oct19);
		map.put("November-December 2019", nov_dec19);
		return map;
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
