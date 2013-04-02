
public class Room 
{
	int id;
	int time;
	int capacity;
	
	public Room(int id, int time, int cap)
	{
		this.id = id;
		this.time = time;
		this.capacity = cap;
	}
	
	@Override 
	public String toString()
	{
		return String.format("Room id:%d, size: %d, time: %d", id, capacity, time);
	}
}
