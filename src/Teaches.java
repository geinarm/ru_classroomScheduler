public class Teaches {
	int courseId;
	int teacherId;

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public Teaches(int courseId, int teacherId) {
		this.courseId = courseId;
		this.teacherId = teacherId;
	}

}
