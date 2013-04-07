import java.io.IOException;
import java.util.LinkedList;

public class Scheduler {
	/*
	 * int numClass = 600; int numStudent = 20000; int numRoom = 300;
	 */
	int numTime = 8;
	School S = new School();

	int[][] overlapGraph;
	boolean[][] proposals;
	Class[][] schedule;
	LinkedList<Class> unassigned = new LinkedList<Class>();

	// int[][] overlapGraph = new int[numClass][numClass];
	// boolean[][] proposals = new boolean[numClass][numRoom * numRoom];
	// Class[][] schedule = new Class[numRoom][numRoom];

	public Scheduler() throws IOException {
		S = S.importSchool();
		overlapGraph = new int[S.classes.size()][S.classes.size()];
		proposals = new boolean[S.classes.size()][S.rooms.size()
				* S.rooms.size()];
		schedule = new Class[S.rooms.size()][numTime];
	}

	public void init() {
		for (int i = 0; i < S.classes.size(); i++) {
			for (int k = 0; k < S.classes.size(); k++) {
				overlapGraph[i][k] = 0;
			}
		}
		// Redundant because of import school //
		/*
		 * getStudents(); getClasses(); getRooms();
		 * 
		 * getRegistrations();
		 */
		/*
		 * for (int i = 0; i < S.classes.size(); i++) { Class c =
		 * S.classes.get(i); // System.out.println("class" + c.id+ ":"); for
		 * (int k = 0; k < c.students.size(); k++) { Student s =
		 * S.students.get(k); System.out.println(s.name); } }
		 */
		getClasses();
		getRegistrations();
		printOverlapGraph();

	}

	public void solve() {
		System.out.println("Solve this shit");

		while (!unassigned.isEmpty()) {
			System.out.println(unassigned.size());
			Class c = unassigned.getFirst();
			unassigned.remove(c);
			Room r = getRoom(c);

			// System.out.println("Proposal:");
			// System.out.println(c);
			// System.out.println(r);
			proposals[c.getInputId()][r.roominputindex] = true;

			// System.out.println(c);
			// System.out.println(r);

			if (schedule[r.getRoominputindex()][r.time] == null) {
				// System.out.println("put it here");
				schedule[r.getRoominputindex()][r.time] = c;
			} else {
				Class c2 = schedule[r.getRoominputindex()][r.time];
				// System.out.println("Compare to class" + c2.id);

				if (eval(c, r) > eval(c2, r)) {
					 System.out.println("Swap");
					unassigned.addLast(c2);
					schedule[r.getRoominputindex()][r.time] = c;
				} else {
					unassigned.addLast(c);
				 System.out.println("Denied");
				}
			}
		}

		System.out.println("Stuff left: " + unassigned.size());
		printSchedule();
	}

	public Room getRoom(Class c) {
		int best = -10000;
		int index = -1;

		for (int i = 0; i < S.rooms.size(); i++) {
			Room r = S.rooms.get(i);
			if (proposals[c.getInputId()][r.getRoominputindex()])
				continue;

			int score = quickEval(c, r);
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

		for (int i = 0; i < S.rooms.size(); i++) {
			if (r.id == i)
				continue;
			if (schedule[i][r.time] != null) {
				Class c2 = schedule[i][r.time];
				score -= overlapGraph[c.inputId][c2.inputId];
			}
		}

		// System.out.println("Eval " + score);
		return score;
	}

	private void printSchedule() {
		for (int i = 0; i < numTime; i++) {
			for (int j = 0; j < S.rooms.size(); j++) {
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
