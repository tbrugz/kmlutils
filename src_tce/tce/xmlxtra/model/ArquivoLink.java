package tce.xmlxtra.model;

import tbrugz.xml.model.skel.Element;

public class ArquivoLink implements Element {
	String idOrigem;
	String idDestino;

	public String getIdOrigem() {
		return idOrigem;
	}
	public void setIdOrigem(String idOrigem) {
		this.idOrigem = idOrigem;
	}
	public String getIdDestino() {
		return idDestino;
	}
	public void setIdDestino(String idDestino) {
		this.idDestino = idDestino;
	}
	
	public String getId() {
		return idOrigem+"->"+idDestino;
	}
	
}
