import java.util.ArrayList;

public class SearchNode implements Comparable<SearchNode>{
	int c, t;
	SearchNode parent;
	int depth;
	int score;
	int[] assignments;
	//boolean[][] expansions;
	ArrayList<Assignment> ass;
	
	public SearchNode(SearchNode p, int s)
	{
		ass = new ArrayList<Assignment>();
		parent = p;
		score = s;
		assignments = new int[Solver.classCount];
		
		if(parent != null){
			depth = p.depth +1;
			for(int i=0; i < Solver.classCount; i++){
				assignments[i] = parent.assignments[i];
			}
		}
		
		//expansions = new boolean[Solver.classCount][Solver.timeslotCount];
	}
	
	public boolean timeTaken(int i)
	{
		if(t == i)
			return true;
		
		if(parent == null)
			return false;
		
		return parent.timeTaken(i);
	}

	@Override
	public int compareTo(SearchNode n1) 
	{	
		//Check for greater score
		if(n1.score < this.score)
			return -1;
		   
		if(n1.score > this.score)
			return 1;
		   
		//If score is equal, depth counts
		if(n1.depth < this.depth)
			return -1;
		   
		if(n1.depth > this.depth)
			return 1;
		   
		return 0;
	}
	
	@Override
	public boolean equals(Object o1)
	{
		SearchNode n1 = (SearchNode)o1;
		if(n1.score == this.score && n1.depth == this.depth)
			return true;
		
		return false;
	}


}
