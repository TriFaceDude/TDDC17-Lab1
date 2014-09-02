package tddc17;

import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class TileMap {

	private static final Hashtable<Tile.Type, String> TILE_SYMBOL_MAP = new Hashtable<Tile.Type, String>(){{
		put(Tile.Type.Unkown, "?");
		put(Tile.Type.Wall, "#");
		put(Tile.Type.Clear, "--");
		put(Tile.Type.Dirt, "D");
	}};
	
	Point size;
	Tile[][] map;
	
	public TileMap(Point size){
		this(size.getX(), size.getY());
	}
	
	public TileMap(int width, int height){
		
		size = new Point(width, height);
		map = new Tile[height][width];
		init(map);
	}
	
	public void extendSizeTo(int width, int height){
		
		Tile[][] newMap = new Tile[height][width];
		init(newMap);
		transferTiles(this.map, newMap);
		
		this.size = new Point(width, height);
		this.map = newMap;
	}
	
	LinkedList<Tile> openList = new LinkedList<Tile>();
	LinkedList<Tile> closedList = new LinkedList<Tile>();
	
	public void clearPathLists(){
		openList.clear();
		closedList.clear();
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map[y].length; x++){
				getTile(x, y).setParent(null);
				getTile(x, y).setMovmentCost(0);
				getTile(x, y).setRepulsionCost(0);
			}
		}
	}

	public Stack<Tile> getPath(Point originPosition, Point destinationPosition, int mode){
		
		Stack<Tile> result = new Stack<Tile>();
		clearPathLists();
		
		if(!Point.inBounds(destinationPosition, Point.Zero(), size)){
			
			result.push(getTile(originPosition));
			return result;		
		}
			
		
		Tile tile = pathfind(originPosition, destinationPosition, mode);
		
		while(tile != null){
			result.push(tile);
			tile = tile.getParent();	
		}
		
		result.pop();
		return result;
	}
	
	private Tile pathfind(Point originPosition, Point destinationPosition, int mode){
		
		Iterator<Tile> iter;
		
		Tile origin = getTile(originPosition);
		Tile destination = getTile(destinationPosition);

		closedList.add(origin);
		
		LinkedList<Tile> adjacentTiles = getTiles(Point.getAdjacentPoints(originPosition, Point.Zero(), this.size));
		iter = adjacentTiles.iterator();
		
		while(iter.hasNext()){
			Tile tile = iter.next();
			
			if(mode == Tile.TO_DEST){					// If we are looking for a specific tile
				if(tile == destination){				// If current tile is adjacent to destination;
					destination.setParent(origin);		// Set destination parent to current tile and return destination tile.
					return destination;
				}
			} else{
				
				if(tile.getType() == Tile.Type.Unkown){	// We are looking for any unknown tile
					
					tile.setParent(origin);				// Set tile parent to current tile and return it
					return tile;
				}
			}

			
			if(tile.getType() == Tile.Type.Wall){	// Remove if tile is a wall or in closedList
				
				iter.remove();
			}
			else if(closedList.contains(tile) && (tile.getCostThrough(origin, destination, mode, 0) > tile.getTotalCost())){				
	
				iter.remove();	
			}
			else{
				
				tile.calcCost(origin, destination, mode, 0); // Calculates cost for tile and sets origin to parent
			}		
		}				
		
		openList.addAll(adjacentTiles);				// Adds adjacent tiles to openList
		
		if(openList.isEmpty())						// If openList is empty no path could be found, return current tile
			return origin;
					
		sortByCost(openList);						// Sort openList in ascending cost order
		Tile cheapest = openList.getFirst();
		openList.removeFirst();						// Remove first tile from openList
		
		return pathfind(cheapest.getPosition(), destination.getPosition(), mode);
	}
	
	public Tile getPathExplore(Point origin, Point home){
		
		return new Tile(Point.Zero());
	}
	
	public void setTileType(Point position, Tile.Type type){
		
		getTile(position).setType(type);
	}
	
	public void print(){
		
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map[y].length; x++){
				System.out.print(TILE_SYMBOL_MAP.get(map[y][x].getType()));	
			}
			System.out.println("");
		}
	}
	
	public Tile getTile(int x, int y) {
		return map[x][x];
	}
	
	public Tile getTile(Point position){
		return getTile(position.getX(), position.getY());
	}
	
	public LinkedList<Tile> getTiles(List<Point> positionList){
		
		LinkedList<Tile> result = new LinkedList<Tile>();
		
		for(Point position : positionList)
			result.add(getTile(position));
			
		return result;
	}
	
	
	private static void sortByCost(LinkedList<Tile> tiles){
		
		Collections.sort(tiles, new Comparator<Tile>() {
			@Override
			public int compare(Tile a, Tile b){
				return a.getTotalCost() - b.getTotalCost();
			}
		});
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
