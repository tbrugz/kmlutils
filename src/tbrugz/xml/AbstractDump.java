package tbrugz.xml;

import java.io.FileInputStream;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tbrugz.xml.model.skel.Element;

public abstract class AbstractDump {

	static String levelStr = "  "; //"\t";
	
	static Log log = LogFactory.getLog(AbstractDump.class);

	Properties snippets = new Properties();
	
	PrintStream output = null;
	
	public void dumpModel(Element root, PrintStream out) {
		this.output = out;
		//loadProp(snippets, "snippets.properties");
		dumpModel(root, 0);
	}
	
	public void loadSnippets(String file) {
		loadProp(snippets, file);
	}
	
	public void loadProp(Properties prop, String fileName) {
		try {
			prop.load(new FileInputStream(fileName));
		}
		catch(IOException ioe) {
			log.warn("Error loading file: "+ioe);
		} 
	}
	
	public abstract void dumpModel(Element elem, int level);
	
	public void outSnippet(String snippetId, int nestLevel, String... params) {
		String s = snippets.getProperty(snippetId);
		if(s==null) return;
		if(params!=null) {
			for(int i=0;i<params.length;i++) {
				log.debug(i+";"+params[i]);
				s = s.replaceAll("\\{"+i+"}", params[i]);
			}
		}
		if(s!=null) out(s, nestLevel);
	}
	
	public void out(String s, int nestLevel) {
		for(int i=0;i<nestLevel;i++) output.print(levelStr);
		output.println(s);
	}
}
