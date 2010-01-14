package org.lifeform.pricer.solver;

public class SteffensensSolver extends NonLinear {
	double previous, current;
	long n;

	public SteffensensSolver(final Function solve, final double tolerence, final double guess) {
		super(solve, tolerence);
		previous = guess;
	}

	double solve() {
		n = 1;
		double tmp = toSolve.evaluate(previous);
		double hn = (toSolve.evaluate(previous + tmp) - tmp) / tmp;
		do {
			hn = tmp / hn;
			current = previous - hn;
			previous = current;
			n++;
			tmp = toSolve.evaluate(previous);
			hn = (toSolve.evaluate(previous + tmp) - tmp) / tmp;
		} while (Math.abs(hn) > tolerence);
		return current;
	}
}
