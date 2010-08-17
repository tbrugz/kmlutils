package tbrugz.geo;

import java.io.PrintStream;

import tbrugz.geo.model.Group;
import tbrugz.geo.model.Point;
import tbrugz.geo.model.Polygon;
import tbrugz.geo.model.Root;
import tbrugz.xml.model.skel.Composite;
import tbrugz.xml.model.skel.Element;

public class DumpModel {

	static String levelStr = "  "; //"\t";
	
	PrintStream output = null;
	
	public void dumpModel(Root root, PrintStream out) {
		this.output = out;
		dumpModel(root, 0);
	}
	
	void dumpModel(Element elem, int level) {
		if(elem instanceof Root) {
			out("<root id='"+elem.getId()+"'>", level);
		}
		else if(elem instanceof Group) {
			out("<g id='"+elem.getId()+"'>", level);
		}
		else if(elem instanceof Polygon) {
			StringBuffer b = new StringBuffer();
			for(Point p: ((Polygon)elem).points) {
				b.append("["+p.x+","+p.y+"] ");
			}
			out("<polygon id='"+elem.getId()+"' pts=\""+b.toString()+"\"/>", level);
		}
		else {
			out(">> unknown element: "+elem.getClass().getName(), level);
		}
		
		if(elem instanceof Composite) {
			for(Element e: ((Composite)elem).getChildren()) {
				dumpModel(e, level+1);
			}
		}

		if(elem instanceof Root) {
			out("</root>", level);
		}
		else if(elem instanceof Group) {
			out("</g>", level);
		}
	}
	
	void out(String s, int nestLevel) {
		for(int i=0;i<nestLevel;i++) output.print(levelStr);
		output.println(s);
	}
}
