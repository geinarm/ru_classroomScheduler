import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Scheduler {
	/*
	 * int numClass = 600; int numStudent = 20000; int numRoom = 300;
	 */
	int numTime = 8;
	int collisions = 0;
	School S = new School();

	int[][] overlapGraph;
	boolean[][] proposals;
	Class[][][] schedule;
	ArrayList<Timeslot> timeslots = new ArrayList<Timeslot>();
	LinkedList<Class> unassigned = new LinkedList<Class>();

	public Scheduler() throws IOException {
		S = S.importSchool();
		overlapGraph = new int[S.classes.size()][S.classes.size()];
		schedule = new Class[S.rooms.size()][numTime][5];
	}

	public void init() {
		for (int i = 0; i < S.classes.size(); i++) {
			for (int k = 0; k < S.classes.size(); k++) {
				overlapGraph[i][k] = 0;
			}
		}
		
		//Create a timeslot for all times of day for every day for every room
		for(int r=0; r < S.rooms.size(); r++)
		{
			for(int t=0; t < numTime; t++)
			{
				for(int d=0; d < 5; d++)
				{
					timeslots.add(new Timeslot(d, t, S.rooms.get(r)));
				}
			}
		}
		
		proposals = new boolean[S.classes.size()][timeslots.size()];

		getClasses();
		getRegistrations();
		printOverlapGraph();

	}

	public void solve() {
		System.out.println("Solve this shit");
		long timestamp = System.currentTimeMillis();

		while (!unassigned.isEmpty()) {
			//System.out.println(unassigned.size());
			Class c = unassigned.getFirst();
			unassigned.remove(c);
			Timeslot t = getRoom(c);

			if (schedule[t.room.getRoominputindex()][t.time][t.day] == null) {
				// System.out.println("put it here");
				schedule[t.room.getRoominputindex()][t.time][t.day] = c;
			} else {
				Class c2 = schedule[t.room.getRoominputindex()][t.time][t.day];
				// System.out.println("Compare to class" + c2.id);

				if (eval(c, t) > eval(c2, t)) {
					//System.out.println("Swap");
					unassigned.addLast(c2);
					schedule[t.room.getRoominputindex()][t.time][t.day] = c;
				} else {
					unassigned.addLast(c);
			//		System.out.println("Denied");
				}
			}
		}

		//System.out.println("Stuff left: " + unassigned.size());
		printSchedule();
		System.out.println("Final evaluation: " + finalEvaluation());
		System.out.println("Solved in " + (System.currentTimeMillis()-timestamp)/1000.0 + " Seconds");
	}

	public Timeslot getRoom(Class c) {
		int best = -10000;
		int index = -1;

		for (int i = 0; i < timeslots.size(); i++) {
			Timeslot t = timeslots.get(i);
			
			if (proposals[c.getInputId()][i])
				continue;

			int score = eval(c, t);
			if (score > best) {
				best = score;
				index = i;
			}
		}

		if (index == -1) {
			System.out.println("No more rooms");
			printSchedule();
			System.exit(0);
		}

		proposals[c.getInputId()][index] = true;
		return timeslots.get(index);
	}

	public int quickEval(Class c, Timeslot t) {
		int score = 0;

		score -= Math.abs(c.students.size() - t.room.capacity);

		return score;
	}

	public int eval(Class c, Timeslot t) {
		int score = 0;

		if (t.room.capacity < c.students.size())
			score -= 100;

		score -= Math.abs(c.students.size() - t.room.capacity);

		//Check teacher conflicts
		for (int i = 0; i < S.rooms.size(); i++) {
			if (t.room.roominputindex == i)
				continue;
			if (schedule[i][t.time][t.day] != null) {
				Class c2 = schedule[i][t.time][t.day];
				for(int teacherId : c.teachers){
					if(c2.teachers.contains(teacherId))
						return -10000;
				}
			}
		}
		
		//Check student conflicts
		for (int i = 0; i < S.rooms.size(); i++) {
			if (t.room.roominputindex == i)
				continue;
			if (schedule[i][t.time][t.day] != null) {
				Class c2 = schedule[i][t.time][t.day];
				score -= overlapGraph[c.inputId][c2.inputId];
			}
		}
		
		return score;
	}

	private void printSchedule() {
		String times[] = { "08:30", "09:20", "10:20", "11:10", "12:20", "13:10",
				"14:00", "14:55" };
		String days[] = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
		
		for(int d=0; d < 5; d++){
			System.out.println("\n" + days[d] + ":");
			System.out.println("---------------------------------");
			
			for(int r=0; r < S.rooms.size(); r++){
				Room room = S.rooms.get(r);
				System.out.println(room.name + ":");
				
				for(int t=0; t < numTime; t++){
					Class c = schedule[r][t][d];
					System.out.print(times[t] + " : ");
					if(c != null)
						System.out.print(c.name + "\n");
					else
						System.out.print("N/A\n");
					
				}
			}
		}
		
	}

	// Data already imported//
	/*
	 * public void getStudents() { for(int i=0; i < numStudent; i++){
	 * students.add(new Student("student " + i, i)); } }
	 */
	public void getClasses() {
		for (int i = 0; i < S.classes.size(); i++) {
			unassigned.addLast(S.classes.get(i));
		}
	}

	/*
	 * * public void getRooms() { for(int i=0; i < numRoom; i++){ for(int t=0; t
	 * < numTime; t++){ rooms.add(new Room(i, t, (i+1) * 10)); } } }
	 */

	public void getRegistrations() {
		for (int i = 0; i < S.classes.size(); i++) {
			for (int k = 0; k < S.classes.size(); k++) {
				Class c = S.classes.get(k);
				for (int j = 0; j < c.students.size(); j++) {
					if (S.classes.get(i).students.contains(c.students.get(j))) {
						overlapGraph[i][k]++;
						if (i != k)
							overlapGraph[k][i]++;
					}
				}
			}
		}

	}
	
	private int finalEvaluation()
	{
		int collisions = 0;
		
		for(int d=0; d < 5; d++){
			for(int t=0; t < numTime; t++){
				for(int r=0; r < S.rooms.size(); r++){
					if(schedule[r][t][d] != null){
						Class c1 = schedule[r][t][d];
						for(int c=0; c < S.rooms.size(); c++){
							if(schedule[c][t][d] != null && c != r){
								Class c2 = schedule[c][t][d];
								collisions += overlapGraph[c1.inputId][c2.inputId];
							}
						}					
					}
				}
			}
		}
		
		return collisions;
	}

	void printOverlapGraph() {
		System.out.print("  ");
		for (int i = 0; i < S.classes.size(); i++) {
			System.out.print(i + "| ");
		}
		System.out.print("\n");

		for (int x = 0; x < S.classes.size(); x++) {
			System.out.print(x + " ");

			for (int y = 0; y < S.classes.size(); y++) {
				System.out.print(overlapGraph[x][y] + " ");
			}
			System.out.print("\n");
		}
	}
}
