package com.healthsystem.util;

import java.util.Random;

public class SecretCodeGenerator {

	private static final String CHARACTERES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%&*";

	public static String generateSecretCode() {
		char first = CHARACTERES.charAt(new Random().nextInt(CHARACTERES.length()));
		char second = CHARACTERES.charAt(new Random().nextInt(CHARACTERES.length()));
		char third = CHARACTERES.charAt(new Random().nextInt(CHARACTERES.length()));
		char fourth = CHARACTERES.charAt(new Random().nextInt(CHARACTERES.length()));
		return new String(new char[] { first, second, third, fourth });
	}
}
