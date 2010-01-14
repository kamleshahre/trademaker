package org.lifeform.chart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.HighLowRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;

/**
 * Custom renderer for the HighLowChart to paint OHLC bars in different colors.
 * This functionality is not yet available in JFreeChart, thus the need for a
 * custom renderer.
 */
public class MultiColoredBarRenderer extends HighLowRenderer {
	private static final long serialVersionUID = -556574064912698921L;
	private static final Color BULLISH_COLOR = Color.GREEN;
	private static final Color BEARISH_COLOR = Color.RED;
	private static final Color NEUTRAL_COLOR = Color.YELLOW;
	private OHLCDataset dataset;

	@Override
	public Paint getItemPaint(final int series, final int item) {
		double open = dataset.getOpenValue(series, item);
		double close = dataset.getCloseValue(series, item);
		if (open == close) {
			return NEUTRAL_COLOR;
		} else {
			return (close > open) ? BULLISH_COLOR : BEARISH_COLOR;
		}
	}

	@Override
	public XYItemRendererState initialise(final Graphics2D g2, final Rectangle2D dataArea,
			final XYPlot plot, final XYDataset dataset, final PlotRenderingInfo info) {
		this.dataset = (OHLCDataset) dataset;
		return super.initialise(g2, dataArea, plot, dataset, info);
	}
}
