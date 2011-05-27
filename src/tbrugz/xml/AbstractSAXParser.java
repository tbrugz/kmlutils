package tbrugz.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.org.apache.xerces.internal.impl.Constants;

import tbrugz.xml.model.skel.Composite;
import tbrugz.xml.model.skel.CompositeImpl;

public abstract class AbstractSAXParser extends DefaultHandler {
	
	protected CompositeImpl root = null;
	//int nestLevel = 0;
	
	static Log log = LogFactory.getLog(AbstractSAXParser.class);

	public Composite parseDocument(String file) {
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setValidating(false);  
		//spf.setNamespaceAware(false);
		//spf.setSchema(null);
		String LOAD_EXTERNAL_DTD_FEATURE = Constants.XERCES_FEATURE_PREFIX + Constants.LOAD_EXTERNAL_DTD_FEATURE;
		
		/*
		 * about validating:
		 * LOAD_EXTERNAL_DTD_FEATURE = "http://apache.org/xml/features/"+"nonvalidating/load-external-dtd"
		 * http://www.coderanch.com/t/128153/XML/SAXParser-not-Validating
		 * http://sax.sourceforge.net/?selected=get-set
		 * http://www.ibm.com/developerworks/library/x-tipvalschm/
		 * http://www.ibm.com/developerworks/xml/library/x-tipsaxp/
		 * 
		 */

		try {
			spf.setFeature(LOAD_EXTERNAL_DTD_FEATURE, false);

			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			
			// Turn off validation
			//sp.setProperty("http://xml.org/sax/features/validation", false); //setFeature("http://xml.org/sax/features/validation", true);

			log.info("is saxParser validating? " + sp.isValidating());
			
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
	
}
