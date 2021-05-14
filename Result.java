
public class Result {
	private double cost;
	private long time;
	public Result(double cost, Timer timer) {
		this.cost = cost;
		this.time = timer.getTime();
	}
	public double getCost() {
		return cost;
	}
	public long getTime() {
		return time;
	}
}
