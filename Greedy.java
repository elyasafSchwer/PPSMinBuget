import java.util.ArrayList;
import java.util.Collections;

public class Greedy implements Heuristics{

	public Result alg(City city, double pro, int source){
		Timer timer = new Timer();
		Path result = PathTools.get_init_path(city, source);
//		System.out.println("start: " + result);
		ArrayList<Path> neighbors = PathTools.get_neighbors(result);
		while(!neighbors.isEmpty()){
			if((1 - result.get_fail_probabilitie()) >= pro){
//				System.out.println("result : " + result);
				return new Result(result.getMoney(), timer);
			}	
			if(timer.timeOut()) {
				return null;
			}
//			System.out.println("neighbors: " + neighbors);
			result = Collections.max(neighbors);
//			System.out.println("result now: " + result);
			neighbors = PathTools.get_neighbors(result);
		}
//		System.out.println("result : " + result);
		if(1 - result.get_fail_probabilitie() < pro){
			timer.setTimeInf();
			return new Result(Double.POSITIVE_INFINITY, timer);
		}
		return new Result(result.getMoney(), timer);
	}

}
