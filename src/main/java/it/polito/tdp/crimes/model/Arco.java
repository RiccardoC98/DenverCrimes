package it.polito.tdp.crimes.model;

public class Arco {
	String type1;
	String type2;
	int weight;
	public Arco(String type1, String type2, int weight) {
		super();
		this.type1 = type1;
		this.type2 = type2;
		this.weight = weight;
	}
	public String getType1() {
		return type1;
	}
	public String getType2() {
		return type2;
	}
	public int getWeight() {
		return weight;
	}
	
	
}
