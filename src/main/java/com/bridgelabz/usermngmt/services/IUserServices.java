package com.bridgelabz.usermngmt.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.usermngmt.config.Response;
import com.bridgelabz.usermngmt.dto.LoginDto;
import com.bridgelabz.usermngmt.dto.UserDto;
import com.bridgelabz.usermngmt.exception.UserException;

@Service
public interface IUserServices {

	Response register(UserDto userDto, MultipartFile image) throws UserException;

	Response login(LoginDto loginDto) throws UserException;

	Response update(UserDto userDto, String token) throws UserException;

	Response delete(Long userId, String token);

	Response getAll();

	Response getStatus(String token);

}
