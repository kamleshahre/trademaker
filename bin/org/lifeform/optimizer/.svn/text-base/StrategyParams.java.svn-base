package org.lifeform.optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class StrategyParams {
	private final List<StrategyParam> params;
	private final Map<String, StrategyParam> paramsLookUp;

	public StrategyParams() {
		params = new ArrayList<StrategyParam>();
		paramsLookUp = new HashMap<String, StrategyParam>();
	}

	// copy constructor
	public StrategyParams(final StrategyParams params) {
		this.params = new ArrayList<StrategyParam>();
		this.paramsLookUp = new HashMap<String, StrategyParam>();
		for (StrategyParam param : params.getAll()) {
			StrategyParam paramCopy = new StrategyParam(param);
			this.params.add(paramCopy);
			this.paramsLookUp.put(paramCopy.getName(), paramCopy);
		}
	}

	public List<StrategyParam> getAll() {
		return params;
	}

	public void add(final String name, final double min, final double max, final double step) {
		StrategyParam param = new StrategyParam(name, min, max, step);
		params.add(param);
		paramsLookUp.put(name, param);
	}

	public int size() {
		return params.size();
	}

	public StrategyParam get(final int index) {
		return params.get(index);
	}

	public StrategyParam get(final String name) {
		return paramsLookUp.get(name);
	}

	public double get(final String name, final double defaultValue) {
		double value = defaultValue;
		StrategyParam param = paramsLookUp.get(name);
		if (param != null) {
			value = param.getValue();
		}
		return value;
	}

	public int iterations() {
		int iterations = 1;
		for (StrategyParam param : params) {
			iterations *= param.iterations();
		}
		return iterations;
	}

	public String getKey() {
		StringBuilder key = new StringBuilder();
		for (StrategyParam param : params) {
			if (key.length() > 0) {
				key.append("/");
			}
			key.append(param.getValue());
		}

		return key.toString();
	}

}
