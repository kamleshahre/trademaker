package org.lifeform.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.TableColumnModel;

import org.hsqldb.util.DatabaseManagerSwing;
import org.lifeform.configuration.Defaults;
import org.lifeform.optimizer.StrategyParams;
import org.lifeform.service.Dispatcher;
import org.lifeform.service.ModelListener;
import org.lifeform.service.PreferencesHolder;
import org.lifeform.service.TradingTableModel;
import org.lifeform.strategy.Strategy;
import org.lifeform.util.AppUtil;
import org.lifeform.util.ClassFinder;
import org.lifeform.util.DoubleRenderer;

/**
 * Main application window.
 */
public class TradeMaker extends JFrame implements ModelListener {
	private static final long serialVersionUID = -8854527394537561842L;
	private JMenuItem optimizerMenuItem, exitMenuItem, aboutMenuItem,
			userGuideMenuItem, discussionMenuItem, projectHomeMenuItem,
			IBDataMenuItem, OTDataMenuItem, preferencesMenuItem,
			resizeTableMenuItem, databaseMenuItem;
	private JCheckBoxMenuItem macWindowTitleMenuItem;
	private JButton runButton, viewChartButton;
	private List<JRadioButtonMenuItem> lookAndFeelMenuItems;
	private ButtonGroup buttonGroupLookAndFeel;
	private TradingTableModel tradingTableModel;
	private JTable tradingTable;
	private final PreferencesHolder preferences = PreferencesHolder
			.getInstance();
	private int limitTableResize;

	public TradeMaker() throws Exception {

		lookAndFeelMenuItems = new ArrayList<JRadioButtonMenuItem>();

		Dispatcher.addListener(this);
		init();
		populateStrategies();
		pack();
		setLocationRelativeTo(null);

		int lastHeight = preferences.getInt(Defaults.MainWindowHeight);
		int lastWidth = preferences.getInt(Defaults.MainWindowWidth);
		int lastX = preferences.getInt(Defaults.MainWindowX);
		int lastY = preferences.getInt(Defaults.MainWindowY);

		if (lastHeight > 0 && lastWidth > 0)
			setBounds(lastX, lastY, lastWidth, lastHeight);

		setVisible(true);
		limitTableResize = 0;
	}

	public void modelChanged(final ModelListener.Event event, final Object value) {
		switch (event) {
		case STRATEGY_UPDATE:
			Strategy strategy = (Strategy) value;
			tradingTableModel.updateStrategy(strategy);
			if (limitTableResize == 0) {
				AppUtil.autoResizeTable(tradingTable, true);
				limitTableResize = 1;
			}
			break;
		case STRATEGIES_START:
			runButton.setEnabled(false);
			break;
		case STRATEGIES_END:
			runButton.setEnabled(true);
			AppUtil.autoResizeTable(tradingTable, true);
			break;
		}
	}

	public void userGuideAction(final ActionListener action) {
		userGuideMenuItem.addActionListener(action);
	}

	public void discussionAction(final ActionListener action) {
		discussionMenuItem.addActionListener(action);
	}

	public void projectHomeAction(final ActionListener action) {
		projectHomeMenuItem.addActionListener(action);
	}

	public void runStrategiesAction(final ActionListener action) {
		runButton.addActionListener(action);
	}

	public void optimizerAction(final ActionListener action) {
		optimizerMenuItem.addActionListener(action);
	}

	public void IBHistoricalDataAction(final ActionListener action) {
		IBDataMenuItem.addActionListener(action);
	}

	public void OTHistoricalDataAction(final ActionListener action) {
		OTDataMenuItem.addActionListener(action);
	}

	public void exitAction(final ActionListener action) {
		exitMenuItem.addActionListener(action);
	}

	public void exitAction(final WindowAdapter action) {
		addWindowListener(action);
	}

	public void aboutAction(final ActionListener action) {
		aboutMenuItem.addActionListener(action);
	}

	public void strategyChartAction(final ActionListener action) {
		viewChartButton.addActionListener(action);
	}

	public void doubleClickTableAction(final MouseAdapter action) {
		tradingTable.addMouseListener(action);
	}

	public void lookAndFeelAction(final ItemListener listener) {
		for (JRadioButtonMenuItem m : lookAndFeelMenuItems) {
			m.addItemListener(listener);
		}
	}

	public void macWindowTitleAction(final ItemListener listener) {
		macWindowTitleMenuItem.addItemListener(listener);
	}

	public void preferencesAction(final ActionListener action) {
		preferencesMenuItem.addActionListener(action);
	}

	private void populateStrategies() throws Exception {
		try {
			ClassFinder classFinder = new ClassFinder();
			List<Class<?>> strategies = classFinder.getStrategies();
			for (Class<?> strategyClass : strategies) {

				Constructor<?> constructor = strategyClass
						.getConstructor(StrategyParams.class);
				Strategy strategy = (Strategy) constructor
						.newInstance(new StrategyParams());
				tradingTableModel.addStrategy(strategy);
			}
			AppUtil.autoResizeTable(tradingTable, true);
		} catch (Exception e) {
			throw new Exception("Could not populate strategies: "
					+ e.getMessage());
		}
	}

	public TradingTableModel getTradingTableModel() {
		return tradingTableModel;
	}

	public JTable getTradingTable() {
		return tradingTable;
	}

	private void init() throws Exception {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		int menuKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		// file menu //////////////////////////////////////////////////////////
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setMnemonic('X');
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke('W', menuKeyMask));
		fileMenu.add(exitMenuItem);

		// tools menu /////////////////////////////////////////////////////////
		JMenu toolsMenu = new JMenu("Tools");
		toolsMenu.setMnemonic('T');
		JMenu historicalDataMenu = new JMenu("Historical Data");
		historicalDataMenu.setMnemonic('H');
		IBDataMenuItem = new JMenuItem("Interactive Brokers...");
		OTDataMenuItem = new JMenuItem("Open Tick...");
		IBDataMenuItem.setAccelerator(KeyStroke.getKeyStroke('I', menuKeyMask));
		OTDataMenuItem.setAccelerator(KeyStroke.getKeyStroke('T', menuKeyMask));
		historicalDataMenu.add(IBDataMenuItem);
		historicalDataMenu.add(OTDataMenuItem);
		optimizerMenuItem = new JMenuItem("Strategy Optimizer...");
		optimizerMenuItem.setMnemonic('S');
		optimizerMenuItem.setAccelerator(KeyStroke.getKeyStroke('O',
				menuKeyMask));
		toolsMenu.add(historicalDataMenu);
		// toolsMenu.addSeparator();
		toolsMenu.add(optimizerMenuItem);
		toolsMenu.addSeparator();
		resizeTableMenuItem = new JMenuItem("Resize Columns");
		resizeTableMenuItem.setMnemonic('R');
		resizeTableMenuItem.setAccelerator(KeyStroke.getKeyStroke('L',
				menuKeyMask));
		resizeTableMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				AppUtil.autoResizeTable(tradingTable, true);
			}
		});

		toolsMenu.add(resizeTableMenuItem);
		preferencesMenuItem = new JMenuItem("Preferences");
		preferencesMenuItem.setMnemonic('P');
		preferencesMenuItem.setAccelerator(KeyStroke.getKeyStroke('P',
				menuKeyMask));
		toolsMenu.add(preferencesMenuItem);

		this.databaseMenuItem = new JMenuItem("Database Manager");
		databaseMenuItem.setMnemonic('D');
		databaseMenuItem.setAccelerator(KeyStroke
				.getKeyStroke('D', menuKeyMask));
		databaseMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				try {
					DatabaseManagerSwing.main(new String[0]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		toolsMenu.add(databaseMenuItem);

		// view menu //////////////////////////////////////////////////////////
		JMenu viewMenu = new JMenu("View");
		viewMenu.setMnemonic('V');
		buttonGroupLookAndFeel = new ButtonGroup();
		for (UIManager.LookAndFeelInfo lnf : getInstalledLookAndFeels()) {
			JRadioButtonMenuItem lookAndFeeButtonMenu = new JRadioButtonMenuItem(
					lnf.getName());
			viewMenu.add(lookAndFeeButtonMenu);
			buttonGroupLookAndFeel.add(lookAndFeeButtonMenu);
			lookAndFeelMenuItems.add(lookAndFeeButtonMenu);
		}
		viewMenu.addSeparator();
		macWindowTitleMenuItem = new JCheckBoxMenuItem("Liquid mac decoration");
		viewMenu.add(macWindowTitleMenuItem);

		// help menu //////////////////////////////////////////////////////////
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		userGuideMenuItem = new JMenuItem("User Guide");
		userGuideMenuItem.setMnemonic('U');
		userGuideMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
				0, false));
		discussionMenuItem = new JMenuItem("Discussion Group");
		discussionMenuItem.setMnemonic('D');
		projectHomeMenuItem = new JMenuItem("Project Home");
		projectHomeMenuItem.setMnemonic('P');
		aboutMenuItem = new JMenuItem("About...");
		aboutMenuItem.setMnemonic('A');
		helpMenu.add(userGuideMenuItem);
		helpMenu.add(discussionMenuItem);
		helpMenu.add(projectHomeMenuItem);
		helpMenu.addSeparator();
		helpMenu.add(aboutMenuItem);

		// menu bar ///////////////////////////////////////////////////////////
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(toolsMenu);
		menuBar.add(viewMenu);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);

		// buttons panel //////////////////////////////////////////////////////
		JPanel buttonsPanel = new JPanel();
		viewChartButton = new JButton("Chart");
		viewChartButton.setMnemonic('C');
		runButton = new JButton("Run");
		runButton.setMnemonic('R');
		runButton.requestFocusInWindow();
		buttonsPanel.add(runButton);
		buttonsPanel.add(viewChartButton);

		JScrollPane tradingScroll = new JScrollPane();
		tradingScroll.setAutoscrolls(true);
		JPanel tradingPanel = new JPanel(new BorderLayout());
		tradingPanel.add(tradingScroll, BorderLayout.CENTER);

		tradingTableModel = new TradingTableModel();
		tradingTable = new JTable(tradingTableModel);
		tradingTable.setDefaultRenderer(Double.class, new DoubleRenderer());
		tradingTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);

		// Make some columns wider than the rest, so that the info fits in.
		TableColumnModel columnModel = tradingTable.getColumnModel();

		columnModel.getColumn(0).setPreferredWidth(20); // Activation column
		columnModel.getColumn(1).setPreferredWidth(150); // strategy name column
		columnModel.getColumn(2).setPreferredWidth(30); // ticker column
		columnModel.getColumn(3).setPreferredWidth(40); // bar size column
		columnModel.getColumn(4).setPreferredWidth(120); // last bar time column
		columnModel.getColumn(5).setPreferredWidth(60); // last bar close column
		columnModel.getColumn(6).setPreferredWidth(30); // Position
		columnModel.getColumn(7).setPreferredWidth(30); // Trades
		columnModel.getColumn(8).setPreferredWidth(30); // P&L
		columnModel.getColumn(9).setPreferredWidth(30); // Max DD
		columnModel.getColumn(10).setPreferredWidth(30); // PF
		columnModel.getColumn(11).setPreferredWidth(30); // Kelly
		columnModel.getColumn(12).setPreferredWidth(120); // Trade distribution

		tradingScroll.getViewport().add(tradingTable);
		// mainPanel.add(tradingPanel, BorderLayout.CENTER);

		Image appIcon = Toolkit.getDefaultToolkit().getImage(
				"F:\\research\\resources\\JSystemTrader.jpg");
		setIconImage(appIcon);

		getContentPane().add(tradingPanel, BorderLayout.CENTER);
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		getContentPane().setPreferredSize(new Dimension(800, 300));
		setTitle(AppUtil.getAppName());
		getRootPane().setDefaultButton(runButton);
		pack();
	}

	public String getSelectedLookAndFeel() {
		String selected = null;

		for (JRadioButtonMenuItem m : lookAndFeelMenuItems) {
			if (m.isSelected()) {
				selected = m.getText();
				break;
			}
		}

		assert (selected != null);

		for (UIManager.LookAndFeelInfo lnf : getInstalledLookAndFeels()) {
			if (selected.equals(lnf.getName()))
				return lnf.getClassName();
		}

		assert (false);
		return null;
	}

	public void setSelectedLookAndFeel(final String className) {
		for (UIManager.LookAndFeelInfo lnf : getInstalledLookAndFeels()) {
			if (className.equals(lnf.getClassName())) {
				for (JRadioButtonMenuItem m : lookAndFeelMenuItems) {
					if (m.getText().equals(lnf.getName())) {
						m.setSelected(true);
						break;
					}
				}
				break;
			}
		}
		assert (false);
	}

	public void setMacWindowTitle(final boolean state) {
		macWindowTitleMenuItem.setSelected(state);
	}

	/**
	 * Get all installed LookAndFeels
	 * 
	 * @return Vector with LookAndFeelInfo
	 */
	public Vector<UIManager.LookAndFeelInfo> getInstalledLookAndFeels() {
		Vector<UIManager.LookAndFeelInfo> installedLaF = new Vector<UIManager.LookAndFeelInfo>();
		boolean foundLiquid = false;

		for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager
				.getInstalledLookAndFeels()) {
			installedLaF.add(lookAndFeelInfo);

			if (lookAndFeelInfo.getClassName().equalsIgnoreCase(
					"com.birosoft.liquid.LiquidLookAndFeel"))
				foundLiquid = true;
		}

		// Add LiquidL&F if missing
		if (!foundLiquid) {
			UIManager.LookAndFeelInfo liquid = new UIManager.LookAndFeelInfo(
					"LiquidLookAndFeel",
					"com.birosoft.liquid.LiquidLookAndFeel");
			installedLaF.add(liquid);
		}

		return installedLaF;
	}
}
