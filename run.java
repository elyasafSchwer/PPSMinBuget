import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;

public class run {

	public static Vector<Integer> getTestList(String test_file) throws IOException{
		Vector<Integer> result = new Vector<Integer>();
		FileReader fr = new FileReader(new File(test_file));
		BufferedReader br = new BufferedReader(fr);
		while(br.ready()){
			result.add(Integer.parseInt(br.readLine()));
		}
		br.close();
		fr.close();
		return result;
	}
	
	public static void printTestToFiles(City city, Vector<Integer> test, FileWriter fw_pro, FileWriter fw_time, Heuristics heur) throws IOException{
		HashSet<Integer> over_time_tests = new HashSet<Integer>();
		double pro = 0.48;
		while(pro <= 0.98 - 0.00001){
			StringBuilder sb_pro = new StringBuilder(""+pro+",");
			StringBuilder sb_time = new StringBuilder(""+pro+",");
			for (Integer v : test) {
				Result result = null;
				if(!over_time_tests.contains(v)) {
					result = heur.alg(city, pro, v);
					if(result == null) {
						over_time_tests.add(v);
					}
				}
				sb_pro.append(((result == null) ? "null" : result.getCost()) +",");
				sb_time.append(((result == null) ? "null" : result.getTime()) +",");
			}
			fw_pro.write(sb_pro.toString() + "\n");
			fw_time.write(sb_time.toString() + "\n");
			pro += 0.01;
		}
	}
	
	public static void main(String[] args) throws IOException {
		City city = new City("graph_with_distances.csv", "graph_with_probabilities_m1200.csv");
		//City city = new City("graph_with_distances_debug.csv", "graph_with_probabilities_debug.csv");
		Vector<Integer> test = getTestList("test100ver.csv");
		
		FileWriter fw_cost = new FileWriter(new File("result_cost.csv"));
		FileWriter fw_time = new FileWriter(new File("result_time.csv"));
		
		fw_cost.write("GEEDY\n");
		fw_time.write("GEEDY\n");
		printTestToFiles(city, test, fw_cost, fw_time, new Greedy());
		
		fw_cost.write("OPTIMAL\n");
		fw_time.write("OPTIMAL\n");
		printTestToFiles(city, test, fw_cost, fw_time, new Optimal());
	
		fw_cost.write("BL_G\n");
		fw_time.write("BL_G\n");
		printTestToFiles(city, test, fw_cost, fw_time, new BL_G());
		
		fw_cost.write("BL_R\n");
		fw_time.write("BL_R\n");
		printTestToFiles(city, test, fw_cost, fw_time, new BL_R());
		
		fw_cost.write("NB\n");
		fw_time.write("NB\n");
		printTestToFiles(city, test, fw_cost, fw_time, new NB());
		
		fw_cost.write("ACO\n");
		fw_time.write("ACO\n");
		printTestToFiles(city, test, fw_cost, fw_time, new ACO());
		
		fw_cost.close();
		fw_time.close();
	}

}
