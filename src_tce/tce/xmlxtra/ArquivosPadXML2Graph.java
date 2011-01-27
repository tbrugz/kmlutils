package tce.xmlxtra;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

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
public class ArquivosPadXML2Graph {
	static Log log = LogFactory.getLog(ArquivosPadXML2Graph.class);

	public static void main(String[] args) throws IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream("arquivospad2graph.properties"));
		String fileIn = prop.getProperty("xml2graph.xmlin"); // "work/input/tmp/mci/config-telas.xml";
		String fileOut = prop.getProperty("xml2graph.xmlout"); // "work/output/mci/config-telas-out.2010-09-15.graphml";
		
		log.info("parsing svg: "+fileIn);

		ArquivosPadXMLParser parser = new ArquivosPadXMLParser();
		Root root = parser.parseDocument(fileIn);

		log.info("parsed...");
		
		log.info("writing kml: "+fileOut);
		
		//DumpGXLModel dm = new DumpGXLModel();
		DumpArquivosPADGraphMLModel dm = new DumpArquivosPADGraphMLModel();
		dm.dumpModel(root, new PrintStream(fileOut, "UTF-8"));

		log.info("write done...");
	}

}
