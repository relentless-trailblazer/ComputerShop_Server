package com.computershop.helpers;

import java.security.SecureRandom;

// when call no-args constructor, new password will be generated
public class GenerateRandomPassword {
	private final static Integer shuffleTimes = 20;
	private final static Integer passwordLenght = 10;
	private String newPassword;
	
	public static String generateRandomPassword(int len){
	        // ASCII range – alphanumeric (0-9, a-z, A-Z)
		final String lowerChars = "abcdefghijklmnopqrstuvwxyz";
		final String secialChars = "@$!%*#?&_";
		final String numbers = "0123456789";
		final String upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < len-3; i++)
		{
		    int randomIndex = random.nextInt(lowerChars.length());
		    sb.append(lowerChars.charAt(randomIndex));
		}
		// get a random index of template string
		int randomIdxSpecialChars = random.nextInt(secialChars.length());
		int randomIdxNumbers = random.nextInt(numbers.length());
		int randomIdxUpperChars = random.nextInt(upperChars.length());
		
		// add special char to the end of sb
		sb.append(secialChars.charAt(randomIdxSpecialChars));
		sb.append(numbers.charAt(randomIdxNumbers));
		sb.append(upperChars.charAt(randomIdxUpperChars));
		
		
		System.out.println(sb);
		return sb.toString();
	}
	public static String shuffleString(String str) {
		String str1 = str.substring(0,4);
		String str2 = str.substring(4);
		str = str2+str1;
		int max=str.length()-1;
	    int min=1;
		SecureRandom random = new SecureRandom();
		for(int i = 0; i < shuffleTimes; i++) {
			int randomIndex = random.nextInt(max-min+1)+min;
			str1 = str.substring(0,randomIndex);
			str2 = str.substring(randomIndex);
			str = str2+str1;
		}
		
		return str;
	}
	
	public GenerateRandomPassword() {
		this.newPassword = shuffleString(generateRandomPassword(passwordLenght));
	}
	
	public String getNewPassword() {
		return newPassword;
	}

	
}
