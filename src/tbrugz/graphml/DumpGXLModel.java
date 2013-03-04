package tbrugz.graphml;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tbrugz.graphml.model.Edge;
import tbrugz.graphml.model.Root;
import tbrugz.graphml.model.Node;
import tbrugz.xml.AbstractDump;
import tbrugz.xml.model.skel.Composite;
import tbrugz.xml.model.skel.Element;

public class DumpGXLModel extends AbstractDump {

	static Log log = LogFactory.getLog(DumpGXLModel.class);

	//List<Link> links = new ArrayList<Link>();

	void loadDefaultSnipets() {
		loadSnippets(DumpGraphMLModel.DEFAULT_SNIPPETS_FILE);
		log.info("loaded default snippets file: "+new File(DumpGraphMLModel.DEFAULT_SNIPPETS_FILE).getAbsolutePath());
	}

	@Override
	public void dumpModel(Element root) {
		if(snippets.size()==0) { loadDefaultSnipets(); }
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
			out("<edge id=\""+myl.getSource()+"_"+myl.getTarget()+"\" to=\""+myl.getTarget()+"\" from=\""+myl.getSource()+"\">", level);
			out("  <type xlink:href=\"http://www.gupro.de/GXL/examples/schema/gxl/simpleExample/simpleExampleSchema.gxl#Call\" xlink:type=\"simple\"/>", level);
			out("</edge>", level);
		}
		else {
			log.warn("unknown element: "+elem.getClass().getName()+" , level = "+level);
		}
		
		if(elem instanceof Composite) {
			for(Element e: ((Composite)elem).getChildren()) {
				dumpModel(e, level+1);
			}
		}

		//end of processing
		if(elem instanceof Root) {
			/*for(Link myl: links) {
				out("<edge id=\""+myl.getSource()+"_"+myl.getsTarget()+"\" to=\""+myl.getsTarget()+"\" from=\""+myl.getSource()+"\">", level+1);
				out("  <type xlink:href=\"http://www.gupro.de/GXL/examples/schema/gxl/simpleExample/simpleExampleSchema.gxl#Call\" xlink:type=\" simple\"/>", level+1);
				out("</edge>", level+1);
			}*/
			
			if(hasSnippet("gxl.footer")) {
				outSnippet("gxl.footer", level);
			}
			else {
				out("</graph></gxl>", level);
			}
		}
	}
	
}
