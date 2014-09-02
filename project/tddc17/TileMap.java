package tddc17;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TileMap {

	Tile[][] map;
	
	public TileMap(Point size){
		this(size.getX(), size.getY());
	}
	
	public TileMap(int width, int height){
		
		map = new Tile[height][width];
		init(map);
	}
	
	public void extendSizeTo(int width, int height){
		
		Tile[][] newMap = new Tile[height][width];
		init(newMap);
		transferTiles(this.map, newMap);
		
		this.map = newMap;
	}
	
	public Tile getPathTo(Point originPosition, Point destinationPosition){
		
		Iterator<Tile> iter;
		
		Tile origin = getTile(originPosition);
		Tile destination = getTile(destinationPosition);
		LinkedList<Tile> openList = new LinkedList<Tile>();
		LinkedList<Tile> closedList = new LinkedList<Tile>();
		
		LinkedList<Tile> adjacentTiles = getTiles(Point.getAdjacentPoints(originPosition));
		iter = adjacentTiles.iterator();
		
		while(iter.hasNext()){
			Tile tile = iter.next();
			
			if(tile == destination){				// Base. If current tile is adjacent to destination;
				destination.setParent(tile);		// Set destination parent to current tile and return destination.
				return destination;
			}
			
			if(tile.getState() == Tile.StateType.X)	// Remove if tile is a wall
				iter.remove();
		}				
		
		openList.addAll(adjacentTiles);				//Adds adjacent tiles to openList
		
		for(Tile tile : openList)					// Calculate cost to all tiles in openlist
			tile.calcCost(origin, destination, Tile.TO_DEST);
			
		
		return new Tile(Point.Zero());
	}
	
	public Tile getPathExplore(Point origin, Point home){
		
		return new Tile(Point.Zero());
	}
	
	public void print(){
		
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map[y].length; x++){
				System.out.print(map[y][x]);	
			}
			System.out.println("");
		}
	}
	
	public Tile getTile(Point position){
		
		return map[position.getY()][position.getX()];
	}
	
	public LinkedList<Tile> getTiles(List<Point> positionList){
		
		LinkedList<Tile> result = new LinkedList<Tile>();
		
		for(Point position : positionList)
			result.add(getTile(position));
			
		return result;
	}
	
	private static void init(Tile[][] map){
		
		for(int y = 0; y < map.length; y++)
			for(int x = 0; x < map[y].length; x++)
				map[y][x] = new Tile(new Point(x, y));
	}
	
	private static void transferTiles(Tile[][] origin, Tile[][] destination){
		
		for(int y = 0; y < origin.length; y++){
			for(int x = 0; x < origin[y].length; x++){
				destination[y][x] = origin[y][x];
			}
		}
	}
}
