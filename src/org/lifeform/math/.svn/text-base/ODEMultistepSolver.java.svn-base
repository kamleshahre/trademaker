/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see:
 * <http://www.opensourcephysics.org/>
 */

package org.lifeform.math;

/**
 * ODEMultistepSolver performs multiple ODE steps so that a uniform step size is
 * maintained.
 * 
 * @author Wolfgang Christian
 * @version 1.0
 */
public class ODEMultistepSolver implements ODEAdaptiveSolver {
	/**
	 * A class that saves an internal state that may be different from the
	 * orginal ODE. This internal state is used with interpolation solvers.
	 */
	protected final class InternalODE implements ODE {
		private final ODE ode;
		private double[] engineState = new double[0];

		InternalODE(final ODE ode) {
			this.ode = ode;
			setInitialConditions();
		}

		/**
		 * Gets the rate using the given state.
		 * 
		 * @param state
		 *            double[]
		 * @param rate
		 *            double[]
		 */
		public void getRate(final double[] state, final double[] rate) {
			ode.getRate(state, rate);
		}

		/**
		 * Gets the state.
		 * 
		 * @return double[]
		 */
		public double[] getState() {
			return engineState;
		}

		/**
		 * Sets the initial conditions using the current state of the ODE.
		 * 
		 * @return double[]
		 */
		public void setInitialConditions() {
			double[] state = ode.getState();
			if (state == null) {
				return;
			}
			if (engineState == null || engineState.length != state.length) {
				engineState = new double[state.length]; // create an engine
				// state with the
				// correct size
			}
			System.arraycopy(state, 0, engineState, 0, state.length);
		}

		/**
		 * updates the ODE state using the engine's internal state.
		 */
		public void update() {
			System.arraycopy(engineState, 0, ode.getState(), 0,
					engineState.length);
		}
	}

	private static int maxMessages = 3; // maximum number of error messages

	/**
	 * A factory method that creates a multisetp solver using the RK45 engine.
	 * 
	 * @param ode
	 *            ODE
	 * @return ODESolver
	 */
	public static ODEAdaptiveSolver MultistepRK45(final ODE ode) {
		ODEMultistepSolver multistepSolver = new ODEMultistepSolver();
		multistepSolver.odeEngine = new RK45(multistepSolver.setODE(ode));
		return multistepSolver;
	}

	protected int err_code = NO_ERROR;
	protected int maxIterations = 200;
	protected boolean enableExceptions = false;
	protected String err_msg = "";
	protected ODEAdaptiveSolver odeEngine;

	protected double fixedStepSize = 0.1;

	protected InternalODE internalODE;

	/**
	 * Constructs a ODEMultistepSolver without an ODE so that a factory method
	 * can create a custom solver.
	 */
	protected ODEMultistepSolver() {
	}

	/**
	 * Constructs an ODEMultiStep ODE solver for a system of ordinary
	 * differential equations.
	 * 
	 * The default ODESolver is DormandPrince45. Other solvers can be selected
	 * using factory methods.
	 * 
	 * @param ode
	 */
	public ODEMultistepSolver(final ODE ode) {
		odeEngine = new DormandPrince45(setODE(ode));
	}

	/**
	 * Enables runtime exceptions if the solver does not converge.
	 * 
	 * @param enable
	 *            boolean
	 */
	public void enableRuntimeExpecptions(final boolean enable) {
		enableExceptions = enable;
	}

	/**
	 * Gets the error code. Error codes: ODEAdaptiveSolver.NO_ERROR
	 * ODEAdaptiveSolver.DID_NOT_CONVERGE
	 * ODEAdaptiveSolver.BISECTION_EVENT_NOT_FOUND=2;
	 * 
	 * @return int
	 */
	public int getErrorCode() {
		return err_code;
	}

	/**
	 * Gets the step size. The step size is the fixed step size, not the size of
	 * the ODEAdaptiveSolver steps that are combined into a single step.
	 * 
	 * @return the step size
	 */
	public double getStepSize() {
		return fixedStepSize;
	}

	/**
	 * Gets the tolerance of the adaptive ODE solver.
	 * 
	 * @return
	 */
	public double getTolerance() {
		return odeEngine.getTolerance();
	}

	/**
	 * Initializes the ODE solver.
	 * 
	 * Temporary arrays may be allocated within the ODE solver.
	 * 
	 * @param stepSize
	 */
	public void initialize(final double stepSize) {
		maxMessages = 4; // reset the message counter to produce more messages
		err_msg = "";
		err_code = NO_ERROR;
		fixedStepSize = stepSize;
		internalODE.setInitialConditions();
		odeEngine.initialize(stepSize);
	}

	/**
	 * Steps the ode with a negative stepsize.
	 * 
	 * @return the step size
	 */
	private double minus() { // negative step size
		double tol = odeEngine.getTolerance();
		double remainder = fixedStepSize; // track the remaining step
		if ((odeEngine.getStepSize() >= 0) || ( // is the step negative?
				odeEngine.getStepSize() < fixedStepSize) || ( // is the stepsize
				// larger than
				// what is
				// requested?
				fixedStepSize - odeEngine.getStepSize() == fixedStepSize // is
				// the
				// stepsize
				// smaller
				// than
				// the
				// precision?
				)) {
			odeEngine.setStepSize(fixedStepSize); // reset the step size and let
			// it adapt to an optimum
			// size
		}
		int counter = 0;
		while (remainder < tol * fixedStepSize) { // check to see if we are
			// close enough
			counter++;
			double oldRemainder = remainder;
			if (remainder > odeEngine.getStepSize()) {
				double tempStep = odeEngine.getStepSize(); // save the current
				// optimum step size
				odeEngine.setStepSize(remainder); // set the step RK4/5 size to
				// the remainder
				double delta = odeEngine.step();
				remainder -= delta;
				odeEngine.setStepSize(tempStep); // restore the original step
				// size
			} else {
				remainder -= odeEngine.step(); // do a step and set the
				// remainder
			}
			// check to see if roundoff error prevents further calculation.
			if ((odeEngine.getErrorCode() != ODEAdaptiveSolver.NO_ERROR)
					|| (Math.abs(oldRemainder - remainder) <= Float.MIN_VALUE)
					|| (tol * fixedStepSize / 10.0 < odeEngine.getStepSize())
					|| counter > maxIterations) {
				err_msg = "ODEMultiStep did not converge. Remainder="
						+ remainder;
				err_code = ODEAdaptiveSolver.DID_NOT_CONVERGE;
				if (enableExceptions) {
					throw new SolverException(err_msg);
				}
				if (maxMessages > 0) {
					maxMessages--;
					System.out.println(err_msg);
					// OSPLog.warning(err_msg);
				}
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
		double tol = odeEngine.getTolerance();
		double remainder = fixedStepSize; // track the remaining step
		if ((odeEngine.getStepSize() <= 0) || ( // is the stepsize postive?
				odeEngine.getStepSize() > fixedStepSize) || ( // is the stepsize
				// larger than
				// what is
				// requested?
				fixedStepSize - odeEngine.getStepSize() == fixedStepSize // is
				// the
				// stepsize
				// smaller
				// than
				// the
				// precision?
				)) {
			odeEngine.setStepSize(fixedStepSize); // reset the step size and let
			// it adapt to an optimum
			// size
		}
		int counter = 0;
		while (remainder > tol * fixedStepSize) { // check to see if we are
			// close enough
			counter++;
			double oldRemainder = remainder;
			if (remainder < odeEngine.getStepSize()) { // temporarily reduce the
				// step size so that we
				// hit the exact dt
				// value
				double tempStep = odeEngine.getStepSize(); // save the current
				// optimum step size
				odeEngine.setStepSize(remainder); // set the step size to the
				// remainder
				double delta = odeEngine.step();
				remainder -= delta;
				odeEngine.setStepSize(tempStep); // restore the original step
				// size
			} else {
				remainder -= odeEngine.step(); // do a step and set the
				// remainder
			}
			// check to see if roundoff error prevents further calculation.
			if ((odeEngine.getErrorCode() != ODEAdaptiveSolver.NO_ERROR)
					|| (Math.abs(oldRemainder - remainder) <= Float.MIN_VALUE)
					|| (tol * fixedStepSize / 10.0 > odeEngine.getStepSize())
					|| counter > maxIterations) {
				err_msg = "ODEMultiStep did not converge. Remainder="
						+ remainder;
				err_code = ODEAdaptiveSolver.DID_NOT_CONVERGE;
				if (enableExceptions) {
					throw new SolverException(err_msg);
				}
				if (maxMessages > 0) {
					maxMessages--;
					System.out.println(err_msg);
					// OSPLog.warning(err_msg);
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
	 * Sets the ODE for this solver.
	 * 
	 * @param ode
	 *            ODE
	 * @return MyODE
	 */
	protected InternalODE setODE(final ODE ode) {
		internalODE = new InternalODE(ode);
		return internalODE;
	}

	/**
	 * Sets the fixed step size. Multi-step solvers will perform one or more
	 * internal steps in order to perform a step with the given size.
	 * 
	 * @param stepSize
	 */
	public void setStepSize(final double stepSize) {
		maxMessages = 4; // reset the message counter to produce more messages
		fixedStepSize = stepSize; // the fixed step size
		if (stepSize < 0) {
			odeEngine.setStepSize(Math.max(-Math.abs(odeEngine.getStepSize()),
					stepSize));
		} else { // stepSize is positive
			odeEngine.setStepSize(Math.min(odeEngine.getStepSize(), stepSize));
		}
	}

	/**
	 * Sets the tolerance of the adaptive ODE solver.
	 * 
	 * @param tol
	 *            the tolerance
	 */
	public void setTolerance(double tol) {
		tol = Math.abs(tol);
		odeEngine.setTolerance(tol);
	}

	/**
	 * Steps (advances) the differential equations by the stepSize.
	 * 
	 * The ODESolver invokes the ODE's getRate method to obtain the initial
	 * state of the system. The ODESolver then advances the solution and copies
	 * the new state into the state array at the end of the solution step if
	 * desired tolerance was reached.
	 * 
	 * @return the actual step
	 */
	public double step() {
		err_code = NO_ERROR;
		internalODE.setInitialConditions(); // stores the ode's initial
		// conditions
		double remainder = 0;
		if (fixedStepSize > 0) {
			remainder = plus();
		} else {
			remainder = minus();
		}
		internalODE.update(); // updates the ode
		return fixedStepSize - remainder; // the step size that was actually
		// taken
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
