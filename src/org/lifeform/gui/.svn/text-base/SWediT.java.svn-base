package org.lifeform.gui;

/*
 * @(#)SWediT.java 1.02 2004-04-19
 *  
 * Copyright (c) 2002, 2003, 2004 by James D. Ingraham. All Rights Reserved.
 *
 * The SWediT License is detailed below. Search for SweditLicense or 
 * "SWEDIT LICENSE" to view the license. Alternately, you may have received
 * a copy of license.txt which contains the license information, or if you run
 * SWediT you can choose "License" from the Help menu.
 *  
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineBackgroundEvent;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.lifeform.gui.command.TMCommand;
import org.lifeform.gui.command.TMCommandResult;

//=============================================================================
/*                   
 SWediT is a minamilist text editor.  The build and run requirements are fairly
 simple; you must have Java development software compliant with J2SE 1.2 or 
 later.  You must have SWT (the Standard Widget Toolkit) from Eclipse.org.  To
 compile you must have swt.jar in your classpath.  To run, you must also have 
 the shared library associated with swt.jar, generally a file called 
 swt-win32-xxxx.dll on Windows or libswt-photon-xxxx.so on QNX Neutrino / Photon.

 With any luck, more information is available at swedit.sourceforge.net

 If you see a comment with %%% in it, that's a flag that work needs to be done
 near that comment.  This is a simple thing to search for when trying to think
 of the next thing to work on.
 */
//=============================================================================

public final class SWediT implements VerifyKeyListener, ModifyListener,
		LineStyleListener, LineBackgroundListener, ShellListener {

	// =============================================================================
	// Global variables
	// SWT links everything to a display
	private static Display display = new Display();
	// Keys-to-commands mapping
	private static Map keys = new HashMap();
	// Individual text buffers
	private static Vector buffers = new Vector();
	// Names associated with SWT constatns for keys, e.g. SWT.BS is "Backspace"
	private static Map keyNames = new HashMap();
	private final StyledText editor; // The actual text editor area
	private final Shell shell; // SWT shell (think of a window)
	private final Menu bar; // The Pull-down Menu bar
	private MenuItem linePos; // Line position is shown on the menu bar
	private MenuItem colPos; // Column position is shown on the menu bar
	private boolean changed; // Remember that the text has changed
	private boolean dosFormat; // File should be saved in DOS format?
	private boolean dosFormatDefault; // Files should default to DOS format?
	private Stack history; // Undo stack
	private final Stack rehistory; // Redo stack
	private int currentBuffer; // Tracks the current buffer
	private String filename; // File name for the current buffer
	private boolean quitFlag; // Signal to quit the program
	private boolean cancelFlag; // For returning a cancel from dialogues
	private int numUntitled; // The number of untitled docs opened this session
	private String searchString; // Current search text; used in findNext
	private int lastColumn; // Column to place cursor in
	private boolean expandTabs; // Expand tabs into spaces automatically?
	private boolean askAboutTabs; // Ask what to do with tabs while loading a
	// file?
	private int syndex; // Index to syntax highlighting record
	private String[] block; // Block cut, copy, and paste text
	private int blockStart; // Starting index of the block selection
	private int blockHeight; // Height of the block selection
	private int blockWidth; // Width of the block selection
	private Properties props; // The properties requested by the user
	private boolean debugMode; // Debug Mode displays extra data when exceptions
	// occur
	private final TextColors colors; // Colors to use for syntax highlighting
	private boolean configLoaded; // Was a configuration file loaded?
	private final SweditLicense sweditLicenseDialog;
	private final PrintKeyMap printKeyMapDialog;

	// =============================================================================
	// Main does nothing but call the default constructor, which does all the
	// work
	public static void main(final String[] args) {
		SWediT swedit = new SWediT(args);
	}

	// =============================================================================
	// Default constructor, where everything happens.
	SWediT(final String[] args) {
		// Initialize the global variables
		history = new Stack();
		rehistory = new Stack();
		searchString = new String("");
		currentBuffer = -1;
		quitFlag = false;
		numUntitled = 1;
		lastColumn = 1;
		block = null;
		// Check for command line arguments. Anything besides "-p" is a filename
		// to be edited.
		String[] filenames = new String[args.length];
		int filecount = 0;
		String propsFilename = null;
		// Go through the list of command line arguments
		for (int i = 0; i < args.length; i++) {
			// a -p signifies the name of the properties (config) file to use
			if (args[i].equals("-p")) {
				// The NEXT argument is the properties file name
				i++;
				// Make sure the user didn't put a -p with nothing after it
				if (i < args.length) {
					propsFilename = args[i];
				}
			} else if (args[i].startsWith("-p")) {
				// If the user didn't put a space, chop off the -p
				propsFilename = args[i].substring(2);
			} else {
				filenames[filecount] = args[i];
				filecount++;
			}
		}
		loadProperties(propsFilename); // Load the properties file first
		// Colors are referenced to a display, so they have to be loaded at
		// runtime
		colors = new TextColors(display);
		// Create the shell, which is the connection between Java and the GUI
		shell = new Shell(display);
		// Set the text on title bar to the name of the program
		shell.setText("SWediT - Untitled");
		// Set the shell's layout to a FillLayout
		shell.setLayout(new FillLayout());
		// Set up the size of the shell, in pixles
		// %%% Size shouldn't be greater than the current res. Remember the last
		// setting?
		int width = 640;
		int height = 480;
		try {
			height = Integer
					.parseInt(props.getProperty("Window_Height", "480"));
		} catch (Exception e) {
			exceptionMessage("Exception Parsing Window_Height Property: \n" + e);
		}
		try {
			width = Integer.parseInt(props.getProperty("Window_Width", "640"));
		} catch (Exception e) {
			exceptionMessage("Exception Parsing Window_Width Property: \n" + e);
		}
		shell.setSize(width, height);
		// Set up the license and key map dialogs
		sweditLicenseDialog = new SweditLicense();
		printKeyMapDialog = new PrintKeyMap();
		// Create a new Menu bar for the shell
		bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		setupMenu();

		// create the styled text widget with scroll bars
		editor = new StyledText(shell, SWT.V_SCROLL | SWT.H_SCROLL);
		// Check all key strokes in the verifyKey method the SWediT class
		editor.addVerifyKeyListener(this);
		// Notice when text has changed
		editor.addModifyListener(this);
		// Update style when text has been redrawn
		editor.addLineStyleListener(this);
		// Update line background color
		editor.addLineBackgroundListener(this);
		// The font must be a fixed width font, of reasonable size
		// %%% Support other fonts besides Courier?
		int fontSize = 12;
		try {
			fontSize = Integer.parseInt(props.getProperty("Font_Size", "12"));
		} catch (Exception e) {
			exceptionMessage("Exception Parsing Window_Width Property: \n" + e);
		}
		editor.setFont(new Font(display, "Courier", fontSize, SWT.NORMAL));

		// Set the tab size
		try {
			int tabsize = Integer.parseInt(props.getProperty("Tab_Size", "2"));
			editor.setTabs(tabsize);
		} catch (Exception e) {
			exceptionMessage("Exception Parsing Tab_Size Property: \n" + e);
			editor.setTabs(2);
		}
		// Really, SWediT only cares about a Close command so that files can be
		// saved before exiting, but there are 4 additional methods besides the
		// shellClosed method in the ShellListener interface.
		shell.addShellListener(this);

		// Prep the key stroke data
		setupKeyNames(keyNames);
		setupKeyMap();

		// If filenames were passed, try to load them.
		boolean loaded = false;
		for (int i = 0; i < filecount; i++) {
			if (load(filenames[i])) {
				loaded = true;
			}
		}
		if (!loaded) {
			// The editor always expects at least one buffer
			filename = new String("Untitled");
			buffers.add(new Buffer("", filename, 0, 0, false, new Stack(),
					expandTabs, dosFormatDefault));
			currentBuffer = 0;
		}

		// Get the property for Start Maximized
		shell.setMaximized(Boolean.valueOf(
				props.getProperty("Start_Maximized", "true")).booleanValue());
		// Get this party started!
		shell.open();

		// Standard SWT code
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		// Don't forget to free things up.
		display.dispose();
	} // End of constructor

	// =============================================================================
	// Handles a quit command from the OS, such as clicking the "X" box in the
	// top right or Alt-F4
	public void shellClosed(final ShellEvent e) {
		// Run the quit command
		runCommand(new SweditQuit());
		// Don't let the command go through, since quit has already disposed the
		// shell.
		e.doit = false;
	} // End of shellClosed

	// =============================================================================
	// Not used; needed if implementing ShellListener interface
	public void shellActivated(final ShellEvent e) {
	}

	// Not used; needed if implementing ShellListener interface
	public void shellDeactivated(final ShellEvent e) {
	}

	// Not used; needed if implementing ShellListener interface
	public void shellDeiconified(final ShellEvent e) {
	}

	// Not used; needed if implementing ShellListener interface
	public void shellIconified(final ShellEvent e) {
	}

	// =============================================================================
	// Remember that the text has changed so the app won't quit without saving
	public void modifyText(final ModifyEvent modEvent) {
		if (history.empty()) {
			changed = false;
		} else {
			changed = true;
		}
	}

	// =============================================================================
	// Syntax higlighting. lineGetStyle is part of SWT's LineStyleListener
	// interface, which works with the StyledText widget.
	// %%% Multi-line comments aren't handled
	// %%% Performance?
	public void lineGetStyle(final LineStyleEvent styleEvent) {

		// Since the block selection can be anywhere anytime, figure out if this
		// line has a portion of the block selection
		int startBlockCol = -1, endBlockCol = -1;
		int blockLine = editor.getLineAtOffset(blockStart);
		int thisLine = editor.getLineAtOffset(styleEvent.lineOffset);
		if ((thisLine >= blockLine) && (thisLine < (blockLine + blockHeight))) {
			startBlockCol = blockStart - editor.getOffsetAtLine(blockLine);
			startBlockCol += styleEvent.lineOffset;
			endBlockCol = startBlockCol + blockWidth;
		}

		// Check if the buffer has a valid syntax for highlighting
		if ((syndex < 0) || (syndex >= Syntax.extensions.length)) {
			// If there's no block comment then there is nothing to do.
			if ((blockHeight <= 0) || (blockWidth <= 0) || (blockStart < 0)) {
				return;
			}
			// Find the start and end of the line currently being redisplayed
			int start = styleEvent.lineOffset;
			int end = start + styleEvent.lineText.length();
			// If the block selection doesn't go through this line there is
			// nothing to do
			if ((startBlockCol >= end) || (start >= endBlockCol)) {
				return;
			}
			// The styleEvent wants an array of StyleRanges, but we know there's
			// only 1
			StyleRange[] style = new StyleRange[1];
			style[0] = new StyleRange();
			style[0].background = colors.block;
			style[0].foreground = colors.normal;
			// The startBlockCol (as calculated above) can never be less than
			// the
			// start of the line, and we've already checked to see if it's
			// passed the
			// end so start position is easy. Length is easy because when block
			// selecting the code pads spaces onto the end of lines, so the
			// blockWidth
			// will never be passed the end of the line.
			style[0].start = startBlockCol;
			style[0].length = blockWidth;
			// Set the style for the line.
			styleEvent.styles = style;
			// Nothing else to do.
			return;
		}

		// Loop through each character in the line of text, creating styles to
		// match what's found
		int start = 0; // Always begin at the beginning
		ArrayList ranges = new ArrayList();
		String text = styleEvent.lineText; // text could be modified, so use a
		// temp var
		while (start < text.length()) { // Go through the whole line.
			int end = 0; // Everytime through reset the end position
			String word; // Current "unit" of text found
			int i; // counter
			char currChar = text.charAt(start); // Get the current character
			StyleRange style = new StyleRange(); // Create a style
			// %%% Multi-line comment highlighting
			Integer offsetInteger = new Integer(styleEvent.lineOffset);
			if (false) {
				end = text.indexOf("*/", start) + 2;
				if (end < 2) {
					end = text.length();
				}
				style.foreground = colors.comment;
				style.start = start + styleEvent.lineOffset;
				style.length = end - start;
				// Single (' ') and double (" ") quotes
			} else if ((currChar == '\"') || (currChar == '\'')) {
				end = start + 1;
				boolean ignore = false;
				if (start > 0) {
					// Ignore quotes that are preceeded by a backslash
					if (text.charAt(start - 1) == '\\') {
						ignore = true;
					}
				}
				if (!ignore) {
					// A rare do-while loop. I'm open to suggestions on how to
					// invert it
					// Go until a closing quote is found that is not preceeded
					// by a backslash
					do {
						// Find the index of the next occuring quote (currChar
						// remembers if
						// it's a single or double quote)
						end = text.indexOf(currChar, end) + 1;
						// indexOf() returns -1 if the character is not found;
						// the +1 above
						// makes it 0. If not found, use the end of line and
						// break out.
						if (end == 0) {
							end = text.length();
							break;
						}
					} while (text.charAt(end - 2) == '\\');
					style.foreground = colors.quote;
					style.start = start + styleEvent.lineOffset;
					style.length = end - start;
				}
				// Whitespace (i.e. spaces and tabs) is the easiest.
			} else if (Character.isWhitespace(currChar)) {
				end = start; // Set a zero width for now
				// Loop until there is a non-whitespace charcter
				while (Character.isWhitespace(text.charAt(end))) {
					end++; // Increment the end
					// Stop if the end of the line is reached.
					if (end >= text.length()) {
						end = text.length();
						break;
					}
				}
				// It may seem silly to have a style for whitespace that is
				// normal.
				// However, it is necessary because of block selection, which is
				// handled later in the function.
				style.foreground = colors.normal;
				style.start = start + styleEvent.lineOffset;
				style.length = end - start;
				// Slash
			} else if (currChar == '/') {
				boolean alone = false;
				// Don't go past the end of the text
				if ((start + 1) < text.length()) {
					// If this slash is followed by a 2nd, it is a C++ style
					// comment
					if (text.charAt(start + 1) == '/') {
						end = text.length();
						style.foreground = colors.comment;
						style.start = start + styleEvent.lineOffset;
						style.length = end - start;
						// If this slash is followed by a *, it is a C style
						// comment
						// %%% Only the first line is dealth with, the rest will
						// be
						// %%% incorrectly highlighted with standard rules.
					} else if (text.charAt(start + 1) == '*') {
						end = -1;
						if ((start + 2) < text.length()) {
							end = text.indexOf("*/", start + 2) + 2;
						}
						if (end < 2) {
							end = text.length();
						}
						style.foreground = colors.comment;
						style.start = start + styleEvent.lineOffset;
						style.length = end - start;
						// The slash is alone, check it later
					} else {
						alone = true;
					}
				} else {
					// The slash is the last character on the line, check it
					// later
					alone = true;
				}
				// If this slash is alone or the last character on the line,
				// check to
				// see if it's an operator
				if (alone) {
					end = start + 1;
					if (Syntax.operators[syndex].contains("/")) {
						style.foreground = colors.operator;
						style.start = start + styleEvent.lineOffset;
						style.length = 1;
					}
				}
				// Letters, digits, and the pound sign
			} else if (Character.isLetterOrDigit(currChar) || (currChar == '#')) {
				// Loop through looking for the end of the word
				for (i = start + 1; i < text.length(); i++) {
					// The end is when it's no longer a letter or digit or
					// underscore
					if (!Character.isLetterOrDigit(text.charAt(i))
							&& (text.charAt(i) != '_')) {
						break;
					}
				}
				end = i;
				style = new StyleRange();
				style.start = start + styleEvent.lineOffset;
				style.length = end - start;
				// Get the actual word
				word = text.substring(start, end);
				// See if it's a keyword
				if (Syntax.keywords[syndex].contains(word)) {
					style.foreground = colors.keyword;
					// or a variable type (which most languages consider
					// keywords, but
					// SWediT highlights them differently)
				} else if (Syntax.varTypes[syndex].contains(word)) {
					style.foreground = colors.varType;
					// or just normal
				} else {
					style.foreground = colors.normal;
				}
				// All remaining possible characters. At this point, the only
				// things
				// left are operators and brackets []{}()
			} else {
				end = start + 1;
				style = new StyleRange();
				style.start = start + styleEvent.lineOffset;
				style.length = end - start;
				word = text.substring(start, end);
				// See if it's an operator
				if (Syntax.operators[syndex].contains(word)) {
					style.foreground = colors.operator;
					// or a bracket
				} else if (Syntax.brackets[syndex].contains(word)) {
					style.foreground = colors.bracket;
					// or just normal
				} else {
					style.foreground = colors.normal;
				}
			}
			// Move the index forward to find the next word (watch out for
			// invalid data)
			start = end;
			if (start < 0) {
				break;
			}
			// Handle block selection highlighting. There are basically five
			// possibilities.
			// (1) the style range does not include the block selection at all.
			if ((startBlockCol >= (style.start + style.length))
					|| (style.start >= endBlockCol)) {
				ranges.add(style);
			} else {
				if (startBlockCol <= style.start) {
					if (endBlockCol >= (style.start + style.length)) {
						// (2) block selection starts before the style range,
						// and ends after it
						style.background = colors.block;
						style.foreground = colors.normal;
						ranges.add(style);
					} else {
						// (3) block selection starts before the style range,
						// but ends before
						// the style range ends, cutting the style into 2 pieces
						// Remember the original style info
						Color oldColor = style.foreground;
						int oldLength = style.length;
						// Set the StyleRange up for the block selection portion
						style.length = endBlockCol - style.start;
						int newLength = style.length;
						style.background = colors.block;
						style.foreground = colors.normal;
						ranges.add(style);
						// Set up a new style for the original style portion
						style = new StyleRange();
						style.foreground = oldColor;
						style.start = endBlockCol;
						style.length = oldLength - newLength;
						ranges.add(style);
					}
				} else {
					if (endBlockCol >= (style.start + style.length)) {
						// (4) block selection starts after the style starts and
						// ends after
						// it ends, cutting the style into 2 pieces.
						// Remember the original style info
						int oldLength = style.length;
						Color oldColor = style.foreground;
						// Set the StyleRange up for the original style portion
						style.length = startBlockCol - style.start;
						int newLength = style.length;
						ranges.add(style);
						// Set up a new style for the block selection portion
						style = new StyleRange();
						style.background = colors.block;
						style.foreground = colors.normal;
						style.start = startBlockCol;
						style.length = oldLength - newLength;
						ranges.add(style);
					} else {
						// (5) block selection starts after the style starts and
						// ends before
						// it ends, cutting the style into 3 pieces.
						// Remember the original style info
						int oldLength = style.length;
						Color oldColor = style.foreground;
						// Set the StyleRange up for the 1st original style
						// portion
						style.length = startBlockCol - style.start;
						int newLength = style.length;
						ranges.add(style);
						// Set up a new style for the block selection portion
						style = new StyleRange();
						style.background = colors.block;
						style.foreground = colors.normal;
						style.start = startBlockCol;
						style.length = blockWidth;
						// Set up a new style for the 2nd original style portion
						ranges.add(style);
						newLength += blockWidth;
						style = new StyleRange();
						style.foreground = oldColor;
						style.start = endBlockCol;
						style.length = oldLength - newLength;
						ranges.add(style);
					}
				}
			}
		}
		// If there are any style ranges defined, send them on with the event
		// after converting the ArrayList to an Array.
		if (ranges.size() > 0) {
			styleEvent.styles = (StyleRange[]) ranges
					.toArray(new StyleRange[0]);
		}
	} // end of lineGetStyle

	// =============================================================================
	// Very simple line highlighting method, thanks to SWT's built in
	// LineBackgroundListener / Event.
	public void lineGetBackground(final LineBackgroundEvent event) {
		if (editor.getLineAtOffset(event.lineOffset) == getLine()) {
			// If the cursor is on this line, set this line's background color
			// to the
			// line highlighting color.
			event.lineBackground = colors.line;
		} else {
			// Otherwise do nothing at all.
			event.lineBackground = null;
		}
	}

	// =============================================================================
	// The verifyKey catches ALL key strokes interprets them.
	public void verifyKey(final VerifyEvent event) {
		// %%% Debug info for every key press
		/*
		 * System.out.println("KeyEvent Char: " + event.character + "(" +
		 * (int)event.character + ")" + " Code: " + event.keyCode + " Mods: " +
		 * event.stateMask + " Pos: " + editor.getCaretOffset() +
		 * " Char Count: " + editor.getCharCount());
		 */
		// Under no circumstances should any keystrokes be sent on to the
		// editor in raw form
		event.doit = false;
		// Put the three pieces of data about the keystroke into a SweditKey
		// object. This forms a key for the Map that links key presses to
		// command objects. Retreive the command and store it; not a hundred
		// percent necessary; we could call runCommand with this as a parameter.
		// Seperating it just makes it a little easier to follow and debug.
		SweditKey keystroke = new SweditKey(event.character, event.keyCode,
				event.stateMask);
		TMCommand command = (TMCommand) keys.get(keystroke);
		runCommand(command);
	} // End of verifyKey

	// =============================================================================
	public void runCommand(final TMCommand command) {
		// No point in running a command that doesn't exist
		if (command == null) {
			return;
		}
		// Some of the commands can cause Exceptions, usually an
		// ArrayOutOfBounds.
		// Hopefully, the code catches these conditions before an exception gets
		// thrown, but they are caught in case the code misses one.
		TMCommandResult result;
		int lastLine = getLine();
		try {
			// Execute the command
			result = command.exec();
		} catch (Exception e) {
			exceptionMessage("Exception on command \"" + command + "\" :\n" + e
					+ "\nPos: " + editor.getCaretOffset() + "\nChar Count: "
					+ editor.getCharCount() + "\nColumn: " + getColumn()
					+ "\nlastColumn: " + lastColumn + "\nblockHeight: "
					+ blockHeight + "\nblockWidth: " + blockWidth);
			return;
		}
		// Return if the quit flag is true; that would mean these widgets have
		// all been diposed and any operations on them will cause exceptions
		if (result.quitFlag) {
			return;
		}
		try {
			// If the selection is to be cleared
			if (result.clearSelection == true) {
				// Set the selection length to zero (don't move the caret)
				editor.setSelectionRange(editor.getCaretOffset(), 0);
				// showSelection method moves selection (in this case just the
				// caret)
				// into view by moving the scroll bars. Note that this might not
				// put
				// the scolls bar exactly where you want them.
				editor.showSelection();
			}
			// Display the current line number on the menu bar
			int line = getLine();
			linePos.setText("Line: " + (line + 1) + " / "
					+ editor.getLineCount());
			// Display the column number on the menu bar
			colPos.setText("Column: " + String.valueOf(getColumn()));
			// If the block selection is to be cleared
			if (result.clearBlock == true) {
				blockStart = 0;
				blockWidth = 0;
				// If there is in fact a block already, the screen must be
				// redrawn
				if (blockHeight > 0) {
					blockHeight = 0;
					editor.redraw();
				}
			}
			if (line != lastLine) {
				editor.redrawRange(editor.getOffsetAtLine(line),
						endColumn(line) - editor.getOffsetAtLine(line), true);
				if (lastLine < editor.getLineCount()) {
					editor.redrawRange(editor.getOffsetAtLine(lastLine),
							endColumn(lastLine)
									- editor.getOffsetAtLine(lastLine), true);
				}
			}
			// Almost done; just catch any exceptions that might have been
			// thrown. In
			// theory, the code should prevent exceptions from being thrown in
			// the
			// first place, but it pays to be carefull.
		} catch (Exception e) {
			exceptionMessage("Exception after command \"" + command + "\" :\n"
					+ e + "\nPos: " + editor.getCaretOffset()
					+ "\nChar Count: " + editor.getCharCount() + "\nColumn: "
					+ getColumn() + "\nlastColumn: " + lastColumn
					+ "\nblockHeight: " + blockHeight + "\nblockWidth: "
					+ blockWidth);
		}
	} // End of runCommand

	// =============================================================================

	// Load properties from a file
	public void loadProperties(final String propertiesFilename) {
		props = new Properties();
		try {
			// use the File class constructor to get the proper file name, after
			// making sure it's not something stupid like null or no text.
			if ((propertiesFilename != null) && !propertiesFilename.equals("")) {
				File file = new File(propertiesFilename);
				// Make sure the file exists, then create a stream and use the
				// properties object's built in load function.
				if (file.exists()) {
					FileInputStream fis = new FileInputStream(file);
					props.load(fis);
					fis.close();
					// Theses properties are stored in global variables because
					// they are
					// used several times. There may be other properties that
					// are only
					// accessed once, in which case they are paresed where they
					// are used.
					configLoaded = Boolean.valueOf(
							props.getProperty("Config_Loaded", "false"))
							.booleanValue();
					askAboutTabs = Boolean.valueOf(
							props.getProperty("Ask_About_Tabs", "true"))
							.booleanValue();
					expandTabs = Boolean.valueOf(
							props.getProperty("Expand_Tabs", "true"))
							.booleanValue();
					debugMode = Boolean.valueOf(
							props.getProperty("Debug_Mode", "false"))
							.booleanValue();
					dosFormatDefault = Boolean.valueOf(
							props.getProperty("DOS_Format", "false"))
							.booleanValue();
					dosFormat = dosFormatDefault;
				}
			}
		} catch (Exception e) {
			exceptionMessage("Exception loading Properties File \""
					+ propertiesFilename + "\" :\n" + e);
		}
	}

	// =============================================================================
	// =============================================================================
	// BEGIN SWEDIT COMMANDS SECTION
	// =============================================================================
	// =============================================================================
	// Quit the application. All buffers will be closed in order, with a
	// confirmation dialog for saving if the buffer has changed.
	final class SweditQuit extends TMCommand {
		@Override
		public TMCommandResult exec() {
			quitFlag = true;
			// A global variable is needed to track a cancel request from a
			// dialog
			cancelFlag = false;
			// If there is at least one more buffer and the user hasn't
			// requested a cancel
			while ((buffers.size() > 0) && !cancelFlag) {
				// Close the buffer
				close(true);
			}
			// If all buffers have been closed
			if (buffers.size() == 0) {
				// Dispose of the main shell. This effectively terminates the
				// program.
				shell.dispose();
			}
			// Set the cancelFlag back to false for the next dialog.
			cancelFlag = false;
			return new TMCommandResult(false, false, true);
		}

		@Override
		public String toString() {
			return "Quit";
		}
	}

	// =============================================================================
	// Save the current buffer to file
	final class SweditSave extends TMCommand {
		@Override
		public TMCommandResult exec() {
			// if the file name is known, just write the file to disk
			if (filename.indexOf("Untitled") == -1) {
				save(filename);
			} else {
				// If the file name is not known, it's got to be a Save As
				save();
			}
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Save";
		}
	}

	// =============================================================================
	// Open a FileDialog to get a file name to save the current buffer to
	final class SweditSaveAs extends TMCommand {
		@Override
		public TMCommandResult exec() {
			save();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Save As";
		}
	}

	// =============================================================================
	// Open or Load an existing file through a FileDialog
	final class SweditLoad extends TMCommand {
		@Override
		public TMCommandResult exec() {
			String path;
			File file = new File(filename);
			path = file.getAbsolutePath().substring(0,
					file.getAbsolutePath().lastIndexOf(file.getName()));

			// Open a file dialog
			FileDialog fileOpen = new FileDialog(shell, SWT.OPEN);
			fileOpen.setText("Open File");
			fileOpen.setFilterPath(path);
			fileOpen.open();
			// Check that the filename selected is valid
			if (!fileOpen.getFileName().equals("")) {
				file = new File(fileOpen.getFilterPath(), fileOpen
						.getFileName());
				if (file.exists()) {
					load(file.getPath());
				}
			}
			editor.redraw();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Load";
		}
	}

	// =============================================================================
	// Create a new (untitled) file
	final class SweditNew extends TMCommand {
		@Override
		public TMCommandResult exec() {
			newFile();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "New";
		}
	}

	// =============================================================================
	// Close the current buffer
	final class SweditClose extends TMCommand {
		@Override
		public TMCommandResult exec() {
			close(false);
			editor.redraw();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Close";
		}
	}

	// =============================================================================
	// Undo the last modification to the text
	final class SweditUndo extends TMCommand {
		@Override
		public TMCommandResult exec() {
			Command uncommand;
			boolean linked = false;

			// The while loop handles linked commands. This could have been done
			// with
			// a do while instead of the while(true) with break commands;
			// personal
			// preference, I guess.
			while (true) {
				// If the stack is empty there's no command to undo so just
				// return
				if (history.empty()) {
					return new TMCommandResult(true, true, false);
				}
				// Pop the last command off the top of the stack
				uncommand = (Command) history.pop();
				// Replace the new text with the old text
				editor.replaceTextRange(uncommand.pos(), uncommand.newtext()
						.length(), uncommand.oldtext());
				// Make sure the caret goes to the end of the old text
				editor.setCaretOffset(uncommand.pos()
						+ uncommand.oldtext().length());
				// push this same data onto the Redo stack
				lastColumn = getColumn();
				// If this command is not linked to the next, i.e. we're done
				// undoing
				if (!uncommand.link()) {
					// If the linked flag is set it means that this is the last
					// of a
					// series of linked commands. Because popping from history
					// and
					// pushing to rehistory reverses the order, this is now the
					// FIRST
					// linked command in the redo stack.
					if (linked) {
						rehistory.push(new Command(uncommand.oldtext(),
								uncommand.newtext(), uncommand.pos(), true));
					} else {
						// Normally, just push the command on to the redo stack.
						rehistory.push(uncommand);
					}
					break;
					// If this command is linked to the next
				} else {
					if (linked) {
						// in the midst of undoing a linked command, and not at
						// the end or
						// start of the link, simply push this command on to the
						// redo stack.
						rehistory.push(uncommand);
					} else {
						// If the linked flag is not set it means that this is
						// the first in a
						// series of linked commands. Because popping from
						// history and
						// pushing to rehistory reverses the order, this is now
						// the LAST linked
						// command in rehistory. So push on to the redo stack a
						// command that
						// is identical to this one, except that link is false
						rehistory.push(new Command(uncommand.oldtext(),
								uncommand.newtext(), uncommand.pos(), false));
						// Remember that a linked command has started
						linked = true;
					}
				}
			}
			editor.redraw();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Undo";
		}
	}

	// =============================================================================
	// Redo the last uno
	final class SweditRedo extends TMCommand {
		@Override
		public TMCommandResult exec() {
			Command recommand;
			boolean linked = false;

			while (true) {
				// If the stack is empty just return
				if (rehistory.empty()) {
					return new TMCommandResult(true, true, false);
				}
				// Pop the last command off the top of the stack
				recommand = (Command) rehistory.pop();
				// Replace the old text with the new text
				editor.replaceTextRange(recommand.pos(), recommand.oldtext()
						.length(), recommand.newtext());
				// Make sure the caret goes to the end of the new text
				editor.setCaretOffset(recommand.pos()
						+ recommand.newtext().length());
				lastColumn = getColumn();
				// If this command is not linked to the next, i.e. we're done
				// undoing
				if (!recommand.link()) {
					// If the linked flag is set it means that this is the last
					// of a series
					// of linked commands. Because popping from history and
					// pushing to
					// rehistory reverses the order, this is now the FIRST
					// linked command
					// in the undo stack.
					if (linked) {
						history.push(new Command(recommand.oldtext(), recommand
								.newtext(), recommand.pos(), true));
					} else {
						// Normally, just push the command on to the undo stack.
						history.push(recommand);
					}
					break;
					// If this command is linked to the next
				} else {
					if (linked) {
						// in the midst of redoing a linked command, and not at
						// the end or
						// start of the link, push this command back on to the
						// undo stack.
						history.push(recommand);
					} else {
						// If the linked flag is not set it means that this is
						// the first in
						// a series of linked commands. Because popping from
						// history and
						// pushing to rehistory reverses the order, this is now
						// the LAST
						// linked command in history. So push on to the undo
						// stack a
						// command that is identical to this one, except that
						// link is false
						history.push(new Command(recommand.oldtext(), recommand
								.newtext(), recommand.pos(), false));
						// Remember that a linked command has started
						linked = true;
					}
				}
			}
			editor.redraw();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Redo";
		}
	}

	// =============================================================================
	// Cut the current selection
	final class SweditCut extends TMCommand {
		@Override
		public TMCommandResult exec() {
			// There must be selected text
			if (editor.getSelectionCount() > 0) {
				// Get the data on the selected text
				int selectionStart = editor.getSelectionRange().x;
				Command eventUndo = new Command(editor.getSelectionText(), "",
						selectionStart, false);
				// Cut the selected text and copy it to the clipboard
				editor.invokeAction(ST.CUT);
				editor.redraw();
				lastColumn = getColumn();
				// this command can be undone
				history.push(eventUndo);
				// redo stack must be cleared
				rehistory.clear();
			}
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Cut";
		}
	}

	// =============================================================================
	// Copy to clipboard
	final class SweditCopy extends TMCommand {
		@Override
		public TMCommandResult exec() {
			// The StyledText widget already has a copy to clipboard function
			// So just invoke that action
			editor.invokeAction(ST.COPY);
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Copy";
		}
	}

	// =============================================================================
	// Paste from clipboard
	final class SweditPaste extends TMCommand {
		@Override
		public TMCommandResult exec() {
			// temp variables
			int oldLength, newLength, startPos;
			Command eventUndo;
			String tempString;
			// If text is selected, pasted text replaces it
			if (editor.getSelectionCount() > 0) {
				startPos = editor.getSelectionRange().x;
				tempString = new String(editor.getSelectionText());
			} else {
				startPos = editor.getCaretOffset();
				tempString = "";
			}
			// remember the length of the document prior to the paste
			oldLength = editor.getCharCount();
			// get the data on the selected text, including the text
			// invoke the StyledText widget's built in paste function
			editor.invokeAction(ST.PASTE);
			// Figure out how long the document now is. This is essentially the
			// new
			// length minus the old length, plus the number of any characters
			// that
			// were selected. We need this to grab the pasted text so it can be
			// stripped of control characters and stored in the undo stack
			newLength = editor.getCharCount() - oldLength + tempString.length();
			String pastedText = editor.getTextRange(startPos, newLength);
			String newText = stripControlCharacters(pastedText, expandTabs,
					editor.getTabs());
			editor.replaceTextRange(startPos, pastedText.length(), newText);
			// set up the undo information
			eventUndo = new Command(tempString, newText, startPos, false);
			// push the undo info on to the stack
			history.push(eventUndo);
			// clear the redo stack
			rehistory.clear();
			editor.redraw();
			lastColumn = getColumn();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Paste";
		}
	}

	// =============================================================================
	// Cut block-selected text
	final class SweditCutBlock extends TMCommand {
		@Override
		public TMCommandResult exec() {
			if ((blockHeight <= 0) || (blockWidth <= 0) || (blockStart < 0)) {
				return new TMCommandResult(true, false, false);
			}
			copyBlock();
			// Go through each selected line
			int startLine = editor.getLineAtOffset(blockStart);
			int startCol = blockStart - editor.getOffsetAtLine(startLine);
			int pos;
			boolean link = false;
			for (int line = 0; line < blockHeight; line++) {
				pos = editor.getOffsetAtLine(startLine + line) + startCol;
				editor.replaceTextRange(pos, blockWidth, "");
				history.push(new Command(block[line], "", pos, link));
				// If more than one cut, the commands must be linked
				link = true;
			}
			// Clear the redo stack
			rehistory.clear();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Cut Block";
		}
	}

	// =============================================================================
	final class SweditCopyBlock extends TMCommand {
		@Override
		public TMCommandResult exec() {
			copyBlock();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Copy Block";
		}
	}

	// =============================================================================
	// Paste a block-copied block of text into the document
	final class SweditPasteBlock extends TMCommand {
		@Override
		public TMCommandResult exec() {
			// On the first (or only) line, the undo command does not need to
			// link
			boolean link = false;
			// If text is selected, fail out
			if (editor.getSelectionCount() > 0) {
				return new TMCommandResult(false, true, false);
			}
			if (block == null) {
				return new TMCommandResult(false, true, false);
			}
			if (block.length <= 0) {
				return new TMCommandResult(false, true, false);
			}
			// Go through each selected line
			int startLine = getLine();
			int startCol = getColumn();
			for (int line = 0; line < block.length; line++) {
				// Position the caret at the start of the line
				if ((startLine + line) >= editor.getLineCount()) {
					editor.setCaretOffset(editor.getCharCount() - 1);
					editor.insert("\n");
				}
				editor.setCaretOffset(editor.getOffsetAtLine(startLine + line));
				setColumn(startCol, true);
				editor.insert(block[line]);
				// Remember the command in the undo stack
				history.push(new Command("", block[line], editor
						.getCaretOffset(), link));
				// If more than one insert, the commands must be linked
				link = true;
			}
			// Clear the redo stack
			rehistory.clear();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Paste Block";
		}
	}

	// =============================================================================
	// Lots of cursor movement with selection options, largely self-explanatory
	final class SweditLineDown extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.LINE_DOWN);
			setColumn(lastColumn, false);
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Line Down";
		}
	}

	final class SweditLineUp extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.LINE_UP);
			setColumn(lastColumn, false);
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Line Up";
		}
	}

	final class SweditColNext extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.COLUMN_NEXT);
			lastColumn = getColumn();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Column Next";
		}
	}

	final class SweditColPrev extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.COLUMN_PREVIOUS);
			lastColumn = getColumn();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Column Previous";
		}
	}

	final class SweditWordNext extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.WORD_NEXT);
			lastColumn = getColumn();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Word Next";
		}
	}

	final class SweditWordPrev extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.WORD_PREVIOUS);
			lastColumn = getColumn();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Word Previous";
		}
	}

	final class SweditLineStart extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.LINE_START);
			lastColumn = 1;
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Line Start";
		}
	}

	final class SweditLineEnd extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.LINE_END);
			lastColumn = -1;
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Line End";
		}
	}

	final class SweditPageDown extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.PAGE_DOWN);
			setColumn(lastColumn, false);
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Page Down";
		}
	}

	final class SweditPageUp extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.PAGE_UP);
			setColumn(lastColumn, false);
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Page Up";
		}
	}

	final class SweditWindowStart extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.WINDOW_START);
			lastColumn = 1;
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Window Start";
		}
	}

	final class SweditWindowEnd extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.WINDOW_END);
			lastColumn = getColumn();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Window End";
		}
	}

	final class SweditTextStart extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.TEXT_START);
			lastColumn = 1;
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Text Start";
		}
	}

	final class SweditTextEnd extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.TEXT_END);
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Text End";
		}
	}

	final class SweditScrollUp extends TMCommand {
		@Override
		public TMCommandResult exec() {
			int index = editor.getTopIndex() + 1;
			if (index < editor.getLineCount()) {
				editor.setTopIndex(index);
			}
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Scroll Up";
		}
	}

	final class SweditScrollDown extends TMCommand {
		@Override
		public TMCommandResult exec() {
			int index = editor.getTopIndex() - 1;
			if (index > 0) {
				editor.setTopIndex(index);
			}
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Scroll Down";
		}
	}

	final class SweditSelectLineDown extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.SELECT_LINE_DOWN);
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Select Line Down";
		}
	}

	final class SweditSelectLineUp extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.SELECT_LINE_UP);
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Select Line Up";
		}
	}

	final class SweditSelectColNext extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.SELECT_COLUMN_NEXT);
			lastColumn = getColumn();
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Select Column Next";
		}
	}

	final class SweditSelectColPrev extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.SELECT_COLUMN_PREVIOUS);
			lastColumn = getColumn();
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Select Column Previous";
		}
	}

	final class SweditSelectWordNext extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.SELECT_WORD_NEXT);
			lastColumn = getColumn();
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Select Word Next";
		}
	}

	final class SweditSelectWordPrev extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.SELECT_WORD_PREVIOUS);
			lastColumn = getColumn();
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Select Word Previous";
		}
	}

	final class SweditSelectLineStart extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.SELECT_LINE_START);
			lastColumn = 1;
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Select To Line Start";
		}
	}

	final class SweditSelectLineEnd extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.SELECT_LINE_END);
			lastColumn = -1;
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Select To Line End";
		}
	}

	final class SweditSelectPageDown extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.SELECT_PAGE_DOWN);
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Select Page Down";
		}
	}

	final class SweditSelectPageUp extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.SELECT_PAGE_UP);
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Select Page Up";
		}
	}

	final class SweditSelectWindowStart extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.SELECT_WINDOW_START);
			lastColumn = 1;
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Select To Window Start";
		}
	}

	final class SweditSelectWindowEnd extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.SELECT_WINDOW_END);
			lastColumn = -1;
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Select To Window End";
		}
	}

	final class SweditSelectTextStart extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.SELECT_TEXT_START);
			lastColumn = 1;
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Select To Text Start";
		}
	}

	final class SweditSelectTextEnd extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.invokeAction(ST.SELECT_TEXT_END);
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Select To Text End";
		}
	}

	final class SweditSelectAll extends TMCommand {
		@Override
		public TMCommandResult exec() {
			editor.setSelectionRange(0, editor.getCharCount());
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Select All";
		}
	}

	final class SweditBlockSelectLineDown extends TMCommand {
		@Override
		public TMCommandResult exec() {
			int tempCol = getColumn();
			if (getLine() < (editor.getLineCount() - 1)) {
				if (blockHeight < 1) {
					blockStart = editor.getCaretOffset();
					blockHeight = 2;
					blockWidth = 0;
				} else {
					blockHeight++;
				}
				editor.invokeAction(ST.LINE_DOWN);
				setColumn(tempCol, true);
				int blockEnd = tempCol
						- 1
						+ editor.getOffsetAtLine(editor
								.getLineAtOffset(blockStart)
								+ blockHeight - 1);
				editor.redrawRange(blockStart, blockEnd - blockStart, true);
			}
			return new TMCommandResult(true, false, false);
		}

		@Override
		public String toString() {
			return "Block Select Line Down";
		}
	}

	final class SweditBlockSelectLineUp extends TMCommand {
		@Override
		public TMCommandResult exec() {
			if (blockHeight > 1) {
				int tempCol = getColumn();
				editor.invokeAction(ST.LINE_UP);
				setColumn(tempCol, false);
				int blockEnd = tempCol
						- 1
						+ editor.getOffsetAtLine(editor
								.getLineAtOffset(blockStart)
								+ blockHeight - 1);
				editor.redrawRange(blockStart, blockEnd - blockStart, true);
				blockHeight--;
			}
			return new TMCommandResult(true, false, false);
		}

		@Override
		public String toString() {
			return "Block Select Line Up";
		}
	}

	final class SweditBlockSelectColNext extends TMCommand {
		@Override
		public TMCommandResult exec() {
			if (blockHeight < 1) {
				blockStart = editor.getCaretOffset();
				blockHeight = 1;
				blockWidth = 0;
			}
			setColumn(getColumn() + 1, true);
			blockWidth++;
			lastColumn = getColumn();
			int startLine = editor.getLineAtOffset(blockStart);
			for (int i = 0; i < (blockHeight - 1); i++) {
				int pos = editor.getOffsetAtLine(startLine + i) + lastColumn;
				if (endColumn(startLine + i) <= pos) {
					editor.replaceTextRange(endColumn(startLine + i), 0, " ");
				}
			}
			int blockEnd = lastColumn
					- 1
					+ editor.getOffsetAtLine(editor.getLineAtOffset(blockStart)
							+ blockHeight - 1);
			editor.redrawRange(blockStart, blockEnd - blockStart, true);
			return new TMCommandResult(true, false, false);
		}

		@Override
		public String toString() {
			return "Block Select Column Next";
		}
	}

	final class SweditBlockSelectColPrev extends TMCommand {
		@Override
		public TMCommandResult exec() {
			if ((blockWidth > 0) && (blockHeight > 0)) {
				int blockEnd = lastColumn
						- 1
						+ editor.getOffsetAtLine(editor
								.getLineAtOffset(blockStart)
								+ blockHeight - 1);
				editor.redrawRange(blockStart, blockEnd - blockStart, true);
				blockWidth--;
				editor.invokeAction(ST.COLUMN_PREVIOUS);
				lastColumn = getColumn();
			}
			return new TMCommandResult(true, false, false);
		}

		@Override
		public String toString() {
			return "Block Select Column Previous";
		}
	}

	// =============================================================================
	// Print debug information
	final class SweditDebug extends TMCommand {
		@Override
		public TMCommandResult exec() {
			final MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING
					| SWT.OK);
			mb.setText("Debug Info");
			int pos = editor.getCaretOffset();
			int line = editor.getLineAtOffset(pos);
			mb.setMessage("Debug Data:" + "\nConfig Loaded " + configLoaded
					+ "\nChar Count: " + editor.getCharCount() + "\nPos: "
					+ pos + "\nColumn: " + getColumn() + "\nlastColumn: "
					+ lastColumn + "\nEnd Column: " + endColumn(line)
					+ "\nLine: " + line + "\nLast Line: "
					+ editor.getLineCount() + "\nblockStart: " + blockStart
					+ "\nblockHeight: " + blockHeight + "\nblockWidth: "
					+ blockWidth);
			mb.open();
			editor.redraw();
			return new TMCommandResult(false, false, false);
		}

		@Override
		public String toString() {
			return "Debug Information";
		}
	}

	// =============================================================================
	// Launch a dialog for finding a string in the document
	final class SweditFindString extends TMCommand {
		@Override
		public TMCommandResult exec() {
			FindString fs = new FindString();
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Find";
		}
	}

	// =============================================================================
	// Find the next occurrence of the last String defined in the dialog
	final class SweditFindNext extends TMCommand {
		@Override
		public TMCommandResult exec() {
			findNext();
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Find Next";
		}
	}

	// =============================================================================
	final class SweditReplaceString extends TMCommand {
		@Override
		public TMCommandResult exec() {
			replaceString();
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Replace";
		}
	}

	// =============================================================================
	// Match a bracket, brace, or paranthesis
	final class SweditMatchBracket extends TMCommand {
		int commandInt;

		@Override
		public TMCommandResult exec() {
			String open, close;
			int startPos;
			HashMap brackets = new HashMap();
			// Put the close brackets in a set. Fix the bracket count: ([{
			HashSet closBrackets = new HashSet(Arrays.asList(new String[] {
					")", "]", "}" }));
			boolean direction = true;

			// Store each possible bracket as a key and its match as a value
			brackets.put("(", ")");
			brackets.put("[", "]");
			brackets.put("{", "}");
			brackets.put(")", "(");
			brackets.put("]", "["); // { this commented bracket fixes the count
			// screwup
			brackets.put("}", "{"); // } this commented bracket fixes the count
			// screwup

			// If the selection is only one character long, use that character
			// as what we're trying to match
			if (editor.getSelectionText().length() == 1) {
				open = editor.getSelectionText();
				startPos = editor.getSelectionRange().x;
				// Usually, look at the character after the caret.
			} else {
				open = editor.getTextRange(editor.getCaretOffset(), 1);
				startPos = editor.getCaretOffset();
			}

			// If the character is not a key in the map
			if (!brackets.containsKey(open)) {
				// If we're not at the 0 position
				if (editor.getCaretOffset() > 0) {
					// Get the character before the caret.
					open = editor.getTextRange(editor.getCaretOffset() - 1, 1);
					startPos = editor.getCaretOffset() - 1;
					// If the character is still not a key in the map, give up
					if (!brackets.containsKey(open)) {
						return new TMCommandResult(false, true, false);
					}
				} else {
					// Give up
					return new TMCommandResult(false, true, false);
				}
			}

			// The close is conveniently the value at the open key
			close = (String) brackets.get(open);
			// If this is a close bracket, the direction is backwards
			if (closBrackets.contains(open)) {
				direction = false;
			}

			// The class-scoped commandInt is used because the method is called
			// multiple times and the data must be tracked.
			commandInt = 0;
			// direction == true means forward, otherwise backward
			if (direction == true) {
				// Start after the bracket the caret is at, up to the number
				// of characters in the doc, one at a time
				for (int i = (startPos + 1); i < editor.getCharCount(); i++) {
					// If the match is found, return
					if (compareBracket(i, open, close) == true) {
						return new TMCommandResult(false, true, false);
					}
				}
			} else {
				// Start before the bracket the caret is at, down to the
				// start of the doc, one at a time backwards
				for (int i = (startPos - 1); i >= 0; i--) {
					// If the match is found, return
					if (compareBracket(i, open, close) == true) {
						return new TMCommandResult(false, true, false);
					}
				}
			}
			lastColumn = getColumn();
			return new TMCommandResult(false, true, false);
		}

		// Check if this is the matching bracket, bearing in mind that there
		// could
		// be other open/close pairs inside these brackets
		public boolean compareBracket(final int pos, final String open,
				final String close) {
			// If the character is the same as where we started
			// increment the number of matches we're looking for
			if (editor.getTextRange(pos, 1).equals(open)) {
				commandInt++;
			}
			// If the character is the match
			if (editor.getTextRange(pos, 1).equals(close)) {
				// If we need more than one match
				if (commandInt > 0) {
					// Decrement the number needed
					commandInt--;
					// If this is that matching bracket
				} else {
					// Select the character, scroll it into view and return
					// found (true)
					editor.setCaretOffset(pos);
					editor.setSelectionRange(pos, 1);
					editor.showSelection();
					return true;
				}
			}
			// The match was not found
			return false;
		}

		@Override
		public String toString() {
			return "Match Bracket";
		}
	}

	// =============================================================================
	final class SweditGoToLine extends TMCommand {
		@Override
		public TMCommandResult exec() {
			GoToDialog gtd = new GoToDialog();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Go To Line Number";
		}
	}

	// =============================================================================
	// Switch to the next buffer
	final class SweditNextBuffer extends TMCommand {
		@Override
		public TMCommandResult exec() {
			Buffer tempBuffer;
			// Make sure there is another buffer to switch to
			if (buffers.size() > 1) {
				// Store the current information into the vector
				buffers.set(currentBuffer, new Buffer(editor.getText(),
						filename, editor.getCaretOffset(),
						editor.getTopIndex(), changed, (Stack) history.clone(),
						expandTabs, dosFormat));
				// Increment the current buffer index
				currentBuffer++;
				// Wrap the index back to 0 if it exceeds the number of buffers
				if (currentBuffer >= buffers.size()) {
					currentBuffer = 0;
				}
				// Get the next buffer out of memory
				tempBuffer = (Buffer) buffers.get(currentBuffer);
				// Set the editor data and global vars to the stored data
				editor.setText(tempBuffer.text());
				editor.setCaretOffset(tempBuffer.pos());
				editor.setTopIndex(tempBuffer.topIndex());
				lastColumn = getColumn();
				changed = tempBuffer.changed();
				history = tempBuffer.history();
				filename = tempBuffer.fileName();
				expandTabs = tempBuffer.expandTabs();
				syndex = Syntax.getIndex(filename);
				// Clear the redo stack
				rehistory.clear();
				// update the title bar with the filename
				shell.setText("SWediT - " + filename);
				shell.update();
			}
			editor.redraw();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Next Buffer";
		}
	}

	// =============================================================================
	// Delete the line cursor is on
	final class SweditDeleteLine extends TMCommand {
		@Override
		public TMCommandResult exec() {
			int startCol, endCol, line;
			if (editor.getCharCount() == 0) {
				return new TMCommandResult(false, false, false);
			}
			// Find out what line the caret is on
			line = getLine();
			startCol = editor.getOffsetAtLine(line);
			if (line == (editor.getLineCount() - 1)) {
				// On the last line, the end column is the last character
				endCol = editor.getCharCount();
				startCol--;
				if (startCol < 0) {
					startCol = 0;
				}
			} else {
				endCol = editor.getOffsetAtLine(line + 1);
			}
			Command eventUndo = new Command(editor
					.getText(startCol, endCol - 1), "", startCol, false);
			// Delete the selected text
			editor.replaceTextRange(startCol, endCol - startCol, "");
			editor.setCaretOffset(startCol);
			lastColumn = 1;
			history.push(eventUndo);
			rehistory.clear();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Delete Line";
		}
	}

	// =============================================================================
	// Delete the previous character, i.e. Backspace
	final class SweditDeletePrev extends TMCommand {
		@Override
		public TMCommandResult exec() {
			// When text is already selected, delete and backspace simply
			// delete the selected text
			if (editor.getSelectionCount() > 0) {
				// Get the data on the selected text
				int selectionStart = editor.getSelectionRange().x;
				int selectionLength = editor.getSelectionRange().y;
				Command eventUndo = new Command(editor.getSelectionText(), "",
						selectionStart, false);
				history.push(eventUndo);
				// Delete the selected text
				editor.replaceTextRange(selectionStart, selectionLength, "");
			} else {
				// Check the current position
				int pos = editor.getCaretOffset();
				// If the caret isn't at the start of the document
				if (pos > 0) {
					// Save the previous character to the undo history
					Command eventUndo = new Command(editor.getTextRange(
							pos - 1, 1), "", pos - 1, false);
					history.push(eventUndo);
					// invoke the StyledText widget's built in Delete Previous
					// command
					editor.invokeAction(ST.DELETE_PREVIOUS);
				}
			}
			lastColumn = getColumn();
			rehistory.clear();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Backspace";
		}
	}

	// =============================================================================
	// Delete the next character
	final class SweditDeleteNext extends TMCommand {
		@Override
		public TMCommandResult exec() {
			// When text is already selected, delete and backspace simply
			// delete the selected text
			if (editor.getSelectionCount() > 0) {
				// Get the data on the selected text
				int selectionStart = editor.getSelectionRange().x;
				int selectionLength = editor.getSelectionRange().y;
				Command eventUndo = new Command(editor.getSelectionText(), "",
						selectionStart, false);
				history.push(eventUndo);
				// Delete the selected text
				editor.replaceTextRange(selectionStart, selectionLength, "");
			} else {
				// Check the current position
				int pos = editor.getCaretOffset();
				// If the caret isn't at the end of the document
				if (pos < editor.getText().length()) {
					// Save the next character to the undo history
					Command eventUndo = new Command(editor.getText().substring(
							pos, pos + 1), "", pos, false);
					history.push(eventUndo);
					// invoke the StyledText widget's built in Delete Next
					// command
					editor.invokeAction(ST.DELETE_NEXT);
				}
			}
			lastColumn = getColumn();
			rehistory.clear();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Delete";
		}
	}

	// =============================================================================
	// New line, Enter, Carriage Return, Line Feed, whatever you want to call
	// it. Bottom line is this text editor hates control characters, and \n
	// (newline) and \t (tab) are the only ones allowed in the document
	// So all new line requests are handled here, and this inserts a \n, not
	// the Windows style \r\n.
	final class SweditNewLine extends TMCommand {
		@Override
		public TMCommandResult exec() {
			Command eventUndo;
			// If there is selected text, it will be replaced with the new line
			if (editor.getSelectionCount() > 0) {
				// Get the data on the selected text
				int selectionStart = editor.getSelectionRange().x;
				int selectionLength = editor.getSelectionRange().y;
				// Set up the undo command
				eventUndo = new Command(editor.getSelectionText(), String
						.valueOf('\n'), selectionStart, false);
				// Replace the selected text with the new text.
				editor.replaceTextRange(selectionStart, selectionLength,
						eventUndo.newtext());
			} else {
				// Easy enough; just remember a newline at this pos, then insert
				// it.
				// But wait, there's more! The new line must be indented.
				int currCol, startCol, endCol, line;
				currCol = getColumn();
				// Use a string buffer because characters will be appended
				StringBuffer spaces = new StringBuffer(String.valueOf('\n'));
				line = getLine();
				startCol = editor.getOffsetAtLine(line);
				endCol = endColumn(line);
				int loopCount = 0;
				// Go through the whole line
				for (int i = startCol; i < endCol; i++) {
					// Get each character
					char tempChar = editor.getText().charAt(i);
					// If the character is a space or a tab
					if ((tempChar == ' ') || (tempChar == '\t')) {
						// Increment the count
						loopCount++;
						// Don't pass up the current column. e.g. if the user
						// hits enter on
						// column 2 of a line that starts with 10 spaces, only
						// add 2 spaces.
						if (loopCount < currCol) {
							spaces.append(String.valueOf(tempChar));
						} else {
							break;
						}
					} else {
						break;
					}
				}
				eventUndo = new Command("", spaces.toString(), editor
						.getCaretOffset(), false);
				editor.insert(eventUndo.newtext());
				// But insert is funny, because it doesn't move the caret, do it
				// manually.
				editor.setCaretOffset(eventUndo.pos()
						+ eventUndo.newtext().length());
			}
			lastColumn = getColumn();
			history.push(eventUndo);
			rehistory.clear();
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return "Enter";
		}
	}

	// =============================================================================
	// Indent is slightly different from tab
	final class SweditIndent extends TMCommand {
		@Override
		public TMCommandResult exec() {
			indent();
			lastColumn = getColumn();
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Indent";
		}
	}

	// =============================================================================
	// "Undent" (remove spaces and/or tabs from, i.e. the opposite of indent)
	// the selected line or lines. This is considerably harder than indent.
	final class SweditUndent extends TMCommand {
		@Override
		public TMCommandResult exec() {
			// Create a character array
			char spacesTemp[] = new char[editor.getTabs()];
			// Fill the array with spaces
			Arrays.fill(spacesTemp, ' ');
			// Create a String from the character array
			String spaces = new String(spacesTemp);

			// On the first (or only) undent, the undo command does not need to
			// link
			boolean link = false;
			int selectionStart;
			int selectionLength;
			int startLine;
			int endLine;
			// If text is selected
			if (editor.getSelectionCount() > 0) {
				// Get the selection data
				selectionStart = editor.getSelectionRange().x;
				selectionLength = editor.getSelectionRange().y;
				startLine = editor.getLineAtOffset(selectionStart);
				endLine = editor.getLineAtOffset(selectionStart
						+ selectionLength - 1);
			} else {
				// Indent the line the cursor is on
				selectionStart = editor.getCaretOffset();
				selectionLength = 0;
				startLine = editor.getLineAtOffset(selectionStart);
				endLine = startLine;
			}

			// Go through each selected line
			for (int line = startLine; line <= endLine; line++) {
				// Use the start of the line as the position
				int pos = editor.getOffsetAtLine(line);
				// If the line starts with a tab
				if (editor.getText().charAt(pos) == '\t') {
					// Remove the tab
					editor.replaceTextRange(pos, 1, "");
					history.push(new Command("\t", "", pos, link));
					// One command has executed; from here on out they should
					// link
					link = true;
					if (selectionStart > pos) {
						// If the removed tab was before the start, move the
						// start back 1
						selectionStart--;
					} else if ((selectionStart + selectionLength - 1) >= pos) {
						// If the removed tab was inside the selection move the
						// start back 1
						selectionLength--;
					}
					// If the line starts with a space
				} else if (editor.getText().charAt(pos) == ' ') {
					// Loop through removing spaces, up to the tab width
					for (int i = 0; i < spaces.length(); i++) {
						// if the line still starts with a space
						if (editor.getText().charAt(pos) == ' ') {
							// Remove the space
							editor.replaceTextRange(pos, 1, "");
							history.push(new Command(" ", "", pos, link));
							// One command has executed; from here on out they
							// should link
							link = true;
							if (selectionStart > pos) {
								// If the removed tab was before the start, move
								// the start back 1
								selectionStart--;
							} else if ((selectionStart + selectionLength - 1) >= pos) {
								// If the removed tab was inside the selection
								// move the start back 1
								selectionLength--;
							}
						} else {
							// Break if the line does not begin with a space
							break;
						}
					}
				}
			}
			// Make sure the selection is right
			editor.setSelectionRange(selectionStart, selectionLength);
			editor.showSelection();
			// Clear the redo stack
			rehistory.clear();
			lastColumn = getColumn();
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Undent";
		}
	}

	// =============================================================================
	// Tab is handled a variety of different ways
	final class SweditTab extends TMCommand {
		@Override
		public TMCommandResult exec() {
			String spaces;
			if (expandTabs) {
				char spacesTemp[] = new char[editor.getTabs()];
				Arrays.fill(spacesTemp, ' ');
				spaces = new String(spacesTemp);
			} else {
				spaces = String.valueOf('\t');
			}
			if (editor.getSelectionCount() > 0) {
				indent();
				lastColumn = getColumn();
			} else {
				Command eventUndo = new Command("", spaces, editor
						.getCaretOffset(), false);
				editor.insert(eventUndo.newtext());
				editor.setCaretOffset(eventUndo.pos()
						+ eventUndo.newtext().length());
				lastColumn = getColumn();
				history.push(eventUndo);
				rehistory.clear();
			}
			return new TMCommandResult(false, true, false);
		}

		@Override
		public String toString() {
			return "Tab";
		}
	}

	// =============================================================================
	// Single character commands are simply inserting that character into the
	// text
	final class SweditInsertChar extends TMCommand {
		String command;

		SweditInsertChar(final char charToInsert) {
			this.command = new String(String.valueOf(charToInsert));
		}

		@Override
		public TMCommandResult exec() {
			Command eventUndo;
			// Make sure that character is in a valid range. Note that NO
			// control
			// characters are allowed, and NO "ANSI" characters (like accented
			// vowels or line art) are allowed. This may render this text editor
			// completely useless to some, but is critical for me because junk
			// characters in C programs have caused major headaches; this is one
			// of
			// the main reasons this text editor was written.
			if ((command.charAt(0) > 31) && (command.charAt(0) < 127)) {
				// If there is selected text, it is replaced with the new
				// character
				if (editor.getSelectionCount() > 0) {
					// Get the data on the selected text
					int selectionStart = editor.getSelectionRange().x;
					int selectionLength = editor.getSelectionRange().y;
					eventUndo = new Command(editor.getSelectionText(), command,
							selectionStart, false);
					// Replace the selected text with the new character
					editor.replaceTextRange(selectionStart, selectionLength,
							eventUndo.newtext());
				} else {
					// Easy enough; just remember the new character at this pos,
					// then insert it.
					eventUndo = new Command("", command, editor
							.getCaretOffset(), false);
					editor.insert(eventUndo.newtext());
					// But insert is funny, because it doesn't move the caret,
					// do it manually.
					editor.setCaretOffset(eventUndo.pos() + 1);
				}
				lastColumn = getColumn();
				history.push(eventUndo);
				rehistory.clear();
			} else {
				// If this was single character command outside the valid range
				// it is simply ignored, but there is no reason to clear the
				// selected text.
				return new TMCommandResult(false, true, false);
			}
			return new TMCommandResult(true, true, false);
		}

		@Override
		public String toString() {
			return this.command;
		}
	}

	// =============================================================================
	final class PrintKeyMap extends TMCommand {
		public Shell dialog;

		@Override
		public TMCommandResult exec() {
			if (dialog != null && !dialog.isDisposed()) {
				dialog.forceActive();
			} else {
				dialog = new Shell(display);
				Point size = shell.getSize();
				dialog.setText("Key Mappings");
				dialog.setLayout(new FillLayout());
				dialog.setSize(size.x, size.y);
				dialog.setMaximized(shell.getMaximized());
				TMCommand c = new SweditInsertChar('a');
				final Map sortByKeys = new TreeMap();
				final Set sortByValues = new TreeSet(new Comparator() {
					public int compare(final Object o1, final Object o2) {
						String[] sa1 = (String[]) o1;
						String[] sa2 = (String[]) o2;
						int c1 = sa1[1].compareTo(sa2[1]);
						if (c1 == 0) {
							return sa1[0].compareTo(sa2[0]);
						} else {
							return c1;
						}
					}
				});
				final Table table = new Table(dialog, SWT.SINGLE);
				TableColumn col1 = new TableColumn(table, SWT.LEFT);
				col1.setText("Key");
				TableColumn col2 = new TableColumn(table, SWT.LEFT);
				col2.setText("Action");
				Iterator mapI = keys.keySet().iterator();
				while (mapI.hasNext()) {
					Object k = mapI.next();
					Object v = keys.get(k);
					if (!(v.getClass().isInstance(c))) {
						String ks = k.toString();
						String vs = v.toString();
						sortByKeys.put(ks, vs);
						sortByValues.add(new String[] { ks, vs });
					}
				}
				mapI = sortByKeys.keySet().iterator();
				while (mapI.hasNext()) {
					Object k = mapI.next();
					TableItem item = new TableItem(table, 0);
					item.setText(new String[] { k.toString(),
							(sortByKeys.get(k)).toString() });
				}
				col1.pack();
				col2.pack();
				col1.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent event) {
						Iterator mapI = sortByKeys.keySet().iterator();
						for (int i = 0; mapI.hasNext()
								&& (i < table.getItemCount()); i++) {
							Object k = mapI.next();
							table.getItem(i).setText(
									new String[] { k.toString(),
											(sortByKeys.get(k)).toString() });
						}
					}
				});
				col2.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent event) {
						Iterator mapI = sortByValues.iterator();
						for (int i = 0; mapI.hasNext()
								&& (i < table.getItemCount()); i++) {
							table.getItem(i).setText((String[]) (mapI.next()));
						}
					}
				});
				table.setHeaderVisible(true);
				table.setLinesVisible(true);
				dialog.open();
			}
			return new TMCommandResult(false, false, false);
		}

		@Override
		public String toString() {
			return "Show Key Map";
		}

	}

	// =============================================================================
	final class SweditAbout extends TMCommand {
		@Override
		public TMCommandResult exec() {
			String message = new String("SWediT" + "\nVersion 1.02"
					+ "\n\nA Minimalist Text Editor" + "\nIn Java Using SWT"
					+ "\n\nCopyright (c) 2002, 2003, 2004"
					+ "\n\nBy James D. Ingraham" + "\n\nswedit.sourceforge.net");
			final MessageBox mb = new MessageBox(shell, SWT.OK
					| SWT.ICON_INFORMATION);
			mb.setText("About SWediT");
			mb.setMessage(message);
			mb.open();
			return new TMCommandResult(false, false, false);
		}

		@Override
		public String toString() {
			return "About SWediT";
		}
	}

	// =============================================================================
	final class SweditLicense extends TMCommand {
		Shell dialog;
		StyledText licenseTextBox;

		@Override
		public TMCommandResult exec() {
			if (dialog != null && !dialog.isDisposed()) {
				dialog.forceActive();
			} else {
				String message = new String(
				// SWEDIT LICENSE
						"SWediT\n\n"
								+

								"Copyright (c) 2002, 2003, 2004 by James D. Ingraham. All Rights Reserved.\n\n"
								+

								"Permission is hereby granted, free of charge, to any person obtaining a copy "
								+ "of this software and associated documentation files (hereafter refered to as "
								+ "\"SWediT\"), to deal in SWediT without restriction, including without "
								+ "limitation the rights to use, copy, modify, merge, publish, distribute, "
								+ "sublicense, and/or sell copies of SWediT, and to permit persons to whom "
								+ "SWediT is furnished to do so, subject to the following conditions:\n\n"
								+

								"(1)The above copyright notice and this permission notice shall be included in "
								+ "all copies or substantial portions of SWediT.\n\n"
								+

								"(2)Redistributions in binary form must reproduce the above copyright notice, "
								+ "this list of conditions and the following disclaimers in the documentation "
								+ "and/or other materials provided with the distribution.\n\n"
								+

								"(3)Any distributor of SWediT in binary form must also make available the "
								+ "soure code for no more than the cost of distribution plus a nominal fee.\n\n"
								+

								"(4)The origin of SWediT must not be misrepresented; Licensee must not claim "
								+ "that he wrote SWediT.\n\n"
								+

								"(5)Altered source versions must be plainly marked as such, and must not be "
								+ "misrepresented as being SWediT.\n\n"
								+

								"(6)The name of the author(s) may not be used to endorse or promote products "
								+ "derived from SWediT without specific prior written permission.\n\n"
								+

								"(7)SWediT is not designed or intended for use in the design, construction, "
								+ "operation or maintenance of systems or equipment where malfunctions can cause "
								+ "injury, death, or environmental damage.  This includes but is not limited to: "
								+ "aircraft / air traffic systems or communications, nuclear facilities, medical "
								+ "equipment, etc. Licensee represents and warrants that he will not use or "
								+ "redistribute SWediT for such purposes.\n\n"
								+

								"SWediT IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR "
								+ "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, "
								+ "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL "
								+ "THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR "
								+ "OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, "
								+ "ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR "
								+ "OTHER DEALINGS IN THE SOFTWARE.\n");
				// END OF LICENSE
				Point size = shell.getSize();
				dialog = new Shell(display);
				dialog.setText("SWediT License");
				dialog.setLayout(new FillLayout());
				dialog.setSize(size.x, size.y);
				dialog.setMaximized(shell.getMaximized());
				// create the styled text widget with scroll bars
				licenseTextBox = new StyledText(dialog, SWT.V_SCROLL);
				licenseTextBox.setWordWrap(true);
				// The font must be a fixed width font, of reasonable size
				licenseTextBox.setFont(new Font(display, "Courier", 12,
						SWT.NORMAL));
				licenseTextBox.setText(message);
				licenseTextBox.setEditable(false);
				Color background = new Color(display, 220, 220, 220);
				licenseTextBox.setBackground(background);
				licenseTextBox.setStyleRange(new StyleRange(0, 6, new Color(
						display, 0, 0, 0), background, SWT.BOLD));
				licenseTextBox.addVerifyKeyListener(new quitOnEscapeKey());
				dialog.open();
			}
			return new TMCommandResult(false, false, false);
		}

		@Override
		public String toString() {
			return "SWediT License";
		}

		private class quitOnEscapeKey implements VerifyKeyListener {
			// The verifyKey catches key strokes interprets them. We only care
			// about
			// the escape key.
			public void verifyKey(final VerifyEvent event) {
				if ((event.character == ctrl('a'))
						&& (event.stateMask == SWT.CTRL)) {
					licenseTextBox.setSelectionRange(0, licenseTextBox
							.getCharCount());
				} else if (event.character == SWT.ESC) {
					// Dispose of dialog shell. This terminates the dialog.
					dialog.dispose();
					event.doit = false;
				}
			} // End of verifyKey
		}
	}

	// =============================================================================
	final class SweditCancel extends TMCommand {
		@Override
		public TMCommandResult exec() {
			// Set the selection length to zero (don't move the caret)
			editor.setSelectionRange(editor.getCaretOffset(), 0);
			// put the cursor in view
			editor.showSelection();
			// Set current line number on the menu bar;
			int line = getLine();
			linePos.setText("Line: " + (line + 1) + " / "
					+ editor.getLineCount());
			// Set column number on the menu bar, which requires our own method
			colPos.setText("Column: " + String.valueOf(getColumn()));
			// Clear the block selection
			blockStart = 0;
			blockWidth = 0;
			blockHeight = 0;
			// Redraw the screen
			editor.redraw();
			return new TMCommandResult(false, false, false);
		}

		@Override
		public String toString() {
			return "Cancel";
		}
	}

	// =============================================================================
	// =============================================================================
	// END SWEDIT COMMANDS SECTION
	// =============================================================================
	// =============================================================================

	// Figure out the end column of a given line
	public int endColumn(final int line) {
		if (line == (editor.getLineCount() - 1)) {
			// On the last line, the end column is the last character
			return editor.getCharCount();
		} else {
			// Normally, the end column is simply the start of the next line
			return editor.getOffsetAtLine(line + 1) - 1;
		}
	} // end of endColumn

	// =============================================================================

	// Figure out what column of the line the caret is at
	public int getColumn() {
		int pos, line;
		// For readability, get the caret position into a temp variable
		pos = editor.getCaretOffset();
		// For readability, get the line number into a temp variable
		line = editor.getLineAtOffset(pos);
		// From the caret position, subtract the positon of the start of the
		// line
		// then add 1 to 1 index instead of 0 index
		return pos - editor.getOffsetAtLine(line) + 1;
	} // end of getColumn

	// =============================================================================
	// Move the caret to the proper column
	public void setColumn(final int newColPos, final boolean pad) {
		int startCol, endCol, line, currPos;
		// Get the current position of the cursor
		currPos = editor.getCaretOffset();
		// Find out what line the caret is on
		line = editor.getLineAtOffset(currPos);
		startCol = editor.getOffsetAtLine(line);
		currPos -= startCol;
		if (currPos == newColPos) {
			return;
		}
		endCol = endColumn(line);
		if (newColPos == -1) {
			// -1 means the user has hit "End" so always go to the end of the
			// line
			editor.setCaretOffset(endCol);
		} else if (newColPos > (endCol - startCol)) {
			// If the column position is past the end of the line, go to the end
			// of the line
			editor.setCaretOffset(endCol);
			if (pad) {
				char[] spaces = new char[newColPos - endCol + startCol - 1];
				Arrays.fill(spaces, ' ');
				editor.insert(new String(spaces));
				editor.setCaretOffset(endCol + spaces.length);
			}
		} else {
			// Normally, just set the column to the desired position
			editor.setCaretOffset(startCol + newColPos - 1);
		}
	} // end of setColumn

	// =============================================================================

	// Figure out what line the caret is on
	public int getLine() {
		int pos, line;
		// For readability, get the caret position into a temp variable
		pos = editor.getCaretOffset();
		// For readability, get the line number into a temp variable
		line = editor.getLineAtOffset(pos);
		return line;
	} // end of getLine

	// =============================================================================
	// Launch a dialog to go do a line number
	final class GoToDialog implements SelectionListener, KeyListener {
		Shell dialog;
		RowLayout dialogLayout;
		Button ok, cancel;
		Text input;
		int lineNum;

		GoToDialog() {
			dialog = new Shell(shell, SWT.BORDER | SWT.TITLE
					| SWT.APPLICATION_MODAL);
			dialogLayout = new RowLayout();
			dialogLayout.wrap = true;
			dialogLayout.justify = true;
			dialog.setLayout(dialogLayout);
			dialog.setSize(220, 120);
			dialog.setText("Go To Line");
			input = new Text(dialog, SWT.SINGLE);
			input.setLayoutData(new RowData(200, 40));
			input.setFont(new Font(display, "Courier", 12, SWT.NORMAL));
			ok = new Button(dialog, SWT.PUSH);
			ok.setText("Ok");
			ok.setLayoutData(new RowData(100, 40));
			cancel = new Button(dialog, SWT.PUSH);
			cancel.setText("Cancel");
			cancel.setLayoutData(new RowData(100, 40));
			input.addKeyListener(this);
			ok.addSelectionListener(this);
			cancel.addSelectionListener(this);
			dialog.open();
			while (!dialog.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		}

		public void keyPressed(final KeyEvent kpe) {
		}

		public void keyReleased(final KeyEvent kre) {
			if (kre.character == '\r') {
				try {
					// Get the integer that was typed in to the input Text
					lineNum = Integer.parseInt(input.getText());
					// If that line number is valid
					if ((lineNum > 0) && (lineNum <= editor.getLineCount())) {
						// Check each posible offset in the doc, up to the
						// number of chars
						editor.setCaretOffset(editor
								.getOffsetAtLine(lineNum - 1));
						lastColumn = 1;
					}
					// This catches non-integer values; very necessary
				} catch (Exception e) {
				}
				dialog.dispose();
			}
		}

		public void widgetDefaultSelected(final SelectionEvent wdse) {
		}

		public void widgetSelected(final SelectionEvent wse) {
			if (wse.widget.equals(ok)) {
				try {
					// Get the integer that was typed in to the input Text
					lineNum = Integer.parseInt(input.getText());
					// If that line number is valid
					if ((lineNum > 0) && (lineNum <= editor.getLineCount())) {
						// Check each posible offset in the doc, up to the
						// number of chars
						editor.setCaretOffset(editor
								.getOffsetAtLine(lineNum - 1));
						lastColumn = 1;
					}
					// This catches non-integer values; very necessary
				} catch (Exception e) {
				}
			}
			dialog.dispose();
		}
	}

	// =============================================================================
	// Dialog for searching / finding a string in the document
	// %%% Several features are not yet implemented. Search backwards, match
	// case,
	// match whole words, search only in selection, regular expressions
	final class FindString implements SelectionListener, KeyListener {
		Shell dialog;
		RowLayout dialogLayout;
		Button ok, cancel;
		Text input;

		FindString() {
			// Create a new shell as a dialog box
			dialog = new Shell(shell, SWT.BORDER | SWT.TITLE
					| SWT.APPLICATION_MODAL);
			// Use a row layout
			dialogLayout = new RowLayout();
			// Wrap the rows, but since this thing can't be resized
			// the rows stay in place
			dialogLayout.wrap = true;
			// %%% Justify doesn't seem to work to well
			dialogLayout.justify = true;
			// Set the dialog's layout to the layout just set up
			dialog.setLayout(dialogLayout);
			// Set the size
			// %%% Sizing should be a little smarter
			dialog.setSize(300, 120);
			// Set the title of the dialog so the user will know what's going on
			dialog.setText("Find Text");
			// Create a Text field for the user to input the search string
			input = new Text(dialog, SWT.SINGLE);
			// Set the size of the Text field
			// %%% Sizing should be a little smarter
			input.setLayoutData(new RowData(280, 40));
			// Make sure the text has the same font as the main doc
			// %%% This should probably be a global var
			input.setFont(new Font(display, "Courier", 12, SWT.NORMAL));
			// Set the text to the last search string
			input.setText(searchString);
			input.setSelection(0, searchString.length());
			// Create an ok button
			ok = new Button(dialog, SWT.PUSH);
			// Set the text on the button
			ok.setText("Ok");
			// Set the size of the button
			// %%% Sizing should be a little smarter
			ok.setLayoutData(new RowData(100, 40));
			// Create a cancel button
			cancel = new Button(dialog, SWT.PUSH);
			// Set the label on the button
			cancel.setText("Cancel");
			// Set the size of the button
			// %%% Sizing should be a little smarter
			cancel.setLayoutData(new RowData(100, 40));
			// Define what to do when a user selects something
			input.addKeyListener(this);
			ok.addSelectionListener(this);
			cancel.addSelectionListener(this);
			// Open the the dialog
			dialog.open();
			// Wait for the dialog to die
			while (!dialog.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} // End of FindString constructor

		public void keyPressed(final KeyEvent kpe) {
		}

		public void keyReleased(final KeyEvent kre) {
			if (kre.character == '\r') {
				// Grab the search text and put in the global search string
				searchString = new String(input.getText());
				// Call the findNext routine, which does the real work
				findNext();
				// close the dialog
				dialog.dispose();
			}
		}

		public void widgetDefaultSelected(final SelectionEvent wdse) {
		}

		public void widgetSelected(final SelectionEvent wse) {
			if (wse.widget.equals(ok)) {
				// Grab the search text and put in the global search string
				searchString = new String(input.getText());
				// Call the findNext routine, which does the real work
				findNext();
			}
			dialog.dispose();
		}
	} // End of class FindString

	// =============================================================================
	// Find a string in the text
	// %%% Several features are not yet implemented. Search backwards, match
	// case,
	// match whole words, search only in selection, regular expressions
	public boolean findNext() {
		// index returned by the String.indexOf method
		int index;
		// Make sure there is a search string
		if (searchString.length() > 0) {
			// Get the index of the searchString in the String returned by
			// editor.getText, starting at the current caret position
			index = editor.getText().indexOf(searchString,
					editor.getCaretOffset());
			// if the index is non-negative, the string has been found
			if (index >= 0) {
				// set the offset to the index that was found
				editor.setCaretOffset(index);
				// Select the whole search string
				editor.setSelectionRange(index, searchString.length());
				// scroll the window so the selection is visible
				editor.showSelection();
				lastColumn = getColumn();
				editor.redraw();
				return true;
			}
		}
		return false;
	} // end of findNext

	// =============================================================================
	// Dialog for replacing a string in the document
	// %%% Several features are not yet implemented. Search backwards, match
	// case,
	// match whole words, search only in selection, regular expressions, replace
	// all
	public void replaceString() {
		// Create a new shell as a dialog box
		final Shell dialog = new Shell(shell, SWT.BORDER | SWT.TITLE
				| SWT.APPLICATION_MODAL);
		// Use a row layout
		final RowLayout dialogLayout = new RowLayout();
		// Wrap the rows, but since this thing can't be resized
		// the rows stay in place
		dialogLayout.wrap = true;
		// %%% Justify doesn't seem to work to well
		dialogLayout.justify = true;
		// Set the dialog's layout to the layout just set up
		dialog.setLayout(dialogLayout);
		// Set the size
		// %%% Sizing should be a little smarter
		dialog.setSize(320, 150);
		// Set the title of the dialog so the user will know what's going on
		dialog.setText("Replace Text");
		// Create a Text field for the user to input the search string
		final Text search = new Text(dialog, SWT.SINGLE);
		// Set the size of the Text field
		// %%% Sizing should be a little smarter
		search.setLayoutData(new RowData(300, 40));
		// Make sure the text has the same font as the main doc
		// %%% This should probably be a global var
		search.setFont(new Font(display, "Courier", 12, SWT.NORMAL));
		// Set the text to the last search string
		search.setText(searchString);
		// Create a Text field for the user to input the replace string
		final Text replace = new Text(dialog, SWT.SINGLE);
		// Set the size of the Text field
		// %%% Sizing should be a little smarter
		replace.setLayoutData(new RowData(300, 40));
		// Make sure the text has the same font as the main doc
		// %%% This should probably be a global var
		replace.setFont(new Font(display, "Courier", 12, SWT.NORMAL));
		// Set the text to the last search string
		replace.setText(searchString);
		// Create an next button
		final Button next = new Button(dialog, SWT.PUSH);
		// Set the text on the button
		next.setText("Next");
		// Set the size of the button
		// %%% Sizing should be a little smarter
		next.setLayoutData(new RowData(100, 40));
		// Create an next button
		final Button confirmReplace = new Button(dialog, SWT.PUSH);
		// Set the text on the button
		confirmReplace.setText("Replace");
		// Set the size of the button
		// %%% Sizing should be a little smarter
		confirmReplace.setLayoutData(new RowData(100, 40));
		// Create a cancel button
		Button cancel = new Button(dialog, SWT.PUSH);
		// Set the label on the button
		cancel.setText("Cancel");
		// Set the size of the button
		// %%% Sizing should be a little smarter
		cancel.setLayoutData(new RowData(100, 40));
		// Define what to do when a user clicks ok
		next.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				// Grab the search text and put in the global search string
				searchString = new String(search.getText());
				// Call the findNext routine, which does the real work
				if (!findNext()) {
					// close the dialog
					dialog.dispose();
				}
			}
		});
		confirmReplace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				if (editor.getSelectionCount() > 0) {
					int selectionStart, selectionLength;
					// Get the data on the selected text
					selectionStart = editor.getSelectionRange().x;
					selectionLength = editor.getSelectionRange().y;
					Command eventUndo = new Command(editor.getSelectionText(),
							replace.getText(), selectionStart, false);
					// Replace the selected text with the new character
					editor.replaceTextRange(selectionStart, selectionLength,
							eventUndo.newtext());
					history.push(eventUndo);
					rehistory.clear();
				}
				if (!findNext()) {
					// close the dialog
					dialog.dispose();
				}
			}
		});
		// Define what to do when a user clicks ok
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				// close the dialog
				dialog.dispose();
			}
		});
		dialog.open();
		// Wait for the dialog to die
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	} // End of replaceString

	// =============================================================================
	// Indent the selected line or lines
	public void indent() {
		String spaces;
		if (expandTabs) {
			// Create a character array
			char spacesTemp[] = new char[editor.getTabs()];
			// Fill the array with spaces
			Arrays.fill(spacesTemp, ' ');
			// Create a String from the character array
			spaces = new String(spacesTemp);
		} else {
			// The string is just a tab
			spaces = String.valueOf('\t');
		}
		// On the first (or only) indent, the undo command does not need to link
		boolean link = false;
		// If text is selected
		if (editor.getSelectionCount() > 0) {
			// Get the selection data
			int selectionStart = editor.getSelectionRange().x;
			int selectionLength = editor.getSelectionRange().y;
			int startLine = editor.getLineAtOffset(selectionStart);
			int endLine = editor.getLineAtOffset(selectionStart
					+ selectionLength);
			// Move the selection by the number of characters being inserted on
			// one line
			selectionStart += spaces.length();
			selectionLength -= spaces.length();
			// Go through each selected line
			for (int line = startLine; line <= endLine; line++) {
				// Position the caret at the start of the line
				editor.setCaretOffset(editor.getOffsetAtLine(line));
				// insert the indention
				editor.insert(spaces);
				// The selection has now grown by the number of characters
				// inserted
				selectionLength += spaces.length();
				// Remember the command in the undo stack
				history.push(new Command("", spaces, editor.getCaretOffset(),
						link));
				// If more than one insert, the commands must be linked
				link = true;
			}
			// Make sure the selection is right
			editor.setSelectionRange(selectionStart, selectionLength);
		} else {
			// Only one line needs to be indented
			int line = getLine();
			editor.setCaretOffset(editor.getOffsetAtLine(line));
			history
					.push(new Command("", spaces, editor.getCaretOffset(),
							false));
			editor.insert(spaces);
		}
		// Clear the redo stack
		rehistory.clear();
	}

	// =============================================================================
	// Copy block-selected text
	public void copyBlock() {
		if ((blockHeight <= 0) || (blockWidth <= 0) || (blockStart < 0)) {
			return;
		}
		block = new String[blockHeight];
		// Go through each selected line
		int startLine = editor.getLineAtOffset(blockStart);
		int startCol = blockStart - editor.getOffsetAtLine(startLine);
		int pos;
		for (int line = 0; line < blockHeight; line++) {
			pos = editor.getOffsetAtLine(startLine + line) + startCol;
			block[line] = editor.getTextRange(pos, blockWidth);
		}
	}

	// =============================================================================
	// Save the buffer text to disk
	public void save() {
		try {
			// Open a file dialog
			FileDialog fileSave = new FileDialog(shell, SWT.SAVE);
			fileSave.setText("Save File As");
			fileSave.open();
			// Check that the file name is valid
			if (fileSave.getFileName() != null) {
				filename = fileSave.getFileName();
				// use the File constructor to get a complete path to the file
				File file = new File(fileSave.getFilterPath(), filename);
				// Save the file of the given name
				save(file.getPath());
				syndex = Syntax.getIndex(filename);
				shell.setText("SWediT - " + filename);
				shell.update();
			}
		} catch (Exception e) {
			exceptionMessage("Exception Saving File:\n" + e);
		}
	}

	// =============================================================================
	// Save the file to disk. Very simple, because Strings have a getBytes
	// method and FileOutputStreams have a write(byte[]) method.
	public void save(final String filename) {
		String text = editor.getText();
		// When saving in DOS format
		if (dosFormat) {
			// Need a StringBuffer because we'll lots of characters one at a
			// time.
			StringBuffer tempStringBuff = new StringBuffer();
			// Assume that every character is a \n, so double the length is
			// needed
			tempStringBuff.ensureCapacity(2 * text.length());
			// Loop until length is reached.
			for (int i = 0; i < text.length(); i++) {
				char charAt = text.charAt(i);
				if (charAt == '\n') {
					// If it's a newline, put a carriage return and new line
					tempStringBuff.append("\r\n");
				} else if (charAt != '\r') {
					// Put in each character as it is, unless it's a carriage
					// return.
					tempStringBuff.append(charAt);
				}
			}
			// After the StringBuffer is used to convert to DOS format, get a
			// String from it
			text = tempStringBuff.toString();
		}
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			fos.write(text.getBytes());
			fos.close();
			changed = false;
			history.clear();
		} catch (Exception e) {
			exceptionMessage("Exception Saving File \"" + filename + "\":\n"
					+ e);
		}
	} // End of save

	// =============================================================================
	// Load text from a file into a new buffer
	public boolean load(final String loadFilename) {
		byte[] data;
		File file;
		boolean existingFile = false;
		if ((loadFilename == null) || loadFilename.equals("")) {
			return false;
		}
		try {
			// use the File class constructor to get the proper file name
			file = new File(loadFilename);
			existingFile = file.exists();
			// Store the current buffer
			if (currentBuffer >= 0) {
				buffers.set(currentBuffer, new Buffer(editor.getText(),
						filename, editor.getCaretOffset(),
						editor.getTopIndex(), changed, (Stack) history.clone(),
						expandTabs, dosFormat));
			}
			// Set the file name for this buffer
			filename = file.getAbsolutePath();
		} catch (Exception e) {
			exceptionMessage("Exception Loading File \"" + loadFilename
					+ "\":\n" + e);
			return false;
		}
		// Figure out what syntax highlighting (if any) to use.
		syndex = Syntax.getIndex(filename);
		boolean loadChange = false;
		if (existingFile) {
			try {
				// The file input stream can read the data from disk
				FileInputStream fis = new FileInputStream(file);
				// Set up an array of bytes for the text from the file
				data = new byte[(int) file.length()];
				// Read the whole file in one swell foop into the byte array
				fis.read(data, 0, (int) file.length());
				// Close the file
				fis.close();
			} catch (Exception e) {
				exceptionMessage("Exception Loading File \"" + loadFilename
						+ "\":\n" + e);
				return false;
			}
			// Get the default setting of expandTabs from the properties
			expandTabs = Boolean.valueOf(
					props.getProperty("Expand_Tabs", "false")).booleanValue();
			String text = new String(data);
			MessageBox mb = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES
					| SWT.NO);
			mb.setText("Format");
			if (text.indexOf('\r') >= 0) {
				if (!dosFormatDefault) {
					mb
							.setMessage(filename
									+ " appears to be in DOS format.  Convert to Unix?");
					int answer = mb.open();
					if (answer == SWT.YES) {
						dosFormat = false;
						loadChange = true;
					} else if (answer == SWT.NO) {
						dosFormat = true;
					}
				}
			} else {
				if (dosFormatDefault) {
					mb
							.setMessage(filename
									+ " appears to be in Unix format.  Convert to DOS?");
					int answer = mb.open();
					if (answer == SWT.YES) {
						dosFormat = true;
						loadChange = true;
					} else if (answer == SWT.NO) {
						dosFormat = false;
					}
				}
			}
			if (text.indexOf('\t') >= 0) {
				if (askAboutTabs) {
					mb.setText("Tabs");
					mb
							.setMessage(filename
									+ " has tabs.\nDo you want to expand tabs to spaces?");
					int answer = mb.open();
					if (answer == SWT.YES) {
						expandTabs = true;
						loadChange = true;
					} else if (answer == SWT.NO) {
						expandTabs = false;
					}
				}
			}
			// Now it's okay to set the editor's text.
			editor.setText(stripControlCharacters(text, expandTabs, editor
					.getTabs()));
			// The ModifyTextListner just set changed to true; it should only be
			// true
			// if loading the file changed it, i.e. expanding tabs or converting
			// format
			changed = loadChange;
		}
		// Clear the history and rehistory, since they won't be valid for this
		// buffer
		history.clear();
		rehistory.clear();
		// Move to the zero position
		editor.setSelectionRange(0, 0);
		lastColumn = 1;
		editor.showSelection();
		// Set up a new spot in the buffers Vector
		buffers.add(new Buffer(editor.getText(), filename, 0, 0, false,
				new Stack(), expandTabs, dosFormat));
		// New buffers always go at the end. The buffers Vector size is 1
		// indexed,
		// but we need a 0 indexed int for accessing the buffers
		currentBuffer = buffers.size() - 1;
		// Update the title on the shell
		shell.setText("SWediT - " + filename);
		linePos.setText("Line: 1 / " + String.valueOf(editor.getLineCount()));
		colPos.setText("Column: 1");
		return true;
	} // end of load(filename)

	// =============================================================================
	// Ask to close a buffer; note if this requested by a quit command.
	public void close(final boolean quitting) {
		// If the doc has changed, a dialog must be launched
		if (changed == true) {
			final MessageBox mb = new MessageBox(shell, SWT.ICON_QUESTION
					| SWT.YES | SWT.NO | SWT.CANCEL);
			if (quitting) {
				mb.setText("Quit");
			} else {
				mb.setText("Close Buffer");
			}
			mb.setMessage(filename
					+ " has changed.\nDo you want to save the changes?");
			int answer = mb.open();
			if (answer == SWT.YES) {
				save(filename);
				closeBuffer(quitting);
			} else
			// The No button will close the buffer without saving
			if (answer == SWT.NO) {
				closeBuffer(quitting);
			} else {
				// The cancel button sets cancelFlag to true, so that if this is
				// a
				// quit command the quit will abort.
				cancelFlag = true;
			}
		} else {
			// The text hasn't changed, so just close it without asking and
			// without saving.
			closeBuffer(quitting);
		}
	}

	// =============================================================================
	// Actually close the buffer.
	public void closeBuffer(final boolean quitting) {
		Buffer tempBuffer;
		// If there is only one buffer
		if (buffers.size() == 1) {
			// Remove this buffer
			buffers.remove(currentBuffer);
			// zero out current buffer
			currentBuffer = 0;
			// If this isn't part of a quit command, open a new File. Basically,
			// there must always be at least one buffer open.
			if (quitting == false) {
				newFile();
			}
			// If there are several buffers open
		} else if (buffers.size() > 1) {
			// Remove the current buffer
			buffers.remove(currentBuffer);
			// If current buffer is now larger than the total number of buffers,
			if (currentBuffer >= buffers.size()) {
				// set the current buffer to the last buffer
				currentBuffer = buffers.size() - 1;
			}
			// Clear out the block selection
			blockStart = 0;
			blockHeight = 0;
			blockWidth = 0;
			tempBuffer = (Buffer) buffers.get(currentBuffer);
			// Set the editor text to the stored text
			editor.setText(tempBuffer.text());
			// Set the caret position to the stored caret position
			editor.setCaretOffset(tempBuffer.pos());
			// Display the area that was last displayed
			editor.setTopIndex(tempBuffer.topIndex());
			lastColumn = getColumn();
			// Set the changed flag to the stored changed flag
			changed = tempBuffer.changed();
			// Set the Undo Stack to the stored undo Stack
			history = tempBuffer.history();
			// Set the buffer's filename to the stored filename
			filename = tempBuffer.fileName();
			syndex = Syntax.getIndex(filename);
			// Set the expandTabs flag to the stored value
			expandTabs = tempBuffer.expandTabs();
			// Update the title bar with the filename
			shell.setText("SWediT - " + filename);
			shell.update();
			// Clear out the Redo Stack
			rehistory.clear();
		}
	}

	// =============================================================================
	public void newFile() {
		Buffer tempBuffer;
		// If all the buffers have been nuked, don't try and save a non-existant
		// buffer
		if (buffers.size() > 0) {
			// Store the current buffer data into the vector
			buffers.set(currentBuffer, new Buffer(editor.getText(), filename,
					editor.getCaretOffset(), editor.getTopIndex(), changed,
					(Stack) history.clone(), expandTabs, dosFormat));
		}
		// The new filename is untitled, plus the number of untitled docs that
		// have
		// been opened this seesion
		filename = "Untitled_" + numUntitled;

		// Increment the global variable tracking how many untitled docs have
		// been opened
		numUntitled++;
		// Clear the redo and undo stacks
		history.clear();
		rehistory.clear();
		// Set the caret to the start of the doc
		editor.setCaretOffset(0);
		lastColumn = 1;
		// Clear the editor text
		editor.setText("");
		// Add a new buffer so there is space in the vector for this one
		buffers.add(new Buffer(editor.getText(), filename, 0, 0, false,
				new Stack(), true, dosFormatDefault));
		// update the current buffer number; new files always go on the end, but
		// are zero index while size() is 1 indexed
		currentBuffer = buffers.size() - 1;
		// Update the title bar with the filename
		shell.setText("SWediT - " + filename);
		// A new document can't possibly have changes
		changed = false;
		expandTabs = true;
		linePos.setText("Line: 1 / 1");
		colPos.setText("Column: 1");
		shell.update();
	}

	// =============================================================================

	void exceptionMessage(final String message) {
		if (!debugMode) {
			return;
		}
		final MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
		mb.setText("Exception");
		mb.setMessage(message);
		mb.open();
	}

	// =============================================================================
	String stripControlCharacters(final String text, boolean expandTabs,
			final int tabWidth) {
		if (text == null) {
			return null;
		}
		if (tabWidth <= 0) {
			expandTabs = false;
		}
		StringBuffer tempStringBuff = new StringBuffer();
		tempStringBuff.ensureCapacity(2 * text.length());
		// Create an array of characters the length of a tab width
		char spacesTemp[] = new char[tabWidth];
		// Fill the character array with spaces
		Arrays.fill(spacesTemp, ' ');
		// Convert the character array to a string
		String spaces = new String(spacesTemp);
		char charAt;
		// Loop until length is reached.
		for (int i = 0; i < text.length(); i++) {
			// Check the current character.
			charAt = text.charAt(i);
			if ((charAt < 32) || (charAt > 126)) {
				// When encountering a tab
				if (charAt == '\t') {
					if (expandTabs) {
						tempStringBuff.append(spaces);
					} else {
						tempStringBuff.append('\t');
					}
				} else if (charAt == '\n') {
					tempStringBuff.append('\n');
				}
			} else {
				tempStringBuff.append(charAt);
			}
		}
		return tempStringBuff.toString();
	}

	// =============================================================================
	// Set up the pull down menus. bar is a global Menu object.
	public void setupMenu() {
		// File menu
		MenuItem file = new MenuItem(bar, SWT.CASCADE);
		file.setText("&File");
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);

		MenuItem newFile = new MenuItem(fileMenu, SWT.PUSH);
		newFile.setText("&New");
		newFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditNew());
			}
		});
		MenuItem load = new MenuItem(fileMenu, SWT.PUSH);
		load.setText("&Open");
		load.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditLoad());
			}
		});
		MenuItem save = new MenuItem(fileMenu, SWT.PUSH);
		save.setText("&Save");
		save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditSave());
			}
		});
		MenuItem saveAs = new MenuItem(fileMenu, SWT.PUSH);
		saveAs.setText("Save &As");
		saveAs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditSaveAs());
			}
		});
		MenuItem closeBuffer = new MenuItem(fileMenu, SWT.PUSH);
		closeBuffer.setText("&Close");
		closeBuffer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditClose());
			}
		});
		MenuItem exit = new MenuItem(fileMenu, SWT.PUSH);
		exit.setText("E&xit");
		exit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditQuit());
			}
		});

		file.setMenu(fileMenu);

		// Edit Menu
		MenuItem edit = new MenuItem(bar, SWT.CASCADE);
		edit.setText("&Edit");
		Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
		MenuItem undo = new MenuItem(editMenu, SWT.PUSH);
		undo.setText("&Undo");
		undo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditUndo());
			}
		});
		MenuItem redo = new MenuItem(editMenu, SWT.PUSH);
		redo.setText("&Redo");
		redo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditRedo());
			}
		});
		MenuItem cut = new MenuItem(editMenu, SWT.PUSH);
		cut.setText("Cu&t");
		cut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditCut());
			}
		});
		MenuItem copy = new MenuItem(editMenu, SWT.PUSH);
		copy.setText("&Copy");
		copy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditCopy());
			}
		});
		MenuItem paste = new MenuItem(editMenu, SWT.PUSH);
		paste.setText("&Paste");
		paste.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditPaste());
			}
		});
		MenuItem find = new MenuItem(editMenu, SWT.PUSH);
		find.setText("&Find");
		find.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditFindString());
			}
		});
		MenuItem findNext = new MenuItem(editMenu, SWT.PUSH);
		findNext.setText("Find &Next");
		findNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditFindNext());
			}
		});
		MenuItem replace = new MenuItem(editMenu, SWT.PUSH);
		replace.setText("&Replace");
		replace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditReplaceString());
			}
		});
		MenuItem gotoLine = new MenuItem(editMenu, SWT.PUSH);
		gotoLine.setText("&Go To Line");
		gotoLine.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditGoToLine());
			}
		});

		edit.setMenu(editMenu);

		// Help Menu
		MenuItem help = new MenuItem(bar, SWT.CASCADE);
		help.setText("&Help");
		Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
		MenuItem keyMap = new MenuItem(helpMenu, SWT.PUSH);
		keyMap.setText("&Key Map");
		keyMap.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(printKeyMapDialog);
			}
		});
		MenuItem about = new MenuItem(helpMenu, SWT.PUSH);
		about.setText("&About");
		about.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditAbout());
			}
		});
		MenuItem license = new MenuItem(helpMenu, SWT.PUSH);
		license.setText("&License");
		license.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(sweditLicenseDialog);
			}
		});
		help.setMenu(helpMenu);

		// Create a space between the last menu at the "status" info
		MenuItem spacer = new MenuItem(bar, SWT.CASCADE);
		spacer.setText("     ");
		spacer.setEnabled(false);

		// linePos is global, because runCommand updates its text with
		// the current line number of the caret
		linePos = new MenuItem(bar, SWT.CASCADE);
		linePos.setText("Line: 1 / 1");
		linePos.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				runCommand(new SweditGoToLine());
			}
		});
		// colPos is global, because runCommand updates its text with
		// the current column number of the caret
		colPos = new MenuItem(bar, SWT.CASCADE);
		colPos.setText("Column: " + String.valueOf(1));
		colPos.setEnabled(false);
	}

	// =============================================================================
	// This is a simple method that handles the fact that Ctrl-A through Ctrl-Z
	// are ASCII 01 through 26 in SWT. This function allows me to map a key
	// to ctrl('q') rather than 17. I like to see the character because it
	// makes more sense to me.
	public static final int ctrl(final char c) {
		return Character.toUpperCase(c) - 'A' + 1;
	}

	// =============================================================================
	// Set up the key mapping
	// This is fairly straight-forward; put the character, the keyCode, and mod
	// keys into a SweditKey object. That object is the map key to retreive the
	// SweditCommand Object. To have multiple mod keys, OR (|) the mods
	// together.
	// The constants are all defined in the SWT class.
	// %%% Users should be able to change and store the key map.
	public void setupKeyMap() {
		// Add all of the ASCII single-character, non-control values as a String
		// with exactly that character
		for (char i = 32; i < 127; i++) {
			// The key has to be put in twice, once with shift and once without.
			// The actual shifting is handled by SWT, so you get Shift-a when
			// Caps
			// Lock is on, Shift-A when it's not. The look-up looks at the
			// modifers
			// not just the character
			keys.put(new SweditKey(i, 0, 0), new SweditInsertChar(i));
			keys.put(new SweditKey(i, 0, SWT.SHIFT), new SweditInsertChar(i));
		}
		keys.put(new SweditKey('g', 0, SWT.ALT), new SweditGoToLine());
		keys.put(new SweditKey('m', 0, SWT.ALT), new SweditMatchBracket());
		keys.put(new SweditKey('x', 0, SWT.ALT), new SweditQuit());
		keys.put(new SweditKey(ctrl('A'), 0, SWT.CTRL), new SweditSelectAll());
		keys.put(new SweditKey(ctrl('C'), 0, SWT.CTRL), new SweditCopy());
		keys.put(new SweditKey(ctrl('C'), 0, SWT.CTRL | SWT.ALT),
				new SweditCopyBlock());
		keys.put(new SweditKey(ctrl('D'), 0, SWT.CTRL), new SweditDeleteLine());
		keys.put(new SweditKey(ctrl('D'), 0, SWT.CTRL | SWT.ALT | SWT.SHIFT),
				new SweditDebug());
		keys.put(new SweditKey(ctrl('F'), 0, SWT.CTRL), new SweditFindString());
		keys.put(new SweditKey(ctrl('G'), 0, SWT.CTRL), new SweditGoToLine());
		keys.put(new SweditKey(ctrl('I'), 0, SWT.CTRL), new SweditIndent());
		keys.put(new SweditKey(ctrl('I'), 0, SWT.CTRL | SWT.SHIFT),
				new SweditUndent());
		keys.put(new SweditKey(ctrl('L'), 0, SWT.CTRL), new SweditFindNext());
		keys.put(new SweditKey(ctrl('M'), 0, SWT.CTRL),
				new SweditMatchBracket());
		keys.put(new SweditKey(ctrl('N'), 0, SWT.CTRL), new SweditNew());
		keys.put(new SweditKey(ctrl('O'), 0, SWT.CTRL), new SweditLoad());
		keys.put(new SweditKey(ctrl('R'), 0, SWT.CTRL),
				new SweditReplaceString());
		keys.put(new SweditKey(ctrl('S'), 0, SWT.CTRL), new SweditSave());
		keys.put(new SweditKey(ctrl('X'), 0, SWT.CTRL), new SweditCut());
		keys.put(new SweditKey(ctrl('X'), 0, SWT.CTRL | SWT.ALT),
				new SweditCutBlock());
		keys.put(new SweditKey(ctrl('V'), 0, SWT.CTRL), new SweditPaste());
		keys.put(new SweditKey(ctrl('V'), 0, SWT.CTRL | SWT.ALT),
				new SweditPasteBlock());
		keys.put(new SweditKey(ctrl('Y'), 0, SWT.CTRL), new SweditRedo());
		keys.put(new SweditKey(ctrl('Z'), 0, SWT.CTRL), new SweditUndo());
		keys.put(new SweditKey(ctrl('Z'), 0, SWT.CTRL | SWT.SHIFT),
				new SweditRedo());
		keys.put(new SweditKey('\t', '\t', 0), new SweditTab());
		keys.put(new SweditKey('\t', '\t', SWT.SHIFT), new SweditUndent());
		keys.put(new SweditKey(SWT.ESC, SWT.ESC, 0), new SweditCancel());
		keys.put(new SweditKey(SWT.BS, SWT.BS, 0), new SweditDeletePrev());
		keys.put(new SweditKey(SWT.BS, SWT.BS, SWT.ALT), new SweditUndo());
		keys.put(new SweditKey(SWT.CR, SWT.CR, 0), new SweditNewLine());
		keys.put(new SweditKey(SWT.DEL, SWT.DEL, 0), new SweditDeleteNext());
		keys.put(new SweditKey(SWT.DEL, SWT.DEL, SWT.CTRL), new SweditCut());
		keys.put(new SweditKey(0, SWT.INSERT, 0), new SweditPaste());
		keys.put(new SweditKey(0, SWT.INSERT, SWT.CTRL), new SweditCopy());
		keys.put(new SweditKey(0, SWT.HOME, 0), new SweditLineStart());
		keys.put(new SweditKey(0, SWT.END, 0), new SweditLineEnd());
		keys.put(new SweditKey(0, SWT.HOME, SWT.CTRL), new SweditTextStart());
		keys.put(new SweditKey(0, SWT.END, SWT.CTRL), new SweditTextEnd());
		keys.put(new SweditKey(0, SWT.HOME, SWT.CTRL | SWT.SHIFT),
				new SweditSelectTextStart());
		keys.put(new SweditKey(0, SWT.END, SWT.CTRL | SWT.SHIFT),
				new SweditSelectTextEnd());
		keys.put(new SweditKey(0, SWT.HOME, SWT.SHIFT),
				new SweditSelectLineStart());
		keys.put(new SweditKey(0, SWT.END, SWT.SHIFT),
				new SweditSelectLineEnd());
		keys.put(new SweditKey(0, SWT.ARROW_UP, 0), new SweditLineUp());
		keys.put(new SweditKey(0, SWT.ARROW_DOWN, 0), new SweditLineDown());
		keys.put(new SweditKey(0, SWT.ARROW_LEFT, 0), new SweditColPrev());
		keys.put(new SweditKey(0, SWT.ARROW_RIGHT, 0), new SweditColNext());
		keys
				.put(new SweditKey(0, SWT.ARROW_UP, SWT.CTRL),
						new SweditScrollUp());
		keys.put(new SweditKey(0, SWT.ARROW_DOWN, SWT.CTRL),
				new SweditScrollDown());
		keys.put(new SweditKey(0, SWT.ARROW_LEFT, SWT.CTRL),
				new SweditWordPrev());
		keys.put(new SweditKey(0, SWT.ARROW_RIGHT, SWT.CTRL),
				new SweditWordNext());
		keys.put(new SweditKey(0, SWT.ARROW_UP, SWT.SHIFT),
				new SweditSelectLineUp());
		keys.put(new SweditKey(0, SWT.ARROW_DOWN, SWT.SHIFT),
				new SweditSelectLineDown());
		keys.put(new SweditKey(0, SWT.ARROW_LEFT, SWT.SHIFT),
				new SweditSelectColPrev());
		keys.put(new SweditKey(0, SWT.ARROW_RIGHT, SWT.SHIFT),
				new SweditSelectColNext());
		keys.put(new SweditKey(0, SWT.ARROW_UP, SWT.ALT),
				new SweditBlockSelectLineUp());
		keys.put(new SweditKey(0, SWT.ARROW_DOWN, SWT.ALT),
				new SweditBlockSelectLineDown());
		keys.put(new SweditKey(0, SWT.ARROW_LEFT, SWT.ALT),
				new SweditBlockSelectColPrev());
		keys.put(new SweditKey(0, SWT.ARROW_RIGHT, SWT.ALT),
				new SweditBlockSelectColNext());
		keys.put(new SweditKey(0, SWT.ARROW_LEFT, SWT.CTRL | SWT.SHIFT),
				new SweditSelectWordPrev());
		keys.put(new SweditKey(0, SWT.ARROW_RIGHT, SWT.CTRL | SWT.SHIFT),
				new SweditSelectWordNext());
		keys.put(new SweditKey(0, SWT.PAGE_UP, 0), new SweditPageUp());
		keys.put(new SweditKey(0, SWT.PAGE_DOWN, 0), new SweditPageDown());
		keys.put(new SweditKey(0, SWT.PAGE_UP, SWT.CTRL),
				new SweditWindowStart());
		keys.put(new SweditKey(0, SWT.PAGE_DOWN, SWT.CTRL),
				new SweditWindowEnd());
		keys.put(new SweditKey(0, SWT.PAGE_UP, SWT.SHIFT),
				new SweditSelectPageUp());
		keys.put(new SweditKey(0, SWT.PAGE_DOWN, SWT.SHIFT),
				new SweditSelectPageDown());
		keys.put(new SweditKey(0, SWT.PAGE_UP, SWT.CTRL | SWT.SHIFT),
				new SweditSelectWindowStart());
		keys.put(new SweditKey(0, SWT.PAGE_DOWN, SWT.CTRL | SWT.SHIFT),
				new SweditSelectWindowEnd());
		keys.put(new SweditKey(0, SWT.F1, 0), printKeyMapDialog);
		keys.put(new SweditKey(0, SWT.F2, 0), new SweditSave());
		keys.put(new SweditKey(0, SWT.F3, 0), new SweditLoad());
		keys.put(new SweditKey(0, SWT.F4, SWT.CTRL), new SweditClose());
		keys.put(new SweditKey(0, SWT.F6, 0), new SweditNextBuffer());
	}

	// =============================================================================
	public void setupKeyNames(final Map keyNames) {
		keyNames.put(new Integer('\t'), "Tab");
		keyNames.put(new Integer(SWT.BS), "Backspace");
		keyNames.put(new Integer(SWT.DEL), "Delete");
		keyNames.put(new Integer(SWT.CR), "Enter");
		keyNames.put(new Integer(SWT.INSERT), "Insert");
		keyNames.put(new Integer(SWT.HOME), "Home");
		keyNames.put(new Integer(SWT.END), "End");
		keyNames.put(new Integer(SWT.ARROW_UP), "Up");
		keyNames.put(new Integer(SWT.ARROW_DOWN), "Down");
		keyNames.put(new Integer(SWT.ARROW_LEFT), "Left");
		keyNames.put(new Integer(SWT.ARROW_RIGHT), "Right");
		keyNames.put(new Integer(SWT.PAGE_UP), "PageUp");
		keyNames.put(new Integer(SWT.PAGE_DOWN), "PageDown");
		keyNames.put(new Integer(SWT.ESC), "Escape");
		keyNames.put(new Integer(SWT.F1), "F1");
		keyNames.put(new Integer(SWT.F2), "F2");
		keyNames.put(new Integer(SWT.F3), "F3");
		keyNames.put(new Integer(SWT.F4), "F4");
		keyNames.put(new Integer(SWT.F5), "F5");
		keyNames.put(new Integer(SWT.F6), "F6");
		keyNames.put(new Integer(SWT.F7), "F7");
		keyNames.put(new Integer(SWT.F8), "F8");
		keyNames.put(new Integer(SWT.F9), "F9");
		keyNames.put(new Integer(SWT.F10), "F10");
		keyNames.put(new Integer(SWT.F11), "F11");
		keyNames.put(new Integer(SWT.F12), "F12");
	}

	// =============================================================================
	// Class to encapsulate keystroke information. Keystrokes must be hashable
	// in order to look them up quickly. They also need an intelligent toString
	// method so that the key mappings can be outputed.
	final class SweditKey implements Comparable {
		private final int character;
		private final int keyCode;
		private final int stateMask;

		// Require all three fields in the constructor
		SweditKey(final int character, final int keyCode, final int stateMask) {
			this.character = character;
			this.keyCode = keyCode;
			this.stateMask = stateMask;
		}

		// To be equal, all three fields must be equal
		@Override
		public boolean equals(final Object o) {
			SweditKey other = (SweditKey) o;
			if ((this.character == other.character)
					&& (this.keyCode == other.keyCode)
					&& (this.stateMask == other.stateMask)) {
				return true;
			}
			return false;
		}

		// Comparable is needed for the hash. The basic idea of this comparable
		// algorithm is to , then keyCode as the next most-significant, then the
		// mask
		// (i.e. shift, control, or alt) as the least-signifcant.
		public int compareTo(final Object o) {
			SweditKey other = (SweditKey) o;
			// Treat character as the most-significant 32 bits of a 3x32-bit
			// value
			if (this.character > other.character) {
				return 1;
			}
			if (this.character < other.character) {
				return -1;
			}
			// Treat keyCode as the middle-significance 32 bits of a 3x32-bit
			// value
			if (this.keyCode > other.keyCode) {
				return 1;
			}
			if (this.keyCode < other.keyCode) {
				return -1;
			}
			// Treat stateMask as the least-significant 32 bits of a 3x32-bit
			// value
			if (this.stateMask > other.stateMask) {
				return 1;
			}
			if (this.stateMask < other.stateMask) {
				return -1;
			}
			return 0;
		}

		// Hash code, for retrieving the commands mapped to key strokes
		// Hash codes are not guarenteed to be unique.
		@Override
		public int hashCode() {
			int tempcode;
			// In general in SWT's VerifyKeyEvent, there are 3 possibilites.
			// character = keyCode, character = 0, or keyCode = 0.
			// You should never see character and keyCode set to different
			// non-zero values
			// So pick which ever one is not zero, or keyCode if they're equal
			if (this.character > 0) {
				tempcode = this.character;
			} else {
				tempcode = this.keyCode;
			}
			// Set the highest-order bit on if the keystroke requires the
			// Control key
			if ((this.stateMask & SWT.CTRL) > 0) {
				tempcode |= 1 << 31;
			}
			// Set the second-highest-order bit on if the keystroke requires the
			// Alt key
			if ((this.stateMask & SWT.ALT) > 0) {
				tempcode |= 1 << 30;
			}
			// Set the third-highest-order bit on if the keystroke requires the
			// Shift key
			if ((this.stateMask & SWT.SHIFT) > 0) {
				tempcode |= 1 << 29;
			}
			return tempcode;
		}

		// A nice printable string of the key stroke.
		@Override
		public String toString() {
			int key;
			if (this.keyCode == 0) {
				if (((this.stateMask & SWT.CTRL) > 0) && (this.character <= 26)) {
					return new String(maskString(this.stateMask)
							+ (char) ('A' + this.character - 1));
				} else {
					return new String(maskString(this.stateMask)
							+ (char) this.character);
				}
			} else {
				return new String(maskString(this.stateMask)
						+ keyNames.get(new Integer(this.keyCode)));
			}
		}

		// When modifier keys (masks) are used, get the English version
		public String maskString(final int stateMask) {
			if (stateMask == 0) {
				return new String();
			}
			StringBuffer mask = new StringBuffer();
			if ((stateMask & SWT.CTRL) > 0) {
				mask.append("Contrl-");
			}
			if ((stateMask & SWT.ALT) > 0) {
				mask.append("Alt-");
			}
			if ((stateMask & SWT.SHIFT) > 0) {
				mask.append("Shift-");
			}
			return mask.toString();
		}
	}

	// =============================================================================
	// The Command class, which is used to hold undo / redo information
	final class Command {
		private final String newtext; // The new text
		private final String oldtext; // the old text
		private final int pos; // the caret position where it happened
		// Whether or not to "chain" or "link" this command to the next,
		// normally for block operations.
		private final boolean link;

		// There is only one constructor, and constructing is the only way to
		// set
		// the private fields. All fields are sent in the constructor
		Command(final String oldtext, final String newtext, final int pos,
				final boolean link) {
			this.newtext = newtext;
			this.oldtext = oldtext;
			this.pos = pos;
			this.link = link;
		}

		// Field retrieval methods
		String oldtext() {
			return this.oldtext;
		}

		String newtext() {
			return this.newtext;
		}

		int pos() {
			return this.pos;
		}

		boolean link() {
			return this.link;
		}
	} // End of inner class Command

	// =============================================================================
	// The Syntax class, which is used to hold syntax highlighting information
	public final static class Syntax {
		static Set[] extensions = new HashSet[] {
				new HashSet(Arrays.asList(new String[] { ".java" })),
				new HashSet(Arrays.asList(new String[] { ".c", ".h" })),
				new HashSet(Arrays.asList(new String[] { ".cc", ".cpp" })) };

		static Set[] keywords = new HashSet[] {
				new HashSet(Arrays.asList(new String[] { "abstract", "break",
						"case", "catch", "class", "continue", "default", "do",
						"else", "extends", "false", "final", "finally", "for",
						"if", "implements", "import", "instaceOf", "interface",
						"native", "new", "null", "package", "private",
						"protected", "public", "return", "static", "super",
						"switch", "synchronized", "this", "throw", "throws",
						"transient", "true", "try", "volatile", "while",
						"const", "goto" })),
				new HashSet(Arrays.asList(new String[] { "auto", "break",
						"case", "const", "continue", "default", "do", "else",
						"enum", "extern", "for", "goto", "if", "register",
						"return", "signed", "sizeof", "static", "struct",
						"switch", "typedef", "union", "volatile", "while",
						"while", "#define", "#elif", "#else", "#endif", "#if",
						"#ifdef", "#ifndef", "#include", "#line", "#pargmA",
						"#undef" })),
				new HashSet(Arrays.asList(new String[] { "auto", "break",
						"case", "const", "continue", "default", "do", "else",
						"enum", "extern", "for", "goto", "if", "register",
						"return", "signed", "sizeof", "static", "struct",
						"switch", "typedef", "union", "volatile", "while",
						"asm", "catch", "class", "const_cast", "delete",
						"dynamic_cast", "explicit", "false", "friend",
						"inline", "mutable", "namespace", "new", "operator",
						"private", "protected", "public", "reinterpret_cast",
						"static_cast", "template", "this", "throw", "true",
						"try", "typeid", "typename", "using", "virtual",
						"#define", "#elif", "#else", "#endif", "#if", "#ifdef",
						"#ifndef", "#include", "#line", "#pargmA", "#undef" })) };
		static Set[] varTypes = new HashSet[] {
				new HashSet(Arrays.asList(new String[] { "boolean", "byte",
						"char", "double", "float", "int", "long", "short",
						"void" })),
				new HashSet(Arrays.asList(new String[] { "char", "double",
						"float", "int", "long", "short", "void" })),
				new HashSet(Arrays.asList(new String[] { "bool", "char",
						"double", "float", "int", "long", "short", "void",
						"wchar_t" })) };
		static Set[] operators = new HashSet[] {
				new HashSet(Arrays.asList(new String[] { ".", "+", "-", "!",
						"~", "*", "/", "%", "<", ">", "&", "^", "|", "?", ":",
						"=" })),
				new HashSet(Arrays.asList(new String[] { ".", "+", "-", "!",
						"~", "*", "/", "%", "<", ">", "&", "^", "|", "?", ":",
						"=" })) };
		static Set[] brackets = new HashSet[] {
				new HashSet(Arrays.asList(new String[] { "{", "}", "[", "]",
						"(", ")" })),
				new HashSet(Arrays.asList(new String[] { "{", "}", "[", "]",
						"(", ")" })) };

		static int getIndex(final String filename) {
			for (int i = 0; i < extensions.length; i++) {
				Iterator iter = extensions[i].iterator();
				while (iter.hasNext()) {
					if (filename.endsWith((String) iter.next())) {
						return i;
					}
				}
			}
			return -1;
		}
	}

	// =============================================================================
	// Holds the data for a single buffer
	final class Buffer {
		private String text;
		private String filename;
		private int pos;
		private int topIndex;
		private boolean changed;
		private boolean expandTabs;
		private boolean dosFormat;
		private Stack history;

		// The constructor requires values for all private fields
		Buffer(final String text, final String filename, final int pos,
				final int topIndex, final boolean changed, final Stack history,
				final boolean expandTabs, final boolean dosFormat) {
			this.text = text;
			this.filename = filename;
			this.pos = pos;
			this.topIndex = topIndex;
			this.changed = changed;
			this.history = history;
			this.expandTabs = expandTabs;
			this.dosFormat = dosFormat;
		}

		String text() {
			return this.text;
		}

		void text(final String newtext) {
			this.text = newtext;
		}

		String fileName() {
			return this.filename;
		}

		void fileName(final String newFileName) {
			this.filename = newFileName;
		}

		int pos() {
			return this.pos;
		}

		void pos(final int newpos) {
			this.pos = newpos;
		}

		int topIndex() {
			return this.topIndex;
		}

		void topIndex(final int newTopIndex) {
			this.topIndex = newTopIndex;
		}

		boolean changed() {
			return this.changed;
		}

		void changed(final boolean newchanged) {
			this.changed = newchanged;
		}

		Stack history() {
			return (Stack) this.history.clone();
		}

		void history(final Stack newHistory) {
			this.history = newHistory;
		}

		boolean expandTabs() {
			return this.expandTabs;
		}

		void expandTabs(final boolean newExpandTabs) {
			this.expandTabs = newExpandTabs;
		}

		boolean dosFormat() {
			return this.dosFormat;
		}

		void dosFormat(final boolean newDosFormat) {
			this.dosFormat = newDosFormat;
		}
	} // End of inner class Buffer

	// =============================================================================

} // End of class SWediT
