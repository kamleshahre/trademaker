package org.lifeform.chart;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.List;

import javax.swing.JScrollBar;

import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.Timeline;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.AxisChangeListener;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.RendererUtilities;
import org.jfree.data.Range;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;

/**
 * Scroll bar for a combined chart where the horizontal axis represents dates
 */
public class DateScrollBar extends JScrollBar implements AdjustmentListener,
		AxisChangeListener {
	private static final long serialVersionUID = -7383917775710985504L;
	private static final int STEPS = 10000;
	private final DateAxis dateAxis;
	private final Range range;
	private double rangeMin, dateRange, ratio;
	private final CombinedDomainXYPlot combinedDomainPlot;

	public DateScrollBar(final CombinedDomainXYPlot combinedDomainPlot) {
		super(HORIZONTAL);
		this.combinedDomainPlot = combinedDomainPlot;

		dateAxis = (DateAxis) combinedDomainPlot.getDomainAxis();
		range = combinedDomainPlot.getDataRange(dateAxis);

		dateAxis.addChangeListener(this);
		addAdjustmentListener(this);
	}

	private void rangeUpdate() {
		List<?> subPlots = combinedDomainPlot.getSubplots();
		double lowerBound = dateAxis.getLowerBound();
		double upperBound = dateAxis.getUpperBound();

		for (Object subPlot : subPlots) {
			XYPlot plot = (XYPlot) subPlot;
			int datasetCount = plot.getDatasetCount();
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;

			for (int datasetNumber = 0; datasetNumber < datasetCount; datasetNumber++) {
				XYDataset dataset = plot.getDataset(datasetNumber);
				int seriesCount = dataset.getSeriesCount();

				boolean isOHLC = dataset instanceof OHLCDataset;
				OHLCDataset ohlcDataset = isOHLC ? (OHLCDataset) dataset : null;

				for (int series = 0; series < seriesCount; series++) {
					int[] itemBounds = RendererUtilities.findLiveItems(dataset,
							series, lowerBound, upperBound);
					int firstItem = itemBounds[0];
					int lastItem = itemBounds[1];

					for (int item = firstItem; item < lastItem; item++) {
						double high, low;
						if (isOHLC) {
							high = ohlcDataset
									.getHighValue(datasetNumber, item);
							low = ohlcDataset.getLowValue(datasetNumber, item);
						} else {
							high = low = dataset.getYValue(series, item);
						}

						max = Math.max(high, max);
						min = Math.min(low, min);
					}
				}
			}

			if (max > min) {
				plot.getRangeAxis().setRange(min, max);
			}
		}
	}

	public void axisChanged(final AxisChangeEvent event) {
		Timeline timeLine = dateAxis.getTimeline();
		rangeMin = timeLine.toTimelineValue((long) range.getLowerBound());
		double rangeMax = timeLine
				.toTimelineValue((long) range.getUpperBound());

		long dateMin = timeLine
				.toTimelineValue((long) dateAxis.getLowerBound());
		long dateMax = timeLine
				.toTimelineValue((long) dateAxis.getUpperBound());

		dateRange = dateMax - dateMin;
		ratio = STEPS / (rangeMax - rangeMin);

		int newExtent = (int) (dateRange * ratio);
		int newValue = (int) ((dateMin - rangeMin) * ratio);

		setValues(newValue, newExtent, 0, STEPS);
	}

	public void adjustmentValueChanged(final AdjustmentEvent e) {
		long start = (long) (getValue() / ratio + rangeMin);
		long end = (long) (start + dateRange);

		if (end > start) {
			Timeline timeLine = dateAxis.getTimeline();
			start = timeLine.toMillisecond(start);
			end = timeLine.toMillisecond(end);
		}

		dateAxis.setRange(start, end);
		rangeUpdate();
	}

}
