package org.lifeform.gui.dialog;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FindReplaceDialog extends Dialog {
	private ITextViewer viewer;

	private Button doFind;
	private Button doReplace;
	private Button doReplaceFind;

	private Shell m_dlgShell;
	private final int m_offset_before_match_region = -1;
	private final int m_offset_after_match_region = -1;

	private static final String FIND_FIRST = "FIND_FIRST";
	private static final String FIND_NEXT = "FIND_NEXT";
	private static final String REPLACE = "REPLACE";
	private static final String REPLACE_FIND_NEXT = "REPLACE_FIND_NEXT";

	/**
	 * FindReplaceDialog constructor
	 * 
	 * @param shell
	 *            the parent shell
	 * @param document
	 *            the associated document
	 * @param viewer
	 *            the associated viewer
	 */
	public FindReplaceDialog(final Shell shell) {
		super(shell, SWT.DIALOG_TRIM | SWT.MODELESS);
	}

	/**
	 * Opens the dialog box
	 */
	public void open() {
		m_dlgShell = new Shell(getParent(), getStyle());
		m_dlgShell.setText("Find/Replace");
		createContents(m_dlgShell);
		m_dlgShell.pack();
		m_dlgShell.open();
		Display display = getParent().getDisplay();
		while (!m_dlgShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void forceActive() {
		m_dlgShell.forceActive();
	}

	/**
	 * Performs a find/replace
	 * 
	 * @param code
	 *            the code
	 * @param find
	 *            the find string
	 * @param replace
	 *            the replace text
	 * @param forward
	 *            whether to search forward
	 * @param matchCase
	 *            whether to match case
	 * @param wholeWord
	 *            whether to search on whole word
	 * @param regexp
	 *            whether find string is a regular expression
	 */
	protected void doFind(final String code, final String find, final String replace,
			final boolean forward, final boolean matchCase, final boolean wholeWord,
			final boolean regexp) {
	}

	/**
	 * Creates the dialog's contents
	 * 
	 * @param shell
	 */
	protected void createContents(final Shell shell) {
		shell.setLayout(new GridLayout(2, false));

		// Add the text input fields
		Composite text = new Composite(shell, SWT.NONE);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setLayout(new GridLayout(3, true));

		new Label(text, SWT.LEFT).setText("&Find:");
		final Text findText = new Text(text, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		findText.setLayoutData(data);

		new Label(text, SWT.LEFT).setText("R&eplace With:");
		final Text replaceText = new Text(text, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		replaceText.setLayoutData(data);

		// Add the match case checkbox
		final Button match = new Button(text, SWT.CHECK);
		match.setText("&Match Case");

		// Add the whole word checkbox
		final Button wholeWord = new Button(text, SWT.CHECK);
		wholeWord.setText("&Whole Word");

		// Add the regular expression checkbox
		final Button regexp = new Button(text, SWT.CHECK);
		regexp.setText("RegE&xp");

		// Add the direction radio buttons
		final Button down = new Button(text, SWT.RADIO);
		down.setText("D&own");

		final Button up = new Button(text, SWT.RADIO);
		up.setText("&Up");

		// Add the buttons
		Composite buttons = new Composite(shell, SWT.NONE);
		buttons.setLayout(new GridLayout(1, false));

		// Create the Find button
		doFind = new Button(buttons, SWT.PUSH);
		doFind.setText("Fi&nd");
		doFind.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Set the initial find operation to FIND_FIRST
		doFind.setData(FindReplaceDialog.FIND_FIRST);

		// Create the Replace button
		doReplace = new Button(buttons, SWT.PUSH);
		doReplace.setText("&Replace");
		doReplace.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Create the Replace/Find button
		doReplaceFind = new Button(buttons, SWT.PUSH);
		doReplaceFind.setText("Replace/Fin&d");
		doReplaceFind.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		doReplaceFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				doFind(FindReplaceDialog.REPLACE_FIND_NEXT, findText.getText(),
						replaceText.getText(), down.getSelection(), match
								.getSelection(), wholeWord.getSelection(),
						regexp.getSelection());
			}
		});

		// Create the Close button
		Button close = new Button(buttons, SWT.PUSH);
		close.setText("Close");
		close.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		close.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				shell.close();
			}
		});

		// Reset the FIND_FIRST/FIND_NEXT when find text is modified
		findText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent event) {
				doFind.setData(FindReplaceDialog.FIND_FIRST);
				enableReplaceButtons(false);
			}
		});

		// Change to FIND_NEXT and enable replace buttons on successful find
		doFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				// Do the find, pulling the operation code out of the button
				doFind((String) event.widget.getData(), findText.getText(),
						replaceText.getText(), down.getSelection(), match
								.getSelection(), wholeWord.getSelection(),
						regexp.getSelection());
			}
		});

		// Replace loses "find" state, so disable buttons
		doReplace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				doFind(FindReplaceDialog.REPLACE, findText.getText(),
						replaceText.getText(), down.getSelection(), match
								.getSelection(), wholeWord.getSelection(),
						regexp.getSelection());
			}
		});

		// Set defaults
		down.setSelection(true);
		findText.setFocus();
		doReplace.setEnabled(false);
		doReplaceFind.setEnabled(false);
		shell.setDefaultButton(doFind);
	}

	/**
	 * Enables/disables the Replace and Replace/Find buttons
	 * 
	 * @param enable
	 *            whether to enable or disable
	 */
	protected void enableReplaceButtons(final boolean enable) {
		doReplace.setEnabled(enable);
		doReplaceFind.setEnabled(enable);
	}

	/**
	 * Shows an error
	 * 
	 * @param message
	 *            the error message
	 */
	protected void showError(final String message) {
		MessageDialog.openError(getParent(), "Error", message);
	}
}
