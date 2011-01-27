package tce.xmlxtra.model;

import tbrugz.graphml.model.Node;

public class Arquivo extends Node {
	String numeroLei;
	
	public String getNumeroLei() {
		return numeroLei;
	}
	public void setNumeroLei(String numeroLei) {
		this.numeroLei = numeroLei;
	}
	
	@Override
	public String getLabel() {
		return getId()+"\n"+super.getLabel()+"\n"+getNumeroLei();
	}
}
