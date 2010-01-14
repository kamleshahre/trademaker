package org.lifeform.util;

import java.util.List;

public class ArrayUtils {
	public static final int[] trim(final int[] a, final int size) {
		// assert (size <= a.length);
		if (a.length == size) {
			return a;
		} else {
			int[] b = new int[size];
			System.arraycopy(a, 0, b, 0, size);
			return b;
		}
	}

	public static final double[] trim(final double[] a, final int size) {
		// assert (size <= a.length);
		if (a.length == size) {
			return a;
		} else {
			double[] b = new double[size];
			System.arraycopy(a, 0, b, 0, size);
			return b;
		}
	}

	public static final double sum(final double[] a) {
		double sum = 0;
		for (int i = 0; i < a.length; ++i) {
			sum += a[i];
		}
		return sum;
	}

	public static double[] unboxDoubles(final List<Double> results) {
		double[] res = new double[results.size()];
		for (int i = 0; i < results.size(); ++i) {
			res[i] = results.get(i);
		}
		return res;
	}

}
