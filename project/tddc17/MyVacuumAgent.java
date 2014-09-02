package tddc17;


import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Stack;

import aima.core.environment.liuvacuum.*;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.*;


class MyAgentState
{
	public int[][] world = new int[20][20];
	public int initialized = 0;
	static int UNKNOWN 	= 0;
	static int WALL 	= 1;
	static int CLEAR 	= 2;
	final int DIRT		= 3;
	final int ACTION_NONE 		= 0;
	final int ACTION_MOVE_FORWARD 	= 1;
	final int ACTION_TURN_RIGHT 	= 2;
	final int ACTION_TURN_LEFT 		= 3;
	final int ACTION_SUCK	 		= 4;
	
	public int agent_x_position = 1;
	public int agent_y_position = 1;
	public int agent_last_action = ACTION_NONE;
	
	MyAgentState()
	{
		for (int i=0; i < world.length; i++)
			for (int j=0; j < world[i].length ; j++)
				world[i][j] = UNKNOWN;
		world[1][1] = CLEAR;
		agent_last_action = ACTION_NONE;
	}
	
	public void updateWorld(int x_position, int y_position, int info)
	{
		world[x_position][y_position] = info;
	}
	
	public void printWorldDebug()
	{
		for (int i=0; i < world.length; i++)
		{
			for (int j=0; j < world[i].length ; j++)
			{
				if (world[j][i]==UNKNOWN)
					System.out.print(" ? ");
				if (world[j][i]==WALL)
					System.out.print(" # ");
				if (world[j][i]==CLEAR)
					System.out.print(" -- ");
				if (world[j][i]==DIRT)
					System.out.print(" D ");
			}
			System.out.println("");
		}
	}
}

class MyAgentProgram implements AgentProgram {

	private static final Hashtable<Integer, Point> DIR_MAP = new Hashtable<Integer, Point>(){{
		put(RIGHT, new Point(1, 0));
		put(UP, new Point(0, -1));
		put(LEFT, new Point(-1, 0));
		put(DOWN, new Point(0, 1));
	}};
	
	private static final Hashtable<Tile.Type, Integer> TILE_TYPE_MAP = new Hashtable<Tile.Type, Integer>(){{
		put(Tile.Type.Unkown, 0);
		put(Tile.Type.Wall, 1);
		put(Tile.Type.Clear, 2);
		put(Tile.Type.Dirt, 3);
	}};

	boolean updatePath = true;
	Stack<Point> pointsOfInterest = new Stack<Point>(); 			//huhehe 
	Stack<Tile> path = new Stack<Tile>();
	
	private static final int RIGHT = 0;
	private static final int UP = 1;
	private static final int LEFT = 2;
	private static final int DOWN = 3;
	
	// Here you can define your variables!
	public int iterationCounter = 20;
	public MyAgentState state = new MyAgentState();
	
	private TileMap map = new TileMap(20, 20);
	private Point position = new Point(1, 1);
	private int currentDirection = RIGHT;
	private int lastTurnAction = state.ACTION_NONE;
	
	private Point maxFound = Point.Zero();
	private Point minFound = Point.Zero();

	private void move(Point moveValue){
		
		this.position.add(moveValue);
		
		state.agent_x_position += moveValue.getX();
		state.agent_y_position += moveValue.getY();
	}
	
	private Action turnTowards(Point direction){
		
		Point orthoCC = new Point(-DIR_MAP.get(currentDirection).getY(), -DIR_MAP.get(currentDirection).getX());
		
		if(direction.equals(orthoCC)){
			
			this.currentDirection = (this.currentDirection + 1) % 4;
			this.lastTurnAction = state.ACTION_TURN_LEFT;
			state.agent_last_action=state.ACTION_TURN_LEFT;
			return LIUVacuumEnvironment.ACTION_TURN_LEFT;
		}
		else{
			
			this.currentDirection = (this.currentDirection + 3) % 4;
			this.lastTurnAction = state.ACTION_TURN_RIGHT;
			state.agent_last_action=state.ACTION_TURN_RIGHT;
			return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
		}
	}
	
	private Action exploreRoom(){
		
		if(!path.isEmpty() && position.equals(path.lastElement().getPosition())){
			
			updatePath = true;
			
			if(!pointsOfInterest.isEmpty())
				pointsOfInterest.pop();
		}
		
		while(!pointsOfInterest.isEmpty() && updatePath){
				
			path = map.getPath(position, pointsOfInterest.peek(), Tile.TO_DEST);
			
			if(path.lastElement().getPosition().equals(pointsOfInterest.peek()))
				updatePath = false;
		}
		
		while(updatePath){
			path = map.getPath(position, new Point(1, 1), Tile.FROM_DEST);
			
			if(path.lastElement().getType() == Tile.Type.Unkown)
				updatePath = false;
		}
		
		if(isFacing(path.peek().getPosition())){
			
			state.agent_last_action = state.ACTION_MOVE_FORWARD;
			return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
		}
		else{
			
			return turnTowards(Point.direction(position, path.peek().getPosition()));
		}
	}
	
	private boolean isFacing(Point destination){
		
		return (dirAsPoint().equals(Point.direction(position, destination)));
	}
	
	private Point dirAsPoint(){
		
		return DIR_MAP.get(currentDirection);
	}
	
	
	@Override
	public Action execute(Percept percept) {

	    DynamicPercept p = (DynamicPercept) percept;
	    Boolean bump = (Boolean)p.getAttribute("bump");
	    Boolean dirt = (Boolean)p.getAttribute("dirt");
	    Boolean home = (Boolean)p.getAttribute("home");  

	    System.out.println("Pos: " + position.toString());
	    System.out.println("Dir: " + Integer.toString(currentDirection) + " " + DIR_MAP.get(currentDirection));
	    
		if(dirt)
			return cleanCurrentTile();
	    
	    if (state.agent_last_action==state.ACTION_MOVE_FORWARD)
	    {
	    	if (!bump)
	    	{
	    		move(DIR_MAP.get(currentDirection));
	    		
	    	} else
	    	{
	    		updatePath = true;
	    		pointsOfInterest.push(Point.add(position, Point.mul(dirAsPoint(), 2)));
	    		updateWorld(Point.add(this.position, dirAsPoint()), Tile.Type.Wall);
	    	}
	    }
	    
	    updateWorld(position, Tile.Type.Clear);
	    map.print();
	    
	    return exploreRoom();
	}
	
	private Action cleanCurrentTile(){
		
    	updateWorld(position, Tile.Type.Dirt);
    	state.agent_last_action=state.ACTION_SUCK;
    	return LIUVacuumEnvironment.ACTION_SUCK;
	}
	
	private void updateWorld(Point position, Tile.Type type){
		
		state.updateWorld(position.getX(),position.getY(), TILE_TYPE_MAP.get(type));
		map.setTileType(position, type);
	}
}

public class MyVacuumAgent extends AbstractAgent {
    public MyVacuumAgent() {
    	super(new MyAgentProgram());
	}
}
