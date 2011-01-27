package tce.xmlxtra.model;

import tbrugz.graphml.model.Link;

public class ArquivoLink extends Link {
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
