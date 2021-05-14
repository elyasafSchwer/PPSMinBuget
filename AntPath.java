

public class AntPath extends Path{
	double h_v_p = 1;
	double value = -1;
	public AntPath(CityAnt city, int start, double money) {
		super(city, start, money);
	}
	
	public AntPath(Path last_list, CityAnt city_ant, int next_step, double cost) {
		super(last_list, next_step, cost);
		this.h_v_p = 0;
		for(int i = last_list.getVertexList().size() - 1; i < getVertexList().size() - 1; i++){
			this.h_v_p += city_ant.get_ant_of(getGraph().getEdge(getVertexList().get(i), getVertexList().get(i + 1)));
		}
		this.h_v_p = this.h_v_p / (getLength() - last_list.getLength());
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public double getH_v_p() {
		return h_v_p;
	}
	@Override
	public String toString() {
		return super.toString() + " hvp: " + this.h_v_p;
	}
}
