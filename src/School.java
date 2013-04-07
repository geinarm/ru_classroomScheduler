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
				student.add(s);
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
				if (d.length >= 5) { // Case of ',' in name of course
					String a = d[0].replace("course(", "");
					;
					String b = d[1].replace("\"", "");
					String x = d[2].replace("\"", "");
					String e = d[3];
					String f = d[4].replace(").", "");
					if (Integer.parseInt(f) == 0) {
					} else{
						b = b + x;
					Class c = new Class(b, Integer.parseInt(a),
							Integer.parseInt(e), Integer.parseInt(f));
					c.setInputId(inputclassnumber);
					inputclassnumber++;
					classes.add(c);}
				} else {
					String h = d[0].replace("course(", "");
					String j = d[1].replace(").", "");
					String i = d[2];
					String k = d[3];
					j = j.replace("\"", "");
					k = k.replace(").", "");
					if (Integer.parseInt(k) == 0) {
					} else {
						Class c = new Class(j, Integer.parseInt(h),
								Integer.parseInt(i), Integer.parseInt(k));
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
				String i = d[2].replace("\"", "");
				i = i.replace(").", "");
				for (int time = 0; time < 8; time++) {
					for(int day = 0; day < 5; day++){
					Room r = new Room(Integer.parseInt(h), time,
							Integer.parseInt(j), i, day);
					r.setRoominputindex(roominputid);
					roominputid++;
					room.add(r);}
				}
			}
			if (line.startsWith("teacher") == true) {
				String[] d = line.split(",");
				String h = d[0].replace("teacher(", "");
				String j = d[1].replace(").", "");
				j = j.replace("\"", "");
				Teacher t = new Teacher(j, Integer.parseInt(h));
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
				if (teaches.get(i).getCourseId() == S.classes.get(j).getId())
					S.classes.get(j).teachers
							.add(teaches.get(i).getTeacherId());
			}
		}
		for (int i = 0; i < attendent.size(); i++) {
			for (int j = 0; j < S.classes.size(); j++) {
				if (attendent.get(i).courseId == S.classes.get(j).getId())
					S.classes.get(j).students.add(student.get(i).getId());
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

	School() {
	}
}