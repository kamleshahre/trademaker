package org.lifeform.pricer;

public class PDE {

	private double pdeOne;
	private double pdeTwo;

	private void setOne(final double fdiff) {
		pdeOne = fdiff;
	}

	private void setTwo(final double sdiff) {
		pdeTwo = sdiff;
	}

	public double getFdiff() {
		return pdeOne;
	}

	public double getSdiff() {
		return pdeTwo;
	}

	public void diffValues(final double[] coefficients, final double x) {
		double dp2 = 0.0;
		double dp = 0.0;
		int cnt = 0;
		int n = -1;
		double[] firstdiff = new double[coefficients.length];
		for (double d : coefficients) {
			firstdiff[cnt] = (d * n * Math.pow(x, n - 1));
			dp2 += ((d * n * (n - 1)) * Math.pow(x, n - 2));
			dp += firstdiff[cnt];
			System.out.println("Answer  " + firstdiff[cnt] + "TOTAL== dp2="
					+ dp2 + "+dp==" + dp + " pow x,n==" + (Math.pow(x, n))
					+ " n-1 value ==" + (Math.pow(x, n - 1)));
			cnt++;
			n--;
		}
		setOne(dp);
		setTwo(dp2);
	}

}
