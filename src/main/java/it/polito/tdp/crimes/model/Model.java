package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private EventsDao dao = new EventsDao();
	private SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	private float mediaPeso;
		
	public List<String> getAllCategories() {
		return dao.getAllCategories();
	}
	
	public void creaGrafo(String categoria, java.time.Month mese) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class) ;
		this.mediaPeso = 0;
		float counter = 0; // degli archi che hanno senso nella media
		 
		List<String> offense_types =  dao.getOffenseTypes(categoria, mese);
		Graphs.addAllVertices(this.grafo, offense_types);
		
		List<Arco> archi = dao.getEdges(offense_types);
		
		for (Arco a : archi) {
			if (a.getWeight() != 0 ) { // non li considero 
				Graphs.addEdge(this.grafo, a.getType1(), a.getType2(), a.getWeight());
				mediaPeso += a.getWeight();
				counter++;
			}
		}
		
		this.mediaPeso = mediaPeso / counter;

		System.out.println("Grafo creato: \n#vertici: " + grafo.vertexSet().size() +
				"\n#archi: " + grafo.edgeSet().size());
	}
	
	public List<DefaultWeightedEdge> getArchiMaggioriDellaMedia() {
		List<DefaultWeightedEdge> list = new ArrayList<>();
		
		for (DefaultWeightedEdge dfe : this.grafo.edgeSet() ) {
			if (this.grafo.getEdgeWeight(dfe) > mediaPeso) {
				list.add(dfe);
				
			}
		}
		return list;
	}
	
}
