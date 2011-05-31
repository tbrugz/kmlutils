package tbrugz.geo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tbrugz.geo.model.Root;
import tbrugz.geo.parser.SVGParser;
import tbrugz.graphml.DumpGraphMLModel;

public class SVG2GraphML {
	static Log log = LogFactory.getLog(SVG2GraphML.class);
	
	public static void main(String[] args) throws IOException {
		long initTime = System.currentTimeMillis();
		Properties prop = new Properties();
		prop.load(new FileInputStream("svg2graphml.properties"));
		String fileIn = prop.getProperty("svg2graphml.svgin");
		String fileOut = prop.getProperty("svg2graphml.graphmlout");
		float outMaxX = Float.parseFloat(prop.getProperty("svg2kml.maxX"));
		float outMinX = Float.parseFloat(prop.getProperty("svg2kml.minX"));
		float outMaxY = Float.parseFloat(prop.getProperty("svg2kml.maxY"));
		float outMinY = Float.parseFloat(prop.getProperty("svg2kml.minY"));
		
		log.info("parsing svg: "+fileIn);

		SVGParser parser = new SVGParser();
		Root root = parser.parseDocument(fileIn);

		log.info("parsed...");
		
		SVG2GraphTransformer svgt = new SVG2GraphTransformer();
		tbrugz.graphml.model.Root graphmlRoot = svgt.toGraphML(root);
		
		CoordinatesTransformer ct = new CoordinatesTransformer();
		float[] inputBounds = {root.maxX, root.minX, root.maxY, root.minY};
		float[] outputBounds = {outMaxX, outMinX, outMaxY, outMinY,};
		log.info("coords bounds: input: "+SVG2KML.arrayToString(inputBounds)+", output: "+SVG2KML.arrayToString(outputBounds));
		
		ct.transformCoords(root, inputBounds, outputBounds);

		log.info("coords transformed...");
		log.info("writing graplml: "+fileOut);
		
		DumpGraphMLModel dm = new DumpGraphMLModel();
		dm.loadSnippets("graphml-geo-snippets.properties");

		/*
		String idMappingPropFile = prop.getProperty("svg2kml.idmappingsprop");
		if(idMappingPropFile!=null) {
			dm.loadIdMappings(idMappingPropFile);
		}
		*/

		dm.dumpModel(graphmlRoot, new PrintStream(fileOut));

		log.info("write done... time elapsed: "+(System.currentTimeMillis()-initTime)+"ms");
	}

}
