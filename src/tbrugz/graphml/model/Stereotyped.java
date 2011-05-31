package tbrugz.graphml.model;

public interface Stereotyped {
	String getStereotype();
	void setStereotype(String s);
	String getStereotypeParam(int i);
	int getStereotypeParamCount();
}
