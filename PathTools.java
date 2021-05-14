import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class PathTools {
	
	public static Path get_init_path(City city, int source){
		ArrayList<Path> neighbors = new ArrayList<Path>();
		double costs[] = city.getStoreAt(source)[1];
		for (double cost : Arrays.copyOfRange(costs, 0, costs.length -1)) {
			neighbors.add(new Path(city, source, cost));
		}
		return neighbors.isEmpty() ? new Path(city, source, 0) : Collections.max(neighbors);
	}
	
	public static ArrayList<Path> get_neighbors(Path correct_path){
		HashSet<Integer> neighbors_index = correct_path.getNeighbors();
		ArrayList<Path> result = new ArrayList<Path>();
		for (int i : neighbors_index) {
			double costs[] = correct_path.getCity().getStoreAt(i)[1];
			for(double cost: Arrays.copyOfRange(costs, 0, costs.length -1)){
				result.add(new Path(correct_path, i, cost));
			}
		}
		remove_all_not_optimal_path(correct_path.getCity(), correct_path, result);
		return result;
	}
	
	public static ArrayList<Path> get_no_back_neighbors(Path correct_path, double money){
		ArrayList<Path> result = get_neighbors(correct_path);
		result.forEach(p -> p.remove_back_neighbors());
		return result;
	}
	
	public static void remove_all_not_optimal_path(City city, Path father, List<? extends Path> list){
		Iterator<? extends Path> it = list.iterator();
		while (it.hasNext()) {
			Path path = it.next();
			if(path.getWeight() > father.getWeight() + city.getOptimalDist(father.getEndVertex(), path.getEndVertex())){
				it.remove();
			}
		}
	}
	
	public static void remove_all_over_cost_path(List<? extends Path> neighbors, double money) {
		neighbors.removeIf(path->path.getWeight() > money);
	}
	
	public static AntPath get_init_path(CityAnt city, int source){
		ArrayList<AntPath> neighbors = new ArrayList<AntPath>();
		double costs[] = city.getStoreAt(source)[1];
		for (double cost : Arrays.copyOfRange(costs, 0, costs.length -1)) {
			neighbors.add(new AntPath(city, source, cost));
		}
		return neighbors.isEmpty() ? new AntPath(city, source, 0) : Collections.max(neighbors);
	}
	
	public static ArrayList<AntPath> get_neighbors(CityAnt city_ant, AntPath correct_path, double money){
		HashSet<Integer> neighbors_index = correct_path.getNeighbors();
		ArrayList<AntPath> result = new ArrayList<AntPath>();
		for (int i : neighbors_index) {
			double costs[] = correct_path.getCity().getStoreAt(i)[1];
			for(double cost: Arrays.copyOfRange(costs, 0, costs.length -1)){
				result.add(new AntPath(correct_path, city_ant, i, cost));
			}
		}
		remove_all_not_optimal_path(correct_path.getCity(), correct_path, result);
		return result;
	}

	public static ArrayList<Triple> get_triple_neighbors(Triple father){
//		System.out.println("father: " + father);
		HashSet<Integer> neighbors_index = father.getNeighbors();
//		System.out.println("neighbors_index: " + neighbors_index);
		ArrayList<Triple> result = new ArrayList<Triple>();
		for (int i : neighbors_index) {
			double[] costs = father.getCity().getStoreAt(i)[1].clone();
			Arrays.sort(costs);
			result.add(new Triple(father, i, 0, costs[0]));
//			System.out.println(new Triple(father, i, 0, costs[0]));
			for (int j = 0; j < costs.length - 1; j++) {
				result.add(new Triple(father, i, costs[j], costs[j+1]));
//				System.out.println(new Triple(father, i, costs[j], costs[j+1]));
			}
			//result.add(new Triple(father, i, costs[costs.length - 2], costs[costs.length - 2]));
		}
//		result.removeIf(p->p.getMoney() > father.u_i);
		remove_all_not_optimal_path(father.getCity(), father, result);
		return result;
	}
	
	public static ArrayList<Triple> get_no_back_triple_neighbors(Triple father){
		ArrayList<Triple> result = get_triple_neighbors(father);
		result.forEach(p -> p.remove_back_neighbors());
		return result;
	}

	public static void remove_all_empty(List<Triple> neighbors) {
		Iterator<Triple> it = neighbors.iterator();
		while(it.hasNext()){
			Triple triple = it.next();
			if(triple.l_i > triple.u_i){
				it.remove();
			}
		}
	}
	
	
	public static void remove_all_bigger_threshold(List<Triple> neighbors, double threshold) {
		Iterator<Triple> it = neighbors.iterator();
		while(it.hasNext()){
			Triple triple = it.next();
			if(triple.getMoney() > threshold){
				it.remove();
			}
		}
	}
	
	public static void remove_all_longer_threshold(List<Triple> neighbors, int length_threshold) {
		Iterator<Triple> it = neighbors.iterator();
		while(it.hasNext()){
			Triple triple = it.next();
			if(triple.getLength() > length_threshold){
				it.remove();
			}
		}
	}
}
