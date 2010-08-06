package tbrugz.geo.parser;

import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SVG2KML {
	static Log log = LogFactory.getLog(SVG2KML.class);

	public static void main(String[] args) throws IOException {
		SVGParser parser = new SVGParser();
		parser.parseDocument("work/input/Municipalities_of_RS.svg");

		log.info("\n\nparsed...\n\n");
		
		CoordinatesTransformer ct = new CoordinatesTransformer();
		float[] inputBounds = {parser.maxX, parser.minX, parser.maxY, parser.minY};
		float[] outputBounds = {-49.6917f, -57.64777f, -33.7515f, -27.08f,};
		//float[] outputBounds = {-49, -53, -33, -27,};
		//float[] outputBounds = {-49, -53, -27, -33,};
		ct.transformCoords(parser.root, inputBounds, outputBounds);

		log.info("\n\ncoords transformed...\n\n");
		
		DumpKMLModel dm = new DumpKMLModel();
		//dm.dumpModel(parser.root, System.out);
		dm.dumpModel(parser.root, new PrintStream("work/output/Municipalities_of_RS_out.kml"));
	}

}
