import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class School {
	ArrayList<Student> students;
	ArrayList<Class> classes;
	ArrayList<Room> rooms;
	ArrayList<Teacher> teachers;
	int inputclassnumber = 0;
	int roominputid = 0;

	public School importSchool() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(
				"myschool_data.txt"));
		String line;
		ArrayList<Student> student = new ArrayList<Student>();
		ArrayList<Class> classes = new ArrayList<Class>();
		ArrayList<Room> room = new ArrayList<Room>();
		ArrayList<Attends> attendent = new ArrayList<Attends>();
		ArrayList<Teaches> teaches = new ArrayList<Teaches>();
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("student") == true) {
				String[] d = line.split(",");
				String h = d[0].replace("student(", "");
				String j = d[1].replace(").", "");
				j = j.replace("\"", "");
				Student s = new Student(j, Integer.parseInt(h));
				if (!student.contains(s))
					student.add(s);
		//		else
		//			System.out.println("Skipped student");
			}
			if (line.startsWith("attends") == true) {
				String[] d = line.split(",");
				String h = d[0].replace("attends(", "");
				String j = d[1].replace(").", "");
				j = j.replace("\"", "");
				Attends a = new Attends(Integer.parseInt(j),
						Integer.parseInt(h));
				attendent.add(a);
			}
			if (line.startsWith("course") == true) {
				String[] d = line.split(",");
				if (d.length >= 6) { // Case of ',' in name of course
					String a = d[0].replace("course(", "");
					;
					String b = d[1].replace("\"", "");
					String x = d[2].replace("\"", "");
					String e = d[3];
					String f = d[4];
					String g = d[5].replace(").", "");
					if (Integer.parseInt(f) == 0) {

					} else {
						b = b + x;
						Class c = new Class(b, Integer.parseInt(a),
								Integer.parseInt(e), Integer.parseInt(f),g.charAt(0));
						c.setInputId(inputclassnumber);
						inputclassnumber++;
						classes.add(c);
					}
				} else {
					String h = d[0].replace("course(", "");
					String j = d[1].replace(").", "");
					String i = d[2];
					String k = d[3];
					String p = d[4].replace(").", "");
					j = j.replace("\"", "");
					if (Integer.parseInt(k) == 0) {
					} else {
						Class c = new Class(j, Integer.parseInt(h),
								Integer.parseInt(i), Integer.parseInt(k), p.charAt(0));
						c.setInputId(inputclassnumber);
						inputclassnumber++;
						classes.add(c);
					}
				}

			}
			if (line.startsWith("room") == true) {
				String[] d = line.split(",");
				String h = d[0].replace("room(", "");
				String j = d[1].replace(").", "");
				String i = d[2];
				String x = d[3].replace("\"", "");
				i = i.replace(").", "");
				
				//Create a new room
				Room r = new Room(Integer.parseInt(h), 0, Integer.parseInt(j), i, 0,x.charAt(0));
				r.setRoominputindex(roominputid);
				roominputid++;
				room.add(r);
				
				/*One room is enough
				 * for (int time = 0; time < 8; time++) {
					for (int day = 0; day < 5; day++) {
						Room r = new Room(Integer.parseInt(h), time,
								Integer.parseInt(j), i, day);
						r.setRoominputindex(roominputid);
						roominputid++;
						room.add(r);
					}
				}*/
			}
			if (line.startsWith("teacher") == true) {
				String[] d = line.split(",");
				String h = d[0].replace("teacher(", "");
				String j = d[1];//.replace(").", "");
				String daysString = d[2].replace(").", "");
				String[] daysArr = daysString.split("-");
					
				j = j.replace("\"", "");
				Teacher t = new Teacher(j, Integer.parseInt(h));
				
				for(String day : daysArr){
					if(!day.isEmpty())
						t.preferedDays.add(Integer.parseInt(day));
				}
				
				if(!teachers.contains(t))
					teachers.add(t);
			}
			if (line.startsWith("teaches")) {
				String[] d = line.split(",");
				String h = d[0].replace("teaches(", "");
				String j = d[1].replace(").", "");
				j = j.replace("\"", "");
				Teaches t = new Teaches(Integer.parseInt(j),
						Integer.parseInt(h));
				teaches.add(t);
			}
		}
		reader.close();
		School S = new School(student, classes, room, teachers);
		for (int i = 0; i < teaches.size(); i++) {
			for (int j = 0; j < S.classes.size(); j++) {
				if (teaches.get(i).getCourseId() == S.classes.get(j).getId()){
					Teacher teacher = null;
					int id = teaches.get(i).getTeacherId();
					for(Teacher t : teachers){
						if(t.getTeacherId() == id){
							teacher = t;
							break;
						}
					}
					if(teacher != null)
						S.classes.get(j).teachers.add(teacher);
				}
			}
		}
		for (int i = 0; i < attendent.size(); i++) {
			Attends attend = attendent.get(i);
			for (int j = 0; j < S.classes.size(); j++) {
				Class klass = S.classes.get(j);
				if (attend.courseId == klass.getId()) {
					klass.students.add(attend.studentId);
					break;
				}
			}
		}
		return S;
	}

	public School(ArrayList<Student> students, ArrayList<Class> classes,
	ArrayList<Room> rooms, ArrayList<Teacher> teachers) {
		this.students = students;
		this.classes = classes;
		this.rooms = rooms;
		this.teachers = teachers;
	}

	public Class getClassById(int id) {
		for (Class s : classes) {
			if (s.id == id)
				return s;
		}
		return null;
	}

	School() {
	}
	
}