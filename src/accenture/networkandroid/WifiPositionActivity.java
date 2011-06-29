package accenture.networkandroid;

import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Activity for position. Needs a position handler to have any use
 * 
 * @author audun.sorheim, cecilie haugstvedt, kristin astebol
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
		scanList = null;

		// Init GUI
		ssidTextView = (TextView) findViewById(R.id.bssid);
		populatePositionDataButton = (Button) findViewById(R.id.populateLocationButton);
		getCurrentRoomButton = (Button) findViewById(R.id.getRoomButton);

		// Init Positionhandler and scan for ssid
		wifiPositionHandler = new WifiPositionHandler(this);
		wifiPositionHandler.scanForSSID();

		// Sets listener on buttons
		getCurrentRoomButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Room currentRoom = wifiPositionHandler.getPosition();
				if (currentRoom != null) {
					printToScreen(currentRoom.getroomName() + " : "
							+ currentRoom.getroomId());
				} else {
					printToScreen("No room found");
				}
			}
		});
		populatePositionDataButton
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Log.e("KNAPPTEST", "Tester populate knapp");
						updateBSSID();
					}
				});
	}

//	/**
//	 * Gets latitude and longitude, and sets the non existing textviews.
//	 */
//	public void updateLocation() {
//		if (wifiPositionHandler.getCurrentLocation() == null) {
//			Log.e("Location", "Location is null");
//		} else {
//			currentLocation = wifiPositionHandler.getCurrentLocation();
//			Log.e("NetworkAndroidActivity",
//					"Latitude: " + currentLocation.getLatitude()
//							+ " Longitude: " + currentLocation.getLongitude());
//
//			longitudeTextView.setText("" + currentLocation.getLongitude());
//			latitudeTextView.setText("" + currentLocation.getLatitude());
//		}
//	}

	/**
	 * Initializes a SSID search in the handler
	 * Update the (GUI) BSSID (MAC-address for the router)
	 */
	public void updateBSSID() {
		if (wifiPositionHandler.getScanList() == null) {
			Log.e("SCANRESULTS", "There are no scan results");
			wifiPositionHandler.scanForSSID();
		} else {
			scanList = wifiPositionHandler.getScanList();
			Log.e("SCANRESULTS", "Oh yeah");

			scanResult = scanList.get(0);

			Log.e("NetworkAndroidActivity", scanResult.BSSID + " "
					+ scanResult.level);
			ssidTextView.setText(scanResult.BSSID);
		}
		
	}

	
	
	public void printToScreen(String text) {
			Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		}
//	/**
//	 * Deserialize a jsonSignal (string) to readable data in LogCat
//	 * 
//	 * @param jsonSignal
//	 */
//	public void readFromJSON(String jsonSignal) {
//		Gson gson = new Gson();
//		Signal[] signal = gson.fromJson(jsonSignal, Signal[].class);
//		for (int i = 0; i < signal.length; i++) {
//			Log.e("From JSON ",
//					signal[i].getBssid() + " " + signal[i].getSignalStrength());
//		}
//	}

//	/**
//	 * Tests writeListToJSON and readFromJSON
//	 */
//	public void testToFromJSON() {
//		String json = writeListToJSON(scanList);
//		readFromJSON(json);
//		Log.e("STRINGTEST", json);
//	}

	
}