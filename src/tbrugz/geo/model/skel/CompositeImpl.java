package tbrugz.geo.model.skel;

import java.util.ArrayList;
import java.util.List;

public class CompositeImpl implements Composite {
	public String id;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public List<Element> elements = new ArrayList<Element>();

	public List<Element> getChildren() {
		return elements;
	}
}
