package accenture.networkandroid;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity for position. Needs a position handler to have any use
 * @author audun.sorheim, cecilie haugstvedt
 *
 */

public class WifiPositionActivity extends Activity {

	private Button populatePositionDataButton, getCurrentRoomButton;
	private TextView longitudeTextView, latitudeTextView, ssidTextView;
	private ScanResult scanResult;
	private Location currentLocation = null;
	private WifiPositionHandler wifiPositionHandler;
	private List<ScanResult> scanList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Init GUI
		longitudeTextView = (TextView) findViewById(R.id.editText1);
		latitudeTextView = (TextView) findViewById(R.id.editText2);
		ssidTextView = (TextView) findViewById(R.id.editText3);
		populatePositionDataButton = (Button) findViewById(R.id.button1);
		getCurrentRoomButton = (Button) findViewById(R.id.button2);

		// Init Positionhandler and scan for ssid
		wifiPositionHandler = new WifiPositionHandler(this);
		wifiPositionHandler.scanForSSID();

		// Sets listener on buttons
		getCurrentRoomButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getPosition();
			}
		});
		populatePositionDataButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				updateLocation();
				updateBSSID();
			}
		});
	}

	// Updates Latitude and Longitude. Not used
	public void updateLocation() {
		if (wifiPositionHandler.getCurrentLocation() == null) {
			Log.e("Location", "Location is null");
		} else {
			currentLocation = wifiPositionHandler.getCurrentLocation();
			Log.e("NetworkAndroidActivity",
					"Latitude: " + currentLocation.getLatitude()
							+ " Longitude: " + currentLocation.getLongitude());
			
			longitudeTextView.setText("" + currentLocation.getLongitude());
			latitudeTextView.setText("" + currentLocation.getLatitude());
		}
	}

	/**
	 * Update the 
	 */
	public void updateBSSID() {
		wifiPositionHandler.scanForSSID();
		if (wifiPositionHandler.getScanList() == null) {
			Log.e("SCANRESULTS", "There are no scan results");
		} else {
			scanList = wifiPositionHandler.getScanList();
			Log.e("SCANRESULTS", "Oh yeah");

			scanResult = scanList.get(0);
			int bestResult = scanList.get(0).level, currentResult = scanList
					.get(0).level;
			for (ScanResult result : scanList) {
				currentResult = result.level;
				if (currentResult > bestResult) {
					bestResult = currentResult;
					scanResult = result;
				}
				Log.e("NetworkAndroidActivity", result.BSSID + " "
						+ result.level);
			}
			ssidTextView.setText("Level: " + scanResult.level + "BSSID: "
					+ scanResult.BSSID);
		}
	}

	/**
	 * Converts a list of scanResults to a json string
	 * @param results The list of ScanResult
	 * @return json-"string"
	 */
	public String writeListToJSON(List<ScanResult> results) {
		Gson gson = new Gson();
		if (scanList == null) {
			updateBSSID();
		}
			Signal[] protocolArray = new Signal[scanList.size()];
			int counter = 0;
			for (ScanResult result : scanList) {
				Signal protocol = new Signal(result.BSSID, result.level);
				protocolArray[counter] = protocol;
				counter++;
			}
			String json = gson.toJson(protocolArray);
			return json;
	}
	
	/**
	 * Finds the current Room you are in
	 * @return
	 */
	public Room getPosition() {
		Room currentRoom = null;
		if (scanList != null) {
		RestClient restClient = new RestClient(this);
		String json = writeListToJSON(scanList);
		currentRoom = restClient.sendSignal(json);
		}
		if (currentRoom != null) {
			Toast.makeText(this,
					currentRoom.getroomName() + " : " + currentRoom.getroomId(),
					Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(this,
					"FAIL !",
					Toast.LENGTH_SHORT).show();
		}
		return currentRoom;
		
	}
	
	/**
	 * Deserialize a jsonSignal (string) to readable data in LogCat
	 * @param jsonSignal 
	 */
	public void readFromJSON(String jsonSignal) {
		Gson gson = new Gson();
		Signal[] signal = gson.fromJson(jsonSignal, Signal[].class);
		for(int i=0; i<signal.length; i++){
			Log.e("From JSON ", signal[i].getBssid()+" "+signal[i].getSignalStrength());
		}
	}

	/**
	 * Tests writeListToJSON and readFromJSON
	 */
	public void testToFromJSON() {	
		String json = writeListToJSON(scanList);
		readFromJSON(json);		
		Log.e("STRINGTEST", json);
	}
}