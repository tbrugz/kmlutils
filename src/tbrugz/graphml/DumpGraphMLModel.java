package tbrugz.graphml;

import java.io.File;
import java.io.PrintStream;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tbrugz.graphml.model.Edge;
import tbrugz.graphml.model.Root;
import tbrugz.graphml.model.Node;
import tbrugz.graphml.model.Stereotyped;
import tbrugz.xml.AbstractDump;
import tbrugz.xml.model.skel.Composite;
import tbrugz.xml.model.skel.Element;

public class DumpGraphMLModel extends AbstractDump {

	static Log log = LogFactory.getLog(DumpGraphMLModel.class);
	
	static final String DEFAULT_SNIPPETS_FILE = "graphml-snippets.properties";

	Set<String> nodeNames = new TreeSet<String>();
	
	{
		loadSnippets(DEFAULT_SNIPPETS_FILE);
		log.debug("loading default snippets file: "+new File(DEFAULT_SNIPPETS_FILE).getAbsolutePath());
	}
	
	@Override
	public void dumpModel(Element root, PrintStream out) {
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
		}
		else if(elem instanceof Edge) {
			Edge myl = (Edge) elem;
			out("<edge source=\""+myl.getSource()+"\" target=\""+myl.getTarget()+"\">", level);
			outEdgeContents(myl, level+1);
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
			if(hasSnippet("footer")) {
				outSnippet("footer", level);
			}
			else {
				out("</graph></graphml>", level);
			}
		}
	}
	
	public String getSnippetId(Stereotyped st, String s) {
		if(st.getStereotype()!=null) {
			String newS = s+"."+st.getStereotype();
			while(true) {
				if(hasSnippet(newS)) { return newS; }
				int index = newS.lastIndexOf(".");
				if(index<=0) { break; }
				newS = newS.substring(0, index);
			}
		}
		return s;
	}
	
	@Deprecated
	public void oldOutNodeContents(Node t, int level) {
		outSnippet(getSnippetId(t, "node"), level, t.getLabel());
	}

	public void outEdgeContents(Edge l, int level) {
		outSnippet(getSnippetId(l, "edge"), level, l.getName());
	}

	public void outNodeContents(Node t, int level) {
		if(t.getStereotypeParamCount()>0) {
			String[] args = new String[t.getStereotypeParamCount()];
			for(int i=0; i<t.getStereotypeParamCount(); i++) {
				args[i] = escape(t.getStereotypeParam(i));
			}
			outSnippet(getSnippetId(t, "node"), level, args);
		}
		else {
			outSnippet(getSnippetId(t, "node"), level, t.getLabel());
		}
	}
	
	static String escape(String s) {
		if(s==null) return null;
		return s.replaceAll("&", "&amp;");
	}
}
