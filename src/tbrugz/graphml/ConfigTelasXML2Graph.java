package tbrugz.graphml;

import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tbrugz.graphml.model.Root;

/* GXL: http://www.gupro.de/GXL/examples/instance/gxl/simpleExample/content.html
 * http://www.graphviz.org/Download_windows.php
 * http://sourceforge.net/projects/jgraph/
 * http://www.yworks.com/
 * 
 * ver tb: 
 * graphML - http://graphml.graphdrawing.org/
 * http://xmlgraphics.apache.org/batik/
 * 
 */
public class ConfigTelasXML2Graph {
	static Log log = LogFactory.getLog(ConfigTelasXML2Graph.class);

	public static void main(String[] args) throws IOException {
		//Properties prop = new Properties();
		//prop.load(new FileInputStream("xml2xmi.properties"));
		String fileIn = "work/input/config-telas.xml";
		String fileOut = "work/output/config-telas-out.graphml";
		
		log.info("parsing svg: "+fileIn);

		ConfigTelasXMLParser parser = new ConfigTelasXMLParser();
		//Root root = parser.parseDocument("work/input/Municipalities_of_RS.svg");
		Root root = parser.parseDocument(fileIn);

		log.info("parsed...");
		
		log.info("writing kml: "+fileOut);
		
		//DumpGXLModel dm = new DumpGXLModel();
		DumpGraphMLModel dm = new DumpGraphMLModel();
		dm.dumpModel(root, new PrintStream(fileOut));

		log.info("write done...");
	}

}
