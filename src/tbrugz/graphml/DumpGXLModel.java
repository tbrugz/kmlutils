package tbrugz.graphml;

import java.io.PrintStream;
import java.util.Properties;

import tbrugz.graphml.model.Edge;
import tbrugz.graphml.model.Root;
import tbrugz.graphml.model.Node;
import tbrugz.xml.AbstractDump;
import tbrugz.xml.model.skel.Composite;
import tbrugz.xml.model.skel.Element;

public class DumpGXLModel extends AbstractDump {

	Properties snippets = new Properties();
	
	//List<Link> links = new ArrayList<Link>();
	
	@Override
	public void dumpModel(Element root, PrintStream out) {
		loadSnippets("snippets.properties");
		super.dumpModel(root, out);
	}

	@Override
	public void dumpModel(Element root) {
		dumpModel(root, 0);
	}
	
	public void dumpModel(Element elem, int level) {
		if(elem instanceof Root) {
			outSnippet("gxl", level);
		}
		else if(elem instanceof Node) {
			Node t = (Node) elem;
		    out("<node id=\""+t.getId()+"\">", level);
		    out("  <type xlink:href=\"http://www.gupro.de/GXL/examples/schema/gxl/simpleExample/simpleExampleSchema.gxl#Proc\" xlink:type=\"simple\"/>", level);
		    out("</node>", level);
		    /*List<Link> ll = t.getProx();
		    for(Link l: ll) {
		    	l.setSource(t.getId());
		    }
		    links.addAll(ll);*/
		}
		else if(elem instanceof Edge) {
			Edge myl = (Edge) elem;
			out("<edge id=\""+myl.getSource()+"_"+myl.getTarget()+"\" to=\""+myl.getTarget()+"\" from=\""+myl.getSource()+"\">", level+1);
			out("  <type xlink:href=\"http://www.gupro.de/GXL/examples/schema/gxl/simpleExample/simpleExampleSchema.gxl#Call\" xlink:type=\" simple\"/>", level+1);
			out("</edge>", level+1);
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
			/*for(Link myl: links) {
				out("<edge id=\""+myl.getSource()+"_"+myl.getsTarget()+"\" to=\""+myl.getsTarget()+"\" from=\""+myl.getSource()+"\">", level+1);
				out("  <type xlink:href=\"http://www.gupro.de/GXL/examples/schema/gxl/simpleExample/simpleExampleSchema.gxl#Call\" xlink:type=\" simple\"/>", level+1);
				out("</edge>", level+1);
			}*/
			
			out("</graph></gxl>", level);
		}
	}
	
}
