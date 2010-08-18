package tbrugz.graphml.model;

import java.util.List;
import java.util.ArrayList;

import tbrugz.xml.model.skel.Element;

public class Tela implements Element {

	String codigo;
	List<Link> prox = new ArrayList<Link>();
	
	public String getId() {
		return getCodigo();
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public List<Link> getProx() {
		return prox;
	}

	public void setProx(List<Link> prox) {
		this.prox = prox;
	}

	
}
