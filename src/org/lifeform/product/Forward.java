package org.lifeform.product;

public class Forward extends Product {

	double forwardPrice = 0.0d;

	public double getForwardPrice() {
		return forwardPrice;
	}

	public void setForwardPrice(final double forwardPrice) {
		this.forwardPrice = forwardPrice;
	}

	public Forward(final int id) {
		super(id);
	}
}
