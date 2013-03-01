package tbrugz.graphml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tbrugz.graphml.model.Edge;
import tbrugz.graphml.model.Node;
import tbrugz.graphml.model.Root;
import tbrugz.xml.AbstractDump;
import tbrugz.xml.model.skel.Composite;
import tbrugz.xml.model.skel.Element;

/*
 * see:
 * http://en.wikipedia.org/wiki/DOT_language
 * http://en.wikipedia.org/wiki/Graphviz
 */
public class DumpDotModel extends AbstractDump {

	static Log log = LogFactory.getLog(DumpDotModel.class);

	@Override
	public void dumpModel(Element root) {
		dumpModel(root, 0);
	}
	
	public void dumpModel(Element elem, int level) {
		if(elem instanceof Root) {
			out("digraph "
				+(elem.getId()!=null?escapeId(elem.getId())+" ":"")
				+"{\n", level);
		}
		else if(elem instanceof Node) {
			Node t = (Node) elem;
			
			out(escapeId(t.getId())
				+(t.getLabel()!=null?" [label=\""+t.getLabel()+"\"]":"")
				+";\n"
				, level);
			//nodeNames.add(t.getId());
		}
		else if(elem instanceof Edge) {
			Edge edge = (Edge) elem;
			out(escapeId(edge.getSource())
				+" -> "
				+escapeId(edge.getTarget())
				+";\n"
				, level);
		}
		else {
			//XXX add comment do output (as .dot comment)?
			log.warn("unknown element: "+elem.getClass().getName());
		}

		if(elem instanceof Composite) {
			for(Element e: ((Composite)elem).getChildren()) {
				dumpModel(e, level+1);
			}
		}
		
		//end of processing
		if(elem instanceof Root) {
			out("}", level);
		}
	}
	
	String escapeId(String id) {
		return id.replaceAll("[\\.\\$]", "_");
	}
}
