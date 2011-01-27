package tbrugz.graphml.model;

import tbrugz.xml.model.skel.Element;

public class Link implements Element {
	String nome;
	Node destino;
	String sDestino;
	String origem;

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Node getDestino() {
		return destino;
	}
	public void setDestino(Node destino) {
		this.destino = destino;
	}
	public String getsDestino() {
		return sDestino;
	}
	public void setsDestino(String sDestino) {
		this.sDestino = sDestino;
	}
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getId() {
		return origem+"->"+sDestino;
	}
	
}
