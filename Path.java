import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;


public class Path implements GraphPath<Integer, DefaultWeightedEdge>, Comparable<Path>{
	private City city;
	private ArrayList<Integer> list;
	private SimpleWeightedGraph<Integer, DefaultWeightedEdge> path_graph;
	private HashSet<Integer> next_neighbors;
	private double weight = 0;
	private double S_P_v_c = Double.NEGATIVE_INFINITY;
	protected double fail_probabilitie;
	private double money;
	
	public Path(City city, int start, double cost) {
		this.city = city;
		this.list = new ArrayList<Integer>(Arrays.asList(start));
		init_path_graph(start);
		this.next_neighbors = new HashSet<Integer>(Graphs.neighborSetOf(city.getGraph(), start));
		this.fail_probabilitie = Formulas.get_local_fail_probabilitie(city.getStoreAt(start), cost);
		this.money = cost;
		this.S_P_v_c = (1 - this.fail_probabilitie) / cost;
	}
	
	private void init_path_graph(int start) {
		this.path_graph = new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.path_graph.addVertex(start);
	}
	
	//build path from last path and next step
	public Path(Path last_list, int next_step, double cost){
		init_path(last_list);
		add_next_vertex_to_graph_path(next_step);
		//find the sort path on path graph
		GraphPath<Integer, DefaultWeightedEdge> next_steps = DijkstraShortestPath.findPathBetween(this.path_graph, last_list.getEndVertex(), next_step);
		concatenation_paths(this.list, next_steps.getVertexList());
		update_values(next_step, cost, next_steps);
	}
	
	private void update_values(int next_step, double cost, GraphPath<Integer, DefaultWeightedEdge> next_steps) {
		double steps_weight = next_steps.getWeight();
		this.money = this.weight + Math.max((this.money - this.weight), steps_weight + cost);
		this.money = (this.money >= this.weight + cost) ? this.money : this.money + steps_weight + cost; 
		this.weight += steps_weight;
		double next_step_fail_probabilitie = Formulas.get_local_fail_probabilitie(city.getStoreAt(next_step), this.money - this.weight);
		this.fail_probabilitie = Formulas.path_fail_probabilitie(this.money, this.list, this.city);
		this.S_P_v_c = (1 - next_step_fail_probabilitie) / (steps_weight * cost);
		next_neighbors.addAll(Graphs.neighborSetOf(city.getGraph(), next_step));
		remove_if_contains_in_path(next_neighbors);		
	}
	
	private void add_next_vertex_to_graph_path(int next_step){
		this.path_graph.addVertex(next_step);
		for(int i : Graphs.neighborSetOf(city.getGraph(), next_step)) {
			for (Integer j : list) {
				if(i == j) {
					this.path_graph.addEdge(j, next_step, city.getGraph().getEdge(j, next_step));
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void init_path(Path last_list) {
		this.path_graph = (SimpleWeightedGraph<Integer, DefaultWeightedEdge>) last_list.path_graph.clone();
		this.city = last_list.city;
		this.list = new ArrayList<>(last_list.list);
		this.weight = last_list.weight;
		this.money = last_list.money;
		this.fail_probabilitie = last_list.fail_probabilitie;
		this.next_neighbors = new HashSet<Integer>(last_list.getNeighbors());
	}
	
	private void remove_if_contains_in_path(HashSet<Integer> neighbors) {
		Iterator<Integer> it = neighbors.iterator();
		while(it.hasNext()) {
			int v = it.next();
			if(this.list.contains(v)) {
				it.remove();
			}
		}
	}
	//remove back neighbors for NB heuristic
	public void remove_back_neighbors() {
		next_neighbors.clear();
		next_neighbors.addAll(Graphs.neighborSetOf(city.getGraph(), getEndVertex()));
		remove_if_contains_in_path(next_neighbors);
	}
	
	private void concatenation_paths(List<Integer> list1, List<Integer> list2) {
		list1.addAll(list2.subList(1, list2.size()));
	}
	
	@Override
	public List<Integer> getVertexList() {
		return this.list;
	}
	
	public Integer getEndVertex() {
		return this.list.get(this.list.size() - 1);
	}

	public Graph<Integer, DefaultWeightedEdge> getGraph() {
		return this.city.getGraph();
	}

	public Integer getStartVertex() {
		return this.list.get(0);
	}

	public double getWeight() {
		return this.weight;
	}

	public double getMoney() {
		return this.money;
	}
	
	public int compareTo(Path other) {
		return Double.compare(this.S_P_v_c, other.S_P_v_c);
	}
	
	public double get_fail_probabilitie(){
		return this.fail_probabilitie;
	}
	public int getLength() {
		return this.list.size() - 1;
	}
	public double getS_P_v() {
		return S_P_v_c;
	}
	public String toString() {
		return "" + list + " S_P_v_c: " + String.format("%.05f", S_P_v_c) + " fail_pro: " + String.format("%.05f", fail_probabilitie) + " weight: " + weight + " money: " + money;
	}
	public HashSet<Integer> getNeighbors(){
		return this.next_neighbors;
	}

	public City getCity() {
		return this.city;
	}
	
	protected void set_fail_probabilitie(double pro){
		this.fail_probabilitie = pro;
	}
	protected void setMoney(double money){
		this.money = money;
	}
	protected String getListString(){
		return "" + this.list;
	}
}
