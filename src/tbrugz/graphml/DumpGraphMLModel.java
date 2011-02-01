package tbrugz.graphml;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tbrugz.graphml.model.Link;
import tbrugz.graphml.model.Root;
import tbrugz.graphml.model.Node;
import tbrugz.xml.AbstractDump;
import tbrugz.xml.model.skel.Composite;
import tbrugz.xml.model.skel.Element;

/*
[WARN] edge target UC096 not found - from: UC081
[WARN] edge target UC096 not found - from: UC081A
 */
public class DumpGraphMLModel extends AbstractDump {

	static Log log = LogFactory.getLog(AbstractDump.class);

	//List<Link> links = new ArrayList<Link>();
	Set<String> nodeNames = new TreeSet<String>();
	
	@Override
	public void dumpModel(Element root, PrintStream out) {
		loadSnippets("graphml-snippets.properties");
		super.dumpModel(root, out);
	}

	@Override
	public void dumpModel(Element elem, int level) {
		if(elem instanceof Root) {
			outSnippet("graphml", level);
		}
		else if(elem instanceof Node) {
			Node t = (Node) elem;
			out("<node id=\""+t.getId()+"\">", level);
			outNodeContents(t, level+1);
			out("</node>", level);
			nodeNames.add(t.getId());

			/*List<Link> ll = t.getProx();
			for(Link l: ll) {
				l.setSource(t.getId());
			}
			links.addAll(ll);*/
		}
		else if(elem instanceof Link) {
			Link myl = (Link) elem;
			out("<edge source=\""+myl.getSource()+"\" target=\""+myl.getTarget()+"\">", level);
			outEdgeContents(myl, level+1);
			//outSnippet("edge", level+2);
			out("</edge>", level);
		}
		else {
			out(">> unknown element: "+elem.getClass().getName(), level);
		}
		
		if(elem instanceof Composite) {
			for(Element e: ((Composite)elem).getChildren()) {
				dumpModel(e, level+1);
			}
		}

		//end of processing
		if(elem instanceof Root) {
			//edge output
			/*for(Link myl: links) {
				if(nodeNames.contains(myl.getsTarget())) {
					out("<edge source=\""+myl.getSource()+"\" target=\""+myl.getsTarget()+"\">", level+1);
					outEdgeContents(myl, level+2);
					//outSnippet("edge", level+2);
					out("</edge>", level+1);
				}
				else {
					log.warn("edge target "+myl.getsTarget()+" not found - from: "+myl.getSource());
				}
			}*/
			
			out("</graph></graphml>", level);
		}
	}
	
	public void outNodeContents(Node t, int level) {
		outSnippet("node", level, t.getLabel());
	}

	public void outEdgeContents(Link l, int level) {
		outSnippet("edge", level);
	}
}
