import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

public class BL_G implements Heuristics{

	public Result alg(City city, double pro, int source){
		Timer timer = new Timer();
		Triple result = null;
		double cost_threshold = Double.POSITIVE_INFINITY;
		int length_threshold = Integer.MAX_VALUE;

//		System.out.println("start: " + init);

		Stack<Triple> stack = new Stack<Triple>();
		double start_costs[] = city.getStoreAt(source)[1].clone();
		Arrays.sort(start_costs);
		stack.push(new Triple(city, source, 0, start_costs[0]));
		for (int i = 0; i < start_costs.length - 1; i++) {
			Triple new_triple = new Triple(city, source, start_costs[i], start_costs[i+1]);
			stack.push(new_triple);
		}
		
		while(!stack.isEmpty()){
			if(timer.timeOut()) {
				return null;
			}
			Triple father = stack.pop();
//			System.out.println("pop: " + father);
			if((1 - father.get_fail_probabilitie()) >= pro){
				if(result == null){
					result = father;
				}
				else if(result.getMoney() > father.getMoney()){
					result = father;
				}
				length_threshold = Math.min(length_threshold, result.getLength());
				cost_threshold = Math.min(cost_threshold, result.getMoney());
				PathTools.remove_all_bigger_threshold(stack, cost_threshold);
				PathTools.remove_all_longer_threshold(stack, length_threshold);
//				System.out.println("length threshold now: " + length_threshold + " cost threshold now: " + cost_threshold);
			}
			if(((father.u_i - father.l_i) >= 0)){
				ArrayList<Triple> neighbors = PathTools.get_triple_neighbors(father);
				PathTools.remove_all_bigger_threshold(neighbors, cost_threshold);
				PathTools.remove_all_longer_threshold(neighbors, length_threshold);
//				System.out.println("neighbors: " + neighbors);
				while (!neighbors.isEmpty()){
					Triple neighbor = Collections.min(neighbors);
					neighbors.remove(neighbor);
					stack.push(neighbor);
				}
			}
		}
		if(result == null){
			timer.setTimeInf();
			return new Result(Double.POSITIVE_INFINITY, timer);
		}
		return new Result(result.getMoney(), timer);
	}
	
}
