package tbrugz.geo;

import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tbrugz.geo.model.Root;
import tbrugz.geo.parser.SVGParser;

public class SVG2KML {
	static Log log = LogFactory.getLog(SVG2KML.class);

	public static void main(String[] args) throws IOException {
		SVGParser parser = new SVGParser();
		Root root = parser.parseDocument("work/input/Municipalities_of_RS.svg");

		log.info("parsed...");
		
		CoordinatesTransformer ct = new CoordinatesTransformer();
		float[] inputBounds = {root.maxX, root.minX, root.maxY, root.minY};
		float[] outputBounds = {-49.6917f, -57.64777f, -33.7515f, -27.08f,};
		ct.transformCoords(root, inputBounds, outputBounds);

		log.info("coords transformed...");
		
		DumpKMLModel dm = new DumpKMLModel();
		//dm.dumpModel(parser.root, System.out);
		dm.dumpModel(root, new PrintStream("work/output/Municipalities_of_RS_out.kml"));

		log.info("dump done...");
	}

}
