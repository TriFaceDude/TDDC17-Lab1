package tddc17;

public class TileMap {

	Point[][] map;
	
	public TileMap(Point size){
		this(size.getX(), size.getY());
	}
	
	public TileMap(int width, int height){
		
		map = new Point[height][width];
		init(map);
	}
	
	public void extendSizeTo(int width, int height){
		
		Point[][] newMap = new Point[height][width];
		init(newMap);
		transferTiles(this.map, newMap);
		
		this.map = newMap;
	}
	
	public void print(){
		
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map[y].length; x++){
				System.out.print(map[y][x]);	
			}
			System.out.println("");
		}
	}
	
	private static void init(Point[][] map){
		
		for(int y = 0; y < map.length; y++)
			for(int x = 0; x < map[y].length; x++)
				map[y][x] = Point.Zero();
	}
	
	private static void transferTiles(Point[][] origin, Point[][] destination){
		
		for(int y = 0; y < origin.length; y++){
			for(int x = 0; x < origin[y].length; x++){
				destination[y][x] = origin[y][x];
			}
		}
	}
}
