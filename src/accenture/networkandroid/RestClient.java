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

import android.net.wifi.ScanResult;
import android.util.Log;

import com.google.gson.Gson;

public class RestClient {

	private String url = "http://localhost:8080/findmyapp/position/";
	private Gson gson;

	public RestClient() {
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
			Log.w(getClass().getSimpleName(), "Could not create entity from string: " + json, e);
			e.printStackTrace();
		}

		try {

			HttpResponse getResponse = client.execute(request);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) { 
				Log.w(getClass().getSimpleName(), 
						"Error " + statusCode + " for URL " + url); 
				return null;
			}

			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();

		} 
		catch (IOException e) {
			request.abort();
			Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
		}

		return null;

	}

	public Room getRoom(String json){
		json = getSampleData(); //for testing
		InputStream source = retrieveStream(json);

		Gson gson = new Gson();

		Reader reader = new InputStreamReader(source);
		Room room = gson.fromJson(reader, Room.class);

		return room;
	}
	
	public Room deserializeRoom(String jsonRoom) {
		Room room = gson.fromJson(jsonRoom, Room.class);
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
