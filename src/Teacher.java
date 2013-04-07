public class Teacher {
	String name;
	int teacherId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public Teacher(String name, int teacherId) {
		this.name = name;
		this.teacherId = teacherId;
	}

}
