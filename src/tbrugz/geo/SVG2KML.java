package tbrugz.geo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tbrugz.svg.model.Root;
import tbrugz.svg.parser.SVGParser;

public class SVG2KML {
	static Log log = LogFactory.getLog(SVG2KML.class);

	public static void main(String[] args) throws IOException {
		long initTime = System.currentTimeMillis();
		Properties prop = new Properties();
		prop.load(new FileInputStream("svg2kml.properties"));
		String fileIn = prop.getProperty("svg2kml.svgin");
		String fileOut = prop.getProperty("svg2kml.kmlout");
		float outMaxX = Float.parseFloat(prop.getProperty("svg2kml.maxX"));
		float outMinX = Float.parseFloat(prop.getProperty("svg2kml.minX"));
		float outMaxY = Float.parseFloat(prop.getProperty("svg2kml.maxY"));
		float outMinY = Float.parseFloat(prop.getProperty("svg2kml.minY"));
		
		log.info("parsing svg: "+fileIn);

		SVGParser parser = new SVGParser();
		Root root = parser.parseDocument(fileIn);

		log.info("parsed...");
		
		CoordinatesTransformer ct = new CoordinatesTransformer();
		float[] inputBounds = {root.maxX, root.minX, root.maxY, root.minY};
		float[] outputBounds = {outMaxX, outMinX, outMaxY, outMinY,};
		log.info("coords bounds: input: "+arrayToString(inputBounds)+", output: "+arrayToString(outputBounds));
		
		ct.transformCoords(root, inputBounds, outputBounds);

		log.info("coords transformed...");
		log.info("writing kml: "+fileOut);
		
		DumpKMLModel dm = new DumpKMLModel();
		
		String idMappingPropFile = prop.getProperty("svg2kml.idmappingsprop");
		if(idMappingPropFile!=null) {
			dm.loadIdMappings(idMappingPropFile);
		}
		
		dm.dumpModel(root, new PrintStream(fileOut));

		log.info("write done... time elapsed: "+(System.currentTimeMillis()-initTime)+"ms");
	}
	
	static String arrayToString(float[] arr) {
		StringBuffer sb = new StringBuffer();
		for(float elem: arr) {
			sb.append(elem+", ");
		}
		return "["+sb.toString()+"]";
	}

}
