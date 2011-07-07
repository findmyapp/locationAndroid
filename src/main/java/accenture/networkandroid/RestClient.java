package accenture.networkandroid;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class RestClient {

	//private String url = "http://10.0.2.2:8080/findmyapp/position/"; // localhost on emulator
	private String url = "http://findmyapp.net/findmyapp/location";
	private Gson gson;

	public RestClient() {
		Log.e(getClass().getSimpleName(), "RestClient created");
	}

	private InputStream retrieveStream(String json) {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);

		StringEntity entity;
		try {
			entity = new StringEntity(json);
			request.setEntity(entity);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
		} catch (UnsupportedEncodingException e) {
			Log.e(getClass().getSimpleName(), "Could not create entity from string: " + json, e);
			e.printStackTrace();
		}

		try {
			
			HttpResponse getResponse = client.execute(request);
			final int statusCode = getResponse.getStatusLine().getStatusCode();
			Log.e(getClass().getSimpleName(), "Got back status code:" + statusCode);
			if (statusCode != HttpStatus.SC_OK) { 
				Log.e(getClass().getSimpleName(), 
						"Error " + statusCode + " for URL " + url); 
				return null;
			}

			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();

		} 
		catch (IOException e) {
			request.abort();
			Log.e(getClass().getSimpleName(), "Error for URL " + url, e);
		}

		return null;

	}

	public Location getRoom(String json){
		
		Location room = null;
		//json = getSampleData(); //for testing
		InputStream source = retrieveStream(json);

		Gson gson = new Gson();
		if (source != null){
			Log.e(getClass().getSimpleName(), "source is not null :) ");
			Reader reader = new InputStreamReader(source);
			
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(reader);
			room = gson.fromJson(jsonElement.getAsJsonObject().get("room"), Location.class);
			
			if(room == null) {
				Log.e(getClass().getSimpleName(), "Cannot parse json to room :(");
			}
			
		} else {
			Log.e(getClass().getSimpleName(), "source is null :(");
		}

		return room;
	}
	
	public Location deserializeRoom(String jsonRoom) {
		Location room = gson.fromJson(jsonRoom, Location.class);
		return room;
	}
	
	private String getSampleData(){
		Gson gson = new Gson();
		Signal[] protocolArray = new Signal[2];
		protocolArray[0] = new Signal("Strossa",-80);
		protocolArray[1] = new Signal("Storsalen",-20);
		String json = gson.toJson(protocolArray);
		return json;
	}

}
