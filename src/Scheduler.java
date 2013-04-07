import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Scheduler {
	int numClass = 600;
	int numStudent = 20000;
	int numRoom = 300;
	int numTime = 20;
	School S = new School();

	int[][] overlapGraph = new int[numClass][numClass];
	boolean[][] proposals = new boolean[numClass][numRoom * numRoom];
	Class[][] schedule = new Class[numRoom][numRoom];
	LinkedList<Class> unassigned = new LinkedList<Class>();

	public Scheduler() throws IOException {
		S = S.importSchool();
	}

	public void init() {
		for (int i = 0; i < numClass; i++) {
			for (int k = 0; k < numClass; k++) {
				overlapGraph[i][k] = 0;
			}
		}
		// Redundant because of import school //
		/*
		 * getStudents(); getClasses(); getRooms();
		 * 
		 * getRegistrations();
		 */
		for (int i = 0; i < S.classes.size(); i++) {
			Class c = S.classes.get(i);
			// System.out.println("class" + c.id+ ":");
			for (int k = 0; k < c.students.size(); k++) {
				Student s = S.students.get(k);
				System.out.println(s.name);
			}
		}

		printOverlapGraph();

	}

	public void solve() {
		System.out.println("Solve this shit");

		while (!unassigned.isEmpty()) {
			Class c = unassigned.getFirst();
			unassigned.remove(c);
			Room r = getRoom(c);

			// System.out.println("Proposal:");
			// System.out.println(c);
			// System.out.println(r);
			proposals[c.id][r.id + (numRoom * r.time)] = true;

			// System.out.println(c);
			// System.out.println(r);

			if (schedule[r.id][r.time] == null) {
				// System.out.println("put it here");
				schedule[r.id][r.time] = c;
			} else {
				Class c2 = schedule[r.id][r.time];
				// System.out.println("Compare to class" + c2.id);

				if (eval(c, r) > eval(c2, r)) {
					// System.out.println("Swap");
					unassigned.addLast(c2);
					schedule[r.id][r.time] = c;
				} else {
					unassigned.addLast(c);
					// System.out.println("Denied");
				}
			}

			// printSchedule();
		}

		printSchedule();
	}

	public Room getRoom(Class c) {
		int best = -10000;
		int index = -1;

		for (int i = 0; i < S.rooms.size(); i++) {
			Room r = S.rooms.get(i);
			if (proposals[c.id][r.id + (numRoom * r.time)])
				continue;

			int score = quickEval(c, r);
			if (score > best) {
				best = score;
				index = i;
			}
		}

		if (index == -1) {
			printSchedule();
			System.exit(0);
		}

		return S.rooms.get(index);
	}

	public int quickEval(Class c, Room r) {
		int score = 0;

		score -= r.time;

		if (r.capacity < c.students.size())
			score -= 10;

		score -= Math.abs(c.students.size() - r.capacity);

		return score;
	}

	public int eval(Class c, Room r) {
		int score = 0;

		score -= r.time;
		if (r.capacity < c.students.size())
			score -= 10;
		score += (c.students.size() - r.capacity);

		for (int i = 0; i < numRoom; i++) {
			if (r.id == i)
				continue;
			if (schedule[i][r.time] != null) {
				Class c2 = schedule[i][r.time];
				score -= overlapGraph[c.id][c2.id];
			}
		}

		// System.out.println("Eval " + score);
		return score;
	}

	private void printSchedule() {
		for (int i = 0; i < numTime; i++) {
			for (int j = 0; j < numRoom; j++) {
				Class dude = schedule[j][i];
				if (dude != null)
					System.out.print(" " + dude.id);
				else
					System.out.print(" _");
			}
			System.out.print("\n");
		}
	}

	// Data already imported//
	/*
	 * public void getStudents() { for(int i=0; i < numStudent; i++){
	 * students.add(new Student("student " + i, i)); } }
	 * 
	 * public void getClasses() { for(int i=0; i < numClass; i++){ Class c = new
	 * Class(i, 1); classes.add(c); unassigned.addLast(c); } }
	 * 
	 * public void getRooms() { for(int i=0; i < numRoom; i++){ for(int t=0; t <
	 * numTime; t++){ rooms.add(new Room(i, t, (i+1) * 10)); } } }
	 * 
	 * public void getRegistrations() { Random r = new Random(183363); for(int
	 * i=0; i < students.size(); i++){ for(int k=0; k < 5; k++){ if(r.nextInt()
	 * % 2 == 0) continue;
	 * 
	 * int classIndex = Math.abs(r.nextInt() % classes.size()); Class c =
	 * classes.get(classIndex);
	 * 
	 * if(!c.students.contains(i)) c.students.add(i); } }
	 * 
	 * 
	 * for(int i=0; i < numClass; i++){ for(int k=0; k < numClass; k++){ Class c
	 * = classes.get(k); for(int j=0; j < c.students.size(); j++){
	 * if(classes.get(i).students.contains(c.students.get(j))){
	 * overlapGraph[i][k] ++; if(i != k) overlapGraph[k][i] ++; } } } }
	 * 
	 * }
	 */
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
