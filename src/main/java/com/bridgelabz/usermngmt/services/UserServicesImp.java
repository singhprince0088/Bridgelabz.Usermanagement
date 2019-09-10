package com.bridgelabz.usermngmt.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.usermngmt.config.Response;
import com.bridgelabz.usermngmt.config.TokenGenerator;
import com.bridgelabz.usermngmt.dto.LoginDto;
import com.bridgelabz.usermngmt.dto.UserDto;
import com.bridgelabz.usermngmt.exception.UserException;
import com.bridgelabz.usermngmt.model.User;
import com.bridgelabz.usermngmt.repository.IUserRepository;

@Service
@PropertySource("classpath:response.properties")
public class UserServicesImp implements IUserServices {
	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private TokenGenerator tokenGenerator;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private Environment environment;

	@Autowired
	private ModelMapper mapper;

	private final Path fileLocation = java.nio.file.Paths.get("/home/user/USER_DP/");

	@Override
	public Response register(UserDto userDto) throws UserException {
		if (!validateUser(userDto.getEmail())) {
			throw new UserException(environment.getProperty("status.register.emailExistError"));
		}
		User user = mapper.map(userDto, User.class);
		String password = passwordEncoder.encode(userDto.getPassword());
		user.setRegDate(getDate());
		user.setPassword(password);
		userRepository.save(user);
		return new Response(200,"register succesfully") ;
	}

	@Override
	public Response uploadImage(MultipartFile image, String token) {
		long userId = tokenGenerator.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		try {
			Files.copy(image.getInputStream(), fileLocation.resolve(uniqueId), StandardCopyOption.REPLACE_EXISTING);
			user.get().setProfilePic(uniqueId);
			userRepository.save(user.get());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Response(200,"image uploaded succesfully") ;
	}

	@Override
	public Response login(LoginDto loginDto) throws UserException {
		Optional<User> user = userRepository.findByEmail(loginDto.getUsername());
		if (!user.isPresent()) {
			throw new UserException(environment.getProperty("user.login.username"));
		}
		if (!passwordEncoder.matches(loginDto.getPassword(), user.get().getPassword())) {
			throw new UserException(environment.getProperty("user.login.password"));
		}
		if(!user.get().isUserRole()) {
			throw new UserException(environment.getProperty("user.role.admin"));
		}
		String token = tokenGenerator.createToken(user.get().getId());
		System.out.println(token);
		return new Response(200,"login succesfully") ;
	}

	@Override
	public Response update(UserDto userDto, String token, Long adminId) throws UserException {
		Long userId = tokenGenerator.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new UserException(environment.getProperty("user.doesn't.exist"));
		}
		mapper.map(userDto, User.class);
		userRepository.save(user.get());
		return new Response(200,"update succesfully") ;
	}

	@Override
	public Response delete(Long userId, String token) throws UserException {
		Long adminId = tokenGenerator.decodeToken(token);
		Optional<User> admin = userRepository.findById(adminId);
		if (admin.get().isUserRole() == false) {
			throw new UserException(environment.getProperty("user.role.admin"));
		}
		userRepository.deleteById(userId);
		return new Response(200,"delete succesfully") ;
	}

	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@Override
	public HashMap<String, List<User>> getStatus(String token) throws UserException {
		Long adminId = tokenGenerator.decodeToken(token);
		Optional<User> admin = userRepository.findById(adminId);
		if (admin.get().isUserRole() == false) {
			throw new UserException(environment.getProperty("user.role.admin"));
		}
		List<User> active = userRepository.findByStatusTrue();
		List<User> inActive = userRepository.findByStatusFalse();
		HashMap<String, List<User>> map = new HashMap<>();
		map.put("Active", active);
		map.put("InActive", inActive);
		return map;
	}

	public boolean validateUser(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (!user.isPresent()) {
			return true;
		}
		return false;
	}

	public LocalDate getDate() {
		return LocalDate.now();
	}

}
