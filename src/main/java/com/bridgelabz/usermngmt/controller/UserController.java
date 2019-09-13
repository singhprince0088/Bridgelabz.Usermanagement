package com.bridgelabz.usermngmt.controller;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.usermngmt.config.Response;
import com.bridgelabz.usermngmt.dto.LoginDto;
import com.bridgelabz.usermngmt.dto.UserDto;
import com.bridgelabz.usermngmt.exception.UserException;
import com.bridgelabz.usermngmt.model.User;
import com.bridgelabz.usermngmt.services.IUserServices;

/**
 * Controller class for user APIs.
 * 
 * @since september-2019
 * @author Prince Singh
 * @version 1.0
 *
 */
@RequestMapping(value = "/user")
@RestController
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private IUserServices userServices;

	/**
	 * Register User API.
	 * 
	 * @param userDto
	 * @return SuccesResponse
	 * @throws UserException
	 */
	@PostMapping
	public ResponseEntity<Response> register(@RequestBody UserDto userDto) throws UserException {
		logger.info("creating new user - {}", userDto.getEmail());
		return new ResponseEntity<>(userServices.register(userDto), HttpStatus.OK);
	}

	/**
	 * Upload Image.
	 * 
	 * @param token
	 * @param image
	 * @return SuccesResponse
	 * @throws UserException
	 */
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Response> uploadImage(@RequestHeader String token, @RequestParam MultipartFile image)
			throws UserException {
		logger.info("uploading profile pic");
		return new ResponseEntity<>(userServices.uploadImage(image, token), HttpStatus.OK);
	}

	/**
	 * API for Login.
	 * 
	 * @param loginDto
	 * @return SuccesResponse
	 * @throws UserException
	 */
	@PutMapping("/login")
	public ResponseEntity<Response> login(@RequestBody LoginDto loginDto) throws UserException {
		return new ResponseEntity<>(userServices.login(loginDto), HttpStatus.OK);
	}

	/**
	 * API for update user in database.
	 * 
	 * @param adminToken
	 * @param userDto
	 * @param userId
	 * @return SuccesResponse
	 * @throws UserException
	 */
	@PutMapping("/update/{role}")
	public ResponseEntity<Response> update(@RequestParam String adminToken, @RequestBody UserDto userDto,
			@RequestHeader Long userId, @PathVariable boolean role) throws UserException {
		return new ResponseEntity<>(userServices.update(userDto, adminToken, userId, role), HttpStatus.OK);
	}

	/**
	 * API for Delete User.
	 * 
	 * @param userId
	 * @param token
	 * @return SuccesResponse
	 * @throws UserException
	 */
	@DeleteMapping("/delete")
	public ResponseEntity<Response> delete(@PathVariable Long userId, @RequestHeader String token)
			throws UserException {
		return new ResponseEntity<>(userServices.delete(userId, token), HttpStatus.OK);
	}

	/**
	 * Get All users.
	 * 
	 * @param token
	 * @return List of users.
	 * @throws UserException
	 */
	@GetMapping
	public List<User> getAll(@RequestHeader String token) throws UserException {
		return userServices.getAll(token);
	}

}
