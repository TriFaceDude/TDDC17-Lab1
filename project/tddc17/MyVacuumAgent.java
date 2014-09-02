package tddc17;


import java.util.Hashtable;

import aima.core.environment.liuvacuum.*;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.*;


class MyAgentState
{
	public int[][] world = new int[20][20];
	public int initialized = 0;
	final int UNKNOWN 	= 0;
	final int WALL 		= 1;
	final int CLEAR 	= 2;
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
	
	private static final int RIGHT = 0;
	private static final int UP = 1;
	private static final int LEFT = 2;
	private static final int DOWN = 3;
	
	// Here you can define your variables!
	public int iterationCounter = 20;
	public MyAgentState state = new MyAgentState();
	
	private TileMap map = new TileMap(2, 2);
	private Point position = Point.Zero();
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
		
		Point orthoCC = new Point(-this.position.getY(), -this.position.getX());
		
		if(direction == orthoCC){
			
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
	
	private void updateMinMax(){
		
		switch(this.currentDirection){
			case RIGHT:
				maxFound.setX(Math.max(maxFound.getX(), position.getX()));
				break;
			case UP:
				minFound.setY(Math.min(minFound.getY(), position.getY()));
				break;
			case LEFT:
				minFound.setX(Math.min(minFound.getX(), position.getX()));
				break;
			case DOWN:
				maxFound.setY(Math.max(maxFound.getY(), position.getY()));
				break;			
		}
	}
	
	
	@Override
	public Action execute(Percept percept) {
		
		// This example agent program will update the internal agent state while only moving forward.
		// Note! It works under the assumption that the agent starts facing to the right.

	    iterationCounter--;
	    
	    if (iterationCounter==0)
	    	return NoOpAction.NO_OP;

	    DynamicPercept p = (DynamicPercept) percept;
	    Boolean bump = (Boolean)p.getAttribute("bump");
	    Boolean dirt = (Boolean)p.getAttribute("dirt");
	    Boolean home = (Boolean)p.getAttribute("home");
	    System.out.println("percept: " + p);
	    
	    map.print();
	    map.extendSizeTo(2, 4);
	    System.out.println("Pos: " + position.toString());
	    System.out.println("Dir: " + Integer.toString(currentDirection) + " " + DIR_MAP.get(currentDirection));
	    System.out.println("Min: " + minFound.toString());
	    System.out.println("Max: " + maxFound.toString());
	    
	    // State update based on the percept value and the last action
	    if (state.agent_last_action==state.ACTION_MOVE_FORWARD)
	    {
	    	if (!bump)
	    	{
	    		move(DIR_MAP.get(currentDirection));
	    	} else
	    	{
	    		updateMinMax();
	    		state.updateWorld(state.agent_x_position+1,state.agent_y_position,state.WALL);
	    	}
	    }
	    if (dirt)
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.DIRT);
	    else
	    	state.updateWorld(state.agent_x_position,state.agent_y_position,state.CLEAR);
	    
	    //state.printWorldDebug();
	    
	    
	    // Next action selection based on the percept value
	    if (dirt)
	    {
	    	System.out.println("DIRT -> choosing SUCK action!");
	    	state.agent_last_action=state.ACTION_SUCK;
	    	return LIUVacuumEnvironment.ACTION_SUCK;
	    } 
	    else
	    {
	    	if (bump)
	    	{
	    		return turnTowards(Point.add(this.position, DIR_MAP.get(DOWN)));
	    	}
	    	else
	    	{
	    		state.agent_last_action=state.ACTION_MOVE_FORWARD;
	    		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	    	}
	    }
	}
}

public class MyVacuumAgent extends AbstractAgent {
    public MyVacuumAgent() {
    	super(new MyAgentProgram());
	}
}
