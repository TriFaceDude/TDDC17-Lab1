package tddc17;

public class Tile {

	public static final int TO_DEST = 1;
	public static final int FROM_DEST = -1;
	
	public enum StateType{
		V, U, X, G, H
	}
	
	private StateType state;
	private Point position;
	private int cost;
	private Tile parent;
	
	public Tile(Point position) {
		this.position = position;
		this.state = StateType.U;
		this.cost = 0;
		this.parent = null;
		
	}
	
	public void calcCost(Tile parent, Tile destination, int pathMode){
		
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

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public Tile getParent() {
		return parent;
	}

	public void setParent(Tile parent) {
		this.parent = parent;
	}
	
	
	
	
	

	

}
