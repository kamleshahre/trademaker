/*
 * Open Source Physics software is free software as described near the bottom of this code file.
 *
 * For additional information and documentation on Open Source Physics please see: 
 * <http://www.opensourcephysics.org/>
 */

/*
 Fix the following methods: inverseTransform, createInverse
 */
package org.lifeform.math;

/**
 * Title:        InvertibleFunction
 * Description:  An invertible function of one variable.
 */
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public final class FunctionTransform extends AffineTransform {
	private static final long serialVersionUID = -5860085154204253705L;
	double m00;
	double m10 = 1; // identity transform by default
	double m01 = 1;
	double m11;
	double m02;
	double m12;
	double[] flatmatrix = new double[6];
	InvertibleFunction xFunction;
	InvertibleFunction yFunction;
	boolean applyXFunction = false;
	boolean applyYFunction = false;

	public FunctionTransform() {
		super();
	}

	public FunctionTransform(final double m00, final double m10,
			final double m01, final double m11, final double m02,
			final double m12) {
		super(m00, m10, m01, m11, m02, m12);
		this.m00 = m00;
		this.m10 = m10;
		this.m01 = m01;
		this.m11 = m11;
		this.m02 = m02;
		this.m12 = m12;
	}

	/*
	 * Concatenates this FunctionTransform with the given AffineTransform as
	 * specified in AffineTransform. Note-The if specified parameter is a
	 * FunctionTransform, the function is ignored.
	 */
	@Override
	public void concatenate(final AffineTransform Tx) {
		super.concatenate(Tx);
		updateMatrix();
	}

	@Override
	public AffineTransform createInverse()
			throws NoninvertibleTransformException { // FIX_ME
		AffineTransform at = super.createInverse();
		FunctionTransform ft = new FunctionTransform();
		ft.setTransform(at);
		final InvertibleFunction xFunction = new InvertibleFunction() {
			public double evaluate(final double x) {
				return FunctionTransform.this.xFunction.getInverse(x);
			}

			public double getInverse(final double y) {
				return FunctionTransform.this.xFunction.evaluate(y);
			}
		};
		final InvertibleFunction yFunction = new InvertibleFunction() {
			public double evaluate(final double x) {
				return FunctionTransform.this.yFunction.getInverse(x);
			}

			public double getInverse(final double y) {
				return FunctionTransform.this.yFunction.evaluate(y);
			}
		};
		ft.setXFunction(xFunction);
		ft.setYFunction(yFunction);
		return ft;
	}

	@Override
	public void deltaTransform(final double[] srcPts, int srcOff,
			double[] dstPts, int dstOff, int numPts) {
		double M00;
		double M01;
		double M10;
		double M11; // For caching
		if (dstPts == srcPts && dstOff > srcOff && dstOff < srcOff + numPts * 2) {
			// If the arrays overlap partially with the destination higher
			// than the source and we transform the coordinates normally
			// we would overwrite some of the later source coordinates
			// with results of previous transformations.
			// To get around this we use arraycopy to copy the points
			// to their final destination with correct overwrite
			// handling and then transform them in place in the new
			// safer location.
			System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
			// srcPts = dstPts; // They are known to be equal.
			srcOff = dstOff;
		}
		M00 = m00;
		M01 = m01;
		M10 = m10;
		M11 = m11;
		while (--numPts >= 0) {
			double x = srcPts[srcOff++];
			double y = srcPts[srcOff++];
			if (applyXFunction) {
				x = xFunction.evaluate(x);
			}
			if (applyYFunction) {
				y = yFunction.evaluate(y);
			}
			dstPts[dstOff++] = x * M00 + y * M01;
			dstPts[dstOff++] = x * M10 + y * M11;
		}
	}

	@Override
	public Point2D deltaTransform(Point2D ptSrc, Point2D ptDst) {
		if (ptDst == null) {
			if (ptSrc instanceof Point2D.Double) {
				ptDst = new Point2D.Double();
			} else {
				ptDst = new Point2D.Float();
			}
		}
		// Copy source coords into local variables in case src == dst
		double x = ptSrc.getX();
		double y = ptSrc.getY();
		if (applyXFunction) {
			x = xFunction.evaluate(x);
		}
		if (applyYFunction) {
			y = yFunction.evaluate(y);
		}
		ptDst.setLocation(x * m00 + y * m01, x * m10 + y * m11);
		return ptDst;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof FunctionTransform) {
			FunctionTransform a = (FunctionTransform) obj;
			double[] matrix = new double[6];
			a.getMatrix(matrix);
			if ((m00 == matrix[0]) && (m01 == matrix[1]) && (m02 == matrix[2])
					&& (m10 == matrix[3]) && (m11 == matrix[4])
					&& (m12 == matrix[5])) {
				if (applyXFunction == a.applyXFunction
						&& applyYFunction == a.applyYFunction) {
					return (xFunction.getClass() == a.xFunction.getClass())
							&& (yFunction.getClass() == a.yFunction.getClass());
				}
			}
		} else if (obj instanceof AffineTransform) {
			if (!applyXFunction && !applyYFunction) {
				return super.equals(obj);
			}
		}
		return false;
	}

	@Override
	public void inverseTransform(double[] srcPts, int srcOff,
			final double[] dstPts, final int dstOff, final int numPts) // FIX_ME
			throws NoninvertibleTransformException {
		if (dstPts == srcPts && dstOff > srcOff && dstOff < srcOff + numPts * 2) {
			// If the arrays overlap partially with the destination higher
			// than the source and we transform the coordinates normally
			// we would overwrite some of the later source coordinates
			// with results of previous transformations.
			// To get around this we use arraycopy to copy the points
			// to their final destination with correct overwrite
			// handling and then transform them in place in the new
			// safer location.
			System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
			// srcPts = dstPts; // They are known to be equal.
			srcOff = dstOff;
		}
		double det = m00 * m11 - m01 * m10;
		if (Math.abs(det) <= Double.MIN_VALUE) {
			throw new NoninvertibleTransformException("Determinant is " + det);
		}
		// newx = M00 * x + M01 * y + M02;
		/*
		 * dstPts[dstOff++] = (float) (M10 * x + M11 * y + M12); while(--numPts
		 * >= 0) { double x = srcPts[srcOff++]; double y = srcPts[srcOff++];
		 * if(applyXFunction) { x = xFunction.getInverse(x); }
		 * if(applyYFunction) { y = yFunction.getInverse(y); } double div = m00
		 * (x-m02)/m00 z = M00 * x + M01 * y + M02; x * m11 - y * m01) / M00 *
		 * M11 - M01 * M10 x -=- m02; x/m01 x/m00 y -= m12; ptDst.setLocation(x
		 * / m00, y / m11); dstPts[dstOff++] = M00 * x + M01 * y + M02;
		 * dstPts[dstOff++] = M10 * x + M11 * y + M12; }
		 */
	}

	@Override
	public Point2D inverseTransform(Point2D ptSrc, Point2D ptDst)
			throws NoninvertibleTransformException { // FIX_ME
		if (ptDst == null) {
			if (ptSrc instanceof Point2D.Double) {
				ptDst = new Point2D.Double();
			} else {
				ptDst = new Point2D.Float();
			}
		}
		// FIX ME
		return ptDst;
	}

	/*
	 * Pre-concatenates this FunctionTransform with the given AffineTransform as
	 * specified in AffineTransform. Note-The if specified parameter is a
	 * FunctionTransform, the function is ignored.
	 */
	@Override
	public void preConcatenate(final AffineTransform Tx) {
		super.preConcatenate(Tx);
		updateMatrix();
	}

	@Override
	public void rotate(final double theta) {
		super.rotate(theta);
		updateMatrix();
	}

	@Override
	public void rotate(final double theta, final double x, final double y) {
		super.rotate(theta, x, y);
		updateMatrix();
	}

	@Override
	public void scale(final double sx, final double sy) {
		super.scale(sx, sy);
		updateMatrix();
	}

	/*
	 * sets whether this FunctionTransform applies the x function
	 */
	public void setApplyXFunction(final boolean b) {
		applyXFunction = b;
	}

	/*
	 * sets whether this FunctionTransform applies the y function
	 */
	public void setApplyYFunction(final boolean b) {
		applyYFunction = b;
	}

	@Override
	public void setToIdentity() {
		super.setToIdentity();
		updateMatrix();
	}

	@Override
	public void setToRotation(final double theta) {
		super.setToRotation(theta);
		updateMatrix();
	}

	@Override
	public void setToRotation(final double theta, final double x, final double y) {
		super.setToRotation(theta, x, y);
		updateMatrix();
	}

	@Override
	public void setToScale(final double sx, final double sy) {
		super.setToScale(sx, sy);
		updateMatrix();
	}

	@Override
	public void setToShear(final double shx, final double shy) {
		super.setToShear(shx, shy);
		updateMatrix();
	}

	@Override
	public void setToTranslation(final double tx, final double ty) {
		super.setToTranslation(tx, ty);
		updateMatrix();
	}

	@Override
	public void setTransform(final AffineTransform Tx) {
		super.setTransform(Tx);
		updateMatrix();
	}

	@Override
	public void setTransform(final double m00, final double m10,
			final double m01, final double m11, final double m02,
			final double m12) {
		super.setTransform(m00, m10, m01, m11, m02, m12);
		updateMatrix();
	}

	/*
	 * Sets the x function to the specified parameter.
	 * 
	 * @param x the new x function. Can not be null.
	 */
	public void setXFunction(final InvertibleFunction x) {
		assert x != null : "x function can not be null.";
		xFunction = x;
	}

	/*
	 * Sets the y function to the specified parameter.
	 * 
	 * @param y the new y function. Can not be null.
	 */
	public void setYFunction(final InvertibleFunction y) {
		assert y != null : "y function can not be null.";
		yFunction = y;
	}

	@Override
	public void shear(final double shx, final double shy) {
		super.shear(shx, shy);
		updateMatrix();
	}

	@Override
	public void transform(double[] srcPts, int srcOff, double[] dstPts,
			int dstOff, int numPts) {
		double M00;
		double M01;
		double M02;
		double M10;
		double M11;
		double M12; // For caching
		if (dstPts == srcPts && dstOff > srcOff && dstOff < srcOff + numPts * 2) {
			// If the arrays overlap partially with the destination higher
			// than the source and we transform the coordinates normally
			// we would overwrite some of the later source coordinates
			// with results of previous transformations.
			// To get around this we use arraycopy to copy the points
			// to their final destination with correct overwrite
			// handling and then transform them in place in the new
			// safer location.
			System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
			// srcPts = dstPts; // They are known to be equal.
			srcOff = dstOff;
		}
		M00 = m00;
		M01 = m01;
		M02 = m02;
		M10 = m10;
		M11 = m11;
		M12 = m12;
		while (--numPts >= 0) {
			double x = srcPts[srcOff++];
			double y = srcPts[srcOff++];
			if (applyXFunction) {
				x = xFunction.evaluate(x);
			}
			if (applyYFunction) {
				y = yFunction.evaluate(y);
			}
			// W. Christian
			// Java 1.3 bug in Windows VM
			// the following two lines may cause a crash while drawing shapes if
			// |dstPts| is very large
			dstPts[dstOff++] = M00 * x + M01 * y + M02;
			dstPts[dstOff++] = M10 * x + M11 * y + M12;
		}
	}

	@Override
	public void transform(double[] srcPts, int srcOff, float[] dstPts,
			int dstOff, int numPts) {
		double M00;
		double M01;
		double M02;
		double M10;
		double M11;
		double M12; // For caching
		M00 = m00;
		M01 = m01;
		M02 = m02;
		M10 = m10;
		M11 = m11;
		M12 = m12;
		while (--numPts >= 0) {
			double x = srcPts[srcOff++];
			double y = srcPts[srcOff++];
			if (applyXFunction) {
				x = xFunction.evaluate(x);
			}
			if (applyYFunction) {
				y = yFunction.evaluate(y);
			}
			// W. Christian
			// Java 1.3 bug in Windows VM
			// the following two lines cause a crash while drawing shapes if
			// |dstPts| is very large
			dstPts[dstOff++] = (float) (M00 * x + M01 * y + M02);
			dstPts[dstOff++] = (float) (M10 * x + M11 * y + M12);
		}
	}

	@Override
	public void transform(float[] srcPts, int srcOff, double[] dstPts,
			int dstOff, int numPts) {
		double M00;
		double M01;
		double M02;
		double M10;
		double M11;
		double M12; // For caching
		M00 = m00;
		M01 = m01;
		M02 = m02;
		M10 = m10;
		M11 = m11;
		M12 = m12;
		while (--numPts >= 0) {
			double x = srcPts[srcOff++];
			double y = srcPts[srcOff++];
			if (applyXFunction) {
				x = xFunction.evaluate(x);
			}
			if (applyYFunction) {
				y = yFunction.evaluate(y);
			}
			// W. Christian
			// Java 1.3 bug in Windows VM
			// the following two lines may cause a crash while drawing shapes if
			// |dstPts| is very large
			dstPts[dstOff++] = M00 * x + M01 * y + M02;
			dstPts[dstOff++] = M10 * x + M11 * y + M12;
		}
	}

	@Override
	public void transform(float[] srcPts, int srcOff, float[] dstPts,
			int dstOff, int numPts) {
		double M00;
		double M01;
		double M02;
		double M10;
		double M11;
		double M12; // For caching
		if (dstPts == srcPts && dstOff > srcOff && dstOff < srcOff + numPts * 2) {
			// If the arrays overlap partially with the destination higher
			// than the source and we transform the coordinates normally
			// we would overwrite some of the later source coordinates
			// with results of previous transformations.
			// To get around this we use arraycopy to copy the points
			// to their final destination with correct overwrite
			// handling and then transform them in place in the new
			// safer location.
			System.arraycopy(srcPts, srcOff, dstPts, dstOff, numPts * 2);
			// srcPts = dstPts; // They are known to be equal.
			srcOff = dstOff;
		}
		M00 = m00;
		M01 = m01;
		M02 = m02;
		M10 = m10;
		M11 = m11;
		M12 = m12;
		while (--numPts >= 0) {
			double x = srcPts[srcOff++];
			double y = srcPts[srcOff++];
			if (applyXFunction) {
				x = xFunction.evaluate(x);
			}
			if (applyYFunction) {
				y = yFunction.evaluate(y);
			}
			// W. Christian
			// Java 1.3 bug in Windows VM
			// the following two lines may cause a crash while drawing shapes if
			// |dstPts| is very large
			dstPts[dstOff++] = (float) (M00 * x + M01 * y + M02);
			dstPts[dstOff++] = (float) (M10 * x + M11 * y + M12);
		}
	}

	@Override
	public Point2D transform(Point2D ptSrc, Point2D ptDst) {
		if (ptDst == null) {
			if (ptSrc instanceof Point2D.Double) {
				ptDst = new Point2D.Double();
			} else {
				ptDst = new Point2D.Float();
			}
		}
		// Copy source coords into local variables in case src == dst
		double x = ptSrc.getX();
		double y = ptSrc.getY();
		if (applyXFunction) {
			x = xFunction.evaluate(x);
		}
		if (applyYFunction) {
			y = yFunction.evaluate(y);
		}
		ptDst.setLocation(x * m00 + y * m01 + m02, x * m10 + y * m11 + m12);
		return ptDst;
	}

	@Override
	public void transform(Point2D[] ptSrc, int srcOff, Point2D[] ptDst,
			int dstOff, int numPts) {
		while (--numPts >= 0) {
			// Copy source coords into local variables in case src == dst
			Point2D src = ptSrc[srcOff++];
			double x = src.getX();
			double y = src.getY();
			if (applyXFunction) {
				x = xFunction.evaluate(x);
			}
			if (applyYFunction) {
				y = yFunction.evaluate(y);
			}
			Point2D dst = ptDst[dstOff++];
			if (dst == null) {
				if (src instanceof Point2D.Double) {
					dst = new Point2D.Double();
				} else {
					dst = new Point2D.Float();
				}
				ptDst[dstOff - 1] = dst;
			}
			dst.setLocation(x * m00 + y * m01 + m02, x * m10 + y * m11 + m12);
		}
	}

	@Override
	public void translate(double tx, double ty) {
		super.translate(tx, ty);
		updateMatrix();
	}

	private void updateMatrix() {
		getMatrix(flatmatrix);
		m00 = flatmatrix[0];
		m10 = flatmatrix[1];
		m01 = flatmatrix[2];
		m11 = flatmatrix[3];
		m02 = flatmatrix[4];
		m12 = flatmatrix[5];
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
