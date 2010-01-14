package org.lifeform.chart;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.lifeform.configuration.Defaults;
import org.lifeform.optimizer.OptimizationResult;
import org.lifeform.optimizer.PerformanceMetric;
import org.lifeform.optimizer.StrategyParam;
import org.lifeform.optimizer.StrategyParams;
import org.lifeform.service.PreferencesHolder;
import org.lifeform.strategy.Strategy;
import org.lifeform.util.AppUtil;

/**
 * Contour plot of optimization results
 */
public class OptimizationMap {
	private static final Dimension MIN_SIZE = new Dimension(720, 550);// minimum
	// frame
	// size
	private final PreferencesHolder prefs;
	private final PerformanceMetric sortPerformanceMetric;
	private final Strategy strategy;
	private final JDialog parent;
	private final List<OptimizationResult> optimizationResults;

	private JFreeChart chart;
	private JComboBox horizontalCombo, verticalCombo, caseCombo, colorMapCombo;
	private double min, max;
	private ChartPanel chartPanel;

	public OptimizationMap(final JDialog parent, final Strategy strategy,
			final List<OptimizationResult> optimizationResults,
			final PerformanceMetric sortPerformanceMetric) {
		prefs = PreferencesHolder.getInstance();
		this.parent = parent;
		this.strategy = strategy;
		this.optimizationResults = optimizationResults;
		this.sortPerformanceMetric = sortPerformanceMetric;
		chart = createChart();
	}

	public JDialog getChartFrame() {
		final JDialog chartFrame = new JDialog(parent);
		chartFrame.setTitle("Optimization Map - " + strategy);
		chartFrame.setModal(true);

		JPanel northPanel = new JPanel(new SpringLayout());
		JPanel centerPanel = new JPanel(new SpringLayout());
		JPanel chartOptionsPanel = new JPanel(new SpringLayout());

		Border etchedBorder = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder border = BorderFactory.createTitledBorder(etchedBorder,
				"Optimization Map Options");
		chartOptionsPanel.setBorder(border);

		JLabel horizontalLabel = new JLabel("Horizontal:", JLabel.TRAILING);
		horizontalCombo = new JComboBox();
		horizontalLabel.setLabelFor(horizontalCombo);

		JLabel verticalLabel = new JLabel("Vertical:", JLabel.TRAILING);
		verticalCombo = new JComboBox();
		verticalLabel.setLabelFor(verticalCombo);

		StrategyParams params = optimizationResults.get(0).getParams();
		for (StrategyParam param : params.getAll()) {
			horizontalCombo.addItem(param.getName());
			verticalCombo.addItem(param.getName());
		}

		horizontalCombo.setSelectedIndex(0);
		verticalCombo.setSelectedIndex(1);

		JLabel caseLabel = new JLabel("Case:", JLabel.TRAILING);
		caseCombo = new JComboBox(new String[] { "Best", "Worst" });
		caseCombo.setSelectedIndex(0);
		caseLabel.setLabelFor(caseCombo);

		JLabel colorMapLabel = new JLabel("Color map:", JLabel.TRAILING);
		colorMapCombo = new JComboBox(new String[] { "Heat", "Gray" });
		colorMapLabel.setLabelFor(colorMapCombo);

		chartOptionsPanel.add(horizontalLabel);
		chartOptionsPanel.add(horizontalCombo);
		chartOptionsPanel.add(verticalLabel);
		chartOptionsPanel.add(verticalCombo);
		chartOptionsPanel.add(caseLabel);
		chartOptionsPanel.add(caseCombo);
		chartOptionsPanel.add(colorMapLabel);
		chartOptionsPanel.add(colorMapCombo);

		AppUtil.makeOneLineGrid(chartOptionsPanel);
		northPanel.add(chartOptionsPanel);
		AppUtil.makeTopOneLineGrid(northPanel);

		chartPanel = new ChartPanel(chart);
		TitledBorder chartBorder = BorderFactory.createTitledBorder(
				etchedBorder, "Optimization Map");
		chartPanel.setBorder(chartBorder);

		centerPanel.add(chartPanel);
		AppUtil.makeOneLineGrid(centerPanel);

		int chartWidth = prefs.getInt(Defaults.OptimizationMapWidth);
		int chartHeight = prefs.getInt(Defaults.OptimizationMapHeight);
		int chartX = prefs.getInt(Defaults.OptimizationMapX);
		int chartY = prefs.getInt(Defaults.OptimizationMapY);

		chartFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				prefs.set(Defaults.OptimizationMapWidth, chartFrame.getWidth());
				prefs.set(Defaults.OptimizationMapHeight, chartFrame
						.getHeight());
				prefs.set(Defaults.OptimizationMapX, chartFrame.getX());
				prefs.set(Defaults.OptimizationMapY, chartFrame.getY());
				chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});

		horizontalCombo.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				repaint();
			}
		});

		verticalCombo.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				repaint();
			}
		});

		caseCombo.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				repaint();
			}
		});

		colorMapCombo.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				repaint();
			}
		});

		repaint();
		chartFrame.getContentPane().add(northPanel, BorderLayout.NORTH);
		chartFrame.getContentPane().add(centerPanel, BorderLayout.CENTER);
		chartFrame.getContentPane().setMinimumSize(MIN_SIZE);
		chartFrame.pack();
		chartFrame.setLocationRelativeTo(null);
		if (chartX >= 0 && chartY >= 0 && chartHeight > 0 && chartWidth > 0) {
			chartFrame.setBounds(chartX, chartY, chartWidth, chartHeight);
		}

		return chartFrame;
	}

	private void repaint() {
		chart = createChart();
		chartPanel.setChart(chart);
	}

	private double getMetric(final OptimizationResult optimizationResult) {
		double metric = 0;
		switch (sortPerformanceMetric) {
		case NetProfit:
			metric = optimizationResult.getNetProfit();
			break;
		case PF:
			metric = optimizationResult.getProfitFactor();
			break;
		case Kelly:
			metric = optimizationResult.getKellyCriterion();
			break;
		case PI:
			metric = optimizationResult.getPerformanceIndex();
			break;
		}
		return metric;
	}

	private XYZDataset createOptimizationDataset() {
		double[] x, y, z;

		synchronized (optimizationResults) {
			int size = optimizationResults.size();
			x = new double[size];
			y = new double[size];
			z = new double[size];

			Map<String, Double> values = new HashMap<String, Double>();

			int xParameterIndex = (horizontalCombo == null) ? 0
					: horizontalCombo.getSelectedIndex();
			int yParameterIndex = (verticalCombo == null) ? 1 : verticalCombo
					.getSelectedIndex();

			int index = 0;
			min = max = getMetric(optimizationResults.get(index));
			int selectedsCase = (caseCombo == null) ? 0 : caseCombo
					.getSelectedIndex();

			for (OptimizationResult optimizationResult : optimizationResults) {
				StrategyParams params = optimizationResult.getParams();

				x[index] = params.get(xParameterIndex).getValue();
				y[index] = params.get(yParameterIndex).getValue();
				z[index] = getMetric(optimizationResult);

				String key = x[index] + "," + y[index];
				Double value = values.get(key);

				if (value != null) {
					if (selectedsCase == 0) {
						z[index] = Math.max(value, z[index]);
					} else if (selectedsCase == 1) {
						z[index] = Math.min(value, z[index]);
					}
				}

				values.put(key, z[index]);

				min = Math.min(min, z[index]);
				max = Math.max(max, z[index]);
				index++;
			}
		}

		DefaultXYZDataset dataset = new DefaultXYZDataset();
		dataset.addSeries("optimization", new double[][] { x, y, z });

		return dataset;
	}

	private class HeatPaintScale implements PaintScale {
		public Paint getPaint(final double z) {
			double normalizedZ = (z - min) / (max - min);
			double brightness = 1;
			double saturation = Math.max(0.1, Math.abs(2 * normalizedZ - 1));
			double red = 0;
			double blue = 0.7;
			double hue = blue - normalizedZ * (blue - red);
			return Color.getHSBColor((float) hue, (float) saturation,
					(float) brightness);
		}

		public double getUpperBound() {
			return max;
		}

		public double getLowerBound() {
			return min;
		}
	}

	public class GrayPaintScale implements PaintScale {
		public Paint getPaint(final double z) {
			double normalizedZ = z - min;
			double clrs = 255.0 / (max - min);
			int color = (int) (255 - normalizedZ * clrs);
			return new Color(color, color, color, 255);
		}

		public double getUpperBound() {
			return max;
		}

		public double getLowerBound() {
			return min;
		}
	}

	private JFreeChart createChart() {
		XYZDataset dataset = createOptimizationDataset();

		NumberAxis xAxis = new NumberAxis();
		xAxis.setAutoRangeIncludesZero(false);
		NumberAxis yAxis = new NumberAxis();
		yAxis.setAutoRangeIncludesZero(false);

		xAxis.setLabel(horizontalCombo == null ? null
				: (String) horizontalCombo.getSelectedItem());
		yAxis.setLabel(verticalCombo == null ? null : (String) verticalCombo
				.getSelectedItem());

		XYBlockRenderer renderer = new XYBlockRenderer();
		int paintScaleIndex = (colorMapCombo == null) ? 0 : colorMapCombo
				.getSelectedIndex();
		PaintScale paintScale = null;
		switch (paintScaleIndex) {
		case 0:
			paintScale = new HeatPaintScale();
			break;
		case 1:
			paintScale = new GrayPaintScale();
			break;

		}

		renderer.setPaintScale(paintScale);

		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
		chart = new JFreeChart(plot);

		chart.removeLegend();
		chart.getPlot().setOutlineStroke(new BasicStroke(1.0f));
		NumberAxis scaleAxis = new NumberAxis(sortPerformanceMetric.getName());
		scaleAxis.setRange(min, max);
		PaintScaleLegend legend = new PaintScaleLegend(paintScale, scaleAxis);
		legend.setFrame(new BlockBorder(Color.GRAY));
		legend.setPadding(new RectangleInsets(5, 5, 5, 5));
		legend.setMargin(new RectangleInsets(4, 6, 40, 6));
		legend.setPosition(RectangleEdge.RIGHT);
		chart.addSubtitle(legend);

		return chart;
	}
}
