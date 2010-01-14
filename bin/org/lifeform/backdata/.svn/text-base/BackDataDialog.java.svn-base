package org.lifeform.backdata;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.lifeform.configuration.Defaults;
import org.lifeform.core.AppException;
import org.lifeform.market.BarSize;
import org.lifeform.report.Report;
import org.lifeform.service.Dispatcher;
import org.lifeform.service.PreferencesHolder;
import org.lifeform.util.AppUtil;
import org.lifeform.util.ContractFactory;
import org.lifeform.util.MostLiquidContract;

import com.ib.client.Contract;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

/**
 * Dialog to specify options for back data download using IB historial server.
 * Up to one year of intraday historical data can be downloaded for stocks,
 * futures, and indices.
 */
public class BackDataDialog extends JDialog {
	private static final long serialVersionUID = 5570038594317112494L;
	private static final Dimension MIN_SIZE = new Dimension(450, 370);
	private static final String NOT_APPLICABLE = "Not Applicable";

	private JTextFieldDateEditor startDateEditor, endDateEditor;
	private JButton cancelButton, downloadButton, selectFileButton;
	private JTextField tickerText, fileNameText;
	private JComboBox securityTypeCombo, expirationMonthCombo,
			expirationYearCombo, exchangeCombo, currencyCombo, barSizeCombo,
			rthOnlyCombo;
	private JLabel expirationMonthLabel, expirationYearLabel, exchangeLabel;
	private JProgressBar progressBar;
	private BackDataDownloader downloader;
	private final Report eventReport;
	private final PreferencesHolder preferences;

	public BackDataDialog(final JFrame parent) throws Exception {
		super(parent);
		preferences = PreferencesHolder.getInstance();
		eventReport = Dispatcher.getReporter();
		init();
		pack();
		assignListeners();
		setLocationRelativeTo(null);
		restoreLastValues();
		setVisible(true);
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

	public void setProgress(final int value, final String text) {
		progressBar.setValue(value);
		String progressText = text + " " + value + "%";
		progressBar.setString(progressText);
	}

	public void signalCompleted() {
		progressBar.setVisible(false);
		downloadButton.setEnabled(true);
		downloadButton.requestFocus();
		getRootPane().setDefaultButton(downloadButton);
	}

	private void assignListeners() {

		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				try {
					preferences.set(Defaults.IBBackDataStartDate,
							startDateEditor.getText());
					preferences.set(Defaults.IBBackDataEndDate, endDateEditor
							.getText());
					preferences.set(Defaults.IBBackDataTicker, tickerText
							.getText());
					preferences.set(Defaults.IBBackDataFileName, fileNameText
							.getText());
					preferences.set(Defaults.IBBackDataSecType,
							securityTypeCombo.getSelectedIndex());
					preferences.set(Defaults.IBBackDataExpirationMonth,
							expirationMonthCombo.getSelectedIndex());
					preferences.set(Defaults.IBBackDataExpirationYear,
							expirationYearCombo.getSelectedIndex());
					preferences.set(Defaults.IBBackDataExchange, exchangeCombo
							.getSelectedIndex());
					preferences.set(Defaults.IBBackDataCurrency, currencyCombo
							.getSelectedIndex());
					preferences.set(Defaults.IBBackDataBarSize, barSizeCombo
							.getSelectedIndex());
					preferences.set(Defaults.IBBackDataRTHOnly, rthOnlyCombo
							.getSelectedIndex());

					validateOptions();

					String ticker = tickerText.getText();
					String expiration = null;

					if (expirationYearCombo.isEnabled()
							&& expirationMonthCombo.isEnabled()) {
						int expirationMonth = expirationMonthCombo
								.getSelectedIndex();
						String expirationMonthStr = String
								.valueOf(expirationMonth);
						if (expirationMonth < 10) {
							expirationMonthStr = "0" + expirationMonthStr;
						}

						expiration = expirationYearCombo.getSelectedItem()
								+ expirationMonthStr;
					}

					String fileName = fileNameText.getText();
					String securityType = (String) securityTypeCombo
							.getSelectedItem();
					String exchange = (String) exchangeCombo.getSelectedItem();
					String currency = (String) currencyCombo.getSelectedItem();
					BarSize barSize = (BarSize) barSizeCombo.getSelectedItem();

					String rthText = (String) rthOnlyCombo.getSelectedItem();

					boolean rthOnly = rthText.equalsIgnoreCase("yes");
					Contract contract = ContractFactory.makeContract(ticker,
							securityType, exchange, expiration, currency);

					downloader = new BackDataDownloader(BackDataDialog.this,
							contract, barSize, rthOnly, fileName);

					downloadButton.setEnabled(false);
					progressBar.setValue(0);
					progressBar.setVisible(true);
					getRootPane().setDefaultButton(cancelButton);
					downloader.start();

				} catch (Exception ex) {
					eventReport.report(ex);
					AppUtil.showError(BackDataDialog.this, ex.getMessage());
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
				fileChooser.setDialogTitle("Save Back Data As");

				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					fileNameText.setText(file.getAbsolutePath());
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				cancel();
			}
		});

		securityTypeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				String securityType = (String) securityTypeCombo
						.getSelectedItem();
				boolean isFuture = securityType.equalsIgnoreCase("FUT");

				expirationMonthCombo.setEnabled(isFuture);
				expirationYearCombo.setEnabled(isFuture);
				expirationMonthLabel.setEnabled(isFuture);
				expirationYearLabel.setEnabled(isFuture);

				if (isFuture) {
					String yearAndMonth = MostLiquidContract.getMostLiquid();
					int mostLiquidMonth = Integer.valueOf(yearAndMonth
							.substring(4, 6));
					expirationMonthCombo.setSelectedIndex(mostLiquidMonth);
					expirationYearCombo.setSelectedItem(yearAndMonth.substring(
							0, 4));
				} else {
					expirationMonthCombo.setSelectedIndex(0);
					expirationYearCombo.setSelectedIndex(0);
				}

				if (securityType.equalsIgnoreCase("CASH")) {
					exchangeCombo.setSelectedItem("IDEALPRO");
					exchangeCombo.setEnabled(false);
					exchangeLabel.setEnabled(false);
				} else {
					exchangeCombo.setSelectedItem("SMART");
					exchangeCombo.setEnabled(true);
					exchangeLabel.setEnabled(true);
				}

			}
		});
	}

	private void init() throws Exception {

		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Interactive Brokers Historical Data Download");

		getContentPane().setLayout(new BorderLayout());

		JPanel optionsPanel = new JPanel(new SpringLayout());

		Border etchedBorder = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder border = BorderFactory.createTitledBorder(etchedBorder);
		border.setTitle("Historical Data Options");
		optionsPanel.setBorder(border);

		JLabel tickerLabel = new JLabel("Ticker:", JLabel.TRAILING);
		tickerText = new JTextField("MSFT");
		tickerLabel.setLabelFor(tickerText);

		JLabel securityTypeLabel = new JLabel("Security Type:", JLabel.TRAILING);
		securityTypeCombo = new JComboBox(new String[] { "STK", "FUT", "CASH",
				"IND" });
		securityTypeLabel.setLabelFor(securityTypeCombo);

		exchangeLabel = new JLabel("Exchange:", JLabel.TRAILING);
		exchangeCombo = new JComboBox(PreferencesHolder.getInstance()
				.getStringArray(Defaults.Exchanges));
		exchangeLabel.setLabelFor(exchangeCombo);

		expirationYearLabel = new JLabel("Expiry Year:", JLabel.TRAILING);
		expirationYearLabel.setEnabled(false);

		// data for futures can be requested for expired contracts, as well as
		// for
		// the contracts which expire next year
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String[] years = { NOT_APPLICABLE, String.valueOf(year - 1),
				String.valueOf(year), String.valueOf(year + 1) };

		expirationYearCombo = new JComboBox(years);
		expirationYearCombo.setEnabled(false);
		expirationYearLabel.setLabelFor(expirationYearCombo);

		expirationMonthLabel = new JLabel("Expiry Month:", JLabel.TRAILING);
		expirationMonthLabel.setEnabled(false);
		expirationMonthCombo = new JComboBox(new String[] { NOT_APPLICABLE,
				"January", "February", "March", "April", "May", "June", "July",
				"August", "September", "October", "November", "December" });
		expirationMonthCombo.setEnabled(false);
		expirationMonthLabel.setLabelFor(expirationMonthCombo);

		JLabel currencyLabel = new JLabel("Currency:", JLabel.TRAILING);
		currencyCombo = new JComboBox(PreferencesHolder.getInstance()
				.getStringArray(Defaults.Currencies));
		currencyLabel.setLabelFor(currencyCombo);

		JLabel barSizeLabel = new JLabel("Bar Size:", JLabel.TRAILING);
		barSizeCombo = new JComboBox(BarSize.values());
		barSizeCombo.setSelectedItem(BarSize.Min5);
		barSizeLabel.setLabelFor(barSizeCombo);

		JLabel rthOnlyLabel = new JLabel("RTH Only:", JLabel.TRAILING);
		rthOnlyCombo = new JComboBox(new String[] { "Yes", "No" });
		rthOnlyLabel.setLabelFor(rthOnlyCombo);

		String dateFormat = "MMMMM d, yyyy";
		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.YEAR, -1);
		startDateEditor = new JTextFieldDateEditor();
		endDateEditor = new JTextFieldDateEditor();
		startDateEditor.setEditable(false);
		endDateEditor.setEditable(false);

		JLabel startDateLabel = new JLabel("Start Date:",
				SwingConstants.TRAILING);
		JPanel startDatePanel = new JDateChooser(startDate.getTime(),
				dateFormat, startDateEditor);
		startDateLabel.setLabelFor(startDatePanel);

		JLabel endDateLabel = new JLabel("End Date:", SwingConstants.TRAILING);
		JPanel endDatePanel = new JDateChooser(new Date(), dateFormat,
				endDateEditor);
		endDateLabel.setLabelFor(endDatePanel);

		JPanel saveAsPanel = new JPanel(new BorderLayout());
		JLabel saveAsLabel = new JLabel("Save as:", SwingConstants.TRAILING);
		saveAsLabel.setLabelFor(saveAsPanel);

		fileNameText = new JTextField();
		selectFileButton = new JButton("Browse...");
		selectFileButton.setPreferredSize(new Dimension(16, 16));

		saveAsPanel.add(fileNameText, BorderLayout.CENTER);
		saveAsPanel.add(selectFileButton, BorderLayout.EAST);

		optionsPanel.add(tickerLabel);
		optionsPanel.add(tickerText);
		optionsPanel.add(securityTypeLabel);
		optionsPanel.add(securityTypeCombo);
		optionsPanel.add(exchangeLabel);
		optionsPanel.add(exchangeCombo);
		optionsPanel.add(currencyLabel);
		optionsPanel.add(currencyCombo);
		optionsPanel.add(expirationYearLabel);
		optionsPanel.add(expirationYearCombo);
		optionsPanel.add(expirationMonthLabel);
		optionsPanel.add(expirationMonthCombo);
		optionsPanel.add(barSizeLabel);
		optionsPanel.add(barSizeCombo);
		optionsPanel.add(rthOnlyLabel);
		optionsPanel.add(rthOnlyCombo);

		optionsPanel.add(startDateLabel);
		optionsPanel.add(startDatePanel);
		optionsPanel.add(endDateLabel);
		optionsPanel.add(endDatePanel);
		optionsPanel.add(saveAsLabel);
		optionsPanel.add(saveAsPanel);

		// rows, cols, initX, initY, xPad, yPad
		AppUtil.makeCompactGrid(optionsPanel, 11, 2, 12, 6, 11, 5);

		JPanel controlPanel = new JPanel();

		cancelButton = new JButton("Cancel");
		cancelButton.setMnemonic('C');
		downloadButton = new JButton("Download");
		downloadButton.setMnemonic('D');
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		progressBar.setPreferredSize(new Dimension(250, 18));
		progressBar.setVisible(false);
		progressBar.setEnabled(false);
		progressBar.setStringPainted(true);

		controlPanel.add(downloadButton);
		controlPanel.add(cancelButton);
		controlPanel.add(progressBar);

		getContentPane().add(optionsPanel, BorderLayout.NORTH);
		getContentPane().add(controlPanel, BorderLayout.SOUTH);

		getRootPane().setDefaultButton(downloadButton);
		getContentPane().setPreferredSize(MIN_SIZE);
		getContentPane().setMinimumSize(getContentPane().getPreferredSize());

	}

	private void restoreLastValues() {
		String lastStartDate = preferences.get(Defaults.IBBackDataStartDate);
		if (lastStartDate.length() > 0)
			startDateEditor.setText(lastStartDate);
		String lastEndDate = preferences.get(Defaults.IBBackDataEndDate);
		if (lastEndDate.length() > 0)
			endDateEditor.setText(lastEndDate);
		tickerText.setText(preferences.get(Defaults.IBBackDataTicker));
		fileNameText.setText(preferences.get(Defaults.IBBackDataFileName));
		securityTypeCombo.setSelectedIndex(preferences
				.getInt(Defaults.IBBackDataSecType));
		expirationMonthCombo.setSelectedIndex(preferences
				.getInt(Defaults.IBBackDataExpirationMonth));
		expirationYearCombo.setSelectedIndex(preferences
				.getInt(Defaults.IBBackDataExpirationYear));
		exchangeCombo.setSelectedIndex(preferences
				.getInt(Defaults.IBBackDataExchange));
		currencyCombo.setSelectedIndex(preferences
				.getInt(Defaults.IBBackDataCurrency));
		barSizeCombo.setSelectedIndex(preferences
				.getInt(Defaults.IBBackDataBarSize));
		rthOnlyCombo.setSelectedIndex(preferences
				.getInt(Defaults.IBBackDataRTHOnly));
	}

	private void validateOptions() throws Exception {
		String ticker = tickerText.getText();
		if (ticker.length() == 0) {
			tickerText.requestFocus();
			throw new AppException("Ticker must be specified.");
		}

		String expirationYear = (String) expirationYearCombo.getSelectedItem();
		if (expirationYearCombo.isEnabled()
				&& expirationYear.equals(NOT_APPLICABLE)) {
			expirationYearCombo.requestFocus();
			throw new AppException(
					"Expiration year must be specified for security type \"FUT\".");
		}

		String expirationMonth = (String) expirationMonthCombo
				.getSelectedItem();
		if (expirationMonthCombo.isEnabled()
				&& expirationMonth.equals(NOT_APPLICABLE)) {
			expirationMonthCombo.requestFocus();
			throw new AppException(
					"Expiration month must be specified for security type \"FUT\".");
		}

		String fileName = fileNameText.getText();
		if (fileName.length() == 0) {
			fileNameText.requestFocus();
			throw new AppException("File name must be specified.");
		}

		Calendar earliestDate = Calendar.getInstance();
		earliestDate.add(Calendar.YEAR, -1);
		normalizeDate(earliestDate);

		Calendar latestDate = Calendar.getInstance();
		latestDate.add(Calendar.DAY_OF_YEAR, 1);
		normalizeDate(latestDate);

		if (getStartDate().before(earliestDate)) {
			startDateEditor.requestFocus();
			String msg = "Start date may not be more than 1 year from today. This is limit for IB historical servers.";
			msg += Defaults.getLineSeperator()
					+ "If you need a longer download period, you may use OpenTick option.";
			throw new AppException(msg);
		}

		if (getStartDate().after(getEndDate())) {
			startDateEditor.requestFocus();
			String msg = "End date must be after start date.";
			throw new AppException(msg);
		}

		if (getEndDate().after(latestDate)) {
			endDateEditor.requestFocus();
			String msg = "End date may not be after today.";
			throw new AppException(msg);
		}

		try {
			FileOutputStream file = new FileOutputStream(fileNameText.getText());
			file.close();
		} catch (Exception e) {
			throw new AppException("cannot write to " + fileNameText.getText());
		}
	}

	private void cancel() {
		if (downloader != null) {
			downloader.cancel();
		}
		Dispatcher.getTrader().getAssistant().disconnect();
		dispose();
	}

}
