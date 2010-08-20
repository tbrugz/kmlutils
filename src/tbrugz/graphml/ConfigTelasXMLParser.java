package tbrugz.graphml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import tbrugz.graphml.model.Link;
import tbrugz.graphml.model.Root;
import tbrugz.graphml.model.Tela;
import tbrugz.xml.model.skel.Composite;

public class ConfigTelasXMLParser extends DefaultHandler {
	
	public static String ROOT = "config-telas";
	public static String TELA = "tela";
	
	public static String ANY_ID = "id";
	
	Root root = null;
	Composite lastGroupParsed = null;
	int nestLevel = 0;
	
	static Log log = LogFactory.getLog(ConfigTelasXMLParser.class);

	public Root parseDocument(String file) {
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {

			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();

			//parse the file and also register this class for call backs
			sp.parse(file, this);

		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
		
		return root;
	}

	//Event Handlers
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(qName.equalsIgnoreCase(ROOT)) {
			if(root==null) {
				root = new Root();
				lastGroupParsed = root;

				log.debug("<"+ROOT+"> processed, l:"+nestLevel);
				nestLevel++;
			}
			else {
				throw new RuntimeException("<"+ROOT+"> already declared");
			}
		}
		
		if(qName.equalsIgnoreCase(TELA)) {
			Tela t = new Tela();
			lastGroupParsed.getChildren().add(t);
			String codigo = attributes.getValue("codigo");
			String prox = attributes.getValue("proxima-tela");
			String decisaoproxima = attributes.getValue("decisao-proxima");
			t.setCodigo(codigo);
			
			if(prox!=null) {
				Link l = new Link();
				l.setsDestino(prox);
				ArrayList<Link> al = new ArrayList<Link>();
				al.add(l);
				t.setProx(al);
			}
			else if(decisaoproxima!=null){
				String parts[] = decisaoproxima.split("\\.");
				Set<String> proxs = null;
				try {
					proxs = procDecisao(parts[parts.length-1]);
					log.debug("l: "+proxs);
					List<Link> outs = new ArrayList<Link>();
					for(String s: proxs) {
						Link l = new Link();
						String[] pp = s.split("\\.");
						l.setsDestino(pp[1]);
						outs.add(l);
					}
					t.setProx(outs);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			log.trace("<"+TELA+"> processed, l:"+nestLevel);
		}
	}
	
	Pattern pConstantesTelas = Pattern.compile("(ConstantesTelas\\.UC\\w+)");
	
	Set<String> procDecisao(String file) throws IOException {
		File f = new File("work/input/tmp/tela/"+file+".java");
		long length = f.length();
		char[] cb = new char[(int)length];
		//CharBuffer cb = CharBuffer.allocate((int)length);
		FileReader fr = new FileReader(f);
		int iread = fr.read(cb);
		String scontent = new String(cb); //cb.toString();
		
		//retirando comentários - "/* */", "//" 
		scontent = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL).matcher(scontent).replaceAll("");
		scontent = scontent.replaceAll("//.*", "");
		
		Matcher m = pConstantesTelas.matcher(scontent);
		int count = m.groupCount();
		log.debug("count = "+count+" // f:"+f+";"+length+";read:"+iread);
		//log.debug(scontent);

		Set<String> ret = new TreeSet<String>(); 
		while(m.find()) {
			//log.debug("i = "+i);
			String s = m.group();
			ret.add(s);
		}
		return ret;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equalsIgnoreCase(ROOT)) {
			nestLevel--;
			log.debug("</"+ROOT+"> processed, l:"+nestLevel);
		}

		if(qName.equalsIgnoreCase(TELA)) {
			log.trace("</"+TELA+"> processed, l:"+nestLevel);
		}
	}

}
