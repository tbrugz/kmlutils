package tce.xmlxtra;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tce.xmlxtra.model.Arquivo;
import tbrugz.graphml.model.Link;
import tbrugz.graphml.model.Root;
import tbrugz.xml.AbstractDump;
import tbrugz.xml.model.skel.Composite;
import tbrugz.xml.model.skel.Element;

/*
[WARN] edge target UC096 not found - from: UC081
[WARN] edge target UC096 not found - from: UC081A
 */
public class DumpArquivosPADGraphMLModel extends AbstractDump {

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
		else if(elem instanceof Arquivo) {
			Arquivo a = (Arquivo) elem;
		    out("<node id=\""+a.getId()+"\">", level);
			outSnippet("node", level+1, a.getLabel());
		    out("</node>", level);
		    //nodeNames.add(a.getId());
		    
		    /*List<Link> ll = t.getProx();
		    for(Link l: ll) {
		    	l.setOrigem(t.getCodigo());
		    }
		    links.addAll(ll);*/
		}
		else if(elem instanceof Link) {
			Link al = (Link) elem;
			out("<edge source=\""+al.getOrigem()+"\" target=\""+al.getsDestino()+"\">", level);
			outSnippet("edge", level+1);
			out("</edge>", level);
		    //nodeNames.add(a.getId());
		}
		else {
			out(">> unknown element: "+elem+" ; "+((elem!=null)?elem.getClass().getName():""), level);
		}
		
		if(elem instanceof Composite) {
			for(Element e: ((Composite)elem).getChildren()) {
				dumpModel(e, level+1);
			}
		}

		//end of processing
		if(elem instanceof Root) {
			
			//edge output
			/*for(ArquivoLink myl: links) {
				if(nodeNames.contains(myl.getsDestino())) {
					out("<edge source=\""+myl.getOrigem()+"\" target=\""+myl.getsDestino()+"\">", level+1);
					outSnippet("edge", level+2);
					out("</edge>", level+1);
				}
				else {
					log.warn("edge target "+myl.getsDestino()+" not found - from: "+myl.getOrigem());
				}
			}*/
			
			out("</graph></graphml>", level);
		}
	}
}
