package org.lifeform.pricer;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.NewtonSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;

public class RiskFreeYield implements UnivariateRealFunction {
	private double precision = 1e-5;
	private int iterations = 20;
	private double nominalstockprice = 0.0;
	private double term = 0.0;
	private double couponrate = 0.0;
	private double marketpricevalue = 0.0;
	private double firstestimate = 0.0;
	private double rateperAnum = 0.0;
	private double maturityperiod = 0.0;

	public RiskFreeYield(final double facevalue, final double termtopay, final double coupon,
			final double marketprice, final double maturity, final double yieldestimate) {
		this.nominalstockprice = facevalue;
		this.term = termtopay;
		this.couponrate = coupon;
		this.marketpricevalue = marketprice;
		this.maturityperiod = maturity;
		this.firstestimate = yieldestimate;
	}

	public double yieldEstimate() throws ConvergenceException,
			FunctionEvaluationException, IllegalArgumentException {
		UnivariateRealSolver solver = new NewtonSolver();
		solver.setAbsoluteAccuracy(precision);
		solver.setMaximalIterationCount(iterations);
		return (solver.solve(this, 0, 100, firstestimate) * 12.0 / term) * 100.0;
	}

	@Override
	public double value(final double x) throws FunctionEvaluationException {
		rateperAnum = (nominalstockprice * (couponrate / 100));
		// annual cashflow out //
		double poscashflow = rateperAnum * (term / 12);
		// cashflow out per term as monthly amount * termperiod//
		double solution = poscashflow / x
				* (1 - 1 / (Math.pow(1 + x, (maturityperiod / (term / 12)))))
				+ nominalstockprice
				/ (Math.pow(1 + x, (maturityperiod / (term / 12))))
				- marketpricevalue;
		return solution;
	}
}
