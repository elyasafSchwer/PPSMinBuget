import java.io.IOException;
import java.util.HashMap;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class City {
	
	private SimpleWeightedGraph<Integer, DefaultWeightedEdge> graph;
	private HashMap<Integer, double[][]> stores;
	DijkstraShortestPath<Integer, DefaultWeightedEdge> dists;
	
	public City(String graph_file_name, String stores_file_name) throws IOException {
		this.graph = readFile.getGraphFromFile(graph_file_name);
		this.stores = readFile.getStoresFromFile(stores_file_name);
		this.dists = new DijkstraShortestPath<>(graph);
	}
	
	public City(City city) {
		this.graph = city.getGraph();
		this.stores = city.stores;
		this.dists = city.dists;
	}
	
	public double[][] getStoreAt(int i){
		return this.stores.get(i);
	}
	
	public void addFakeVerToStore(int fake){
		double [][] fake_store = {{1}, {Double.POSITIVE_INFINITY}};
		this.stores.put(fake, fake_store);
	}
	
	public SimpleWeightedGraph<Integer, DefaultWeightedEdge> getGraph(){
		return graph;
	}
	
	public double getOptimalDist(int u, int v){
		return this.dists.getPathWeight(u, v);
	}

}
