package com.healthsystem.util;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.nimbusds.oauth2.sdk.Response;

public class Translate {

	private Translate() {

	}


	public static List<String> translate(String expression) {
		
		List<String> list = new ArrayList<>();
		Client newClient = ClientBuilder.newClient();
		Invocation invoke = newClient
				.target("https://translation.googleapis.com/language/translate/v2?key=")
				.request(MediaType.APPLICATION_JSON).buildPost(Entity.json(expression));
		javax.ws.rs.core.Response response = invoke.invoke();

		System.out.println("aquuiii " + response.getStatus());
		if (response.getStatus() == 200) {
			String jsonStr = response.readEntity(String.class);
			System.out.println(jsonStr);
			JSONObject jsonObject = new JSONObject(jsonStr);
			JSONObject dataObject = jsonObject.getJSONObject("data");
			
			JSONArray jsonArray = dataObject.getJSONArray("translations");
			
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject item = (JSONObject) jsonArray.get(i);
				list.add(item.getString("translatedText"));
			}

		}
		
		return list;

	}

	
}
