
import java.util.ArrayList;

public class ACO implements Heuristics{
	
	public Result alg(City city, double pro, int source){
		Timer timer = new Timer();
		CityAnt city_ant = new CityAnt(city);
		AntPath result = iteration(1, city_ant, pro, source);
		for (int i = 2; i <= 50; i++) {
			if(timer.timeOut()) {
				return null;
			}
			AntPath new_path = iteration(i, city_ant, pro, source);
//			System.out.println("PATH :" + new_path);
			city_ant.update_ants(); // ant-- for each ant
			if(result.getMoney() > new_path.getMoney()){
				result = new_path;
				city_ant.update_ants_by_path(new_path);
			}
		}
		if(1 - result.get_fail_probabilitie() < pro){
			timer.setTimeInf();
			return new Result(Double.POSITIVE_INFINITY, timer);
		}
		return new Result(result.getMoney(), timer);
	}
	
	private AntPath iteration(int i, CityAnt city_ant, double pro, int source){
//		System.out.println("ITERATION " + i);
		AntPath new_path = PathTools.get_init_path(city_ant, source);
//		System.out.println("start: " + new_path);
		ArrayList<AntPath> neighbors = PathTools.get_neighbors(city_ant, new_path, pro);
		while(neighbors.size() > 0 && (1 - new_path.get_fail_probabilitie()) < pro){
//			System.out.println("neighbors: " + neighbors);
			new_path = choose_path_acoording_spv_hbp(neighbors);
//			System.out.println("new_path: " + new_path);
			neighbors = PathTools.get_neighbors(city_ant, new_path, pro);
		}
		return new_path;
	}
	
	private AntPath choose_path_acoording_spv_hbp(ArrayList<AntPath> neighbors) {
		double sum = 0;
		for (AntPath antPath : neighbors) {
			sum += (antPath.getS_P_v()) * antPath.getH_v_p();
			antPath.setValue(sum);
		}
		double choose = Math.random() * sum;
		for (AntPath antPath : neighbors) {
			if(choose <= antPath.getValue()){
				return antPath;
			}
		}
		return neighbors.get(neighbors.size() - 1);
	}

}
