package com.computershop.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.computershop.dao.Product;
import com.computershop.dto.SignUpDTO;
import com.computershop.dto.UserDTO;
//import com.computershop.dto.UserDTO;
import com.computershop.exceptions.InvalidException;

public class Validate {
	private static final String regexUsername = "^[a-zA-Z0-9]+([._]?[a-zA-Z0-9]+)*$";
	private static final String regexPassword = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&_]{8,}$";
	private static final String regexEmail = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
	private static final String regexPhone = "^[0-9\\-\\+]{9,15}$";

	static Pattern pattern;
	static Matcher matcher;

	public static Boolean isUser(UserDTO userDTO) {
		pattern = Pattern.compile(regexUsername);
		matcher = pattern.matcher(userDTO.getUsername());
		if (!matcher.find()) {
			throw new InvalidException("Invalid username");
		}

		pattern = Pattern.compile(regexPassword);
		matcher = pattern.matcher(userDTO.getPassword());
		if (!matcher.find()) {
			throw new InvalidException("Invalid password");
		}

		pattern = Pattern.compile(regexEmail);
		matcher = pattern.matcher(userDTO.getEmail());
		if (!matcher.find()) {
			throw new InvalidException("Invalid email");
		}

		pattern = Pattern.compile(regexPhone);
		matcher = pattern.matcher(userDTO.getPhone());
		if (!matcher.find()) {
			throw new InvalidException("Invalid phone");
		}

		return true;
	}

	public static Boolean isEmail(String email) {
		pattern = Pattern.compile(regexEmail);
		matcher = pattern.matcher(email);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	public static Boolean isPhone(String phone) {
		pattern = Pattern.compile(regexPhone);
		matcher = pattern.matcher(phone);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	public static Boolean checkSignUp(SignUpDTO signUpDTO) {
		if (signUpDTO.getFirstName() == null || signUpDTO.getFirstName().trim() == "") {
			throw new InvalidException("Invalid firstname");
		}
		if (signUpDTO.getLastName() == null || signUpDTO.getLastName().trim() == "") {
			throw new InvalidException("Invalid lastname");
		}
		if (signUpDTO.getAddress() == null || signUpDTO.getAddress().trim() == "") {
			throw new InvalidException("Invalid address");
		}
		pattern = Pattern.compile(regexUsername);
		matcher = pattern.matcher(signUpDTO.getUsername());
		if (!matcher.find()) {
			throw new InvalidException("Invalid username");
		}

		pattern = Pattern.compile(regexPassword);
		matcher = pattern.matcher(signUpDTO.getPassword());
		if (!matcher.find()) {
			throw new InvalidException("Invalid password");
		}

		pattern = Pattern.compile(regexEmail);
		matcher = pattern.matcher(signUpDTO.getEmail());
		if (!matcher.find()) {
			throw new InvalidException("Invalid email");
		}

		pattern = Pattern.compile(regexPhone);
		matcher = pattern.matcher(signUpDTO.getPhone());
		if (!matcher.find()) {
			throw new InvalidException("Invalid phone");
		}
		return true;
	}
	
	public static void checkProduct(Product product) {
        if (product.getName().trim().compareTo("") == 0) {
            throw new InvalidException("Invalid name");
        }
        if (product.getDescription().trim().compareTo("") == 0) {
            throw new InvalidException("Invalid description");
        }
        if (product.getBrand().trim().compareTo("") == 0) {
            throw new InvalidException("Invalid brand");
        }
        if (product.getPrice() < 0) {
            throw new InvalidException("Invalid price");
        }
        if (product.getAmount() < 0) {
            throw new InvalidException("Invalid amount");
        }
        
    }

}
