/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ernan Hughes - initial API and implementation
 *******************************************************************************/
package org.lifeform.product;

import java.util.Calendar;

import org.lifeform.time.Period;

public class Option extends Leg {
	private final Underlying underlying;
	private final ExerciseType exercise;
	private final ExerciseStyle optionType;

	public Option(final double principal, final Period accrual, final Underlying underlying,
			final ExerciseType exercise) {
		this(principal, accrual, underlying, exercise, ExerciseStyle.American);
	}

	public Option(final double principal, final Period accrual, final Underlying underlying,
			final ExerciseType exercise, final ExerciseStyle optionType) {
		super(principal, accrual);
		this.underlying = underlying;
		this.exercise = exercise;
		this.optionType = optionType;
	}

	public ExerciseType getExercise() {
		return exercise;
	}

	public ExerciseStyle getOptionType() {
		return optionType;
	}

	public Underlying getUnderlying() {
		return underlying;
	}

	public boolean isExercisable(final Calendar exerciseDate) {
		return false;
	}

}
