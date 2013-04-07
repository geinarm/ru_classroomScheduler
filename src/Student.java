public class Student {
	String name;
	int id;

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

	public Student(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	@Override public boolean equals(Object s) {
		if (this == s) return true;
		if (!(s instanceof Student)) return false;
		
		Student st = (Student) s;
		boolean ret = (st.id == id);
		return ret;
	}
}
