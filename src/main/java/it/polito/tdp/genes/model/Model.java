package it.polito.tdp.genes.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	
	private GenesDao dao;
	private SimpleWeightedGraph<String,DefaultWeightedEdge> grafo;
	
	public Model() {
		this.dao= new GenesDao();
	}
	
	public List<String> getLocalizations(){
		return this.dao.getLocalization();
	}
	
	public List<Arco> getArchi(){
		return this.dao.getArco();
	}
	
	public void creaGrafo() {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//Aggiungo vertici
		Graphs.addAllVertices(this.grafo,this.getLocalizations());
		//Aggiungo gli archi
		for(Arco a : this.getArchi()) {
			Graphs.addEdgeWithVertices(this.grafo, a.getL1(), a.getL2(),a.getPeso());
		}
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Arco> getVicini(String localita){
		List<String > vicini = Graphs.neighborListOf(this.grafo,localita);
		List<Arco> result= new LinkedList<Arco>();
		Collections.sort(vicini);
		for(String s : vicini) {
			DefaultWeightedEdge e = this.grafo.getEdge(localita, s);
			int peso=(int) this.grafo.getEdgeWeight(e);
			Arco a = new Arco(localita,s,peso);
			result.add(a);
		}
		return result;
	}
	
	
	
	
	

}