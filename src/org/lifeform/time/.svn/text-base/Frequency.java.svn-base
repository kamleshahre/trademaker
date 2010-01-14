package org.lifeform.time;

public class Frequency {
	public Type getType() {
		return type;
	}

	public void setType(final Type type) {
		this.type = type;
	}

	public enum Type {
		NON, D, W, M, Q, SA, Y, CNT
	}

	Type type;

	public Frequency(final Type type) {
		this.type = type;
	}

	public String toString() {
		return type.toString();
	}

	public static Frequency NON = new Frequency(Frequency.Type.NON);
	public static Frequency D = new Frequency(Frequency.Type.D);
	public static Frequency W = new Frequency(Frequency.Type.W);
	public static Frequency M = new Frequency(Frequency.Type.M);
	public static Frequency Q = new Frequency(Frequency.Type.Q);
	public static Frequency SA = new Frequency(Frequency.Type.SA);
	public static Frequency Y = new Frequency(Frequency.Type.Y);
	public static Frequency CNT = new Frequency(Frequency.Type.CNT);
}
