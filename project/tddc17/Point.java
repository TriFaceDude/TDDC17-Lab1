package tddc17;

import java.util.LinkedList;

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
	
	public static LinkedList<Point> getAdjacentPoints(Point point, Point minBound, Point maxBound){
		
		LinkedList<Point> result = new LinkedList<Point>();
		
		Point right = add(point, new Point(1, 0));
		Point up = add(point, new Point(0, -1));
		Point left = add(point, new Point(-1, 0));
		Point down = add(point, new Point(0, 1));
		
		if(inBounds(right, minBound, maxBound))
			result.add(Point.add(point, right));
		if(inBounds(up, minBound, maxBound))
			result.add(Point.add(point, up));
		if(inBounds(left, minBound, maxBound))
			result.add(Point.add(point, left));
		if(inBounds(down, minBound, maxBound))
			result.add(Point.add(point, down));
		
		return result;
	}
	
	public static boolean inBounds(Point point, Point minBound, Point maxBound){
		
		if(point.getX() < minBound.getX() || point.getY() < minBound.getY())
			return false;
		if(point.getX() > maxBound.getX() || point.getY() > maxBound.getY())
			return false;
		
		return true;
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
