package org.lifeform.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.lifeform.configuration.Defaults;
import org.lifeform.report.format.Format;
import org.lifeform.util.AppUtil;

public final class Report {
	private final String fieldStart;
	private final String fieldEnd;
	private final String rowStart;
	private final String rowEnd;
	private final String fieldBreak;
	private final Format renderer;

	private final SimpleDateFormat df = new SimpleDateFormat(
			"HH:mm:ss MM/dd/yy z");
	private PrintWriter writer = new PrintWriter(System.out);
	private static boolean isDisabled;
	private final static String FILE_SEP = System.getProperty("file.separator");
	private final static String REPORT_DIR = AppUtil.getAppPath() + FILE_SEP
			+ "reports" + FILE_SEP;

	public Report(final String fileName) throws Exception {
		String reportRendererClass = Defaults.get(Defaults.ReportRenderer);

		Class<? extends Format> clazz = Class.forName(reportRendererClass)
				.asSubclass(Format.class);
		renderer = clazz.newInstance();

		fieldStart = renderer.getFieldStart();
		fieldEnd = renderer.getFieldEnd();
		rowStart = renderer.getRowStart();
		rowEnd = renderer.getRowEnd();
		fieldBreak = renderer.getFieldBreak();
		String emphasisStart = renderer.getEmphasisStart();
		String emphasisEnd = renderer.getEmphasisEnd();
		String rootStart = renderer.getRootStart();
		String fileExtension = renderer.getFileExtension();

		if (isDisabled) {
			return;
		}

		File reportDir = new File(REPORT_DIR);
		if (!reportDir.exists()) {
			reportDir.mkdir();
		}

		String fullFileName = fileName + "." + fileExtension;
		boolean append = "Append"
				.equals(Defaults.get(Defaults.ReportRecycling));
		writer = new PrintWriter(new BufferedWriter(new FileWriter(
				fullFileName, append)));
		StringBuilder s = new StringBuilder();
		s.append(emphasisStart).append("New Report Started: ").append(
				df.format(getDate())).append(emphasisEnd);
		s.append(rootStart);
		reportDescription(s.toString());
	}

	public Format getRenderer() {
		return renderer;
	}

	public static void disable() {
		isDisabled = true;
	}

	public static void enable() {
		isDisabled = false;
	}

	public void report(final StringBuilder message) {
		StringBuilder s = new StringBuilder();
		s.append(rowStart);
		s.append(fieldStart).append(df.format(getDate())).append(fieldEnd);
		s.append(fieldStart).append(message).append(fieldEnd);
		s.append(rowEnd);
		write(s);
	}

	public void report(final String message) {
		report(new StringBuilder(message));
	}

	public void report(final Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		pw.close();
		boolean saved = isDisabled;
		isDisabled = false; // always report exceptions
		report(sw.toString());
		isDisabled = saved;
	}

	public void reportDescription(final String message) {
		StringBuilder s = new StringBuilder();
		s.append(message).append(fieldBreak);
		write(s);
	}

	public void report(final List<?> columns) {
		StringBuilder s = new StringBuilder();
		s.append(rowStart);
		for (Object column : columns) {
			s.append(fieldStart).append(column).append(fieldEnd);
		}
		s.append(rowEnd);
		write(s);
	}

	public void report(final List<?> columns, final Calendar strategyCalendar) {
		StringBuilder s = new StringBuilder();
		s.append(rowStart);

		s.append(fieldStart);
		df.setTimeZone(strategyCalendar.getTimeZone());
		s.append(df.format(strategyCalendar.getTime()));
		s.append(fieldEnd);

		for (Object column : columns) {
			s.append(fieldStart).append(column).append(fieldEnd);
		}

		s.append(rowEnd);
		write(s);
	}

	private Date getDate() {
		return Calendar.getInstance(TimeZone.getDefault()).getTime();
	}

	private synchronized void write(final StringBuilder s) {
		if (!isDisabled) {
			writer.println(s);
			writer.flush();
		}
	}

}
