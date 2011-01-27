package tce.xmlxtra.model;

import tbrugz.xml.model.skel.Element;

public class Arquivo implements Element {
	String id;
	String nome;
	String numeroLei;

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNumeroLei() {
		return numeroLei;
	}
	public void setNumeroLei(String numeroLei) {
		this.numeroLei = numeroLei;
	}
	
	public String getDesc() {
		return getId()+"\n"+getNome()+"\n"+getNumeroLei();
	}
	
}
