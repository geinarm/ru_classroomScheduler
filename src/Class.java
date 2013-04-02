import java.util.ArrayList;


public class Class 
{
	int id;
	int length;
	int studentCount;
	ArrayList<Integer> students;
	
	public Class(int id, int len)
	{
		students = new ArrayList<Integer>();
		this.id = id;
		this.length = len;
	}
	
	@Override 
	public String toString()
	{
		return String.format("Class id: %d, students: %d", id, students.size());
	}
}
