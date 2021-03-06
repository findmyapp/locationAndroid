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

	private accenture.networkandroid.Location currentLocation;
	private Activity activity;
	private List<ScanResult> scanList;
	private RestClient restClient;

	public WifiPositionHandler(Activity a) {
		this.activity = a;
		scanList = null;
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
		restClient = new RestClient();
	}

	public List<ScanResult> getScanList() {
		return scanList;
	}

	public accenture.networkandroid.Location getCurrentLocation() {
		return currentLocation;
	}

	/**
	 * Finds the current Room you are in
	 * Used for testing on phone until server si up and running
	 * @return
	 */
	public accenture.networkandroid.Location getLocation() {
		accenture.networkandroid.Location dummy = new accenture.networkandroid.Location(3, "Dummy Rom");
		if (scanList == null) {
			scanForSSID();
		}
		if (scanList != null) {
			String json = writeListToJSON(scanList);
		}
		return dummy;
	}

	public accenture.networkandroid.Location getPositionFromServer() {
		accenture.networkandroid.Location location = null;
		if (scanList == null) {
			scanForSSID();
		}
		if (scanList != null) {
			String json = writeListToJSON(scanList);
			location = restClient.getRoom(json);
		}

		return location;
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
