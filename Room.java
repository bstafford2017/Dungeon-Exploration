
public class Room {

	private boolean player;
	private int roomNo;
	private Monster hostMon;
	Room nextRoom;
	Room prevRoom;
	
	public Room(boolean player, int roomNo, Monster hostMon) {
		this.player = player;
		this.roomNo = roomNo;
		this.hostMon = hostMon;
		this.nextRoom = null;
		this.prevRoom = null;
	}
	
	public Room(boolean player, int roomNo, Monster hostMon, Room nextRoom, Room prevRoom) {
		this.player = player;
		this.roomNo = roomNo;
		this.hostMon = hostMon;
		this.nextRoom = nextRoom;
		this.prevRoom = prevRoom;
	}
	
	public boolean isPlayer() {
		return player;
	}
	
	public void setPlayer(boolean bool) {
		this.player = bool; 
	}
	
	public void setRoomNo(int number) {
		this.roomNo = number;
	}

	public int getRoomNo() {
		return roomNo;
	}
	
	public Monster getHostMon() {
		return hostMon;
	}
	
	public Room getNextRoom() {
		return nextRoom;
	}
	
	public Room getPrevRoom() {
		return prevRoom;
	}
}
