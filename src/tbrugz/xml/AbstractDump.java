package tbrugz.xml;

import java.io.FileInputStream;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tbrugz.xml.model.skel.Element;

class ReplacerSequence {
	static int count = 0;
	
	public static int getNext() {
		return count++;
	} 
}

public abstract class AbstractDump {

	static String levelStr = "  "; //"\t";
	
	static Log log = LogFactory.getLog(AbstractDump.class);

	protected Properties snippets = new Properties();
	
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
	
	final static Pattern paramPattern = Pattern.compile("(\\{(.)+?})");
	final static ReplaceUtil regexutil = new ReplaceUtil();
	
	public void outSnippet(String snippetId, int nestLevel, String... params) {
		String s = snippets.getProperty(snippetId);
		if(s==null) return;
		
		StringBuilder sb = new StringBuilder(s);
		Matcher matcher = paramPattern.matcher(s);
		log.debug("pattern: "+paramPattern+"; matcher: "+matcher);
		
		int xtraOffset = 0; //replacement string lenght may be different from replaced string lenght, so an extra offset is needed
		while(matcher.find()) {
			String paramGroup = matcher.group(1);
			String replacement = regexutil.procFunc(paramGroup, params);
			log.debug("param: "+paramGroup+"; replacement: "+replacement);
			if(replacement!=null) {
				xtraOffset += replace(sb, replacement, matcher, xtraOffset);
				/*sb.replace(matcher.start()+xtraOffset, matcher.end()+xtraOffset, replacement);
				int originalSize = matcher.end()-matcher.start();
				xtraOffset += replacement.length()-originalSize;*/
				//matcher.replaceFirst(replacement);
				//matcher = paramPattern.matcher(s);
			}
			else {
				//TODOne: handle null ids
				replacement = "nullid_"+ReplacerSequence.getNext();
				xtraOffset += replace(sb, replacement, matcher, xtraOffset);
			}
		}
		
		/*if(params!=null) {
			for(int i=0;i<params.length;i++) {
				log.debug(i+";"+params[i]);
				s = s.replaceAll("\\{"+i+"}", params[i]);
			}
		}*/
		
		if(sb!=null) { out(sb.toString(), nestLevel); }
	}
	
	int replace(StringBuilder sb, String replacement, Matcher matcher, int xtraOffset) {
		sb.replace(matcher.start()+xtraOffset, matcher.end()+xtraOffset, replacement);
		int originalSize = matcher.end()-matcher.start();
		return replacement.length()-originalSize; //StringBuffer size diff; to add in xtraOffset
	}
	
	public void out(String s, int nestLevel) {
		for(int i=0;i<nestLevel;i++) { output.print(levelStr); }
		output.println(s);
	}
	
}
