/**
 * Original author Eugene Kononov <nonlinear5@yahoo.com> 
 * Adapted for JST by Florent Guiliani <florent@guiliani.fr>
 */
package org.lifeform.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import org.lifeform.configuration.Defaults;
import org.lifeform.service.PreferencesHolder;
import org.lifeform.util.AppUtil;
import org.lifeform.util.ClassFinder;

public class PreferencesDialog extends JDialog {
	private static final long serialVersionUID = -6334525854293208625L;
	private static final Dimension FIELD_DIMENSION = new Dimension(
			Integer.MAX_VALUE, 22);

	private JCheckBox emailSMTPSAuthCheck, emailSMTPSQuitWaitCheck,
			advisorAccountCheck, backtestShowNumberOfBarsCheck,
			portfolioSyncCheck;
	private JTextField exchangesText, currenciesText, hostText, portText,
			advisorAccountNumberText, emailTransportProtocolText,
			emailHostText, emailUserText, emailRecipientText, emailSubjectText;
	private JSpinner optimizerSpin, timeLagSpin, clientIdSpin;
	private JComboBox reportRendererCombo, reportRecyclingCombo;
	private JPasswordField emailPasswordField;

	private final PreferencesHolder prefs;

	public PreferencesDialog(final JFrame parent) throws Exception {
		super(parent);
		prefs = PreferencesHolder.getInstance();
		init();
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}

	private void add(final JPanel panel, final Defaults pref, final JTextField textField) {
		textField.setText(prefs.get(pref));
		commonAdd(panel, pref, textField);
	}

	private void add(final JPanel panel, final Defaults pref, final JComboBox comboBox) {
		comboBox.setSelectedItem(prefs.get(pref));
		commonAdd(panel, pref, comboBox);
	}

	private void add(final JPanel panel, final Defaults pref, final JSpinner spinner) {
		spinner.setValue(prefs.getInt(pref));
		commonAdd(panel, pref, spinner);
	}

	private void add(final JPanel panel, final Defaults pref, final JCheckBox checkBox) {
		checkBox.setSelected(prefs.getBool(pref));
		commonAdd(panel, pref, checkBox);
	}

	private void commonAdd(final JPanel panel, final Defaults pref, final Component component) {
		JLabel fieldNameLabel = new JLabel(pref.getName() + ":",
				JLabel.TRAILING);
		fieldNameLabel.setLabelFor(component);
		component.setPreferredSize(FIELD_DIMENSION);
		component.setMaximumSize(FIELD_DIMENSION);
		panel.add(fieldNameLabel);
		panel.add(component);
	}

	private void init() throws Exception {

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Preferences");

		JPanel contentPanel = new JPanel(new BorderLayout());

		JPanel buttonsPanel = new JPanel();
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		okButton.setMnemonic('O');
		cancelButton.setMnemonic('C');
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);

		JPanel noticePanel = new JPanel();
		JLabel noticeLabel = new JLabel(
				"Some of the preferences will not take effect until "
						+ AppUtil.getAppName() + " is restarted.");
		noticeLabel.setForeground(Color.red);
		noticePanel.add(noticeLabel);

		getContentPane().add(noticePanel, BorderLayout.NORTH);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

		JTabbedPane tabbedPane1 = new JTabbedPane();
		contentPanel.add(tabbedPane1, BorderLayout.CENTER);

		// TWS Connection
		JPanel connectionTab = new JPanel(new SpringLayout());
		tabbedPane1.addTab("TWS Connection", connectionTab);
		hostText = new JTextField();
		portText = new JTextField();
		clientIdSpin = new JSpinner();
		advisorAccountCheck = new JCheckBox();
		advisorAccountNumberText = new JTextField();
		portfolioSyncCheck = new JCheckBox();
		timeLagSpin = new JSpinner();
		add(connectionTab, Defaults.Host, hostText);
		add(connectionTab, Defaults.Port, portText);
		add(connectionTab, Defaults.ClientID, clientIdSpin);
		add(connectionTab, Defaults.AdvisorAccount, advisorAccountCheck);
		add(connectionTab, Defaults.AdvisorAccountNumber,
				advisorAccountNumberText);
		add(connectionTab, Defaults.PortfolioSync, portfolioSyncCheck);
		add(connectionTab, Defaults.TimeLagAllowed, timeLagSpin);
		AppUtil.makeCompactGrid(connectionTab, 7, 2, 12, 12, 8, 5);
		setWidth(connectionTab, clientIdSpin, 45);
		setWidth(connectionTab, timeLagSpin, 45);

		// Reporting
		JPanel reportingTab = new JPanel(new SpringLayout());
		tabbedPane1.addTab("Reporting", reportingTab);
		reportRendererCombo = new JComboBox();
		try {
			ClassFinder classFinder = new ClassFinder();
			List<Class<?>> reportrenderers = classFinder.getReportFormats();
			for (Class<?> reportrendererClass : reportrenderers) {
				reportRendererCombo
						.addItem(reportrendererClass.getSimpleName());
			}
		} catch (Exception e) {
			throw new Exception("Could not populate reportrenderers: "
					+ e.getMessage());
		}
		reportRecyclingCombo = new JComboBox(new String[] { "Append",
				"Override" });
		add(reportingTab, Defaults.ReportRenderer, reportRendererCombo);
		add(reportingTab, Defaults.ReportRecycling, reportRecyclingCombo);
		AppUtil.makeCompactGrid(reportingTab, 2, 2, 12, 12, 8, 5);
		setWidth(reportingTab, reportRecyclingCombo, 85);

		// BackTest & Optimizer
		JPanel backtestTab = new JPanel(new SpringLayout());
		tabbedPane1.addTab("Backtests", backtestTab);
		backtestShowNumberOfBarsCheck = new JCheckBox();
		optimizerSpin = new JSpinner();
		add(backtestTab, Defaults.BacktestShowNumberOfBar,
				backtestShowNumberOfBarsCheck);
		add(backtestTab, Defaults.OptimizerMaxThread, optimizerSpin);
		AppUtil.makeCompactGrid(backtestTab, 2, 2, 12, 12, 8, 5);
		setWidth(backtestTab, optimizerSpin, 45);

		// IB Back data downloader properties
		JPanel ibBackdataTab = new JPanel(new SpringLayout());
		tabbedPane1.addTab("IB Backdata", ibBackdataTab);
		exchangesText = new JTextField();
		currenciesText = new JTextField();
		add(ibBackdataTab, Defaults.Exchanges, exchangesText);
		add(ibBackdataTab, Defaults.Currencies, currenciesText);
		AppUtil.makeCompactGrid(ibBackdataTab, 2, 2, 12, 12, 8, 5);

		// Emails
		JPanel emailTab = new JPanel(new SpringLayout());
		tabbedPane1.addTab("Emails", emailTab);
		emailTransportProtocolText = new JTextField();
		emailSMTPSAuthCheck = new JCheckBox();
		emailSMTPSQuitWaitCheck = new JCheckBox();
		emailHostText = new JTextField();
		emailUserText = new JTextField();
		emailPasswordField = new JPasswordField();
		emailSubjectText = new JTextField();
		emailRecipientText = new JTextField();
		add(emailTab, Defaults.MailTransportProtocol,
				emailTransportProtocolText);
		add(emailTab, Defaults.MailSMTPSAuth, emailSMTPSAuthCheck);
		add(emailTab, Defaults.MailSMTPSQuitWair, emailSMTPSQuitWaitCheck);
		add(emailTab, Defaults.MailHost, emailHostText);
		add(emailTab, Defaults.MailUser, emailUserText);
		add(emailTab, Defaults.MailPassword, emailPasswordField);
		add(emailTab, Defaults.MailSubject, emailSubjectText);
		add(emailTab, Defaults.MailRecipient, emailRecipientText);
		AppUtil.makeCompactGrid(emailTab, 8, 2, 12, 12, 8, 5);

		// Set values...
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				try {
					// TWS Connection
					prefs.set(Defaults.Host, hostText.getText());
					prefs.set(Defaults.Port, portText.getText());
					prefs.set(Defaults.ClientID, String.valueOf(clientIdSpin
							.getValue()));
					prefs.set(Defaults.AdvisorAccount, advisorAccountCheck
							.isSelected());
					prefs.set(Defaults.AdvisorAccountNumber,
							advisorAccountNumberText.getText());
					prefs.set(Defaults.PortfolioSync, portfolioSyncCheck
							.isSelected());
					prefs.set(Defaults.TimeLagAllowed, String
							.valueOf(timeLagSpin.getValue()));

					// Reporting
					prefs.set(Defaults.ReportRenderer,
							(String) reportRendererCombo.getSelectedItem());
					prefs.set(Defaults.ReportRecycling,
							(String) reportRecyclingCombo.getSelectedItem());

					// Backtest & Optimizer
					prefs.set(Defaults.BacktestShowNumberOfBar,
							backtestShowNumberOfBarsCheck.isSelected());
					prefs.set(Defaults.OptimizerMaxThread, String
							.valueOf(optimizerSpin.getValue()));

					// IB back data
					prefs.set(Defaults.Exchanges, exchangesText.getText());
					prefs.set(Defaults.Currencies, currenciesText.getText());

					// Emails
					prefs.set(Defaults.MailTransportProtocol,
							emailTransportProtocolText.getText());
					prefs.set(Defaults.MailSMTPSAuth, emailSMTPSAuthCheck
							.isSelected());
					prefs.set(Defaults.MailSMTPSQuitWair,
							emailSMTPSQuitWaitCheck.isSelected());
					prefs.set(Defaults.MailHost, emailHostText.getText());
					prefs.set(Defaults.MailUser, emailUserText.getText());
					prefs.set(Defaults.MailPassword, new String(
							emailPasswordField.getPassword()));
					prefs.set(Defaults.MailSubject, emailSubjectText.getText());
					prefs.set(Defaults.MailRecipient, emailRecipientText
							.getText());

					dispose();
				} catch (Exception ex) {
					AppUtil.showError(PreferencesDialog.this, ex.getMessage());
				}
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				dispose();
			}
		});

		setPreferredSize(new Dimension(500, 350));

	}

	private void setWidth(final JPanel p, final Component c, final int width) throws Exception {
		SpringLayout layout;
		try {
			layout = (SpringLayout) p.getLayout();
			SpringLayout.Constraints spinLayoutConstraint = layout
					.getConstraints(c);
			spinLayoutConstraint.setWidth(Spring.constant(width));
		} catch (ClassCastException exc) {
			throw new Exception(
					"The first argument to makeGrid must use SpringLayout.");
		}
	}

	public static void loadProperties() {
		// read pkb.config
		// loadProperties is called when application startup
		resetProperties();
		try {
			File file = new File(System.getProperty("user.dir")
					+ File.separator + "pkb.config");
			if (file.exists()) {
				Properties propFromFile = new Properties();
				propFromFile.load(new FileInputStream(file));
				// overwrite pkbProps
				Iterator<?> itr = propFromFile.keySet().iterator();
				while (itr.hasNext()) {
					String key = (String) itr.next();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void saveProperties() {
		try {
			File file = new File(System.getProperty("user.dir")
					+ File.separator + "pkb.config");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void resetProperties() {
	}

}
