package com.aporter.makemelaugh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.aporter.makemelaugh.R;
import com.google.android.glass.app.Card;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

public class MainActivity extends Activity {
	
	private static final String TAG = "MakeMeLaugh";
	private static final String URL = "http://api.icndb.com/jokes/random";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		String jokeText = "Sad face. We couldn't find a joke :(";
		
		try {
			
			Card card = new Card(this);
			
			String rawJSON = getRandomJoke(URL);
			JSONObject valueObject = new JSONObject(rawJSON).getJSONObject("value");
			jokeText = valueObject.getString("joke");
			
			card.setText(jokeText);
		    card.setFootnote(R.string.text_footer);
			setContentView(card.toView());

		} catch (Exception e) {
			
			Log.e(TAG, e.getMessage());
			System.exit(1);
		}

	}
	
	public static String getRandomJoke(String urlValue) {

		String result = "An error occured.";
		
	    HttpClient httpClient = new DefaultHttpClient();

	    // Prepare a request object
	    HttpGet httpGet = new HttpGet(urlValue); 
	    
	    // Execute the request
	    HttpResponse response;
	    
	    try {
	        response = httpClient.execute(httpGet);
	        
	        // Examine the response status
	        Log.i(TAG,response.getStatusLine().toString());

	        // Get hold of the response entity
	        HttpEntity entity = response.getEntity();
	        
	        // If the response does not enclose an entity, there is no need
	        // to worry about connection release

	        if (entity != null) {

	            // A Simple JSON Response Read
	            InputStream inputStream = entity.getContent();
	            
	            result =  convertStreamToString(inputStream);
	            
	            // now you have the string representation of the HTML request
	            inputStream.close();	            
	        }

	    } catch (Exception e) { 
	    	System.out.println("9");
	    	e.printStackTrace();
	    	}
		return result;
	}

	private static String convertStreamToString(InputStream inputStream) {
	    /*
	     * To convert the InputStream to String we use the BufferedReader.readLine()
	     * method. We iterate until the BufferedReader return null which means
	     * there's no more data to read. Each line will appended to a StringBuilder
	     * and returned as String.
	     */
	    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	    StringBuilder stringBuilder = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	        	stringBuilder.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	        	inputStream.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return stringBuilder.toString();
	}
}
