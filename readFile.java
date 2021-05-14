import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class readFile {
	
	public static SimpleWeightedGraph<Integer, DefaultWeightedEdge> getGraphFromFile(String file_name) throws IOException{
		SimpleWeightedGraph<Integer, DefaultWeightedEdge>  graph = 
				new SimpleWeightedGraph<Integer, DefaultWeightedEdge>
		(DefaultWeightedEdge.class); 
		FileReader fr = new FileReader(file_name);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		String arr[];
		DefaultWeightedEdge e;
		int u, v;
		while(line != null){
			arr = line.split(",");
			u = Integer.parseInt(arr[0]);
			v = Integer.parseInt(arr[1]);
			graph.addVertex(u);
			graph.addVertex(v);
			if(u != v && !graph.containsEdge(v, u)){
				e = graph.addEdge(v, u); 
				graph.setEdgeWeight(e, Double.parseDouble(arr[2]));
			}
			line = br.readLine();
		}
		fr.close();
		br.close();
		return graph;
	}

	public static HashMap<Integer, double[][]> getStoresFromFile(String file_name) throws IOException{
		HashMap<Integer, double[][]> stores = new HashMap<Integer, double[][]>();
		FileReader fr = new FileReader(file_name);
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		String arr[];
		int n;
		int probabilities;
		int i;
		double[][] store;
		double sum_probabilities;
		while(line != null){
			arr = line.split(",");
			n = Integer.parseInt(arr[0]);
			probabilities = Integer.parseInt(arr[1]);
			store = new double[2][probabilities + 1];
			i = 0;
			sum_probabilities = 0;
			while(i < probabilities){
				store[0][i] = Double.parseDouble(arr[2 + 2*i]);
				store[1][i] = Double.parseDouble(arr[2 + 2*i + 1]);
				sum_probabilities += store[0][i];
				i++;
			}
			store[0][i] = 1 - sum_probabilities;
			store[1][i] = Double.POSITIVE_INFINITY;
			stores.put(n, store);
			line = br.readLine();
		}
		fr.close();
		br.close();
		return stores;
	}

}