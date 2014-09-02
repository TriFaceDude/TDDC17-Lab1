package tddc17;

import java.util.Collections;
import java.util.Comparator;
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
				destination.setParent(tile);		// Set destination parent to current tile and return destination tile.
				return destination;
			}
			
			if(tile.getState() == Tile.StateType.wall)	// Remove if tile is a wall or in closedList
				iter.remove();
			
			tile.calcCost(origin, destination, Tile.TO_DEST);
			
			if(closedList.contains(tile)){
				
				return origin;
			}

		}				
		
		openList.addAll(adjacentTiles);				//Adds adjacent tiles to openList
		
		if(openList.isEmpty())						// If openList is empty no path could be found, return current tile
			return origin;
		
		for(Tile tile : openList)					// Calculate cost to all tiles in openList
			tile.calcCost(origin, destination, Tile.TO_DEST);
					
		sortByCost(openList);						// Sort openList in ascending order
		
		Tile cheapestTile = openList.getFirst();	// Select the first (cheapest) tile
		openList.removeFirst();						// Remove first tile from openList
		closedList.add(cheapestTile);				// and add it to the closedList
		
		return getPathTo(cheapestTile.getPosition(), destination.getPosition());
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
	
	
	private static void sortByCost(LinkedList<Tile> tiles){
		
		Collections.sort(tiles, new Comparator<Tile>() {
			@Override
			public int compare(Tile a, Tile b){
				return a.totalCost() - b.totalCost();
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
