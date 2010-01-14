package org.lifeform.service;

//import com.birosoft.liquid.LiquidLookAndFeel;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import org.lifeform.backdata.BackDataDialog;
import org.lifeform.backdata.BackTestStrategyRunner;
import org.lifeform.chart.StrategyPerformanceChart;
import org.lifeform.configuration.Defaults;
import org.lifeform.gui.dialog.PreferencesDialog;
import org.lifeform.gui.dialog.TradeMaker;
import org.lifeform.gui.dialog.TradingModeDialog;
import org.lifeform.opentick.OTBackDataDialog;
import org.lifeform.optimizer.OptimizerDialog;
import org.lifeform.strategy.Strategy;
import org.lifeform.strategy.StrategyRunner;
import org.lifeform.util.AppUtil;

import com.birosoft.liquid.LiquidLookAndFeel;

/**
 * Acts as a controller in the Model-View-Controller pattern
 */

public class MainController {
	private final TradeMaker mainFrame;
	private final TradingModeDialog tradingModeDialog;
	private final PreferencesHolder preferences = PreferencesHolder
			.getInstance();

	public MainController() throws Exception, IOException {
		boolean lookAndFeelMacTitle = preferences
				.getBool(Defaults.LookAndFeelMacStyle);
		if (lookAndFeelMacTitle)
			LiquidLookAndFeel.setLiquidDecorations(true, "mac");

		String lookAndFeelClassName = preferences
				.get(Defaults.LookAndFeelClassName);
		setappLookandFeel(lookAndFeelClassName);

		mainFrame = new TradeMaker();
		tradingModeDialog = new TradingModeDialog(mainFrame);

		mainFrame.setSelectedLookAndFeel(lookAndFeelClassName);
		mainFrame.setMacWindowTitle(lookAndFeelMacTitle);

		assignListeners();
	}

	private void assignListeners() {
		mainFrame.IBHistoricalDataAction(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				try {
					Dispatcher.setTradingMode();
					new BackDataDialog(mainFrame);
				} catch (Throwable t) {
					Dispatcher.getReporter().report(t);
					AppUtil.showError(mainFrame, t.getMessage());
				} finally {
					mainFrame.setCursor(Cursor
							.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});

		mainFrame.OTHistoricalDataAction(new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				try {
					new OTBackDataDialog(mainFrame);
				} catch (Throwable t) {
					Dispatcher.getReporter().report(t);
					AppUtil.showError(mainFrame, t.getMessage());
				} finally {
					mainFrame.setCursor(Cursor
							.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});

		tradingModeDialog.selectFileAction(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(AppUtil
						.getAppPath());
				fileChooser.setDialogTitle("Select Historical Data File");

				String filename = tradingModeDialog.getFileName();
				if (filename.length() != 0) {
					fileChooser.setSelectedFile(new File(filename));
				}

				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					tradingModeDialog.setFileName(file.getAbsolutePath());
				}
			}
		});

		mainFrame.runStrategiesAction(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				try {
					mainFrame.setCursor(Cursor
							.getPredefinedCursor(Cursor.WAIT_CURSOR));
					ArrayList<Strategy> selectedStrategies = mainFrame
							.getTradingTableModel().getSelectedStrategies();
					if (selectedStrategies.size() == 0) {
						AppUtil
								.showError(mainFrame,
										"At least one strategy must be selected to run.");
						return;
					}

					tradingModeDialog.setVisible(true);
					if (tradingModeDialog.getAction() == JOptionPane.CANCEL_OPTION) {
						return;
					}

					mainFrame.getTradingTableModel().saveStrategyStatus();
					mainFrame.getTradingTableModel().reset();

					Dispatcher.setActiveStrategies(selectedStrategies.size());
					Dispatcher.Mode mode = tradingModeDialog.getMode();
					if (mode == Dispatcher.Mode.BACK_TEST) {
						Dispatcher.setBackTestingMode(tradingModeDialog);
						for (Strategy strategy : selectedStrategies) {
							new BackTestStrategyRunner(strategy).start();
						}
					}

					if (mode == Dispatcher.Mode.TRADE) {
						Dispatcher.setTradingMode();
						for (Strategy strategy : selectedStrategies) {
							new Thread(new StrategyRunner(strategy)).start();
						}
					}
				} catch (Throwable t) {
					Dispatcher.getReporter().report(t);
					AppUtil.showError(mainFrame, t.toString());
				} finally {
					mainFrame.setCursor(Cursor
							.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});

		mainFrame.optimizerAction(new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				try {
					mainFrame.setCursor(Cursor
							.getPredefinedCursor(Cursor.WAIT_CURSOR));
					OptimizerDialog optimizerDialog = new OptimizerDialog(
							mainFrame);
					optimizerDialog.addWindowListener(new WindowAdapter() {

						@Override
						public void windowClosed(final WindowEvent arg0) {
							saveOptimizerDimensions(arg0);
						}

						@Override
						public void windowClosing(final WindowEvent arg0) {
							saveOptimizerDimensions(arg0);
						}

						private void saveOptimizerDimensions(
								final WindowEvent arg0) {
							preferences.set(Defaults.OptimizerHeight, arg0
									.getWindow().getHeight());
							preferences.set(Defaults.OptimizerWidth, arg0
									.getWindow().getWidth());
							preferences.set(Defaults.OptimizerX, arg0
									.getWindow().getX());
							preferences.set(Defaults.OptimizerY, arg0
									.getWindow().getY());
						}
					});
				} catch (Throwable t) {
					Dispatcher.getReporter().report(t);
					AppUtil.showError(mainFrame, t.getMessage());
				} finally {
					mainFrame.setCursor(Cursor
							.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});

		mainFrame.userGuideAction(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				try {
					String url = "http://www.myjavaserver.com/~nonlinear/JSystemTrader/UserManual.pdf";
					AppUtil.openURL(url);
				} catch (Exception t) {
					Dispatcher.getReporter().report(t);
					AppUtil.showError(mainFrame, t.getMessage());
				}
			}
		});

		mainFrame.discussionAction(new ActionListener() {
			public void actionPerformed(final ActionEvent ae) {
				try {
					String url = "http://groups.google.com/group/jsystemtrader/topics?gvc=2";
					AppUtil.openURL(url);
				} catch (Exception t) {
					Dispatcher.getReporter().report(t);
					AppUtil.showError(mainFrame, t.getMessage());
				}
			}
		});

		mainFrame.projectHomeAction(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				try {
					String url = "http://code.google.com/p/jsystemtrader/";
					AppUtil.openURL(url);
				} catch (Exception t) {
					Dispatcher.getReporter().report(t);
					AppUtil.showError(mainFrame, t.getMessage());
				}
			}
		});

		mainFrame.exitAction(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				exit();
			}
		});

		mainFrame.exitAction(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				exit();
			}
		});

		mainFrame.aboutAction(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				// new AboutDialog(mainFrame);
			}
		});

		mainFrame.strategyChartAction(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				try {
					mainFrame.setCursor(Cursor
							.getPredefinedCursor(Cursor.WAIT_CURSOR));
					TradingTableModel ttm = mainFrame.getTradingTableModel();
					int selectedRow = mainFrame.getTradingTable()
							.getSelectedRow();
					if (selectedRow < 0) {
						String message = "No strategy is selected.";
						AppUtil.showError(mainFrame, message);
						return;
					}
					Strategy strategy = ttm.getStrategyForRow(mainFrame
							.getTradingTable().getSelectedRow());
					StrategyPerformanceChart spChart = new StrategyPerformanceChart(
							strategy);
					JFrame chartFrame = spChart.getChartFrame(mainFrame);

					chartFrame.setVisible(true);
				} catch (Exception ex) {
					Dispatcher.getReporter().report(ex);
					AppUtil.showError(mainFrame, ex.toString());
				} finally {
					mainFrame.setCursor(Cursor
							.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}

			}
		});

		mainFrame.doubleClickTableAction(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 2) {
					try {
						mainFrame.setCursor(Cursor
								.getPredefinedCursor(Cursor.WAIT_CURSOR));
						TradingTableModel ttm = mainFrame
								.getTradingTableModel();
						int selectedRow = mainFrame.getTradingTable()
								.getSelectedRow();
						if (selectedRow < 0) {
							return;
						}
						Strategy strategy = ttm.getStrategyForRow(mainFrame
								.getTradingTable().getSelectedRow());
						StrategyPerformanceChart spChart = new StrategyPerformanceChart(
								strategy);
						JFrame chartFrame = spChart.getChartFrame(mainFrame);

						chartFrame.setVisible(true);
					} catch (Exception ex) {
						Dispatcher.getReporter().report(ex);
						AppUtil.showError(mainFrame, ex.toString());
					} finally {
						mainFrame.setCursor(Cursor
								.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				}
			}
		});

		mainFrame.lookAndFeelAction(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String newLook = mainFrame.getSelectedLookAndFeel();
					preferences.set(Defaults.LookAndFeelClassName, newLook);
					setappLookandFeel(newLook);
				}
			}
		});

		mainFrame.macWindowTitleAction(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				preferences.set(Defaults.LookAndFeelMacStyle, e
						.getStateChange() == ItemEvent.SELECTED);
				AppUtil
						.showMessage(mainFrame,
								"Liquid mac decoration will take effect after restarting JST");
			}
		});

		mainFrame.preferencesAction(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				try {
					mainFrame.setCursor(Cursor
							.getPredefinedCursor(Cursor.WAIT_CURSOR));
					JDialog preferencesDialog = new PreferencesDialog(mainFrame);
					preferencesDialog.setVisible(true);
				} catch (Throwable t) {
					AppUtil.showError(mainFrame, t.getMessage());
				} finally {
					mainFrame.setCursor(Cursor
							.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});

	}

	private void setappLookandFeel(final String lookAndFeelName) {
		try {
			UIManager.setLookAndFeel(lookAndFeelName);
		} catch (Throwable t) {
			AppUtil
					.showMessage(
							null,
							t.getMessage()
									+ ": Unable to set custom look & feel. The default L&F will be used.");
		}

		// Set the color scheme explicitly
		ColorUIResource color = new ColorUIResource(102, 102, 153);
		UIManager.put("Label.foreground", color);
		UIManager.put("TitledBorder.titleColor", color);

		if (mainFrame != null) {
			SwingUtilities.updateComponentTreeUI(mainFrame);
			mainFrame.pack();
		}

		if (tradingModeDialog != null) {
			SwingUtilities.updateComponentTreeUI(tradingModeDialog);
			tradingModeDialog.pack();
		}
	}

	private void exit() {
		preferences.set(Defaults.MainWindowHeight, mainFrame.getSize().height);
		preferences.set(Defaults.MainWindowWidth, mainFrame.getSize().width);
		preferences.set(Defaults.MainWindowX, mainFrame.getX());
		preferences.set(Defaults.MainWindowY, mainFrame.getY());
		Dispatcher.exit();
	}

	public static void main(final String[] args) {
		try {
			MainController ctr = new MainController();

		} catch (Exception e) {
		}
	}
}
