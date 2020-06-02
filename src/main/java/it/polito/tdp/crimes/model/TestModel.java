package it.polito.tdp.crimes.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model mo = new Model();
		
		mo.creaGrafo("drug-alcohol", java.time.Month.MARCH);
		System.out.println("Archi con peso maggiore della media: \n");
		System.out.println(mo.getArchiMaggioriDellaMedia());
	}

}
