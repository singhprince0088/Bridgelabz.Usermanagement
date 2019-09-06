package com.bridgelabz.usermngmt.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
	public Response register(UserDto userDto, MultipartFile image) throws UserException {
		if (!validateUser(userDto.getEmail())) {
			throw new UserException(environment.getProperty("status.register.emailExistError"));
		}
		User user = mapper.map(userDto, User.class);
		String password = passwordEncoder.encode(userDto.getPassword());
		user.setRegDate(getDate());
		user.setPassword(password);

		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		try {
			Files.copy(image.getInputStream(), fileLocation.resolve(uniqueId), StandardCopyOption.REPLACE_EXISTING);
			user.setProfilePic(uniqueId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		userRepository.save(user);
		return null;
	}

	public boolean validateUser(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (!user.isPresent()) {
			return true;
		}
		return false;
	}

	public String getDate() {
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return localDate.format(dateFormat);
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
		String token = tokenGenerator.createToken(user.get().getId());
		System.out.println(token);
		return null;
	}

	@Override
	public Response update(UserDto userDto, String token) throws UserException {
		Long userId = tokenGenerator.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new UserException(environment.getProperty("user.doesn't.exist"));
		}
		mapper.map(userDto, User.class);
		userRepository.save(user.get());
		return null;
	}

	@Override
	public Response delete(Long userId, String token) {
		Long adminId = tokenGenerator.decodeToken(token);
		Optional<User> admin = userRepository.findById(adminId);
		
		return null;
	}

	@Override
	public Response getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getStatus(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
