package accenture.networkandroid;


public class Signal {
	
	private String bssid;
	private int signalStrength;
	
	public Signal(String bssid, int signalStrength) {
		
		this.bssid = bssid;
		this.signalStrength = signalStrength;
	}

	public String getBssid() {
		return bssid;
	}

	public int getSignalStrength() {
		return signalStrength;
	}

}
