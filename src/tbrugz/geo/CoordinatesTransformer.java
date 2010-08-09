package tbrugz.geo;

import tbrugz.geo.model.Point;
import tbrugz.geo.model.Polygon;
import tbrugz.geo.model.Root;
import tbrugz.geo.model.skel.Composite;
import tbrugz.geo.model.skel.Element;

public class CoordinatesTransformer {

	//{parser.maxX, parser.minX, parser.maxY, parser.minY};
	
	float[] inputBounds = null;
	float[] outputBounds = null;

	float XinputInterval;
	float YinputInterval;
	float XoutputInterval;
	float YoutputInterval;
	
	public void transformCoords(Root root, float[] inputBounds, float[] outputBounds) {
		this.inputBounds = inputBounds;
		this.outputBounds = outputBounds;
		
		XinputInterval = inputBounds[0]-inputBounds[1];
		YinputInterval = inputBounds[2]-inputBounds[3];
		XoutputInterval = outputBounds[0]-outputBounds[1];
		YoutputInterval = outputBounds[2]-outputBounds[3];
		
		transformCoords(root);
	}
	
	void transformCoords(Composite comp) {
		for(Element e: comp.getChildren()) {
			if(e instanceof Composite) {
				transformCoords((Composite) e);
			}
			else if(e instanceof Polygon) {
				Polygon pol = (Polygon) e;
				/*for(Point p: pol.points) {
					p = transformPoint(p);
				}*/
				for(int i=0;i<pol.points.size();i++) {
					Point p = pol.points.get(i);
					pol.points.set(i, transformPoint(p));
					//p = transformPoint(p);
				}
				pol.centre = transformPoint(pol.centre);
			}
		}
	}
	
	Point transformPoint(Point p) {
		/*float XinputInterval = inputBounds[0]-inputBounds[1];
		float YinputInterval = inputBounds[2]-inputBounds[3];
		float XoutputInterval = outputBounds[0]-outputBounds[1];
		float YoutputInterval = outputBounds[2]-outputBounds[3];*/
		
		float X = (((p.x - inputBounds[1]) / XinputInterval ) * XoutputInterval ) + outputBounds[1];
		float Y = (((p.y - inputBounds[3]) / YinputInterval ) * YoutputInterval ) + outputBounds[3];
		
		Point newp = new Point();
		newp.x = X;
		newp.y = Y;
		
		return newp;
	}
}
