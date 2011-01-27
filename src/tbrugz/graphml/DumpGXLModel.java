package tbrugz.graphml;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import tbrugz.graphml.model.Link;
import tbrugz.graphml.model.Root;
import tbrugz.graphml.model.Node;
import tbrugz.xml.AbstractDump;
import tbrugz.xml.model.skel.Composite;
import tbrugz.xml.model.skel.Element;

public class DumpGXLModel extends AbstractDump {

	Properties snippets = new Properties();
	
	List<Link> links = new ArrayList<Link>();
	
	@Override
	public void dumpModel(Element root, PrintStream out) {
		loadSnippets("snippets.properties");
		super.dumpModel(root, out);
	}

	@Override
	public void dumpModel(Element elem, int level) {
		if(elem instanceof Root) {
			outSnippet("gxl", level);
		}
		else if(elem instanceof Node) {
			Node t = (Node) elem;
		    out("<node id=\""+t.getId()+"\">", level);
		    out("  <type xlink:href=\"http://www.gupro.de/GXL/examples/schema/gxl/simpleExample/simpleExampleSchema.gxl#Proc\" xlink:type=\"simple\"/>", level);
		    //out("  <attr name=\" file\">", level);
		    //out("    <string> main.c</string>", level);
		    //out("  </attr>", level);
		    out("</node>", level);
		    List<Link> ll = t.getProx();
		    for(Link l: ll) {
		    	l.setOrigem(t.getId());
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
				out("<edge id=\""+myl.getOrigem()+"_"+myl.getsDestino()+"\" to=\""+myl.getsDestino()+"\" from=\""+myl.getOrigem()+"\">", level+1);
				out("  <type xlink:href=\"http://www.gupro.de/GXL/examples/schema/gxl/simpleExample/simpleExampleSchema.gxl#Call\" xlink:type=\" simple\"/>", level+1);
				out("</edge>", level+1);
			}
			
			out("</graph></gxl>", level);
		}
	}
	
}
