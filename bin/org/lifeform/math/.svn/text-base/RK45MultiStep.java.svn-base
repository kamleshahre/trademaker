/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

/**
 * Title: RK45MultiStep Description: Perform multiple RK4/5 ODE steps so that a
 * uniform step size is maintained
 * 
 * @author Wolfgang Christian
 * @version 1.0
 */
public class RK45MultiStep extends RK45 {
	private static int maxMessages = 3; // maximum number of error messages
	private double fixedStepSize = 0.1;
	protected int maxIterations = 200;

	/**
	 * Constructs the RK45MultiStep ODESolver for a system of ordinary
	 * differential equations.
	 * 
	 * @param _ode
	 *            the system of differential equations.
	 */
	public RK45MultiStep(final ODE _ode) {
		super(_ode);
	}

	/**
	 * Gets the error code. Error codes: ODEAdaptiveSolver.NO_ERROR
	 * ODEAdaptiveSolver.DID_NOT_CONVERGE
	 * 
	 * @return int
	 */
	public int getErrorCode() {
		return error_code;
	}

	/**
	 * Gets the step size.
	 * 
	 * The step size is the fixed step size, not the size of the RK4/5 steps
	 * that are combined into a single step.
	 * 
	 * @return the step size
	 */
	public double getStepSize() {
		return fixedStepSize;
	}

	/**
	 * Initializes the ODE solver.
	 * 
	 * Temporary state and rate arrays are allocated by invoking the superclass
	 * method.
	 * 
	 * @param stepSize
	 */
	public void initialize(final double stepSize) {
		fixedStepSize = stepSize;
		super.initialize(stepSize / 2); // be conservative with the adaptive
		// solver
	}

	/**
	 * Steps the ode with a negative stepsize.
	 * 
	 * @return the step size
	 */
	private double minus() { // negative step size
		double remainder = fixedStepSize; // dt will keep track of the remaining
		// time
		if ((super.getStepSize() >= 0) || ( // is the step negative?
				super.getStepSize() < fixedStepSize) || ( // is the stepsize
				// larger than what
				// is requested?
				fixedStepSize - super.getStepSize() == fixedStepSize // is the
				// stepsize
				// smaller
				// than
				// the
				// precision?
				)) {
			super.setStepSize(fixedStepSize); // reset the step size and let it
			// adapt to an optimum size
		}
		int counter = 0;
		while (remainder < tol * fixedStepSize) { // check to see if we are
			// close enough
			counter++;
			double oldRemainder = remainder;
			if (remainder > super.getStepSize()) {
				double tempStep = super.getStepSize(); // save the current
				// optimum step size
				super.setStepSize(remainder); // set the step RK4/5 size to the
				// remainder
				double delta = super.step();
				remainder -= delta;
				super.setStepSize(tempStep); // restore the original step size
			} else {
				remainder -= super.step(); // do a rk45 step and set the
				// remainder
			}
			// check to see if roundoff error prevents further calculation.
			if ((error_code != NO_ERROR)
					|| (Math.abs(oldRemainder - remainder) <= Float.MIN_VALUE)
					|| (tol * fixedStepSize / 10.0 < super.getStepSize())
					|| counter > maxIterations) {
				error_code = DID_NOT_CONVERGE;
				if (enableExceptions) {
					throw new SolverException(
							"RK45 ODE solver did not converge.");
				}
				if (maxMessages <= 0) {
					break;
				}
				maxMessages--;
				System.err
						.println("Warning: RK45MultiStep did not converge. Remainder="
								+ remainder);
				if (maxMessages == 0) {
					System.err.println("Further warnings surppressed.");
				}
				break;
			}
		}
		return remainder;
	}

	/**
	 * Steps the ode with a postive stepsize.
	 * 
	 * @return the step size
	 */
	private double plus() { // positive step size
		double remainder = fixedStepSize; // dt will keep track of the remaining
		// time
		if ((super.getStepSize() <= 0) || ( // is the stepsize postive?
				super.getStepSize() > fixedStepSize) || ( // is the stepsize
				// larger than what
				// is requested?
				fixedStepSize - super.getStepSize() == fixedStepSize // is the
				// stepsize
				// smaller
				// than
				// the
				// precision?
				)) {
			super.setStepSize(fixedStepSize); // reset the step size and let it
			// adapt to an optimum size
		}
		int counter = 0;
		while (remainder > tol * fixedStepSize) { // check to see if we are
			// close enough
			counter++;
			double oldRemainder = remainder;
			if (remainder < super.getStepSize()) { // temporarily reduce the
				// step size so that we hit
				// the exact dt value
				double tempStep = super.getStepSize(); // save the current
				// optimum step size
				super.setStepSize(remainder); // set the RK4/5 step size to the
				// remainder
				double delta = super.step();
				remainder -= delta;
				super.setStepSize(tempStep); // restore the original step size
			} else {
				remainder -= super.step(); // do a rk45 step and set the
				// remainder
			}
			// check to see if roundoff error prevents further calculation.
			if ((error_code != NO_ERROR)
					|| (Math.abs(oldRemainder - remainder) <= Float.MIN_VALUE)
					|| (tol * fixedStepSize / 10.0 > super.getStepSize())
					|| counter > maxIterations) {
				error_code = DID_NOT_CONVERGE;
				if (enableExceptions) {
					throw new SolverException(
							"RK45 ODE solver did not converge.");
				}
				maxMessages--;
				System.err
						.println("Warning: RK45MultiStep did not converge. Remainder="
								+ remainder);
				if (maxMessages == 0) {
					System.err
							.println("RK45 ODE solver did not converge. Further warnings suppressed.");
				}
				break;
			}
		}
		return remainder;
	}

	/**
	 * Sets the number of error messages if ODE solver did not converge.
	 * 
	 * @param n
	 *            int
	 */
	public void setMaximumNumberOfErrorMessages(final int n) {
		maxMessages = n;
	}

	/**
	 * Sets the maximum number of iterations.
	 * 
	 * @param n
	 *            maximum
	 */
	public void setMaxIterations(final int n) {
		maxIterations = Math.max(1, n);
	}

	/**
	 * Method setStepSize
	 * 
	 * @param stepSize
	 */
	public void setStepSize(final double stepSize) {
		maxMessages = 4; // reset the message counter
		fixedStepSize = stepSize; // the fixed step size
		if (stepSize < 0) {
			super.setStepSize(Math
					.max(-Math.abs(super.getStepSize()), stepSize));
		} else { // stepSize is positive
			super.setStepSize(Math.min(super.getStepSize(), stepSize));
		}
		super.setStepSize(stepSize); // the variable step size
	}

	/**
	 * Steps (advances) the differential equations by the stepSize.
	 * 
	 * The ODESolver invokes the ODE's getRate method to obtain the initial
	 * state of the system. The ODESolver then advances the solution and copies
	 * the new state into the state array at the end of the solution step.
	 * 
	 * @return the step size
	 */
	public double step() {
		error_code = NO_ERROR;
		if (fixedStepSize > 0) {
			return fixedStepSize - plus();
		} else {
			return fixedStepSize - minus();
		}
	}

}

/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Ernan Hughes - initial API and implementation
 *******************************************************************************/
