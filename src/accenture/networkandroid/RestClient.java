package accenture.networkandroid;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class RestClient {

	private String url = "";
	private Activity activity;
	private Gson gson;

	public RestClient(Activity activity) {
		this.activity = activity;
		gson = new Gson();

	}

	public Room sendSignal(String json) {
		return null;
	}

	//Bruk i sendSignal eller slett ?
	private Room retrieveStream(String url) {
		InputStream source;

		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);

		try {

			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w(getClass().getSimpleName(), "Error " + statusCode
						+ " for URL " + url);
				return null;
			}

			HttpEntity getResponseEntity = getResponse.getEntity();

			source = getResponseEntity.getContent();
			Reader reader = new InputStreamReader(source);
			Room room = gson.fromJson(reader, Room.class);
			Toast.makeText(activity,
					room.getroomName() + " : " + room.getroomId(),
					Toast.LENGTH_SHORT).show();
			return room;

		} catch (IOException e) {
			getRequest.abort();
			Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
		}

		return null;
	}
	
	public Room deserializeRoom(String jsonRoom) {
		Room room = gson.fromJson(jsonRoom, Room.class);
		return room;
	}

}
