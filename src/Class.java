import java.util.ArrayList;

public class Class {
	String name;
	int id;
	int length;
	int numberaweek; // Dummy value always 4;
	int studentCount;
	ArrayList<Integer> students = new ArrayList<Integer>();
	ArrayList<Integer> teachers = new ArrayList<Integer>();
	int inputId;
	int day;
	char wantroomtype;
	

	public int getInputId() {
		return inputId;
	}

	public void setInputId(int inputId) {
		this.inputId = inputId;
	}

	public Class(String name, int id, int numberaweek, int studentCount, char wantroomtype) {
		this.name = name;
		this.id = id;
		this.numberaweek = numberaweek;
		this.studentCount = studentCount;
		this.wantroomtype = wantroomtype;
	}

	public String getName() {
		return name;
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

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getNumberaweek() {
		return numberaweek;
	}

	public void setNumberaweek(int numberaweek) {
		this.numberaweek = numberaweek;
	}

	public int getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(int studentCount) {
		this.studentCount = studentCount;
	}

	public ArrayList<Integer> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<Integer> students) {
		this.students = students;
	}

	public Class(int id, int len) {
		students = new ArrayList<Integer>();
		this.id = id;
		this.length = len;
	}

	@Override
	public String toString() {
		return String.format("Class id: %d, students: %d", id, students.size());
	}
}
