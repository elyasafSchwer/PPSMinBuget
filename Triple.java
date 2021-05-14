
public class Triple extends Path{
	double l_i;
	double u_i;
	
	public Triple(City city, int start, double l, double u) {
		super(city, start, l);
		this.l_i = l;
		this.u_i = u;
		set_fail_probabilitie(Formulas.get_local_fail_probabilitie(getCity().getStoreAt(start), l));
	}
	
	public Triple(Triple father, int next_step, double c_x, double c_x_plus_1) {
		super(father, next_step, c_x);
		this.l_i = Math.max(father.l_i - (getWeight() - father.getWeight()), c_x);
		this.u_i = Math.min(father.u_i - (getWeight() - father.getWeight()), c_x_plus_1);
		setMoney(l_i + getWeight());
		set_fail_probabilitie(father.get_fail_probabilitie() * Formulas.get_local_fail_probabilitie(getCity().getStoreAt(next_step), c_x));
	}
	
	@Override
	public String toString() {
		return "[" + l_i + ", " + u_i + "), " + "" + getListString() + " fail_pro: " + String.format("%.05f", fail_probabilitie) + " weight: " + getWeight() + " money: " + getMoney();
	}
}
