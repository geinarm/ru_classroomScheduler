public class Attends {
	int courseId;
	int studentId;

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public Attends(int courseId, int studentId) {
		this.courseId = courseId;
		this.studentId = studentId;
	}

}
