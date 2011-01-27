package tce.xmlxtra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import tce.xmlxtra.model.Arquivo;
import tce.xmlxtra.model.ArquivoLink;
import tbrugz.graphml.model.Root;

public class ArquivosPadXMLParser extends DefaultHandler {
	
	public static String ROOT = "arquivos";
	public static String NODE = "arquivo";
	
	//public static String ANY_ID = "id";
	
	Root root = null;
	//Composite lastGroupParsed = null;
	List<Arquivo> nodes = new ArrayList<Arquivo>();
	List<ArquivoLink> links = new ArrayList<ArquivoLink>();
	int nestLevel = 0;
	
	static Log log = LogFactory.getLog(ArquivosPadXMLParser.class);

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
				//lastGroupParsed = root;

				log.debug("<"+ROOT+"> processed, l:"+nestLevel);
				nestLevel++;
			}
			else {
				throw new RuntimeException("<"+ROOT+"> already declared");
			}
		}
		
		if(qName.equalsIgnoreCase(NODE)) {
			Arquivo aa = new Arquivo();
			//lastGroupParsed.getChildren().add(aa);
			String identificador = attributes.getValue("identificador");
			String nomeCompleto = attributes.getValue("nome-completo");
			//String prox = attributes.getValue("proxima-tela");
			String numeroLei = attributes.getValue("numero-lei");
			String depende = attributes.getValue("depende");
			
			aa.setId(identificador);
			aa.setNome(nomeCompleto);
			aa.setNumeroLei(numeroLei);
			nodes.add(aa);
			
			if(depende!=null && !depende.equals("")) {
				String[] deps = depende.split(",");
				for(String s: deps) {
					ArquivoLink link = new ArquivoLink();
					link.setIdOrigem(identificador);
					link.setIdDestino(s.trim());
					links.add(link);
				}
			}
			
			/*if(prox!=null) {
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
					proxs = procDecisao(parts[parts.length-1], pConstantesTelasNaoPadrao);
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
			}*/
			
			log.trace("<"+NODE+"> processed, l:"+nestLevel);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equalsIgnoreCase(ROOT)) {
			nestLevel--;

			root.elements.addAll(nodes);
			root.elements.addAll(links);

			log.debug("</"+ROOT+"> processed, l:"+nestLevel);
		}

		if(qName.equalsIgnoreCase(NODE)) {
			log.trace("</"+NODE+"> processed, l:"+nestLevel);
		}
	}

}
