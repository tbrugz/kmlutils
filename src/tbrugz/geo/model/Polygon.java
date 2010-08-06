package tbrugz.geo.model;

import java.util.ArrayList;
import java.util.List;

import tbrugz.geo.model.skel.Element;

public class Polygon implements Element {
	String id;

	public Point centre = null;
	
	public List<Point> points = new ArrayList<Point>();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
