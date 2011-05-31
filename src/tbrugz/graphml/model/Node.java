package tbrugz.graphml.model;

import tbrugz.xml.model.skel.Element;

public class Node implements Element, Stereotyped {

	String id;
	String label;
	boolean initialNode;
	boolean finalNode;
	
	//List<Link> prox = new ArrayList<Link>();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/*public List<Link> getProx() {
		return prox;
	}

	public void setProx(List<Link> prox) {
		this.prox = prox;
	}*/

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getStereotype() {
		return null;
	}

	public void setStereotype(String s) {}

	public String getStereotypeParam(int i) {
		return null;
	}

	public int getStereotypeParamCount() {
		return 0;
	}
}
