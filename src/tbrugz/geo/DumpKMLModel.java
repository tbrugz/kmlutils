package tbrugz.geo;

import java.io.FileInputStream;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tbrugz.geo.model.Group;
import tbrugz.geo.model.Point;
import tbrugz.geo.model.Polygon;
import tbrugz.geo.model.Root;
import tbrugz.geo.model.skel.Composite;
import tbrugz.geo.model.skel.Element;

public class DumpKMLModel {

	static String levelStr = "  "; //"\t";
	
	static Log log = LogFactory.getLog(DumpKMLModel.class);

	Properties idMappings = new Properties();
	Properties snippets = new Properties();
	
	PrintStream output = null;
	
	public void dumpModel(Root root, PrintStream out) {
		this.output = out;
		loadProp(idMappings, "idmappings.properties");
		loadProp(snippets, "snippets.properties");
		dumpModel(root, 0);
	}
	
	void loadProp(Properties prop, String fileName) {
		try {
			prop.load(new FileInputStream(fileName));
		}
		catch(IOException ioe) {
			log.warn("Error loading id mappings file: "+ioe);
		} 
	}
	
	void dumpModel(Element elem, int level) {
		if(elem instanceof Root) {
			out("<kml xmlns=\"http://earth.google.com/kml/2.0\"><Document id=\""+getId(elem.getId())+"\"><name>"+getName(elem.getId())+"</name><visibility>0</visibility>", level);
			outSnippet("Document", level);
			//out("<root id='"+elem.getId()+"'>", level);
		}
		else if(elem instanceof Group) {
			out("<Folder id=\""+getId(elem.getId())+"\"><name>"+getName(elem.getId())+"</name><visibility>0</visibility>", level);
			outSnippet("Folder", level);
			//out("<g id='"+elem.getId()+"'>", level);
		}
		else if(elem instanceof Polygon) {
			Polygon polygon = ((Polygon)elem);
			StringBuffer b = new StringBuffer();
			for(Point p: polygon.points) {
				b.append(p.x+","+p.y+",0 ");
			}
			out("<Placemark id=\""+getId(elem.getId())+"\"><name>"+getName(elem.getId())+"</name><visibility>1</visibility>", level);

			//setting style...
			String style = getStyle(elem.getId());
			if(style==null) {
				int styleId = (int) (Math.random()*4+1);
				style = "style"+styleId;
			}
			out("<styleUrl>#"+style+"</styleUrl>", level);
			outSnippet("Placemark", level);

			//setting centre
			//out("<Point><coordinates>"+polygon.centre.x+","+polygon.centre.y+",0</coordinates></Point>", level);

			out("<Polygon id=\"Polygon"+getId(elem.getId())+"\"><outerBoundaryIs><LinearRing><coordinates>", level);
			out(b.toString(), level+1);
			out("</coordinates></LinearRing></outerBoundaryIs></Polygon>", level);
			out("</Placemark>", level);
		}
		else {
			out(">> unknown element: "+elem.getClass().getName(), level);
		}
		
		if(elem instanceof Composite) {
			for(Element e: ((Composite)elem).getChildren()) {
				dumpModel(e, level+1);
			}
		}

		if(elem instanceof Root) {
			out("</Document></kml>", level);
			//out("</root>", level);
		}
		else if(elem instanceof Group) {
			out("</Folder>", level);
			//out("</g>", level);
		}
	}
	
	String getId(String inputId) {
		if(idMappings==null) return inputId;
		String outputId = idMappings.getProperty(inputId+".id");
		
		if(outputId != null) return outputId;
		return inputId;
	}

	String getName(String inputId) {
		if(idMappings==null) return inputId;
		String outputName = idMappings.getProperty(inputId+".name");
		
		if(outputName != null) return outputName;
		return getId(inputId);
	}

	String getStyle(String inputId) {
		if(idMappings==null) return null;
		String outputStyleId = idMappings.getProperty(inputId+".styleId");
		
		if(outputStyleId != null) return outputStyleId;
		return null;
	}
	
	void outSnippet(String snippetId, int nestLevel) {
		String s = snippets.getProperty(snippetId);
		if(s!=null) out(s, nestLevel);
	}
	
	void out(String s, int nestLevel) {
		for(int i=0;i<nestLevel;i++) output.print(levelStr);
		output.println(s);
	}
}