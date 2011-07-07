package accenture.networkandroid;

public class Location {
	
	private int locationId;
	private String locationName;
	
	public Location(int id, String name) {
		locationId = id;
		locationName = name;
	}
	
	public int getlocationId() {
		return locationId;
	}
	
	public String getlocationName() {
		return locationName;
	}

}
