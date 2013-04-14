
public class Assignment {
	public int course;
	public int timeslot;
	
	public Assignment(int c, int t){
		course = c;
		timeslot = t;
	}
	
	@Override
	public boolean equals(Object o1)
	{
		Assignment a1 = (Assignment)o1;
		if(a1.course == this.course && a1.timeslot == this.timeslot)
			return true;
		
		return false;
	}
}
