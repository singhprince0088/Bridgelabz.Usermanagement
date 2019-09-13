package com.bridgelabz.usermngmt.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
import com.bridgelabz.usermngmt.config.Utility;
import com.bridgelabz.usermngmt.dto.LoginDto;
import com.bridgelabz.usermngmt.dto.UserDto;
import com.bridgelabz.usermngmt.exception.UserException;
import com.bridgelabz.usermngmt.model.User;
import com.bridgelabz.usermngmt.repository.IUserRepository;

/**
 * This class implements all methods of {@link IUserServices} interface. which
 * performs CRUD of user in database.
 * 
 * @since september-2019
 * @author Prince Singh
 * @version 1.0
 *
 */
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

	@Autowired
	private Response response;

	@Autowired
	private Utility utility;

	private final Path fileLocation = java.nio.file.Paths.get("/home/user/USER_DP/");

	/**
	 * This method is used for register new user in database.
	 * 
	 * @param userDto
	 * @return SuccesResponse.
	 * @throws UserException.
	 */
	@Override
	public Response register(UserDto userDto) throws UserException {
		if (!validateUser(userDto.getEmail())) {
			throw new UserException(environment.getProperty("status.register.emailExistError"));
		}
		User user = mapper.map(userDto, User.class);
		if (userRepository.findAll().size() == 0) {
			user.setUserRole(true);
		}
		String password = passwordEncoder.encode(userDto.getPassword());
		user.setRegDate(utility.getDate());
		user.setPassword(password);
		userRepository.save(user);
		return new Response(200, "register succesfully");
	}

	/**
	 * Uploading Image to user.
	 * 
	 * @param Multipartfile  (image).
	 * @param Token(contains userId).
	 * @return SuccesResponse.
	 * @throws UserException.
	 */
	@Override
	public Response uploadImage(MultipartFile image, String token) throws UserException {
		long userId = tokenGenerator.decodeToken(token);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserException(environment.getProperty("user.doesn't.exist")));
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		try {
			Files.copy(image.getInputStream(), fileLocation.resolve(uniqueId), StandardCopyOption.REPLACE_EXISTING);
			user.setProfilePic(uniqueId);
			userRepository.save(user);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response.buildSuccesResponse(null);
	}

	/**
	 * Login.
	 * 
	 * @param loginDto
	 * @return SuccesResponse.
	 * @throws UserException.
	 */
	@Override
	public Response login(LoginDto loginDto) throws UserException {
		Optional<User> user = userRepository.findByEmail(loginDto.getUsername());
		if (!user.isPresent()) {
			throw new UserException(environment.getProperty("user.login.username"));
		}
		if (!passwordEncoder.matches(loginDto.getPassword(), user.get().getPassword())) {
			throw new UserException(environment.getProperty("user.login.password"));
		}
		if (!user.get().isUserRole()) {
			throw new UserException(environment.getProperty("user.role.admin"));
		}
		String token = tokenGenerator.createToken(user.get().getId());
		System.out.println(token);
		return response.buildSuccesResponse(null);
	}

	/**
	 * Update user.
	 * 
	 * @param userDto
	 * @param adminToken
	 * @param userId
	 * @return SuccesResponse.
	 * @throws UserException.
	 */
	@Override
	public Response update(UserDto userDto, String adminToken, Long userId, boolean role) throws UserException {
		if (validateAdmin(adminToken)) {
			userRepository.findById(userId).map(user -> {
				mapper.map(userDto, User.class);
				return user;
			}).map(user -> {
				user.setUserRole(role);
				return user;
			}).map(userRepository::save)
					.orElseThrow(() -> new UserException(environment.getProperty("user.doesn't.exist")));
		}
		return response.buildSuccesResponse(null);
	}

	/**
	 * Delete user.
	 * 
	 * @param Token
	 * @param userId
	 * @return SuccesResponse.
	 * @throws UserException.
	 */
	@Override
	public Response delete(Long userId, String token) throws UserException {
		if (validateAdmin(token)) {
			return userRepository.findById(userId).map(user -> {
				userRepository.delete(user);
				return response.buildSuccesResponse(null);
			}).orElseThrow(() -> new UserException(environment.getProperty("user.doesn't.exist")));
		}
		return null;
	}

	/**
	 * getAll users.
	 * 
	 * @param Token(Admin)
	 * @param userId
	 * @return List of users.
	 * @throws UserException.
	 */
	@Override
	public List<User> getAll(String token) throws UserException {
		Long adminId = tokenGenerator.decodeToken(token);
		userRepository.findById(adminId).filter(admin -> admin.isUserRole() == true)
				.orElseThrow(() -> new UserException(environment.getProperty("user.role.admin")));
		return userRepository.findAll();
	}

	/**
	 * method for validating user.
	 * 
	 * @param email
	 * @return true if user present,otherwiswe false
	 */
	public boolean validateUser(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (!user.isPresent()) {
			return true;
		}
		return false;
	}

	/**
	 * method for validating admin.
	 * 
	 * @param token
	 * @return true if admin available, otherwise false
	 * @throws UserException
	 */
	public boolean validateAdmin(String token) throws UserException {
		User admin = userRepository.findById(tokenGenerator.decodeToken(token))
				.orElseThrow(() -> new UserException(environment.getProperty("user.role.admin")));
		if (admin.isUserRole() == true) {
			return true;
		}
		return false;
	}
}
