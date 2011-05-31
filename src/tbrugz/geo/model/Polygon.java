package tbrugz.geo.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tbrugz.xml.model.skel.Element;

public class Polygon implements Element {
	String id;

	static Log log = LogFactory.getLog(Polygon.class);

	public Point centre = null;
	
	public List<Point> points = new ArrayList<Point>();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void addPoint(Point p) {
		points.add(p);
		/*if(points.size()==1) {
			log.trace("poly: 1str point: "+p);
		}*/
	}
	
	/*public void add1stPointAgain() {
		addPoint(points.get(0));
	}*/
	
	public void setPolygonCentre() {
		float sumX=0, sumY=0;
		for(Point p: points) {
			sumX+=p.x;
			sumY+=p.y;
		}
		float avgX = sumX/points.size();
		float avgY = sumY/points.size();
		centre = new Point();
		centre.x = avgX;
		centre.y = avgY;
	}
	
	//TODO: add barycenter - http://en.wikipedia.org/wiki/Center_of_mass
}
