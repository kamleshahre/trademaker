package org.lifeform.configuration;

public enum Defaults {
	Host("Host", "localhost"), Port("Port", "7496"), ClientID("Client ID", "0"), AdvisorAccount(
			"Enable advisor account", "true"), AdvisorAccountNumber(
			"Advisor account number", ""), PortfolioSync(
			"Enable portfolio sync (experimental)", "true"), TimeLagAllowed(
			"Time lag allowed", "2"),

	// Reporting
	ReportRenderer("Report renderer", "org.lifeform.report.format.CSV"), ReportRecycling(
			"Report recycling", "Append"),

	// Backtest & Optimizer
	BacktestShowNumberOfBar("Show number of loaded bars", "true"), BackTesterFileName(
			"backTester.dataFileName", ""), BackTesterReportComboIndex(
			"backTester.report.comboindex", "0"), OptimizerMaxThread(
			"Number of threads used by the optimizer", "2"), OptimizerFileName(
			"optimizer.dataFileName", ""), OptimizerMinTrades(
			"optimizer.minTrades", "20"), OptimizerSelectBy(
			"optimizer.selectBy", ""), OptimizerStrategyName(
			"optimizer.strategy.name", ""), OptimizerWidth("optimizer.width",
			"-1"), OptimizerHeight("optimizer.height", "-1"), OptimizerX(
			"optimizer.x", "-1"), OptimizerY("optimizer.y", "-1"),

	// IB Back data downloader properties
	Exchanges(
			"Exchanges",
			"SMART,GLOBEX,ECBOT,CBOE,NYSE,NASDAQ,AMEX,NYMEX,LIFFE,IDEALPRO,DTB,SGX,KSE,HKFE"), Currencies(
			"Currencies", "USD,EUR,GBP,CHF,JPY,AUD,KRW,HKD"), IBBackDataStartDate(
			"IBBackDataStartDate", ""), IBBackDataEndDate("IBBackDataEndDate",
			""), IBBackDataTicker("IBBackDataTicker", "GOOG"), IBBackDataFileName(
			"IBBackDataFileName", ""), IBBackDataSecType("IBBackDataSecType",
			"0"), IBBackDataExpirationMonth("IBBackDataExpirationMonth", "0"), IBBackDataExpirationYear(
			"IBBackDataExpirationYear", "0"), IBBackDataExchange(
			"IBBackDataExchange", "0"), IBBackDataCurrency(
			"IBBackDataCurrency", "0"), IBBackDataBarSize("IBBackDataBarSize",
			"0"), IBBackDataRTHOnly("IBBackDataRTHOnly", "0"),

	// EMail properties
	MailTransportProtocol("Mail Transport Protocol", "smtps"), MailSMTPSAuth(
			"Enable SMTPS Auth", "true"), MailSMTPSQuitWair(
			"Enable SMTPS Quitwait", "false"), MailHost(
			"Mail server name or ip", "smtp.gmail.com"), MailUser(
			"Mail account login", "ernanhughes@gmail.com"), MailPassword(
			"Mail account password", "EmailPassword"), MailSubject(
			"Mail subject", "Trade Maker Notification"), MailRecipient(
			"Mail Recipient", "EMailRecipient"),

	// Main window
	MainWindowWidth("mainwindow.width", "-1"), MainWindowHeight(
			"mainwindow.height", "-1"), MainWindowX("mainwindow.x", "-1"), MainWindowY(
			"mainwindow.y", "-1"),

	// Chart
	ChartWidth("chart.width", "-1"), ChartHeight("chart.height", "-1"), ChartX(
			"chart.x", "-1"), ChartY("chart.y", "-1"), ChartState(
			"chart.state", "-1"),

	// OpenTick
	OpenTickUserName("opentick.username", ""), OpenTickPassword(
			"opentick.password", ""), OpenTickSecurity("opentick.security", ""), OpenTickExchange(
			"opentick.exchange", ""), OpenTickBarsize("opentick.barsize", "0"), OpenTickDateStart(
			"opentick.datestart", "0"), OpenTickDateEnd("opentick.dateend", "0"), OpenTickFileName(
			"opentick.filename", ""), EPSILON("EPSILON", "1e-12"),

	// Look and feel
	LookAndFeelClassName("lookAndFeel.className",
			"com.birosoft.liquid.LiquidLookAndFeel"), LookAndFeelMacStyle(
			"lookAndFeel.alaMacWindowTitle", "false"), APP_NAME(
			"application.Name", "Trade Maker"),

	// Web Access
	WebAccess("Web Access", "disabled"), WebAccessPort("Web Access Port",
			"1234"), WebAccessUser("Web Access User", "admin"), WebAccessPassword(
			"Web Access Password", "admin"), WebAccessTableLayout(
			"Table Layout", "simple"),

	// Back tester
	BackTesterUseDateRange("backTester.useDateRange", "false"), BackTesterTestingPeriodStart(
			"backTester.testingPeriodStart", ""), BackTesterTestingPeriodEnd(
			"backTester.testingPeriodEnd", ""),

	// Optimizer
	OptimizerMethod("optimizer.method", ""), OptimizerWindowWidth(
			"optimizerwindow.width", "-1"), OptimizerWindowHeight(
			"optimizerwindow.height", "-1"), OptimizerWindowX(
			"optimizerwindow.x", "-1"), OptimizerWindowY("optimizerwindow.y",
			"-1"), OptimizerUseDateRange("optimizer.useDateRange", "false"), OptimizerTestingPeriodStart(
			"optimizer.testingPeriodStart", ""), OptimizerTestingPeriodEnd(
			"optimizer.testingPeriodEnd", ""),

	// Performance chart
	PerformanceChartWidth("performance.chart.width", "-1"), PerformanceChartHeight(
			"performance.chart.height", "-1"), PerformanceChartX(
			"performance.chart.x", "-1"), PerformanceChartY(
			"performance.chart.y", "-1"), PerformanceChartState(
			"performance.chart.state", "-1"),

	// Optimizer
	DivideAndConquerCoverage("Divide & Conquer coverage", "10"), StrategiesPerProcessor(
			"Strategies per processor", "50"),

	// Optimization Map
	OptimizationMapWidth("optimization.map.width", "-1"), OptimizationMapHeight(
			"optimization.map.height", "-1"), OptimizationMapX(
			"optimization.map.x", "-1"), OptimizationMapY("optimization.map.y",
			"-1"),

	// Collective2
	Collective2Password("C2 Password", ""), Collective2Strategies(
			"C2 Strategies", ""),

	// Time Server
	NTPTimeServer("NTP time server", "ntp2.usno.navy.mil"),

	// Look & Feel
	LookAndFeel("Look & Feel", "Substance"), Skin("Skin", "Mist Aqua");

	private final String name, defaultValue;

	Defaults(String name, String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public String getDefault() {
		return defaultValue;
	}

	public String getName() {
		return name;
	}

	public static void initFromTable(Object[][] table) {

	}

	public static boolean contains(String string) {
		// TODO Auto-generated method stub
		return false;
	}

	public static String get(Defaults val) {
		return val.getDefault();
	}

	public static String get(String val) {
		return null;
	}

	public static String getLineSeperator() {
		return System.getProperty("line.separator");
	}
}
