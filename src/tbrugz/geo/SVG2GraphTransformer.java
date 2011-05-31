package tbrugz.geo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tbrugz.geo.model.Group;
import tbrugz.geo.model.Point;
import tbrugz.geo.model.Polygon;
import tbrugz.geo.model.Root;
import tbrugz.graphml.model.Link;
import tbrugz.graphml.model.NodeXY;
import tbrugz.xml.model.skel.Composite;
import tbrugz.xml.model.skel.Element;

class SVG2GraphTransformer {
	
	static Log log = LogFactory.getLog(SVG2GraphTransformer.class);

	Map<Point, List<String>> pointmap = new HashMap<Point, List<String>>();
	tbrugz.graphml.model.Root graphRoot;
	List<Element> graphl;
	Set<Link> linkset = new HashSet<Link>(); 
	
	int countNodes = 0;
	int countEdges = 0;
	int countTotalConnections = 0;
	
	public SVG2GraphTransformer() {
		graphRoot = new tbrugz.graphml.model.Root();
		graphl = graphRoot.getChildren();
	}

	tbrugz.graphml.model.Root toGraphML(Root svgRoot) {
		procSVGNodes(svgRoot);
		procEdges(svgRoot);
		
		//System.out.print(pointmap);
		for(Point p: pointmap.keySet()) {
			List<String> ls = pointmap.get(p);
			if(ls.size()>1) {
				//get all combinations of 2 elements from List<String>
				addEdges(ls, graphRoot, 1);
			}
		}
		
		log.info("#nodes = "+countNodes+" ; #edges = "+countEdges+" ; #totalConn = "+countTotalConnections);
		return graphRoot;
	}
	
	void addEdges(List<String> nodes, Composite root, int startIndex) {
		for(int i=startIndex;i<nodes.size();i++) {
			Link l = new Link();
			l.setSource(nodes.get(startIndex-1));
			l.setTarget(nodes.get(i));
			if(l.getSource().equals(l.getTarget())) {
				//do not add "self-edge"
			}
			else {
				//TODOne: do not add duplicate edges
				if(linkset.contains(l)) {
				}
				else {
					linkset.add(l);
					countEdges++;
					root.getChildren().add(l);
				}
				countTotalConnections++;
			}
		}
		if(startIndex<nodes.size()) { addEdges(nodes, root, startIndex+1); }
	}
	
	void procSVGNodes(Composite svgRoot) {
		for(Element e: svgRoot.getChildren()) {
			if(e instanceof Group) {
				procSVGNodes((Group)e);
			}
			else if(e instanceof Polygon) {
				Polygon p = (Polygon) e;
				NodeXY n = new NodeXY();
				n.setId(p.getId());
				n.setLabel(p.getId()); //XXX: better label?
				n.setX(p.centre.x);
				n.setY(p.centre.y);
				graphl.add(n);
				countNodes++;
			}
		}
	}

	void procEdges(Composite root) {
		for(Element e: root.getChildren()) {
			if(e instanceof Polygon) {
				Polygon poly = ((Polygon) e);
				for(Point p: poly.points) {
					List<String> ls = pointmap.get(p);
					if(ls!=null) {
						ls.add(poly.getId());
					}
					else {
						List<String> nls = new ArrayList<String>();
						nls.add(poly.getId());
						pointmap.put(p, nls);
					}
				}
			}
			if(e instanceof Composite) {
				procEdges((Composite)e);
			}
		}
	}
}

