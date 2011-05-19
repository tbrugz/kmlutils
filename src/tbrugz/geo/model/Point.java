package tbrugz.geo.model;

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
}
