package org.lifeform.opentick;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.lifeform.configuration.Defaults;
import org.lifeform.market.BarSize;
import org.lifeform.report.Report;
import org.lifeform.service.Dispatcher;
import org.lifeform.service.PreferencesHolder;
import org.lifeform.util.AppUtil;

import com.opentick.OTExchange;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

/**
 * Dialog to specify options for OpenTick back data download.
 */
public class OTBackDataDialog extends JDialog {
	private static final long serialVersionUID = 3659058053675474535L;

	private static final Dimension MIN_SIZE = new Dimension(600, 320);

	private JButton logInButton, cancelButton, closeButton, downloadButton,
			selectFileButton;
	private JTextField fileNameText, userNameText;
	private JPasswordField passwordText;
	private JComboBox securityCombo, exchangeCombo, barSizeCombo;
	private JLabel progressLabel;
	private JProgressBar progressBar;
	private JTextFieldDateEditor startDateEditor, endDateEditor;
	private JPanel progressPanel;
	private final ArrayList<String> exchangeList;
	private final PreferencesHolder preferences;
	private OTBackDataDownloader downloader;
	private final Report eventReport;

	public OTBackDataDialog(final JFrame parent) throws Exception {
		super(parent);
		eventReport = Dispatcher.getReporter();
		exchangeList = new ArrayList<String>();
		preferences = PreferencesHolder.getInstance();
		init();
		pack();
		assignListeners();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void setExchanges(final List<?> exchanges) {
		for (Object exchangeObj : exchanges) {
			OTExchange exchange = (OTExchange) exchangeObj;
			exchangeList.add(exchange.getCode().toUpperCase() + ": "
					+ exchange.getTitle());
		}

		Collections.sort(exchangeList);
		exchangeCombo.setModel(new DefaultComboBoxModel(exchangeList
				.toArray(new String[exchangeList.size()])));
		String lastExchange = preferences.get(Defaults.OpenTickExchange);
		if (lastExchange.length() > 0) {
			exchangeCombo.setSelectedItem(lastExchange);
		} else {
			exchangeCombo.setSelectedIndex(0);
		}
	}

	public void setSecurities(final String[] securities) {
		setIndeterminateProgress("Populating securities for exchange "
				+ getFullExchange());
		Arrays.sort(securities);
		ComboBoxModel model = new DefaultComboBoxModel(securities);
		securityCombo.setModel(model);
		String lastSecurity = preferences.get(Defaults.OpenTickSecurity);
		if (lastSecurity.length() > 0) {
			securityCombo.setSelectedItem(lastSecurity);
		}
		closeProgress();
		exchangeCombo.setEnabled(true);
		securityCombo.setEnabled(true);
		downloadButton.setEnabled(true);
		downloadButton.requestFocus();
	}

	public String getExchange() {
		String exchange = (String) exchangeCombo.getSelectedItem();
		exchange = exchange.substring(0, exchange.indexOf(":"));
		return exchange;
	}

	public String getFullExchange() {
		return (String) exchangeCombo.getSelectedItem();
	}

	public String getSecurity() throws Exception {
		String security = (String) securityCombo.getSelectedItem();
		if (security == null)
			throw new Exception("Security is not specified.");
		security = security.substring(0, security.indexOf(":"));
		return security;
	}

	private void normalizeDate(final Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	public Calendar getStartDate() {
		Calendar startDate = Calendar.getInstance();
		startDate.setTimeInMillis(startDateEditor.getDate().getTime());
		normalizeDate(startDate);
		return startDate;
	}

	public Calendar getEndDate() {
		Calendar endDate = Calendar.getInstance();
		endDate.setTimeInMillis(endDateEditor.getDate().getTime());
		endDate.add(Calendar.DAY_OF_YEAR, 1);
		normalizeDate(endDate);
		return endDate;
	}

	public BarSize getBarSize() {
		return (BarSize) barSizeCombo.getSelectedItem();
	}

	public String getFileName() {
		return fileNameText.getText();
	}

	public String getUserName() {
		return userNameText.getText();
	}

	public String getPassword() {
		return new String(passwordText.getPassword());
	}

	public void closeProgress() {
		progressPanel.setVisible(false);
	}

	public void setProgress(final int percent, String progressText,
			final String label) {
		progressLabel.setText(label + ":");
		progressPanel.setVisible(true);
		progressText += " (" + percent + "%)";
		progressBar.setString(progressText);
		progressBar.setValue(percent);
	}

	public void setIndeterminateProgress(final String text) {
		progressBar.setIndeterminate(true);
		progressLabel.setText(text + ":");
		progressPanel.setVisible(true);
	}

	public void signalCompleted() {
		downloadButton.setEnabled(true);
		downloadButton.requestFocus();
		progressPanel.setVisible(false);
		cancelButton.setEnabled(false);
		closeProgress();
	}

	public void enableLogin() {
		logInButton.setEnabled(true);
	}

	private void assignListeners() {

		logInButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				try {
					preferences.set(Defaults.OpenTickUserName, userNameText
							.getText());
					preferences.set(Defaults.OpenTickPassword, new String(
							passwordText.getPassword()));
					setIndeterminateProgress("Logging to OpenTick server");
					logInButton.setEnabled(false);
					exchangeCombo.setEnabled(false);
					securityCombo.setEnabled(false);
					downloader = new OTBackDataDownloader(OTBackDataDialog.this);
				} catch (Exception ex) {
					closeProgress();
					logInButton.setEnabled(true);
					eventReport.report(ex);
					AppUtil.showError(OTBackDataDialog.this, ex.getMessage());
				}
			}
		});

		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				try {
					if (downloader != null) {
						String msg = "Logging out of OpenTick server";
						setIndeterminateProgress("Logging out of OpenTick server");
						eventReport.report(msg);
						downloader.logout();
					} else {
						close();
					}
				} catch (Exception ex) {
					eventReport.report(ex);
					AppUtil.showError(OTBackDataDialog.this, ex.getMessage());
				}
			}
		});

		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				try {
					validateOptions();

					downloadButton.setEnabled(false);
					cancelButton.setEnabled(true);
					cancelButton.requestFocus();
					progressBar.setIndeterminate(false);
					progressBar.setValue(0);
					progressPanel.setVisible(true);
					preferences.set(Defaults.OpenTickSecurity,
							(String) securityCombo.getSelectedItem());
					preferences.set(Defaults.OpenTickExchange,
							(String) exchangeCombo.getSelectedItem());
					preferences.set(Defaults.OpenTickBarsize, barSizeCombo
							.getSelectedIndex());
					preferences.set(Defaults.OpenTickDateStart, startDateEditor
							.getDate().getTime());
					preferences.set(Defaults.OpenTickDateEnd, endDateEditor
							.getDate().getTime());
					preferences.set(Defaults.OpenTickFileName, fileNameText
							.getText());
					downloader.download();
				} catch (Exception ex) {
					eventReport.report(ex);
					AppUtil.showError(OTBackDataDialog.this, ex.getMessage());
				}

			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				cancel();
			}
		});

		selectFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(AppUtil
						.getAppPath());
				fileChooser.setDialogTitle("Save Data As");

				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					fileNameText.setText(file.getAbsolutePath());
				}
			}
		});

		exchangeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				try {
					downloadButton.setEnabled(false);
					cancelButton.setEnabled(false); // there is no way to cancel
					// request for securities
					setIndeterminateProgress("Getting securities for exchange "
							+ getFullExchange());

					exchangeCombo.setEnabled(false);
					securityCombo.removeAllItems();
					securityCombo.setEnabled(false);
					downloader.getSecurities(getExchange());
				} catch (Exception ex) {
					eventReport.report(ex);
					AppUtil.showError(OTBackDataDialog.this, ex.getMessage());
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
		setTitle("Open Tick Historical Data Download");

		getContentPane().setLayout(new BorderLayout());

		JPanel authPanel = new JPanel(new SpringLayout());
		Border authPanelEtchedBorder = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder authPanelborder = BorderFactory
				.createTitledBorder(authPanelEtchedBorder);
		authPanelborder.setTitle("Open Tick Authentication");
		authPanel.setBorder(authPanelborder);

		JLabel userNameLabel = new JLabel("User Name:", SwingConstants.TRAILING);
		userNameText = new JTextField();
		userNameText.setText(preferences.get(Defaults.OpenTickUserName));
		userNameLabel.setLabelFor(userNameText);

		JLabel passwordLabel = new JLabel("Password:", SwingConstants.TRAILING);
		passwordText = new JPasswordField();
		passwordText.setText(preferences.get(Defaults.OpenTickPassword));
		passwordLabel.setLabelFor(passwordText);
		logInButton = new JButton("Login");
		logInButton.setMnemonic('L');

		authPanel.add(userNameLabel);
		authPanel.add(userNameText);
		authPanel.add(passwordLabel);
		authPanel.add(passwordText);
		authPanel.add(logInButton);

		// rows, cols, initX, initY, xPad, yPad
		AppUtil.makeCompactGrid(authPanel, 1, 5, 12, 6, 11, 5);

		JPanel optionsPanel = new JPanel(new SpringLayout());
		Border etchedBorder = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder border = BorderFactory.createTitledBorder(etchedBorder);
		border.setTitle("Historical Data Options");
		optionsPanel.setBorder(border);

		JLabel exchangeLabel = new JLabel("Exchange:", SwingConstants.TRAILING);
		exchangeCombo = new JComboBox();
		exchangeCombo.setEnabled(false);

		JLabel securityLabel = new JLabel("Security:", SwingConstants.TRAILING);
		securityCombo = new JComboBox();
		securityCombo.setEnabled(false);
		securityLabel.setLabelFor(securityCombo);
		exchangeLabel.setLabelFor(exchangeCombo);

		JLabel barSizeLabel = new JLabel("Bar Size:", SwingConstants.TRAILING);
		barSizeCombo = new JComboBox(BarSize.values());
		barSizeCombo.setSelectedItem(BarSize.Min5);
		barSizeLabel.setLabelFor(barSizeCombo);
		int lastBarSize = preferences.getInt(Defaults.OpenTickBarsize);
		if (lastBarSize >= 0) {
			barSizeCombo.setSelectedIndex(lastBarSize);
		}

		String dateFormat = "MMMMM d, yyyy";
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.YEAR, -3);
		Calendar endDate = Calendar.getInstance();
		startDateEditor = new JTextFieldDateEditor();
		endDateEditor = new JTextFieldDateEditor();
		startDateEditor.setEditable(false);
		endDateEditor.setEditable(false);
		long lastStartDate = preferences.getLong(Defaults.OpenTickDateStart);
		if (lastStartDate > 0) {
			startDate.setTime(new Date(lastStartDate));
		}
		long lastEndDate = preferences.getLong(Defaults.OpenTickDateEnd);
		if (lastEndDate > 0) {
			endDate.setTime(new Date(lastEndDate));
		}

		JLabel startDateLabel = new JLabel("Start Date:",
				SwingConstants.TRAILING);
		JPanel startDatePanel = new JDateChooser(startDate.getTime(),
				dateFormat, startDateEditor);
		startDateLabel.setLabelFor(startDatePanel);

		JLabel endDateLabel = new JLabel("End Date:", SwingConstants.TRAILING);
		JPanel endDatePanel = new JDateChooser(endDate.getTime(), dateFormat,
				endDateEditor);
		endDateLabel.setLabelFor(endDatePanel);

		JPanel saveAsPanel = new JPanel(new BorderLayout());
		JLabel saveAsLabel = new JLabel("Save as:", SwingConstants.TRAILING);
		saveAsLabel.setLabelFor(saveAsPanel);

		fileNameText = new JTextField();
		selectFileButton = new JButton("Browse...");
		selectFileButton.setPreferredSize(new Dimension(16, 16));
		String lastFileName = preferences.get(Defaults.OpenTickFileName);
		if (lastFileName.length() > 0) {
			fileNameText.setText(lastFileName);
		}

		saveAsPanel.add(fileNameText, BorderLayout.CENTER);
		saveAsPanel.add(selectFileButton, BorderLayout.EAST);

		optionsPanel.add(exchangeLabel);
		optionsPanel.add(exchangeCombo);
		optionsPanel.add(securityLabel);
		optionsPanel.add(securityCombo);
		optionsPanel.add(barSizeLabel);
		optionsPanel.add(barSizeCombo);
		optionsPanel.add(startDateLabel);
		optionsPanel.add(startDatePanel);
		optionsPanel.add(endDateLabel);
		optionsPanel.add(endDatePanel);
		optionsPanel.add(saveAsLabel);
		optionsPanel.add(saveAsPanel);

		// rows, cols, initX, initY, xPad, yPad
		AppUtil.makeCompactGrid(optionsPanel, 6, 2, 12, 6, 11, 5);

		JPanel controlPanel = new JPanel(new BorderLayout());
		cancelButton = new JButton("Cancel");
		cancelButton.setMnemonic('C');
		cancelButton.setEnabled(false);
		closeButton = new JButton("Close");
		closeButton.setMnemonic('o');
		downloadButton = new JButton("Download");
		downloadButton.setEnabled(false);
		downloadButton.setMnemonic('D');
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setPreferredSize(new Dimension(250, 18));
		progressBar.setEnabled(false);
		progressBar.setStringPainted(true);

		progressPanel = new JPanel(new SpringLayout());
		progressPanel.setVisible(false);
		progressLabel = new JLabel("", SwingConstants.TRAILING);
		progressLabel.setLabelFor(progressBar);
		progressPanel.add(progressLabel);
		progressPanel.add(progressBar);
		AppUtil.makeCompactGrid(progressPanel, 1, 2, 12, 6, 5, 0);

		JPanel buttonsPanel = new JPanel();

		controlPanel.add(progressPanel, BorderLayout.NORTH);
		controlPanel.add(buttonsPanel, BorderLayout.SOUTH);
		buttonsPanel.add(downloadButton);
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(closeButton);

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(authPanel, BorderLayout.NORTH);
		northPanel.add(optionsPanel, BorderLayout.SOUTH);

		getContentPane().add(northPanel, BorderLayout.NORTH);
		getContentPane().add(controlPanel, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(logInButton);
		getContentPane().setPreferredSize(MIN_SIZE);
		getContentPane().setMinimumSize(getContentPane().getPreferredSize());
	}

	private void validateOptions() throws Exception {
		String security = (String) securityCombo.getSelectedItem();
		if (security == null || security.length() == 0) {
			securityCombo.requestFocus();
			throw new Exception("Security must be specified.");
		}

		String fileName = fileNameText.getText();
		if (fileName.length() == 0) {
			fileNameText.requestFocus();
			throw new Exception("File name must be specified.");
		}

		if (getStartDate().after(getEndDate())) {
			startDateEditor.requestFocus();
			String msg = "End date must be after start date.";
			throw new Exception(msg);
		}

		Calendar latestDate = Calendar.getInstance();
		latestDate.add(Calendar.DAY_OF_YEAR, 1);
		normalizeDate(latestDate);

		if (getEndDate().after(latestDate)) {
			endDateEditor.requestFocus();
			String msg = "End date may not be after today.";
			throw new Exception(msg);
		}

	}

	private void cancel() {
		if (downloader != null) {
			downloader.cancel();
		}
	}

	public void close() {
		cancel();
		dispose();
	}
}
