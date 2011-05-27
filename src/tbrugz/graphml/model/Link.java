package tbrugz.graphml.model;

import tbrugz.xml.model.skel.Element;

public class Link implements Element, Stereotyped {
	String name;
	//Node source;
	//Node target;
	String target;
	String source;

	public String getName() {
		return name;
	}
	public void setName(String nome) {
		this.name = nome;
	}
	/*public Node getTarget() {
		return target;
	}
	public void setTarget(Node destino) {
		this.target = destino;
	}*/
	public String getTarget() {
		return target;
	}
	public void setTarget(String sTarget) {
		this.target = sTarget;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String sSource) {
		this.source = sSource;
	}

	public String getId() {
		return name;
	}
	public String toString() {
		return source+"->"+target;
	}
	public String getStereotype() {
		return null;
	}
	public void setStereotype(String s) {
	}
	
}
