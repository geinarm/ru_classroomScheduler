import java.util.ArrayList;

public class Teacher {
	String name;
	int teacherId;
	ArrayList<Integer> preferedDays;

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
		preferedDays = new ArrayList<Integer>();
	}
	
	@Override public boolean equals(Object t) {
		if (this == t) return true;
		if (!(t instanceof Teacher)) return false;
		
		Teacher teach = (Teacher) t;
		boolean ret = (teach.teacherId == teacherId);
		return ret;
	}

}
