package org.lifeform.chart;

import java.awt.Cursor;
import java.awt.Graphics;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class ChartMonitor extends ChartPanel {
	private static final long serialVersionUID = 7384544621610798677L;

	public ChartMonitor(final JFreeChart chart, final boolean useBuffer) {
		super(chart, useBuffer);
	}

	@Override
	public void paint(final Graphics g) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		super.paint(g);
		setCursor(Cursor.getDefaultCursor());
	}

}
