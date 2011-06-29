package accenture.networkandroid;

import java.util.List;

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

		public void onStatusChanged(String provider, int status, Bundle extras) {}
		public void onProviderEnabled(String provider) {}
		public void onProviderDisabled(String provider) {}
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
	
	//Scans for Wifi BSSIDs / Signal Strength
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
	

}
