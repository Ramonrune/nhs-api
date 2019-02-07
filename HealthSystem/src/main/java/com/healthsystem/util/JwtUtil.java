package com.healthsystem.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

public class JwtUtil {

	public static String getUserId(String JWTEncoded) {
		try {
			String[] split = JWTEncoded.split("\\.");
			JSONParser parser = new JSONParser();
			JSONObject json = null;
			try {
				json = (JSONObject) parser.parse(getJson(split[1]));
			} catch (ParseException e) {
				e.printStackTrace();

				return "";
				// TODO Auto-generated catch block
			}

			return (String) json.get("sub");
		} catch (Exception e) {
			return "";
		}
	}

	private static String getJson(String strEncoded) {
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(strEncoded);
			return new String(decodedBytes, "UTF-8");

		} catch (Exception e) {

		}
		return strEncoded;
	}

}