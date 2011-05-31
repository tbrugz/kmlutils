package tbrugz.svg.model;

public class Point implements Cloneable {
	public float x, y;
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch(CloneNotSupportedException cnse) {
			System.out.println("Point: clone not supported");
			return null;
		}
	}
	
	@Override
	public String toString() {
		return "["+x+","+y+"]";
	}
	
	static float equalityPrecision = 0.1f; //XXX: 0.1f
	
	static int rounded(float f) {
		return Math.round(f*equalityPrecision);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Point) {
			return rounded( ((Point) obj).x )==rounded(x) && rounded( ((Point) obj).y )==rounded(y); 
		}
		return false;
	}
	
	static int reminderDiv = 10000;
	
	@Override
	public int hashCode() {
		return rounded(x)+rounded(y)%reminderDiv;
	}
	
	public void addPoint(Point p) {
		x += p.x;
		y += p.y;
	}
}
