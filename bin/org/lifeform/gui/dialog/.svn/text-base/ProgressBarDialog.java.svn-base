package org.lifeform.gui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.lifeform.gui.dialog.PBDialogDemo.ProcessThread;

/**
 * progress bar dialog.
 */
public class ProgressBarDialog extends Dialog {

	public Label processMessageLabel; // info of process finish
	private Button cancelButton; // cancel button
	private Composite cancelComposite;
	private Label lineLabel;//
	private Composite progressBarComposite;//
	private CLabel message;//
	public ProgressBar progressBar = null; //

	private Object result; //
	public Shell shell; //
	public Display display = null;

	public volatile boolean isClosed = false;// closed state

	protected int executeTime = 100;// process times
	protected String processMessage = "please wait ...";// procress info
	protected String shellTitle = "Progress..."; //
	protected Image processImage = null;// image
	protected boolean mayCancel = false; // cancel
	protected int processBarStyle = SWT.SMOOTH; // process bar style

	public ProcessThread processThread;

	public void setMayCancel(final boolean mayCancel) {
		this.mayCancel = mayCancel;
	}

	public void setExecuteTime(final int executeTime) {
		this.executeTime = executeTime;
	}

	public void setProcessImage(final Image processImage) {
		this.processImage = processImage;
	}

	public void setProcessMessage(final String processInfo) {
		this.processMessage = processInfo;
	}

	public ProgressBarDialog(final Shell parent) {
		super(parent);
	}

	public void registerProcessThread(final ProcessThread thread) {
		processThread = thread;
	};

	public Object open() {
		createContents();
		shell.open();
		shell.layout();

		processThread.start();

		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	protected void createContents() {
		shell = new Shell(getParent(), SWT.TITLE | SWT.PRIMARY_MODAL
				| SWT.DIALOG_TRIM);
		shell.setLocation(getParent().toDisplay(100, 100));
		display = shell.getDisplay();
		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 10;

		shell.setLayout(gridLayout);
		if (mayCancel) {
			shell.setSize(400, 180);
		} else {
			shell.setSize(400, 128);
		}
		shell.setText(shellTitle);

		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				true, false));
		composite.setLayout(new GridLayout());

		message = new CLabel(composite, SWT.NONE);
		message.setImage(processImage);
		message.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
				true, false));
		message.setText(processMessage);

		progressBarComposite = new Composite(shell, SWT.NONE);
		progressBarComposite.setLayoutData(new GridData(GridData.FILL,
				GridData.CENTER, false, false));
		progressBarComposite.setLayout(new FillLayout());

		progressBar = new ProgressBar(progressBarComposite, processBarStyle);
		progressBar.setMaximum(executeTime);

		processMessageLabel = new Label(shell, SWT.NONE);
		processMessageLabel.setLayoutData(new GridData(GridData.FILL,
				GridData.CENTER, false, false));

		if (mayCancel) {
			lineLabel = new Label(shell, SWT.HORIZONTAL | SWT.SEPARATOR);
			lineLabel.setLayoutData(new GridData(GridData.FILL,
					GridData.CENTER, false, false));

			cancelComposite = new Composite(shell, SWT.NONE);
			cancelComposite.setLayoutData(new GridData(GridData.END,
					GridData.CENTER, false, false));
			final GridLayout gridLayout_1 = new GridLayout();
			gridLayout_1.numColumns = 2;
			cancelComposite.setLayout(gridLayout_1);

			cancelButton = new Button(cancelComposite, SWT.NONE);
			cancelButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					isClosed = true;
				}
			});
			cancelButton.setLayoutData(new GridData(78, SWT.DEFAULT));
			cancelButton.setText("Cancel");
			cancelButton.setEnabled(this.mayCancel);
		}

	}

	public void setShellTitle(final String shellTitle) {
		this.shellTitle = shellTitle;
	}

	public void setProcessBarStyle(final boolean pStyle) {
		if (pStyle)
			this.processBarStyle = SWT.SMOOTH;
		else
			this.processBarStyle = SWT.NONE;

	}
}

class PBDialogDemo extends Shell {

	public static void main(final String args[]) {
		try {
			Display display = Display.getDefault();
			PBDialogDemo shell = new PBDialogDemo(display, SWT.SHELL_TRIM);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PBDialogDemo(final Display display, final int style) {
		super(display, style);
		setText("ProgressBar Dialog");
		setSize(218, 98);
		setLayout(new FillLayout());

		final Button openProgressbarDialogButton = new Button(this, SWT.NONE);
		openProgressbarDialogButton
				.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						ProgressBarDialog dpb = new ProgressBarDialog(
								PBDialogDemo.this);
						dpb.registerProcessThread(new ProcessThread(100, dpb));
						dpb.open();
					}
				});
		openProgressbarDialogButton.setText("Open ProgressBar Dialog");
	}

	@Override
	protected void checkSubclass() {
	}

	public class ProcessThread extends Thread {
		private int max = 0;

		private volatile boolean shouldStop = false;

		private ProgressBarDialog dialog;

		public ProcessThread() {
			this.max = 100;
		}

		public ProcessThread(final int max, final ProgressBarDialog dialog) {
			this.max = max;
			this.dialog = dialog;
		}

		public void update(final String message, final int progress) {
			if (dialog.display.isDisposed()) {
				return;
			}
			try {
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			dialog.display.syncExec(new Runnable() {
				public void run() {
					if (dialog.progressBar.isDisposed()) {
						return;
					}
					dialog.processMessageLabel.setText(message);
					dialog.progressBar.setSelection(progress);
					if (progress == max || dialog.isClosed) {
						// stay a while before close the window.
						try {
							Thread.sleep(1500);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						if (dialog.isClosed) {
							shouldStop = true;//
						}
						dialog.shell.close();//
					}
				}
			});
		}

		public void process() {
			for (final int[] i = new int[] { 1 }; i[0] <= max; i[0]++) {
				//
				try {
					Thread.sleep((long) (Math.random() * 100));
				} catch (Exception e) {
					e.printStackTrace();
				}

				update("step " + i[0], i[0]);

				if (shouldStop) {
					break;
				}
			}
		}

		@Override
		public void run() {
			process();
		}
	}
}