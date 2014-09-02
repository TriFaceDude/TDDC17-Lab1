package tddc17;

public class Tile {

	public static final int TO_DEST = 1;
	public static final int FROM_DEST = -1;
	public static final int UNVISITED_COST = 1;
	public static final int VISITED_COST = 2;
	public static final int TURN_COST = 1;
	
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
	
	public void calcCost(Tile parent, Tile destination, int pathMode, int turnCount){
		
		if (destination.state == StateType.unvisited) {
			repulsionCost += UNVISITED_COST;
		
		} else if (destination.state == StateType.visited) {
			repulsionCost += VISITED_COST;
		}
		
		setMovmentCost(Point.manhattanDistance(this.position, destination.position)*pathMode);
		
		setRepulsionCost(repulsionCost + (TURN_COST*turnCount) + parent.repulsionCost);
		 
		this.parent = parent;
		
	}
	
	public int getCostFrom(Tile parent, Tile tile,int pathMode, int turnCount){
		
		int tempRepulsionCost = this.repulsionCost;
		
		if (tile.state == StateType.unvisited) {
			tempRepulsionCost = repulsionCost + UNVISITED_COST;
		
		} else if (tile.state == StateType.visited) {
			tempRepulsionCost = repulsionCost + VISITED_COST;
		}
		
		return((Point.manhattanDistance(this.position, tile.position)*pathMode) + (tempRepulsionCost + (TURN_COST*turnCount) + parent.repulsionCost));
	
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

	public int getRepulsionCost() {
		return repulsionCost;
	}

	public void setRepulsionCost(int cost) {
		this.repulsionCost = cost;
	}
	
	public int getMovmentCost() {
		return movmentCost;
	}

	public void setMovmentCost(int cost) {
		this.repulsionCost = cost;
	}
	
	public int getTotalCost () {
		return repulsionCost + movmentCost;
	}

	public Tile getParent() {
		return parent;
	}

	public void setParent(Tile parent) {
		this.parent = parent;
	}
	
	
	
	
	

	

}
