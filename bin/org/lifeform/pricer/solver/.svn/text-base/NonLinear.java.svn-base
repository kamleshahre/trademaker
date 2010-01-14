package org.lifeform.pricer.solver;

import org.lifeform.configuration.Defaults;

public class NonLinear {
	protected final Function toSolve;
	protected final double tolerence;

	public NonLinear(final Function solve) {
		this(solve, Double.valueOf(Defaults.EPSILON.getDefault()));
	}

	public NonLinear(final Function solve, final double tolerence) {
		this.toSolve = solve;
		this.tolerence = tolerence;
	}
}
