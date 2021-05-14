import java.io.IOException;

interface Heuristics {
	public abstract Result alg(City city, double pro, int source) throws IOException;
}
