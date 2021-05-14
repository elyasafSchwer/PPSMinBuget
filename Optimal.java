import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;


public class Optimal implements Heuristics{

	public Result alg(City city, double pro, int source) throws IOException{
		
		Timer timer = new Timer();
		Triple result = null;
		double cost_threshold = Double.POSITIVE_INFINITY;
		
//		FileWriter fw = new FileWriter(new File("op.txt"));

//		System.out.println(Arrays.deepToString(city.getStoreAt(source)));
		
		Stack<Triple> stack = new Stack<Triple>();
		double start_costs[] = city.getStoreAt(source)[1].clone();
		Arrays.sort(start_costs);
		stack.push(new Triple(city, source, 0, start_costs[0]));
		for (int i = 0; i < start_costs.length - 1; i++) {
			Triple new_triple = new Triple(city, source, start_costs[i], start_costs[i+1]);
			stack.push(new_triple);
		}
		
//		System.out.println("start: " + neighborsToString(new ArrayList<Triple>(stack)));
//		fw.write("start: " + neighborsToString(new ArrayList<Triple>(stack)) + "\n\n");
		
		while(!stack.isEmpty()){
			if(timer.timeOut()) {
				return null;
			}
			Triple father = stack.pop();
			
//			System.out.println("pop: " + father +"\n");
//			fw.write("pop: " + father + "\n\n");
			
			if((1 - father.get_fail_probabilitie()) >= pro){
				if(result == null){
					result = father;
				}
				else if(result.getMoney() > father.getMoney()){
					result = father;
				}
				cost_threshold = Math.min(cost_threshold, result.getMoney());
				PathTools.remove_all_bigger_threshold(stack, cost_threshold);
				
//				System.out.println("cost threshold now: " + cost_threshold);
//				fw.write("cost threshold now: " + cost_threshold + "\n\n");
			}
			if(((father.u_i - father.l_i) >= 0)){
				ArrayList<Triple> neighbors = PathTools.get_triple_neighbors(father);
				PathTools.remove_all_bigger_threshold(neighbors, cost_threshold);			
//				System.out.println("neighbors:" + neighborsToString(neighbors));
//				fw.write("neighbors:" + neighborsToString(neighbors));
				
				for(Triple path : neighbors){
					stack.push(path);
				}
			}
		}
		
//		System.out.println("result :" + result);
//		fw.write("result :" + result + "\n\n");
//		fw.close();
		if(result == null){
			timer.setTimeInf();
			return new Result(Double.POSITIVE_INFINITY, timer);
		}
		return new Result(result.getMoney(), timer);
	}

	public static String neighborsToString(ArrayList<Triple> neighbors){
		String result = "[ \n";
		for (Triple t : neighbors) {
			result += "\t\t" + t + "\n";
		}
		return result + "\t]" + "\n\n";
	}
}
