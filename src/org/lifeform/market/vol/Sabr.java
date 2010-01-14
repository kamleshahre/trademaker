package org.lifeform.market.vol;

import static org.junit.Assert.assertEquals;

import org.lifeform.configuration.Defaults;

public class Sabr {

	public Sabr(final double alpha, final double beta, final double rho, final double nu)
			throws ArithmeticException {
		super();
		this.alpha = alpha;
		this.rho = rho;
		this.beta = beta;
		this.nu = nu;
		if (!(alpha > 0.0)) {
			throw new ArithmeticException("alpha must be positive: " + alpha
					+ " not allowed");
		}

		if (!(beta >= 0.0 && beta <= 1.0)) {
			throw new ArithmeticException("beta must be in (0.0, 1.0): " + beta
					+ " not allowed");
		}
		if (!(nu >= 0.0)) {
			throw new ArithmeticException("nu must be non negative: " + nu
					+ " not allowed");
		}
		if (!(rho * rho < 1.0)) {
			throw new ArithmeticException("rho square must be less than one: "
					+ rho + " not allowed");
		}
	}

	protected final double alpha;
	protected final double rho;
	protected final double beta;
	protected final double nu;

	public boolean isClose(final double one, final double two) {
		return one - two < Double.valueOf(Defaults.EPSILON.getDefault());
	}

	public double volatility(final double strike, final double forward, final double expiryTime) {
		final double oneMinusBeta = 1.0 - beta;
		final double A = Math.pow(forward * strike, oneMinusBeta);
		final double sqrtA = Math.sqrt(A);
		double logM;
		if (!isClose(forward, strike))
			logM = Math.log(forward / strike);
		else {
			final double epsilon = (forward - strike) / strike;
			logM = epsilon - .5 * epsilon * epsilon;
		}
		final double z = (nu / alpha) * sqrtA * logM;
		final double B = 1.0 - 2.0 * rho * z + z * z;
		final double C = oneMinusBeta * oneMinusBeta * logM * logM;
		final double tmp = (Math.sqrt(B) + z - rho) / (1.0 - rho);
		final double xx = Math.log(tmp);
		final double D = sqrtA * (1.0 + C / 24.0 + C * C / 1920.0);
		final double d = 1.0
				+ expiryTime
				* (oneMinusBeta * oneMinusBeta * alpha * alpha / (24.0 * A)
						+ 0.25 * rho * beta * nu * alpha / sqrtA + (2.0 - 3.0
						* rho * rho)
						* (nu * nu / 24.0));

		double multiplier;
		// computations become precise enough if the square of z worth
		// slightly more than the precision machine (hence the m)
		final double m = 10;
		if (Math.abs(z * z) > Double.valueOf(Defaults.EPSILON.getDefault()) * m)
			multiplier = z / xx;
		else {
			double talpha = (0.5 - rho * rho) / (1.0 - rho);
			double tbeta = alpha - .5;
			double tgamma = rho / (1 - rho);
			multiplier = 1.0 - beta * z
					+ (tgamma - talpha + tbeta * tbeta * .5) * z * z;
		}
		return (alpha / D) * multiplier * d;
	}

	public static void main(final String[] args) {
		double strike = 0.0398;
		double forward = 0.0398;
		double expiryTime = 5.0;
		double alpha = 0.0305473;
		double beta = 0.5;
		double nu = 0.34;
		double rho = -0.11;

		Sabr sabr = new Sabr(alpha, beta, rho, nu);
		double sabrVol = sabr.volatility(strike, forward, expiryTime);
		assertEquals(0.16, sabrVol, 1.0e-6);

		strike = 0.0598;
		sabrVol = sabr.volatility(strike, forward, expiryTime);
		assertEquals(0.15641233662902254, sabrVol, 1.0e-6);

		strike = 0.0198;
		sabrVol = sabr.volatility(strike, forward, expiryTime);
		assertEquals(0.2373848, sabrVol, 1.0e-6);
	}

}
