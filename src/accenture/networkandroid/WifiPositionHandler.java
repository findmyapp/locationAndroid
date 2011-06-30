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

	private Location currentLocation;
	private Activity activity;
	private List<ScanResult> scanList;

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
	}

	public List<ScanResult> getScanList() {
		return scanList;
	}

	public Location getLocation() {
		return currentLocation;
	}

	/**
	 * Finds the current Room you are in
	 * Used for testing on phone until server si up and running
	 * @return
	 */
	public Room getPosition() {
		Room dummy = new Room(3, "Dummy Rom");
		if (scanList == null) {
			scanForSSID();
		}
		if (scanList != null) {
			String json = writeListToJSON(scanList);
		}
		return dummy;
	}

	public Room getPositionFromServer() {
		Room room = null;
		RestClient restClient;
		// scan for BSSID will not work on emulator, scanlist always null
		//		if (scanList == null) {
		//			scanForSSID();
		//		}
		//		if (scanList != null) {
		//			String json = writeListToJSON(scanList);
		//			restClient = new RestClient();
		//			room = restClient.getRoom(json);
		//		}
		if (scanList == null){
			restClient = new RestClient();
			room = restClient.getRoom("not used");
		}

		return room;
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
