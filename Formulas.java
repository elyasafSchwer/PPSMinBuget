import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Formulas {
	public static double get_local_fail_probabilitie(double[][] store, double money){
		double result = 0;
		for (int i = 0; i < store[0].length; i++) {
			if(store[1][i] > money){
				result += store[0][i];
			}
		}
		return result;
	}
	
	public static double path_fail_probabilitie(double money, List<Integer> path, City city){
		SimpleWeightedGraph<Integer, DefaultWeightedEdge> graph = city.getGraph();
		HashSet<Integer> old_vertex = new HashSet<Integer>();
		double remainder = money;
		double result = 1;
		Iterator<Integer> it = path.iterator();
		int i = it.next(), j;
		while(it.hasNext()){
			if(!old_vertex.contains(i)){
				result *= Formulas.get_local_fail_probabilitie(city.getStoreAt(i), remainder);
				old_vertex.add(i);
			}
			if(it.hasNext()){
				j = i;
				i = it.next();
				remainder -= graph.getEdgeWeight(graph.getEdge(j, i));
			}
		}
		result *= Formulas.get_local_fail_probabilitie(city.getStoreAt(i), remainder);
		return result;
	}
}
