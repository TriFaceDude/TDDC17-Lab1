package tddc17;

public class Point {

	private int x, y;
	
	public Point(int x, int y){
		
		this.x = x;
		this.y = y;
	}
	
	public void add(Point point){
		this.add(point.getX(), point.getY());
	}
	
	public void add(int x, int y){
		this.x += x;
		this.y += y;
	}
	
	public void sub(int x, int y){
		this.x -= x;
		this.y -= y;
	}
	
	public Point getOrthoCC(){
		
		return new Point(this.getY(), this.getX());
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	@Override
	public String toString(){
		
		return "( " + Integer.toString(this.x) + ", " + Integer.toString(this.y) + " )";
	}
	
	public static int manhattanDistance(Point a, Point b){
		int result = 0;
		
		result += Math.abs(a.getX() - b.getX());
		result += Math.abs(a.getY() - b.getY());
		
		return result;
	}
	public static Point add(Point a, Point b){
		
		return new Point(a.getX() + b.getX(), a.getY() + b.getY());
	}
	
	public static Point sub(Point a, Point b){
		
		return new Point(a.getX() - b.getX(), a.getY() - b.getY());
	}
	
	public static Point Zero(){
		
		return new Point(0, 0);
	}
}
