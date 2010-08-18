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
import tbrugz.graphml.model.Tela;
import tbrugz.xml.AbstractDump;
import tbrugz.xml.model.skel.Composite;
import tbrugz.xml.model.skel.Element;

/*
[WARN] edge target UC096 not found - from: UC081
[WARN] edge target UC096 not found - from: UC081A
 */
public class DumpGraphMLModel extends AbstractDump {

	static Log log = LogFactory.getLog(AbstractDump.class);

	List<Link> links = new ArrayList<Link>();
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
		else if(elem instanceof Tela) {
			Tela t = (Tela) elem;
		    out("<node id=\""+t.getCodigo()+"\">", level);
			outSnippet("node", level+1, t.getCodigo());
		    out("</node>", level);
		    nodeNames.add(t.getCodigo());
		    
		    List<Link> ll = t.getProx();
		    for(Link l: ll) {
		    	l.setOrigem(t.getCodigo());
		    }
		    links.addAll(ll);
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
			for(Link myl: links) {
				if(nodeNames.contains(myl.getsDestino())) {
					out("<edge source=\""+myl.getOrigem()+"\" target=\""+myl.getsDestino()+"\">", level+1);
					outSnippet("edge", level+2);
					out("</edge>", level+1);
				}
				else {
					log.warn("edge target "+myl.getsDestino()+" not found - from: "+myl.getOrigem());
				}
			}
			
			out("</graph></graphml>", level);
		}
	}
}
