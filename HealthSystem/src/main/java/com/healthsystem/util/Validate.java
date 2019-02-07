package com.healthsystem.util;

public class Validate {

	
	public static boolean validateNotNull(String ...fields){
		
		boolean status = true;
		
		for(String field : fields){
			if(field == null){
				status = false;
				break;
			}
			if(field.trim().equals("")){
				status = false;
				break;
			}
		}
		
		
		return status;
	}
}
