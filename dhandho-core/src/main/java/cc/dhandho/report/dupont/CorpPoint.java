package cc.dhandho.report.dupont;

import com.age5k.jcps.JcpsException;

public class CorpPoint {
	public String corpId;
	public Double[] point;

	public boolean containsNull() {
		for (Double d : point) {
			if (d == null) {
				return true;
			}
		}
		return false;
	}

	public double distance(CorpPoint cp) {
		return distance(this.point, cp.point);
	}

	private static double distance(Double[] p1, Double[] p2) {
		double total = 0D;
		if (p1.length != p2.length) {
			throw new JcpsException("length not same.");
		}
		for (int i = 0; i < p1.length; i++) {
			double dI = (p1[i] - p2[i]);
			total += dI * dI;
		}

		return Math.sqrt(total);

	}
}
