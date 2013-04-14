import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;


public class Solver {
	public static int classCount;
	public static int timeslotCount;
	public static int numTime = 8;
	
	int[][] overlapGraph;
	int[][] scoreGraph;
	PriorityQueue<SearchNode> frontier;
	ArrayList<Timeslot> timeslots;
	School s;
	Random rand = new Random(System.currentTimeMillis());
	
	public Solver()
	{
		frontier = new PriorityQueue<SearchNode>();
		timeslots = new ArrayList<Timeslot>();
		s = new School();
	}
	
	public void init()
	{
		try {
			s = s.importSchool();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Create a timeslot for all times of day for every day for every room
		for(int r=0; r < s.rooms.size(); r++)
		{
			for(int t=0; t < numTime; t++)
			{
				for(int d=0; d < 5; d++)
				{
					timeslots.add(new Timeslot(d, t, s.rooms.get(r)));
				}
			}
		}
		
		classCount = s.classes.size();
		timeslotCount = timeslots.size();
		
		overlapGraph = new int[classCount][classCount];
		scoreGraph = new int[classCount][timeslotCount];
		
		getRegistrations();
		
		//Static evaluation
		for(int c=0; c < s.classes.size(); c++){
			for(int t=0; t < timeslots.size(); t++){
				scoreGraph[c][t] = eval(s.classes.get(c), timeslots.get(t));
			}
		}
	}
	
	public void solve()
	{		
		long timestamp = System.currentTimeMillis();
		
		//Create root node
		SearchNode root = new SearchNode(null, 0);
		frontier.add(root);
		
		SearchNode bestNode = null;
		int foundLeafs = 0;
		
		//Run the search
		while(!frontier.isEmpty())
		{
			//Get current node
			SearchNode node = frontier.peek();
			
			//System.out.println("Node: " + node.c + "-" + node.t + " score: " + node.score); // + " c: " + frontier.size()
			
			//Expand current node
			SearchNode next = expandNode(node);
			if(next == null){
				System.out.println("Cutoff");
				frontier.remove(node);
				continue; // Cutoff
			}
			
			//Evaluate the next node
			int conflict = 0;
			Timeslot timeslot = timeslots.get(next.t);
			for(int i=0; i < classCount; i++){
				if(next.c == i)
					continue;
				
				//For every assigned class in this node check if its in conflict
				int tIndex = next.assignments[i] -1;
				if(tIndex != -1){
					Timeslot t = timeslots.get(tIndex);
					if(t.day == timeslot.day && t.time == timeslot.time){
						conflict += overlapGraph[next.c][i];
					}
				}
			}
			
			next.score -= conflict;
			next.score += node.depth;
			
			//is this a goal
			if(next.depth == classCount){
				System.out.println("Found a leaf " + next.score);
				
				if (bestNode == null || next.score > bestNode.score) {
					bestNode = next;
					System.out.println("It's SO much better now.");
					
					Class[][][] schedule = getSchedule(bestNode);
					System.out.println("Best score: " + bestNode.score + " - Final evaluation: " + finalEvaluation(schedule));
					System.out.println("Solved in " + (System.currentTimeMillis()-timestamp)/1000.0 + " Seconds");
				}
				
//				if (++foundLeafs >= 100)
//					break;
			}
			
			// Milliseconds
			if ((System.currentTimeMillis()-timestamp) > 500000) {
				System.out.println("Time limit reached.");
				break;
			}
				
			//System.out.println("Next: " + next.c + "-" + next.t + "  Score: " + next.score + "  Depth: " + next.depth);
			//break;
			
			if(next.score > 0)
				frontier.add(next);
		}
		
		Class[][][] schedule = getSchedule(bestNode);
		
		System.out.println("Best score: " + bestNode.score + " - Final evaluation: " + finalEvaluation(schedule));
		printSchedule(schedule);
		
		System.out.println("Done");
		System.out.println("Solved in " + (System.currentTimeMillis()-timestamp)/1000.0 + " Seconds");
	}
	
	Class[][][] getSchedule(SearchNode node) {
		Class[][][] schedule = new Class[s.rooms.size()][numTime][5];
		for (int i = 0; i < node.assignments.length; i++) {
			Timeslot timeslot = timeslots.get(node.assignments[i] - 1);
			schedule[timeslot.room.roominputindex][timeslot.time][timeslot.day] = s.classes.get(i);
		}
		return schedule;
	}
	
	public SearchNode expandNode(SearchNode node)
	{
		node.score -= 1;
		
		int bestScore = -10000;
		int bestC= -1;
		int bestT= -1;
		
		for(int c=0; c < classCount; c++){
			if(node.assignments[c] != 0)
				continue;
			
			for(int t=0; t < timeslotCount; t++){
				//if(node.expansions[c][t])
				//	continue;
				if(node.ass.contains(new Assignment(c, t)))
					continue;
				if(node.timeTaken(t))
					continue;
				
				int score = scoreGraph[c][t];
				if(score > bestScore){
					bestScore = score;
					bestC = c;
					bestT = t;
				}
			}
		}
		
		if(bestC == -1)
			return null;
		
		SearchNode newNode = new SearchNode(node, bestScore +1);
		newNode.c = bestC;
		newNode.t = bestT;
		newNode.assignments[bestC] = bestT+1;
		//node.expansions[bestC][bestT] = true;
		node.ass.add(new Assignment(bestC, bestT));		
		return newNode;
	}
	
	public int eval(Class c, Timeslot t) {
		
		//return -(rand.nextInt() % 100);
		
		int score = 0;
		
		if (t.room.capacity < c.students.size())
			score -= 10;

		score -= Math.abs(c.students.size() - t.room.capacity) / 10;
		
		if(c.wantroomtype == t.room.type)
			score += 1;
		else
			score -= 10;
		
		/*if(c.numberaweek == 2 && c.day == t.day)
			score -= 1000;
		if (t.room.capacity < c.students.size())
			score -= 100;
		
		if(Math.abs(c.students.size() - t.room.capacity) > 40)
			score -= Math.abs(c.students.size() - t.room.capacity);
		else
			score += Math.abs(c.students.size() - t.room.capacity);
		
		 */
		return score;
	}
	

	public void getRegistrations() {
		for (int i = 0; i < s.classes.size(); i++) {
			for (int k = 0; k < s.classes.size(); k++) {
				Class c = s.classes.get(k);
				
				//Check students
				for (int j = 0; j < c.students.size(); j++) {
					if (s.classes.get(i).students.contains(c.students.get(j))) {
						overlapGraph[i][k]++;
						if (i != k)
							overlapGraph[k][i]++;
					}
				}
				
				//Check teachers
				for (int j = 0; j < c.teachers.size(); j++) {
					if (s.classes.get(i).teachers.contains(c.teachers.get(j))) {
						overlapGraph[i][k] += 10000;
						if (i != k)
							overlapGraph[k][i] += 10000;
					}
				}
			}
		}

	}

	private void printSchedule(Class schedule[][][]) {
		String content ="";
		String times[] = { "08:30", "09:20", "10:20", "11:10", "12:20", "13:10",
				"14:00", "14:55" };
		String days[] = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
		
		for(int d=0; d < 5; d++){
			content = content + "\n" + days[d] + ":"+"\n";
			content = content +"---------------------------------\n";
			
			for(int r=0; r < s.rooms.size(); r++){
				Room room = s.rooms.get(r);
				content = content + (room.name + ":");
				
				for(int t=0; t < numTime; t++){
					Class c = schedule[r][t][d];
					content = content + (times[t] + " : ");
					if(c != null)
						content = content + (c.name + "\n");
					else
						content = content +("N/A\n");
					
				}
			}
		}
		try{
			File file = new File("output.txt");
			 
			// if file does not exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int finalEvaluation(Class schedule[][][])
	{
		int collisions = 0;
		
		for(int d=0; d < 5; d++){
			for(int t=0; t < numTime; t++){
				for(int r=0; r < s.rooms.size(); r++){
					if(schedule[r][t][d] != null){
						Class c1 = schedule[r][t][d];
						for(int c=0; c < s.rooms.size(); c++){
							if(schedule[c][t][d] != null && c != r){
								Class c2 = schedule[c][t][d];
								collisions += overlapGraph[c1.inputId][c2.inputId];
							}
						}					
					}
				}
			}
		}
		collisions = collisions/4;
		return collisions;
	}
}
