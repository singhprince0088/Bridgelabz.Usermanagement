package com.bridgelabz.usermngmt.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.usermngmt.config.TokenGenerator;
import com.bridgelabz.usermngmt.config.Utility;
import com.bridgelabz.usermngmt.exception.UserException;
import com.bridgelabz.usermngmt.model.User;
import com.bridgelabz.usermngmt.repository.IUserRepository;

/**
 * This class implements all methods of {@link IDashboardServices} interface.
 * which exposes dashboard services logic.
 * 
 * @since september-2019
 * @author Prince Singh
 * @version 1.0
 *
 */
@Service
@PropertySource("classpath:response.properties")
public class DashboardServicesImp implements IDashboardServices {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private TokenGenerator tokenGenerator;

	@Autowired
	private Environment environment;

	@Autowired
	private Utility util;

	/**
	 * This method contains logic of giving status of users.
	 * 
	 * @param Token(Admin)
	 * @return active/inactive users
	 * @throws UserException.
	 */
	@Override
	public HashMap<String, List<User>> getStatus(String token) throws UserException {
		if (validateAdmin(token)) {
			List<User> active = userRepository.findByStatusTrue();
			List<User> inActive = userRepository.findByStatusFalse();
			HashMap<String, List<User>> map = new HashMap<>();
			map.put("Active", active);
			map.put("InActive", inActive);
			return map;
		}
		return null;
	}

	/**
	 * Get all Locations by number of users.
	 * 
	 * @param Token(contains adminId).
	 * @return Locations.
	 * @throws UserException.
	 */
	@Override
	public HashMap<String, Long> getLocation(String token) throws UserException {
		if (!validateAdmin(token)) {
			throw new UserException(environment.getProperty("user.role.admin"));
		}
		List<User> allUser = userRepository.findAll();
		HashMap<String, Long> map = new LinkedHashMap<>();
		for (User user : allUser) {
			if (!(map.containsKey(user.getCountry()))) {
				long count = 0;
				for (User user2 : allUser) {
					if (user.getCountry().equalsIgnoreCase(user2.getCountry())) {
						count++;
					}
					map.put(user.getCountry(), count);
				}
			}
		}
		LinkedHashMap<String, Long> sortedMap = new LinkedHashMap<>();
		map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
		return sortedMap;
	}

	/**
	 * Get number of Genders.
	 * 
	 * @param Token(contains adminId).
	 * @return Gender.
	 * @throws UserException.
	 */
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

	/**
	 * Get list of users according to latest registration.
	 * 
	 * @param Token(contains adminId).
	 * @return List of users.
	 * @throws UserException.
	 */
	@Override
	public List<User> getLatest(String token) throws UserException {
		if (!validateAdmin(token)) {
			throw new UserException(environment.getProperty("user.role.admin"));
		}
		List<User> latestSorted = userRepository.findAll().stream()
				.sorted(Comparator.comparing(User::getRegDate).reversed()).collect(Collectors.toList());
		return latestSorted;
	}

	/**
	 * Get all number of users by age group.
	 * 
	 * @param Token(contains adminId).
	 * @return age groups.
	 * @throws UserException.
	 */
	@Override
	public HashMap<String, Integer> getAge(String token) throws UserException {
		if (!validateAdmin(token)) {
			throw new UserException(environment.getProperty("user.role.admin"));
		}
		List<User> allUser = userRepository.findAll();
		int _below18 = 0;
		int between18_25 = 0;
		int between25_32 = 0;
		int between32_40 = 0;
		int over40_ = 0;

		for (User user : allUser) {
			Period period = Period.between(user.getRegDate(), util.getDate());
			if (period.getYears() < 18) {
				_below18++;
			} else if (period.getYears() >= 18 || period.getYears() < 25) {
				between18_25++;
			} else if (period.getYears() >= 25 || period.getYears() < 32) {
				between25_32++;
			} else if (period.getYears() >= 32 || period.getYears() < 40) {
				between32_40++;
			} else if (period.getYears() > 40) {
				over40_++;
			}
		}
		HashMap<String, Integer> map = new HashMap<>();
		map.put("Under 18", _below18);
		map.put("18-25", between18_25);
		map.put("25-32", between25_32);
		map.put("32-40", between32_40);
		map.put("over 40", over40_);

		return map;
	}

	/**
	 * Get history of last six months registrations.
	 * 
	 * @param Token(contains adminId).
	 * @return no. of registration in two consecutive months groups.
	 * @throws UserException.
	 */
	@Override
	public HashMap<String, Integer> getHistory(String token) {
		int count0 = 0, count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0;
		LocalDate todayDate = LocalDate.now();
		LocalDate presentMonth = todayDate.minusMonths(0);
		LocalDate firstLastMonth = todayDate.minusMonths(1);
		LocalDate secondLastMonth = todayDate.minusMonths(2);
		LocalDate thirdLastMonth = todayDate.minusMonths(3);
		LocalDate fourthLastMonth = todayDate.minusMonths(4);
		LocalDate fifthLastMonth = todayDate.minusMonths(5);
		LocalDate sixthLastMonth = todayDate.minusMonths(6);

		List<User> users = userRepository.findAll();
		for (User user : users) {
			if (user.getRegDate().getMonth().equals(presentMonth.getMonth())) {
				count0++;
			} else if (user.getRegDate().getMonth().equals(firstLastMonth.getMonth())) {
				count1++;
			} else if (user.getRegDate().getMonth().equals(secondLastMonth.getMonth())) {
				count2++;
			} else if (user.getRegDate().getMonth().equals(thirdLastMonth.getMonth())) {
				count3++;
			} else if (user.getRegDate().getMonth().equals(fourthLastMonth.getMonth())) {
				count4++;
			} else if (user.getRegDate().getMonth().equals(fifthLastMonth.getMonth())) {
				count5++;
			} else if (user.getRegDate().getMonth().equals(sixthLastMonth.getMonth())) {
				count6++;
			}
		}
		LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
		map.put("Present Month", count0);
		map.put("First Last Month", count1);
		map.put("Second Last Month", count2);
		map.put("Third Last Month", count3);
		map.put("Fourth Last Month", count4);
		map.put("Fifth Last Month", count5);
		map.put("Sixth Last Month", count6);
		return map;
	}

	/**
	 * Validate admin.
	 * 
	 * @param token.
	 * @return true if admin present, or false.
	 */
	public boolean validateAdmin(String token) {
		Long adminId = tokenGenerator.decodeToken(token);
		Optional<User> admin = userRepository.findById(adminId);
		if (admin.get().isUserRole() == false) {
			return false;
		}
		return true;
	}
}
