package tbrugz.geo;

import java.io.PrintStream;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tbrugz.geo.model.Group;
import tbrugz.geo.model.Point;
import tbrugz.geo.model.Polygon;
import tbrugz.geo.model.Root;
import tbrugz.xml.AbstractDump;
import tbrugz.xml.model.skel.Composite;
import tbrugz.xml.model.skel.Element;

class StringUniquener {
	Set<String> stringSet = new TreeSet<String>();
	
	public String getUnique(String s) {
		if(stringSet.contains(s)) {
			return getUnique(s+"z");
		}
		stringSet.add(s);
		return s;
	}
}

public class DumpKMLModel extends AbstractDump {

	static Log log = LogFactory.getLog(DumpKMLModel.class);

	Properties idMappings = new Properties();
	StringUniquener uniqueIds = new StringUniquener();
	
	@Override
	public void dumpModel(Element root, PrintStream out) {
		loadSnippets("snippets.properties");
		super.dumpModel(root, out);
	}
	
	public void loadIdMappings(String propFile) {
		log.info("loading mappings file: "+propFile);
		loadProp(idMappings, propFile); //"idmappings.properties");
	}
	
	@Override
	public void dumpModel(Element elem, int level) {
		if(elem instanceof Root) {
			outSnippet("Document", level, uniqueIds.getUnique( normalizeId( getId(elem.getId()) ) ), getName(elem.getId()) );
		}
		else if(elem instanceof Group) {
			outSnippet("Folder", level, uniqueIds.getUnique( normalizeId( getId(elem.getId()) ) ), getName(elem.getId()));
		}
		else if(elem instanceof Polygon) {
			Polygon polygon = ((Polygon)elem);
			StringBuffer b = new StringBuffer();
			for(Point p: polygon.points) {
				b.append(p.x+","+p.y+",0 ");
			}
			//adding first point again for a closed Polygon
			b.append(polygon.points.get(0).x+","+polygon.points.get(0).y+",0 ");

			outSnippet("Placemark", level, 
					uniqueIds.getUnique( normalizeId( getId(elem.getId()) ) ), //{0}
					getName(elem.getId()), //{1} 
					getProp(elem.getId(), "description", "#id = "+getId(elem.getId())), //{2}
					getProp(elem.getId(), "long"), //{3}
					getProp(elem.getId(), "lat"), //{4}
					b.toString() //polygon border, {5}
					);

			//XXX: centroid?
			//out("<Point><coordinates>"+polygon.centre.x+","+polygon.centre.y+",0</coordinates></Point>", level);

			//outSnippet("Polygon", level+1, getId(elem.getId()), b.toString());
			
			outSnippet("Placemark.end", level); 
		}
		else {
			log.warn("unknown element: "+elem.getClass().getName());
			//out(">> unknown element: "+elem.getClass().getName(), level);
		}
		
		if(elem instanceof Composite) {
			for(Element e: ((Composite)elem).getChildren()) {
				dumpModel(e, level+1);
			}
		}

		if(elem instanceof Root) {
			outSnippet("Document.end", level); 
		}
		else if(elem instanceof Group) {
			outSnippet("Folder.end", level); 
		}
	}
	
	String getId(String inputId) {
		if(idMappings==null) return inputId;
		String outputId = idMappings.getProperty(inputId+".id");
		
		if(outputId != null) return outputId;
		return inputId;
	}

	String getName(String inputId) {
		return getProp(inputId, "name", getId(inputId));
	}

	String getProp(String inputId, String prop) {
		return getProp(inputId, prop, getId(inputId));
	}
	
	String getProp(String inputId, String prop, String defaultValue) {
		if(idMappings==null) return inputId;
		String outputName = idMappings.getProperty(inputId+"."+prop); //description
		
		if(outputName != null) return outputName;
		return defaultValue;
	}
	
	String getStyle(String inputId) {
		return getProp(inputId, "styleId", getId(inputId));
	}
	
	static String normalizeId(String s) {
		if(s==null) return "idnull";
		return s.replaceAll(" ", "-");
	}
	
}
