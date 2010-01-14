package org.lifeform.pricer.option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lifeform.core.Pair;

public class Lattice<T, V> {
	public enum Type {
		Binomial, Trinomial, Pentagonal
	}

	Map<Integer, ArrayList<Pair<T, V>>> lattice = new HashMap<Integer, ArrayList<Pair<T, V>>>();
	int minIndex = 0;
	int maxIndex = 0;
	int rows = 0;
	Type type;

	public Lattice() {
		this(1, Type.Binomial);
	}

	public Lattice(final int nRows, final Type type) {
		this(0.0d, nRows, Type.Binomial);
	}

	public Lattice(final double initalVal, final int nRows, final Type type) {
		rows = nRows;
	}

	public int getMinIndex() {
		return minIndex;
	}

	public void setMinIndex(final int minIndex) {
		this.minIndex = minIndex;
	}

	public int getMaxIndex() {
		return maxIndex;
	}

	public void setMaxIndex(final int maxIndex) {
		this.maxIndex = maxIndex;
	}

	public Map<Integer, ArrayList<Pair<T, V>>> getLattice() {
		return lattice;
	}

	public int getDepth() {
		return lattice.size();
	}

	public List<Pair<T, V>> get(final int row) {
		return lattice.get(row);
	}

}
