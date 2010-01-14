package org.lifeform.market.interpolation;

public class Interpolator {
	static public double linear(final double x, final double x0,
			final double x1, final double y0, final double y1) {
		if ((x1 - x0) == 0) {
			return (y0 + y1) / 2;
		} else {
			return y0 + (x - x0) * (y1 - y0) / (x1 - x0);
		}
	}
}
