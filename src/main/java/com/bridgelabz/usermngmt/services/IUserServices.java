package com.bridgelabz.usermngmt.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.usermngmt.config.Response;
import com.bridgelabz.usermngmt.dto.LoginDto;
import com.bridgelabz.usermngmt.dto.UserDto;
import com.bridgelabz.usermngmt.exception.UserException;
import com.bridgelabz.usermngmt.model.User;

/**
 * Interface for user services implemented in class {@link UserServicesImp}
 * 
 * @since september-2019
 * @author Prince Singh
 * @version 1.0
 *
 */
@Service
public interface IUserServices {

	Response register(UserDto userDto) throws UserException;

	Response login(LoginDto loginDto) throws UserException;

	Response update(UserDto userDto, String adminToken, Long userId, boolean role) throws UserException;

	Response delete(Long userId, String token) throws UserException;

	List<User> getAll(String token) throws UserException;

	Response uploadImage(MultipartFile image, String token) throws UserException;

}
