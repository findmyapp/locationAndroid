package accenture.networkandroid;

public class Room {
	
	private int roomId;
	private String roomName;
	
	public Room(int id, String name) {
		roomId = id;
		roomName = name;
	}
	
	public int getroomId() {
		return roomId;
	}
	
	public String getroomName() {
		return roomName;
	}

}
