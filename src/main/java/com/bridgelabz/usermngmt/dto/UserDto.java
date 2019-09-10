package com.bridgelabz.usermngmt.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

	@NotEmpty(message = "Please Enter First Name")
	private String firstName;

	@NotEmpty(message = "Please Enter Middle Name")
	private String middleName;

	@NotEmpty(message = "Please Enter Last Name")
	private String lastName;

	@NotEmpty(message = "Please Enter DOB")
	private String dob;

	@NotEmpty(message = "Please Enter Gender")
	private String gender;

	@NotEmpty(message = "Please Enter Country Name")
	private String country;

	@Size(min = 0, max = 10, message = "Please Enter Phone Number in b/w 1 to 10")
	@NotEmpty(message = "Please Enter Phone Number")
	private String phone;

	@NotEmpty(message = "Please Enter Country Extension")
	private String extension;

	@Pattern(regexp = ("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
	@Email(message = "Please Enter Valid Email")
	@NotEmpty(message = "Please Enter Email")
	private String email;

	@NotEmpty(message = "Please Enter Address")
	private String address;

	@NotEmpty(message = "Please Enter User Name")
	private String username;

	@NotEmpty(message = "Please Enter Password")
	private String password;
	
	@NotEmpty(message = "Please Enter Status")
	private boolean status;

}
