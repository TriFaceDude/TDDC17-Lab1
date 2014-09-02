package tddc17;

public class Tile {

	public static final int TO_DEST = 1;
	public static final int FROM_DEST = -1;
	
	public enum StateType{
		visited, unvisited, wall
	}
	
	private StateType state;
	private Point position;
	private int repulsionCost;
	private int movmentCost;
	
	private Tile parent;
	
	
	public Tile(Point position) {
		this.position = position;
		this.state = StateType.unvisited;
		this.repulsionCost = 0;
		this.movmentCost = 0;
		this.parent = null;
		
	}
	
	public int calcCost(Tile parent, Tile destination, int pathMode){
		
		 
		
		if (destination.state == StateType.unvisited) {
			movmentCost += 1;
		
		} else if (destination.state == StateType.visited) {
			movmentCost += 2;
		}
		
		 
		
		//avst�nd = Movment
		return movmentCost;
		
	}
	
	public static int distance (Tile a, Tile b){
		return Point.manhattanDistance(a.getPosition(), b.getPosition());
	}

	public StateType getState() {
		return state;
	}

	public void setState(StateType state) {
		this.state = state;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public int repulsionCost() {
		return repulsionCost;
	}

	public void setrepulsionCost(int cost) {
		this.repulsionCost = cost;
	}
	
	public int movmentCost() {
		return movmentCost;
	}

	public void setmovmentCost(int cost) {
		this.repulsionCost = cost;
	}
	
	public int totalCost () {
		return repulsionCost + movmentCost;
	}

	public Tile getParent() {
		return parent;
	}

	public void setParent(Tile parent) {
		this.parent = parent;
	}
	
	
	
	
	

	

}
