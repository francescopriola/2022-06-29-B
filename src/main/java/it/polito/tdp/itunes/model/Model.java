package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private Graph<Album, DefaultWeightedEdge> graph;
	private ItunesDAO dao;
	private List<Album> best;
	
	public Model() {
		this.dao = new ItunesDAO();
	}
	
	public void creaGrafo(int n) {
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.graph, this.dao.getVertex(n));
		
		for(Album a1: this.graph.vertexSet()) {
			for(Album a2: this.graph.vertexSet()) {
				if(!a1.equals(a2)) {
					DefaultWeightedEdge edge = this.graph.getEdge(a1, a2);
					DefaultWeightedEdge edgeBack = this.graph.getEdge(a2, a1);
				
					double weight = (int)a1.getDurata()+(int)a2.getDurata();
				
					if(edge == null && edgeBack == null && a1.getDurata() != a2.getDurata() && weight > 4*n) {
						if(a1.getDurata() < a2.getDurata())
							edge = this.graph.addEdge(a1, a2);
						else 
							edge = this.graph.addEdge(a2, a1); 
					
						this.graph.setEdgeWeight(edge, weight);
					}
				}
			}
		}
		
		System.out.println("#VERTICI: " + this.graph.vertexSet().size());
		System.out.println("#ARCHI: " + this.graph.edgeSet().size());
		
		for(Album a : this.graph.vertexSet()) {
			double sommaIn = 0;
			for(DefaultWeightedEdge entrante : this.graph.incomingEdgesOf(a))
				sommaIn += (int)this.graph.getEdgeWeight(entrante);
			
			double sommaOut = 0;
			for(DefaultWeightedEdge uscente : this.graph.outgoingEdgesOf(a))
				sommaOut += (int)this.graph.getEdgeWeight(uscente);
			
			double bilancio = sommaIn - sommaOut;
			a.setBilancio(bilancio);
		}
	}
	
	public String getNVertex() {
		return "#VERTICI: " + this.graph.vertexSet().size();
	}
	
	public String getNEdge() {
		return "#ARCHI: " + this.graph.edgeSet().size();
	}
	
	public Graph<Album, DefaultWeightedEdge> getGraph(){
		return this.graph;
	}
	
	public String getAdiacenze(Album a1) {
		String res = "";
		List<Album> adiacenze = new LinkedList<>();
		
		for(Album a : Graphs.neighborListOf(this.graph, a1))
			adiacenze.add(a);
		
		Collections.sort(adiacenze);
		for(Album a : adiacenze)
			res += a.getTitle() + ", Bilancio= " + a.getBilancio() + "\n";
		
		return res;
	}
	
	public String calcolaPercorso(Album a1, Album a2, int x) {
		this.best = new ArrayList<Album>();
		List<Album> parziale = new ArrayList<Album>();
		parziale.add(a1);
		
		this.ricorsiva(parziale, a1, a2, x, a1);
		
		String res = "Percorso:\n";
		for(Album a: this.best)
			res += a+"\n";
			
		return res;
	}

	private void ricorsiva(List<Album> parziale, Album a1, Album a2, int x, Album last) {
		if(last.equals(a2)) {
			if(parziale.size() > this.best.size()) 
				this.best = new ArrayList<Album>(parziale);
		} else {
			for(Album a: Graphs.neighborListOf(this.graph, last)) {
				if(!parziale.contains(a)) {
					DefaultWeightedEdge edge = this.graph.getEdge(last, a);
					double weight = 0.0;
					if(edge != null)
						weight = this.graph.getEdgeWeight(edge);
					
					if(weight >= x) {
						if(a.getBilancio() > a1.getBilancio()) {
							parziale.add(a);
							this.ricorsiva(parziale, a1, a2, x, a);
							parziale.remove(a);
						}
					}
				}
			}
		}
		
	}
}
