package com.grame.faust;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.JsonReader;

public class ui{
	String JSONparameters = new String();
	
	/*
	 * Simple function that turns an InputStream into a String
	 */
	public static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;

	    while ((line = reader.readLine()) != null) {
	        sb.append(line);
	    }

	    is.close();

	    return sb.toString();
	}
	
	public String getJSONui(InputStream in) throws IOException {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		String theUI = null;
		
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			System.out.println("voila:" + name);
			if (name.equals("ui")) {
				theUI = reader.nextString();
			}
		}
		return theUI;
	}
	
	/*
	 * Read the JSON text file in /assets and store its values
	 * in JSONparameters.
	 */
	public void getJSONParameters(Context c){
		Context myContext = c;
        AssetManager am = myContext.getAssets();
        int length = 0;
        String out = "";
        try {
			InputStream is = am.open("params.json");
			//JSONparameters = getJSONui(is);
			//System.out.println("voila:" + JSONparameters);
			
			JSONparameters = convertStreamToString(is);
			JSONObject jsonObject = new JSONObject(JSONparameters);
			JSONArray jsonArray = jsonObject.getJSONArray("ui");
			JSONObject jData = jsonArray.getJSONObject(0);
			out = jData.getString("type");
			length = jsonArray.length();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
        System.out.println("voila: " + out);
        //System.out.println("voila:" + JSONparameters);
	}
}