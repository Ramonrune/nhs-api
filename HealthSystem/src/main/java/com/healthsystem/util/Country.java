package com.healthsystem.util;

public class Country {
	private Country(){
		
	}
	
	public static String getName(String code){
		if(code.equals("BRA")){
			return "Brasil";
		}
		
		if(code.equals("FR")){
			return "Africa do sul";
		}
		
		return "";
	}
}
