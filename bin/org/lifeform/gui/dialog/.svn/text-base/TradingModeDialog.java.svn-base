package org.lifeform.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.lifeform.configuration.Defaults;
import org.lifeform.service.Dispatcher;
import org.lifeform.service.PreferencesHolder;
import org.lifeform.util.AppUtil;

public class TradingModeDialog extends JDialog {
	private static final long serialVersionUID = 5573967099063178651L;
	private static final Dimension MIN_SIZE = new Dimension(700, 120);
	private JTextField fileNameText;
	private JComboBox reportCombo;
	private JLabel fileNameLabel, reportLabel;
	private JRadioButton backtestModeRadio, tradingModeRadio;
	private int action;
	private JButton cancelButton, okButton, selectFileButton;
	private final PreferencesHolder preferences;

	public TradingModeDialog(final JFrame parent) throws Exception {
		super(parent);
		preferences = PreferencesHolder.getInstance();
		init();
		setModal(true);
		pack();
		assignListeners();
		setLocationRelativeTo(null);
		action = JOptionPane.CANCEL_OPTION;
	}

	private void assignListeners() {
		ActionListener radioButtonListener = new ActionListener() {
			public void actionPerformed(final ActionEvent evt) {
				JRadioButton button = (JRadioButton) evt.getSource();
				boolean isEnabled = (button == backtestModeRadio);
				fileNameLabel.setEnabled(isEnabled);
				fileNameText.setEnabled(isEnabled);
				reportLabel.setEnabled(isEnabled);
				reportCombo.setEnabled(isEnabled);
				selectFileButton.setEnabled(isEnabled);
			}
		};

		backtestModeRadio.addActionListener(radioButtonListener);
		tradingModeRadio.addActionListener(radioButtonListener);

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				action = JOptionPane.CANCEL_OPTION;
				cancel();
			}
		});

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				try {
					if (backtestModeRadio.isSelected()) {
						String fileName = getFileName();
						preferences.set(Defaults.BackTesterFileName, fileName);
						preferences.set(Defaults.BackTesterReportComboIndex,
								reportCombo.getSelectedIndex());
						if (fileName == null || fileName.length() == 0) {
							String msg = "File name must be specified.";
							AppUtil.showError(TradingModeDialog.this, msg);
							return;
						}
					}

					action = JOptionPane.OK_OPTION;
					cancel();
				} catch (Exception ex) {
					Dispatcher.getReporter().report(ex);
					AppUtil.showError(TradingModeDialog.this, ex.getMessage());
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				cancel();
			}
		});

	}

	private void init() {

		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Select Strategy Running Mode");

		getContentPane().setLayout(new BorderLayout());

		JPanel optionsPanel = new JPanel(new SpringLayout());
		JPanel backTestOptionsPanel = new JPanel(new SpringLayout());
		JPanel tradingOptionsPanel = new JPanel(new SpringLayout());

		backtestModeRadio = new JRadioButton("Backtest mode");
		backtestModeRadio.setSelected(true);
		tradingModeRadio = new JRadioButton(
				"Real time mode (with actual or paper trading account)");
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(backtestModeRadio);
		buttonGroup.add(tradingModeRadio);

		fileNameLabel = new JLabel("Historical data file:", JLabel.TRAILING);
		fileNameText = new JTextField();
		fileNameText.setText(preferences.get(Defaults.BackTesterFileName));

		selectFileButton = new JButton("...");
		Dimension buttonSize = new Dimension(22, 22);
		selectFileButton.setMaximumSize(buttonSize);
		selectFileButton.setPreferredSize(buttonSize);
		fileNameLabel.setLabelFor(fileNameText);

		reportLabel = new JLabel("Reports:", JLabel.TRAILING);
		reportCombo = new JComboBox(new String[] { "Disable", "Enable" });
		reportCombo.setMaximumSize(new Dimension(80, 20));
		reportCombo.setSelectedIndex(preferences
				.getInt(Defaults.BackTesterReportComboIndex));
		reportLabel.setLabelFor(reportCombo);

		backTestOptionsPanel.add(backtestModeRadio);
		backTestOptionsPanel.add(reportLabel);
		backTestOptionsPanel.add(reportCombo);
		backTestOptionsPanel.add(fileNameLabel);
		backTestOptionsPanel.add(fileNameText);
		backTestOptionsPanel.add(selectFileButton);

		AppUtil.makeOneLineGrid(backTestOptionsPanel, 6);

		tradingOptionsPanel.add(tradingModeRadio);
		AppUtil.makeOneLineGrid(tradingOptionsPanel, 1);

		optionsPanel.add(backTestOptionsPanel);
		optionsPanel.add(tradingOptionsPanel);
		// rows, cols, initX, initY, xPad, yPad
		AppUtil.makeCompactGrid(optionsPanel, 2, 1, 0, 12, 0, 0);

		JPanel controlPanel = new JPanel();

		cancelButton = new JButton("Cancel");
		cancelButton.setMnemonic('C');
		okButton = new JButton("OK");
		okButton.setMnemonic('K');
		controlPanel.add(okButton);
		controlPanel.add(cancelButton);

		getContentPane().add(optionsPanel, BorderLayout.NORTH);
		getContentPane().add(controlPanel, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(okButton);
		getContentPane().setPreferredSize(MIN_SIZE);
		getContentPane().setMinimumSize(getContentPane().getPreferredSize());
	}

	private void cancel() {
		dispose();
	}

	public void selectFileAction(final ActionListener actionListener) {
		selectFileButton.addActionListener(actionListener);
	}

	public boolean isReportEnabled() {
		return (reportCombo.getSelectedIndex() == 1);
	}

	public String getFileName() {
		return fileNameText.getText();
	}

	public Dispatcher.Mode getMode() {
		return backtestModeRadio.isSelected() ? Dispatcher.Mode.BACK_TEST
				: Dispatcher.Mode.TRADE;
	}

	public void setFileName(final String fileName) {
		fileNameText.setText(fileName);
	}

	public int getAction() {
		return action;
	}

}
