package accenture.networkandroid;

import java.util.List;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Position handler that scans and retrieves position related wifi information
 * 
 * @author audun.sorheim, cecilie haugstvedt
 * 
 */

public class WifiPositionHandler {

	private LocationManager locationManager;
	private Location currentLocation;
	private Activity activity;
	private List<ScanResult> scanList;

	public WifiPositionHandler(Activity a) {
		this.activity = a;
		scanList = null;
		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);
		Log.e("LocationManager",
				"LM is not null ?:" + locationManager.equals(null));

		currentLocation = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}

	// Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			// Called when a new location is found by the network location
			// provider.
			makeUseOfNewLocation(location);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}
	};

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void makeUseOfNewLocation(Location location) {
		if (location == null) {
			Log.e("Location", "Location is null");
		} else {
			Log.e("NetworkAndroidActivity",
					"Latitude: " + location.getLatitude() + " Longitude: "
							+ location.getLongitude());
			currentLocation = location;
		}

	}

	// Scans for Wifi BSSIDs / Signal Strength
	public void scanForSSID() {
		IntentFilter i = new IntentFilter();
		i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		activity.registerReceiver(new BroadcastReceiver() {
			public void onReceive(Context c, Intent i) {
				// Code to execute when SCAN_RESULTS_AVAILABLE_ACTION event
				// occurs
				WifiManager w = (WifiManager) c
						.getSystemService(Context.WIFI_SERVICE);
				scanList = w.getScanResults();
			}
		}, i);
		// Now you can call this and it should execute the broadcastReceiver's
		// onReceive()
		WifiManager wm = (WifiManager) activity
				.getSystemService(Context.WIFI_SERVICE);
	}

	public List<ScanResult> getScanList() {
		return scanList;
	}

	public Location getLocation() {
		return currentLocation;
	}

	/**
	 * Finds the current Room you are in
	 * 
	 * @return
	 */
	public Room getPosition() {
		Room dummy = new Room(3, "Dummy Rom");
//		RestClient restClient;

		if (scanList == null) {
			scanForSSID();
		}
		if (scanList != null) {
			String json = writeListToJSON(scanList);
//			restClient = new RestClient();
//			currentRoom = restClient.getRoom(json);

		}
		return dummy;
	}
	
	/**
	 * Converts a list of scanResults to a json string
	 * 
	 * @param results
	 *            The list of ScanResult
	 * @return json-"string"
	 */
	public String writeListToJSON(List<ScanResult> results) {
		Gson gson = new Gson();
		if (scanList == null) {
			scanForSSID();
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

}
