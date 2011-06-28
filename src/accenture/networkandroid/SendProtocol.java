package accenture.networkandroid;

import java.util.List;

import android.net.wifi.ScanResult;

public class SendProtocol {
	
	private String[] bssid;
	private int[] signalStrength;
	
	public SendProtocol(List<ScanResult> results) {
		int sizeOfList = 0;
		sizeOfList = results.size();
		bssid = new String[sizeOfList];
		signalStrength = new int[sizeOfList];
		
		int counter = 0;
		for (ScanResult result : results) {
			
			bssid[counter]= result.BSSID;
			signalStrength[counter]= result.level;
			counter++;
			
		}
	}

	public String[] getBssid() {
		return bssid;
	}

	public int[] getSignalStrength() {
		return signalStrength;
	}

}
