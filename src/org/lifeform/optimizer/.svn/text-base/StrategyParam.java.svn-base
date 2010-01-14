package org.lifeform.optimizer;

/**
 */
public class StrategyParam {
	public void setMin(final double min) {
		this.min = min;
	}

	public void setMax(final double max) {
		this.max = max;
	}

	public void setStep(final double step) {
		this.step = step;
	}

	private double min, max, step;
	private double value;
	private final String name;

	private StrategyParam(final String name, final double min, final double max, final double step,
			final double value) {
		this.name = name;
		this.min = min;
		this.max = max;
		this.step = step;
		this.value = value;
	}

	public StrategyParam(final String name, final double min, final double max, final double step) {
		this(name, min, max, step, 0);
	}

	// copy constructor
	public StrategyParam(final StrategyParam param) {
		this(param.name, param.min, param.max, param.step, param.value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Name: ").append(name);
		sb.append(" Min: ").append(min);
		sb.append(" Max: ").append(max);
		sb.append(" Step: ").append(step);

		return sb.toString();
	}

	public long iterations() {
		long iterations = (long) ((max - min) / step) + 1;
		return iterations;
	}

	public String getName() {
		return name;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public double getStep() {
		return step;
	}

	public double getValue() {
		return value;
	}

	public void setValue(final double value) {
		this.value = value;
	}
}
