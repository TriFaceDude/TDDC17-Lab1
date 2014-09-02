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
			result.add(right);
		if(inBounds(up, minBound, maxBound))
			result.add(up);
		if(inBounds(left, minBound, maxBound))
			result.add(left);
		if(inBounds(down, minBound, maxBound))
			result.add(down);
		
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
	
	public static Point mul(Point a, int i){
		
		return new Point(a.getX()*i, a.getY()*i);
	}
	
	public static Point divideBySelf(Point point){
		
		return new Point(point.getX()/Math.max(Math.abs(point.getX()), 1) ,
							point.getY()/Math.max(Math.abs(point.getY()), 1));
	}
	
	public static Point direction(Point a, Point b){
		
		return divideBySelf(sub(b, a));
	}
	
	public static Point Zero(){
		
		return new Point(0, 0);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(!(obj instanceof Point))
			return false;
		
		Point point = (Point)obj;
		
		return (this.x == point.getX() && this.y == point.getY());
	}
	
	
}
