package tbrugz.xml;

import java.io.FileInputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tbrugz.util.FileReaderProperties;
import tbrugz.xml.model.skel.Element;

class ReplacerSequence {
	static int count = 0;
	
	public static int getNext() {
		return count++;
	} 
}

public abstract class AbstractDump {

	static final String levelStr = "  "; //"\t";
	
	static final Log log = LogFactory.getLog(AbstractDump.class);

	protected final Properties snippets = new FileReaderProperties();
	
	PrintStream output = null;
	
	public void dumpModel(Element root, PrintStream out) {
		this.output = out;
		//loadProp(snippets, "snippets.properties");
		dumpModel(root);
	}
	
	public void loadSnippets(String file) {
		loadProp(snippets, file);
	}
	
	public void loadProp(Properties prop, String fileName) {
		Properties ptemp = new Properties();
		try {
			File f = new File(fileName);
			ptemp.load(new FileInputStream(f));
			prop.putAll(ptemp);
			log.info("loaded prop file: "+f.getAbsolutePath());
		}
		catch(IOException ioe) {
			try {
				String resource = "/"+fileName;
				InputStream is = AbstractDump.class.getResourceAsStream(resource);
				if(is==null) { log.warn("resource not found: "+resource); return; }
				ptemp.load(is);
				prop.putAll(ptemp);
				log.info("loaded prop resource: "+resource);
				//log.info("loaded prop resource: "+resource, new Throwable());
			}
			catch(IOException e) {
				log.warn("error loading file: "+ioe);
			}
		}
	}
	
	public abstract void dumpModel(Element root);
	
	//XXX add: public abstract String getDefaultFileFormat();
	
	//public abstract void dumpModel(Element elem, int level);
	
	final static Pattern paramPattern = Pattern.compile("(\\{(.)+?})");
	final static ReplaceUtil regexutil = new ReplaceUtil();

	public boolean hasSnippet(String snippetId) {
		return null!=snippets.getProperty(snippetId);
	}
	
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
			
			String tmpSnippetId = snippetId;
			while(replacement==null) {
				replacement = snippets.getProperty("defaults."+tmpSnippetId+"."+paramGroup);
				log.debug("replacement NULL: "+snippetId+"/"+tmpSnippetId+"/"+paramGroup+"; new replacement: "+replacement);
				
				int index = tmpSnippetId.lastIndexOf(".");
				if(index<=0) { break; }
				tmpSnippetId = tmpSnippetId.substring(0, index);
				
				//replacement = "nullid_"+ReplacerSequence.getNext();
				/*sb.replace(matcher.start()+xtraOffset, matcher.end()+xtraOffset, replacement);
				int originalSize = matcher.end()-matcher.start();
				xtraOffset += replacement.length()-originalSize;*/
				//matcher.replaceFirst(replacement);
				//matcher = paramPattern.matcher(s);
			}
			if(replacement==null) { replacement = "nullid_"+ReplacerSequence.getNext(); }
			xtraOffset += replace(sb, replacement, matcher, xtraOffset);
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
