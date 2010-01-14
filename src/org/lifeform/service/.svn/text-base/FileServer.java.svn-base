/*******************************************************************************
 * Copyright (c) 2009 Lifeform Software.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ernan Hughes - initial API and implementation
 *******************************************************************************/
package org.lifeform.service;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class FileServer extends JPanel implements ActionListener,
		PropertyChangeListener {
	private static final long serialVersionUID = -3487778479868819135L;

	class CopyData {
		private int progress;
		private String fileName;
		private long totalKiloBytes;
		private long kiloBytesCopied;

		CopyData(final int progress, final String fileName, final long totalKiloBytes,
				final long kiloBytesCopied) {
			this.progress = progress;
			this.fileName = fileName;
			this.totalKiloBytes = totalKiloBytes;
			this.kiloBytesCopied = kiloBytesCopied;
		}

		String getFileName() {
			return fileName;
		}

		long getKiloBytesCopied() {
			return kiloBytesCopied;
		}

		int getProgress() {
			return progress;
		}

		long getTotalKiloBytes() {
			return totalKiloBytes;
		}
	}

	class CopyFiles extends SwingWorker<Void, CopyData> {
		private static final int PROGRESS_CHECKPOINT = 10000;
		private File srcDir;
		private File destDir;

		CopyFiles(final File src, final File dest) {
			this.srcDir = src;
			this.destDir = dest;
		}

		private long calcTotalBytes(final File[] files) {
			long tmpCount = 0;
			for (File f : files) {
				tmpCount += f.length();
			}
			return tmpCount;
		}

		// perform time-consuming copy task in the worker thread
		@Override
		public Void doInBackground() {
			int progress = 0;
			// initialize bound property progress (inherited from SwingWorker)
			setProgress(0);
			// get the files to be copied from the source directory
			File[] files = srcDir.listFiles();
			// determine the scope of the task
			long totalBytes = calcTotalBytes(files);
			long bytesCopied = 0;

			while (progress < 100 && !isCancelled()) {
				// copy the files to the destination directory
				for (File f : files) {
					File destFile = new File(destDir, f.getName());
					long previousLen = 0;

					try {
						InputStream in = new FileInputStream(f);
						OutputStream out = new FileOutputStream(destFile);
						byte[] buf = new byte[1024];
						int counter = 0;
						int len;

						while ((len = in.read(buf)) > 0) {
							out.write(buf, 0, len);
							counter += len;
							bytesCopied += (destFile.length() - previousLen);
							previousLen = destFile.length();
							if (counter > PROGRESS_CHECKPOINT
									|| bytesCopied == totalBytes) {
								// get % complete for the task
								progress = (int) ((100 * bytesCopied) / totalBytes);
								counter = 0;
								CopyData current = new CopyData(progress, f
										.getName(),
										getTotalKiloBytes(totalBytes),
										getKiloBytesCopied(bytesCopied));

								// set new value on bound property
								// progress and fire property change event
								setProgress(progress);

								// publish current progress data for copy task
								publish(current);
							}
						}
						in.close();
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return null;
		}

		// perform final updates in the event dispatch thread
		@Override
		public void done() {
			try {
				// call get() to tell us whether the operation completed or
				// was canceled; we don't do anything with this result
				get();
				console.append("Copy operation completed.\n");
			} catch (InterruptedException e) {

			} catch (CancellationException e) {
				// get() throws CancellationException if background task was
				// canceled
				console.append("Copy operation canceled.\n");
			} catch (ExecutionException e) {
				console.append("Exception occurred: " + e.getCause());
			}
			// reset the example app
			copyButton.setEnabled(true);
			progressMonitor.setProgress(0);
		}

		private long getKiloBytesCopied(final long bytesCopied) {
			return Math.round(bytesCopied / 1024);
		}

		private long getTotalKiloBytes(final long totalBytes) {
			return Math.round(totalBytes / 1024);
		}

		// process copy task progress data in the event dispatch thread
		@Override
		public void process(final List<CopyData> data) {
			if (isCancelled()) {
				return;
			}
			CopyData update = new CopyData(0, "", 0, 0);
			for (CopyData d : data) {
				// progress updates may be batched, so get the most recent
				if (d.getKiloBytesCopied() > update.getKiloBytesCopied()) {
					update = d;
				}
			}

			// update the progress monitor's status note with the
			// latest progress data from the copy operation, and
			// additionally append the note to the console
			String progressNote = update.getKiloBytesCopied() + " of "
					+ update.getTotalKiloBytes() + " kb copied.";
			String fileNameNote = "Now copying " + update.getFileName();

			if (update.getProgress() < 100) {
				progressMonitor.setNote(progressNote + " " + fileNameNote);
				console.append(progressNote + "\n" + fileNameNote + "\n");
			} else {
				progressMonitor.setNote(progressNote);
				console.append(progressNote + "\n");
			}
		}
	}

	private static final int DEFAULT_WIDTH = 700;
	private static final int DEFAULT_HEIGHT = 350;

	public static void main(final String[] args) {
		// tell the event dispatch thread to schedule the execution
		// of this Runnable (which will create the example app GUI) for a later
		// time
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// create example app window
				JFrame frame = new JFrame("File Server");
				// application will exit on close
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

				// create example app content pane
				// ProgressMonitorExample constructor does additional GUI setup
				JComponent contentPane = new FileServer();
				contentPane.setOpaque(true);
				frame.setContentPane(contentPane);

				// display example app window
				frame.setVisible(true);
			}
		});
	}

	private JButton copyButton;

	private JTextArea console;

	private ProgressMonitor progressMonitor;

	private CopyFiles operation;

	public FileServer() {
		// set up the copy files button
		copyButton = new JButton("Copy Files");
		copyButton.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(copyButton);

		// set up the console for display of operation output
		console = new JTextArea(15, 60);
		console.setMargin(new Insets(5, 5, 5, 5));
		console.setEditable(false);
		add(new JScrollPane(console), BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	public void actionPerformed(final ActionEvent event) {
		// make sure there are files to copy
		File srcDir = new File("in");
		if (srcDir.exists()
				&& (srcDir.listFiles() != null && srcDir.listFiles().length > 0)) {
			// set up the destination directory
			File destDir = new File("out");
			// create the progress monitor
			progressMonitor = new ProgressMonitor(FileServer.this,
					"Operation in progress...", "", 0, 100);
			progressMonitor.setProgress(0);

			// schedule the copy files operation for execution on a background
			// thread
			operation = new CopyFiles(srcDir, destDir);
			// add ProgressMonitorExample as a listener on CopyFiles;
			// of specific interest is the bound property progress
			operation.addPropertyChangeListener(this);
			operation.execute();
			// we're running our operation; disable copy button
			copyButton.setEnabled(false);
		} else {
			console.append("The sample application needs files to copy."
					+ " Please add some files to the in directory"
					+ " located at the project root.");
		}
	}

	// executes in event dispatch thread
	public void propertyChange(final PropertyChangeEvent event) {
		// if the operation is finished or has been canceled by
		// the user, take appropriate action
		if (progressMonitor.isCanceled()) {
			operation.cancel(true);
		} else if (event.getPropertyName().equals("progress")) {
			// get the % complete from the progress event
			// and set it on the progress monitor
			int progress = ((Integer) event.getNewValue()).intValue();
			progressMonitor.setProgress(progress);
		}
	}
}
