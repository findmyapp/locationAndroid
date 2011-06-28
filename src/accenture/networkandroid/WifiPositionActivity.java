package accenture.networkandroid;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity for position. Needs a position handler to have any use
 * @author audun.sorheim, cecilie haugstvedt
 *
 */

public class WifiPositionActivity extends Activity {

	private Button getLocationButton, testJSON;
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
		getLocationButton = (Button) findViewById(R.id.button1);
		testJSON = (Button) findViewById(R.id.button2);

		// Init Positionhandler and scan for ssid
		wifiPositionHandler = new WifiPositionHandler(this);
		wifiPositionHandler.scanForSSID();

		// Sets listener on buttons
		testJSON.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				testJSONArray();
			}
		});
		getLocationButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				updateLocation();
				updateBSSID();
			}
		});
	}

	// Updates Latitude and Longitude
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

	// Updates BSSID
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

	// Converts a list of scanresults to jsonArray
	public JSONArray writeListToJSON(List<ScanResult> results) {
		JSONArray jsonArray = new JSONArray();
		for (ScanResult singleResult : results) {
			jsonArray.put(singleResult);
		}
		return jsonArray;
	}

	// tests if jsonarray works
	public void testJSONArray() {
		if (scanList == null) {
			updateBSSID();
		} else {
			JSONArray test = writeListToJSON(scanList);
			for (int i = 0; i < test.length(); i++) {
				try {
					ScanResult scan = (ScanResult) test.get(i);
					Log.e("TESTING JSON ARRAY", scan.BSSID);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}