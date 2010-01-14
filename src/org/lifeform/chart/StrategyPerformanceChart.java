package org.lifeform.chart;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;
import org.lifeform.chart.indicator.IndicatorValue;
import org.lifeform.configuration.Defaults;
import org.lifeform.market.PriceBar;
import org.lifeform.market.QuoteHistory;
import org.lifeform.position.Position;
import org.lifeform.position.ProfitAndLoss;
import org.lifeform.position.ProfitAndLossHistory;
import org.lifeform.service.Dispatcher;
import org.lifeform.service.PreferencesHolder;
import org.lifeform.strategy.Strategy;
import org.lifeform.util.AppUtil;

/**
 * Multi-indicator strategy performance chart where indicators can be grouped
 * together and displayed on subplots, one group of indicators per plot.
 */

public class StrategyPerformanceChart {
	private static final int PRICE_PLOT_WEIGHT = 5;
	private static final int ANNOTATION_RADIUS = 6;
	private static final Font ANNOTATION_FONT = new Font("SansSerif",
			Font.BOLD, 11);
	private static final Paint BACKGROUND_COLOR = new GradientPaint(0, 0,
			new Color(0, 0, 176), 0, 0, Color.BLACK);

	private JFreeChart chart;
	private CombinedDomainXYPlot combinedPlot;

	private DateAxis dateAxis;
	private final Strategy strategy;
	private final Map<Integer, TimeSeriesCollection> tsCollections;
	private final Map<Integer, StringBuilder> indicatorLabels;
	private FastXYPlot pricePlot, pnlPlot;
	private CandlestickRenderer candleRenderer;
	private MultiColoredBarRenderer mcbRenderer;
	private JComboBox chartTypeCombo, timeLineCombo, timeZoneCombo;
	private JCheckBox tradesVisibilityCheck, pnlVisibilityCheck;
	private final ArrayList<CircledTextAnnotation> annotations = new ArrayList<CircledTextAnnotation>();
	private final PreferencesHolder preferences;

	public StrategyPerformanceChart(final Strategy strategy) throws Exception {
		preferences = PreferencesHolder.getInstance();
		this.strategy = strategy;
		tsCollections = new HashMap<Integer, TimeSeriesCollection>();
		indicatorLabels = new HashMap<Integer, StringBuilder>();
		chart = createChart();
	}

	private void setRenderer() {
		int chartType = chartTypeCombo.getSelectedIndex();
		pricePlot.setRenderer(chartType == 0 ? candleRenderer : mcbRenderer);
	}

	private void setTimeline() {
		int timeLineType = timeLineCombo == null ? 0 : timeLineCombo
				.getSelectedIndex();
		QuoteHistory qh = strategy.getQuoteHistory();
		MarketTimeLine mtl = new MarketTimeLine(qh);
		SegmentedTimeline segmentedTimeline = (timeLineType == 0) ? mtl
				.getAllHours() : mtl.getNormalHours();
		dateAxis.setTimeline(segmentedTimeline);
	}

	private void setTimeZone() {
		int timeZoneType = timeZoneCombo == null ? 0 : timeZoneCombo
				.getSelectedIndex();
		TimeZone tz = timeZoneType == 0 ? strategy.getTradingSchedule()
				.getTimeZone() : TimeZone.getDefault();
		dateAxis.setTimeZone(tz);
	}

	public JFrame getChartFrame(final JFrame parent) {
		final JFrame chartFrame = new JFrame("Strategy Performance Chart - "
				+ strategy);
		chartFrame.setIconImage(parent.getIconImage());

		Container contentPane = chartFrame.getContentPane();

		final JPanel chartOptionsPanel = new JPanel(new BorderLayout());
		JPanel chartControlsPanel = new JPanel(new SpringLayout());
		chartOptionsPanel.add(chartControlsPanel, BorderLayout.NORTH);

		Border etchedBorder = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder border = BorderFactory.createTitledBorder(etchedBorder);
		border.setTitle("Chart Options");
		chartOptionsPanel.setBorder(border);

		JLabel chartTypeLabel = new JLabel("Bar Type:", SwingConstants.TRAILING);
		chartTypeCombo = new JComboBox(new String[] { "Candle", "OHLC" });
		chartTypeLabel.setLabelFor(chartTypeCombo);

		JLabel timeLineLabel = new JLabel("Timeline:", SwingConstants.TRAILING);
		timeLineCombo = new JComboBox(new String[] { "All Hours",
				"Trading Hours" });
		timeLineLabel.setLabelFor(timeLineCombo);

		JLabel timeZoneLabel = new JLabel("Time Zone:", SwingConstants.TRAILING);
		timeZoneCombo = new JComboBox(new String[] { "Exchange", "Local" });
		timeZoneLabel.setLabelFor(timeZoneCombo);

		tradesVisibilityCheck = new JCheckBox("Show trades");
		tradesVisibilityCheck.setSelected(true);
		pnlVisibilityCheck = new JCheckBox("Show P&L");
		pnlVisibilityCheck.setSelected(true);

		chartControlsPanel.add(chartTypeLabel);
		chartControlsPanel.add(chartTypeCombo);
		chartControlsPanel.add(timeLineLabel);
		chartControlsPanel.add(timeLineCombo);
		chartControlsPanel.add(timeZoneLabel);
		chartControlsPanel.add(timeZoneCombo);
		chartControlsPanel.add(tradesVisibilityCheck);
		chartControlsPanel.add(pnlVisibilityCheck);

		AppUtil.makeCompactGrid(chartControlsPanel, 1, 8, 12, 5, 8, 5);// rows,
		// cols,
		// initX,
		// initY,
		// xPad,
		// yPad

		setRenderer();

		chartTypeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setRenderer();
			}
		});

		timeLineCombo.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setTimeline();
			}
		});

		timeZoneCombo.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				setTimeZone();
			}
		});

		pnlVisibilityCheck.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				boolean show = pnlVisibilityCheck.isSelected();
				if (show) {
					combinedPlot.add(pnlPlot, 1);
				} else {
					combinedPlot.remove(pnlPlot);
				}
			}
		});

		tradesVisibilityCheck.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				boolean show = tradesVisibilityCheck.isSelected();
				for (CircledTextAnnotation annotation : annotations) {
					if (show) {
						pricePlot.addAnnotation(annotation);
					} else {
						pricePlot.removeAnnotation(annotation);
					}
				}
			}
		});

		contentPane.add(chartOptionsPanel, BorderLayout.NORTH);

		JPanel scrollBarPanel = new JPanel(new BorderLayout());
		DateScrollBar dateScrollBar = new DateScrollBar(combinedPlot);
		scrollBarPanel.add(dateScrollBar, BorderLayout.SOUTH);

		ChartMonitor chartMonitor = new ChartMonitor(chart, true);
		chartMonitor.setRangeZoomable(false);

		contentPane.add(chartMonitor, BorderLayout.CENTER);
		contentPane.add(scrollBarPanel, BorderLayout.PAGE_END);

		chartFrame.setContentPane(contentPane);
		chartFrame.pack();

		int chartHeight = preferences.getInt(Defaults.ChartHeight);
		int chartWidth = preferences.getInt(Defaults.ChartWidth);
		int chartX = preferences.getInt(Defaults.ChartX);
		int chartY = preferences.getInt(Defaults.ChartY);
		int chartState = preferences.getInt(Defaults.ChartState);

		if (chartState >= 0)
			chartFrame.setExtendedState(chartState);

		if (chartX >= 0 && chartY >= 0 && chartHeight > 0 && chartWidth > 0)
			chartFrame.setBounds(chartX, chartY, chartWidth, chartHeight);
		else
			RefineryUtilities.centerFrameOnScreen(chartFrame);

		chartFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				preferences.set(Defaults.ChartHeight, chartFrame.getHeight());
				preferences.set(Defaults.ChartWidth, chartFrame.getWidth());
				preferences.set(Defaults.ChartX, chartFrame.getX());
				preferences.set(Defaults.ChartY, chartFrame.getY());
				preferences.set(Defaults.ChartState, chartFrame
						.getExtendedState());
				chartFrame
						.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			}
		});

		return chartFrame;
	}

	private TimeSeries createIndicatorSeries(
			final ChartableIndicator chartableIndicator) {

		TimeSeries ts = new TimeSeries(chartableIndicator.getName(),
				Second.class);
		ts.setRangeDescription(chartableIndicator.getName());

		List<IndicatorValue> values = chartableIndicator.getIndicator()
				.getHistory();

		synchronized (values) {
			for (IndicatorValue indicatorValue : values) {
				try {
					ts.add(new Second(new Date(indicatorValue.getDate())),
							indicatorValue.getValue(), false);
				} catch (Exception e) {
					Dispatcher.getReporter().report(e);
				}
			}
		}
		ts.fireSeriesChanged();
		return ts;
	}

	private TimeSeries createProfitAndLossSeries(final ProfitAndLossHistory plHistory) {

		TimeSeries ts = new TimeSeries("P&L", Second.class);
		ts.setRangeDescription("P&L");

		synchronized (plHistory) {
			for (ProfitAndLoss profitAndLoss : plHistory.getHistory()) {
				try {
					ts.add(new Second(new Date(profitAndLoss.getDate())),
							profitAndLoss.getValue(), false);
				} catch (Exception e) {
					Dispatcher.getReporter().report(e);
				}
			}
		}
		ts.fireSeriesChanged();
		return ts;
	}

	private OHLCDataset createHighLowDataset() {
		QuoteHistory qh = strategy.getQuoteHistory();
		int qhSize = qh.size();
		Date[] dates = new Date[qhSize];

		double[] highs = new double[qhSize];
		double[] lows = new double[qhSize];
		double[] opens = new double[qhSize];
		double[] closes = new double[qhSize];
		double[] volumes = new double[qhSize];

		for (int bar = 0; bar < qhSize; bar++) {
			PriceBar priceBar = qh.getPriceBar(bar);

			dates[bar] = new Date(priceBar.getDate());
			highs[bar] = priceBar.getHigh();
			lows[bar] = priceBar.getLow();
			opens[bar] = priceBar.getOpen();
			closes[bar] = priceBar.getClose();
			volumes[bar] = priceBar.getVolume();
		}

		String ticker = strategy.getContract().m_symbol;
		return new DefaultHighLowDataset(ticker, dates, highs, lows, opens,
				closes, volumes);
	}

	private JFreeChart createChart() {

		// create OHLC bar renderer
		mcbRenderer = new MultiColoredBarRenderer();
		mcbRenderer.setSeriesPaint(0, Color.WHITE);
		mcbRenderer.setBaseStroke(new BasicStroke(3));
		mcbRenderer.setSeriesPaint(0, new Color(250, 240, 150));

		// create candlestick renderer
		candleRenderer = new CandlestickRenderer(3);
		candleRenderer.setDrawVolume(false);
		candleRenderer
				.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_AVERAGE);
		candleRenderer.setUpPaint(Color.GREEN);
		candleRenderer.setDownPaint(Color.RED);
		candleRenderer.setSeriesPaint(0, new Color(250, 240, 150));
		candleRenderer.setBaseStroke(new BasicStroke(1));

		dateAxis = new DateAxis();

		setTimeline();
		setTimeZone();

		// create price plot
		OHLCDataset highLowDataset = createHighLowDataset();
		NumberAxis priceAxis = new NumberAxis("Price");
		priceAxis.setAutoRangeIncludesZero(false);
		pricePlot = new FastXYPlot(highLowDataset, dateAxis, priceAxis, null);
		pricePlot.setBackgroundPaint(BACKGROUND_COLOR);

		// parent plot
		combinedPlot = new CombinedDomainXYPlot(dateAxis);
		combinedPlot.setGap(10.0);
		combinedPlot.setOrientation(PlotOrientation.VERTICAL);
		combinedPlot.add(pricePlot, PRICE_PLOT_WEIGHT);

		// Put all indicators into groups, so that each group is
		// displayed on its own subplot
		for (ChartableIndicator chartableIndicator : strategy.getIndicators()) {
			TimeSeries ts = createIndicatorSeries(chartableIndicator);
			int subChart = chartableIndicator.getChart();
			if (subChart >= 0) {
				TimeSeriesCollection tsCollection = tsCollections.get(subChart);
				if (tsCollection == null) {
					tsCollection = new TimeSeriesCollection();
					tsCollections.put(subChart, tsCollection);
				}
				tsCollection.addSeries(ts);

				// prepare indicators group labels
				StringBuilder indicatorLabel = indicatorLabels.get(subChart);
				if (indicatorLabel == null)
					indicatorLabel = new StringBuilder();
				else
					indicatorLabel.append(", ");
				indicatorLabel.append(chartableIndicator.getName());
				indicatorLabels.put(subChart, indicatorLabel);
			}
		}

		// create P&L series
		TimeSeriesCollection profitAndLossCollection = new TimeSeriesCollection();
		ProfitAndLossHistory plHistory = strategy.getPositionManager()
				.getProfitAndLossHistory();
		TimeSeries profitAndLoss = createProfitAndLossSeries(plHistory);
		profitAndLossCollection.addSeries(profitAndLoss);
		tsCollections.put(-1, profitAndLossCollection);

		// Plot positions
		for (Position position : strategy.getPositionManager()
				.getPositionsHistory()) {

			double avgFillPrice = position.getAvgFillPrice();
			double orderAvgFillPrice = position.getOrderAvgFillPrice();
			Date date = new Date(position.getDate());
			int pos = position.getPosition();

			Color bkColor = Color.YELLOW;
			if (pos > 0)
				bkColor = Color.GREEN;
			else if (pos < 0)
				bkColor = Color.RED;

			String annotationText = String.valueOf(Math.abs(pos));
			if (pos == 0 || avgFillPrice == orderAvgFillPrice) {
				_addCircledTextAnnotation(annotationText, date.getTime(),
						pos == 0 ? orderAvgFillPrice : avgFillPrice, bkColor);
			} else {
				_addCircledTextAnnotation(annotationText, date.getTime(),
						orderAvgFillPrice, bkColor);
				_addCircledTextAnnotation("A", date.getTime(), avgFillPrice,
						bkColor);
			}

			// Add a "A" point that represent the average fill price for the
			// total position
			if (pos != 0 && avgFillPrice != orderAvgFillPrice) {
				CircledTextAnnotation averageCircledText = new CircledTextAnnotation(
						"A", date.getTime(), avgFillPrice, ANNOTATION_RADIUS);
				averageCircledText.setFont(ANNOTATION_FONT);
				averageCircledText.setBkColor(bkColor);
				averageCircledText.setPaint(Color.BLACK);
				averageCircledText.setTextAnchor(TextAnchor.CENTER);

				pricePlot.addAnnotation(averageCircledText);
				annotations.add(averageCircledText);
			}
		}

		// Now that the indicators are grouped, create subplots
		AbstractXYItemRenderer renderer;
		for (Map.Entry<Integer, TimeSeriesCollection> mapEntry : tsCollections
				.entrySet()) {
			int subChart = mapEntry.getKey();
			TimeSeriesCollection tsCollection = mapEntry.getValue();

			if (subChart == -1) {
				renderer = new XYLineAndShapeRenderer();
				renderer.setSeriesShape(0, new Ellipse2D.Double(-2, -2, 4, 4));
			} else {
				renderer = new StandardXYItemRenderer();
				renderer.setBaseStroke(new BasicStroke(2));
			}

			if (subChart == 0) {
				pricePlot.setDataset(1, tsCollection);
				pricePlot.setRenderer(1, renderer);
			} else {
				String collectionName = (subChart == -1) ? "P&L"
						: indicatorLabels.get(subChart).toString();
				NumberAxis indicatorAxis = new NumberAxis(collectionName);
				indicatorAxis.setAutoRangeIncludesZero(false);
				FastXYPlot plot = new FastXYPlot(tsCollection, dateAxis,
						indicatorAxis, renderer);
				plot.setBackgroundPaint(BACKGROUND_COLOR);
				int weight = 1;
				if (subChart == -1) {
					pnlPlot = plot;
				}
				combinedPlot.add(plot, weight);
			}
		}

		combinedPlot.setDomainAxis(dateAxis);

		// Finally, create the chart
		chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, combinedPlot,
				true);
		chart.getLegend().setPosition(RectangleEdge.TOP);
		chart.getLegend().setBackgroundPaint(Color.LIGHT_GRAY);

		return chart;

	}

	private void _addCircledTextAnnotation(final String msg, final double x, final double y,
			final Color color) {
		CircledTextAnnotation averageCircledText = new CircledTextAnnotation(
				msg, x, y, ANNOTATION_RADIUS);
		averageCircledText.setFont(ANNOTATION_FONT);
		averageCircledText.setBkColor(color);
		averageCircledText.setPaint(Color.BLACK);
		averageCircledText.setTextAnchor(TextAnchor.CENTER);

		pricePlot.addAnnotation(averageCircledText);
		annotations.add(averageCircledText);
	}
}
