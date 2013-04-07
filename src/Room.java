public class Room {
	int id;
	int time;
	int capacity;
	String name;
	int roominputindex;
	

	public int getRoominputindex() {
		return roominputindex;
	}

	public void setRoominputindex(int roominputindex) {
		this.roominputindex = roominputindex;
	}

	public String getName() {
		return name;
	}

	public Room(int id, int capacity, String name) {
		super();
		this.id = id;
		this.capacity = capacity;
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Room(int id, int time, int cap) {
		this.id = id;
		this.time = time;
		this.capacity = cap;
	}

	@Override
	public String toString() {
		return String.format("Room id:%d, size: %d, time: %d", id, capacity,
				time);
	}
}
