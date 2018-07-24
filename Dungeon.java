
public class Dungeon {

	private static Room startRoom = null;
	private static int size = 0;
	
	public static int getSize() {
		return size;
	}
	
	public static Room getStartRoom() {
		return startRoom;
	}

	public static void addFront(boolean player, int roomNo, Monster hostMon) {
		if(startRoom == null) {
			startRoom = new Room(player, roomNo, hostMon);
		} else {
			Room room = new Room(player, roomNo, hostMon, startRoom, null);
			startRoom.prevRoom = room;
			startRoom = room;
		}
		size++;
	}
	
	public static void addBack(boolean player, int roomNo, Monster hostMon) {
		if(startRoom == null) {
			startRoom = new Room(player, roomNo, hostMon);
		} else {
			Room current = startRoom;
			while(current.nextRoom != null) {
				current = current.nextRoom;
			}
			Room newRoom = new Room(player, roomNo, hostMon, null, current);
			current.nextRoom = newRoom;
		}
		size++;
	}
	
	public static void removeFront() {
		boolean player = isPlayerHere(startRoom.getRoomNo());
		if(player) {
			System.out.println("You can NOT delete the room you are in!");
		} else {
			if(startRoom == null) {
				return;
			}
			startRoom = startRoom.nextRoom;
			startRoom.prevRoom = null;
			size--;
		}
	}
	
	public static void removeBack() {
		boolean player = isPlayerHere(size);
		if(player) {
			System.out.println("You can NOT delete the room you are in!");
			return;
		} else {
			if(startRoom == null) {
				return;
			}
			if(startRoom.nextRoom == null) {
				startRoom = null;
				size--;
				return;
			}
			Room current = startRoom;
			while(current.nextRoom.nextRoom != null) {
				current = current.nextRoom;
			}
			current.nextRoom = null;
			size--;
		}
	}
	
	public static void insert(boolean player, int roomNo, Monster hostMon, int index) {
		if(startRoom == null) {
			startRoom = new Room(player, roomNo, hostMon);
		}
		if(index < 1 || index > size) {
			return;
		}
		Room current = startRoom;
		for(int i = 1; i < index; i++) {
			current = current.nextRoom;
		}
		if(current.prevRoom == null) {
			Room newRoom = new Room(player, roomNo, hostMon, current, null);
			current.prevRoom = newRoom;
			startRoom = newRoom;
		} else {
			Room newRoom = new Room(player, roomNo, hostMon, current, current.prevRoom);
			current.prevRoom.nextRoom = newRoom;
			current.prevRoom = newRoom;
		}
		size++;
	}
	
	public static void remove(int index) {
		
		boolean player = isPlayerHere(index);
		if(player) {
			System.out.println("You can NOT delete the room you are in!");
			return;
		} else {
			if (startRoom == null)
				return;
			if(index < 1 || index > size)
				return;
			Room current = startRoom;
			for(int i = 1; i < index; i++) {
				current = current.nextRoom;
			}
			if(current.nextRoom == null) {
					current.prevRoom.nextRoom = null;
			}
			else if(current.prevRoom == null) { 
				current = current.nextRoom;
				current.prevRoom = null;
				startRoom = current;
			}else {
				current.prevRoom.nextRoom = current.nextRoom;
				current.nextRoom.prevRoom = current.prevRoom;
			}
			size--;
		}
	}
	
	public static void print() {
		Room current = startRoom;
		System.out.println("---------ROOM LIST---------");
		System.out.printf("Player Room # Monster\n");
		while(current != null) {
			System.out.printf("%-7b%-7d%-20s\n", current.isPlayer(), current.getRoomNo(), current.getHostMon().getName());
			current = current.nextRoom;
		}
	}
	
	public static void move() {
		Room current = startRoom;
		while(true) {
			if(current.isPlayer()) {
				current.setPlayer(false);
				if(current.nextRoom == null) {
					System.out.println("You have completed Dungeon Exploration!");
					break;
				} else {
					System.out.println("You have completed Room #" + current.getRoomNo());
					current.nextRoom.setPlayer(true);
					break;
				}
			} else {
				current = current.nextRoom;
			}
		}
	}
	
	public static boolean isPlayerHere(int index) {
		Room current = startRoom;
		boolean bool = false;
		while(current.nextRoom != null) {
			if(current.isPlayer()) {
				if(current.getRoomNo() == index) {
					bool = true;
				}
			}
			current = current.nextRoom;
		}
		if(bool == false) {
			return false;
		}
		return bool;
	}
	
	public static void removeAll() {
		startRoom = null;
	}
	
	public static Room getRoom(int index) {
		Room current = startRoom;
		boolean flag = false;
		while(current != null) {
			if(current.getRoomNo() == index) {
				return current;
			}
			current = current.nextRoom;
		}
		if(flag == false) {
			current = null;
		}
		return current;
	}
}
