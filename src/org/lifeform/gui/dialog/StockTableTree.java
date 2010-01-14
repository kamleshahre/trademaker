package org.lifeform.gui.dialog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class StockTableTree extends ApplicationWindow {
	// The TableTreeViewer
	private TableTreeViewer ttv;

	/**
	 * StockTableTree constructor
	 */
	public StockTableTree() {
		super(null);
	}

	/**
	 * Runs the application
	 */
	public void run() {
		// Don't return from open() until window closes
		setBlockOnOpen(true);

		// Open the main window
		open();

		// Dispose the display
		Display.getCurrent().dispose();
	}

	/**
	 * Configures the shell
	 * 
	 * @param shell
	 *            the shell
	 */
	@Override
	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		shell.setText("Stock Tree");
	}

	/**
	 * Creates the main window's contents
	 * 
	 * @param parent
	 *            the main window
	 * @return Control
	 */
	@Override
	protected Control createContents(final Composite parent) {
		// Create the table viewer to display the Stocks
		ttv = new TableTreeViewer(parent);
		ttv.getTableTree().setLayoutData(new GridData(GridData.FILL_BOTH));

		// Set the content and label providers
		ttv.setContentProvider(new StockTreeContentProvider());
		ttv.setLabelProvider(new StockTreeLabelProvider());
		ttv.setInput(new StockTableModel());

		// Set up the table
		Table table = ttv.getTableTree().getTable();
		new TableColumn(table, SWT.LEFT).setText("Quote Name");
		new TableColumn(table, SWT.LEFT).setText("Full Name");
		new TableColumn(table, SWT.RIGHT).setText("Points");
		new TableColumn(table, SWT.RIGHT).setText("Rebounds");
		new TableColumn(table, SWT.RIGHT).setText("Assists");

		// Expand everything
		ttv.expandAll();

		// Pack the columns
		for (int i = 0, n = table.getColumnCount(); i < n; i++) {
			table.getColumn(i).pack();
		}

		// Turn on the header and the lines
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Pack the window
		parent.pack();

		// Scroll to top
		ttv.reveal(ttv.getElementAt(0));

		return ttv.getTableTree();
	}

	/**
	 * The application entry point
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String[] args) {
		new StockTableTree().run();
	}
}

/**
 * This class provides the content for the TableTreeViewer in StockTableTree
 */

class StockTreeContentProvider implements ITreeContentProvider {
	private static final Object[] EMPTY = new Object[] {};

	/**
	 * Gets the children for a team or Stock
	 * 
	 * @param arg0
	 *            the team or Stock
	 * @return Object[]
	 */
	public Object[] getChildren(final Object arg0) {
		if (arg0 instanceof Team)
			return ((Team) arg0).getStocks().toArray();
		// Stocks have no children . . . except Shawn Kemp
		return EMPTY;
	}

	/**
	 * Gets the parent team for a Stock
	 * 
	 * @param arg0
	 *            the Stock
	 * @return Object
	 */
	public Object getParent(final Object arg0) {
		return ((Stock) arg0).getTeam();
	}

	/**
	 * Gets whether this team or Stock has children
	 * 
	 * @param arg0
	 *            the team or Stock
	 * @return boolean
	 */
	public boolean hasChildren(final Object arg0) {
		return getChildren(arg0).length > 0;
	}

	/**
	 * Gets the elements for the table
	 * 
	 * @param arg0
	 *            the model
	 * @return Object[]
	 */
	public Object[] getElements(final Object arg0) {
		// Returns all the teams in the model
		return ((StockTableModel) arg0).teams;
	}

	/**
	 * Disposes any resources
	 */
	public void dispose() {
		// We don't create any resources, so we don't dispose any
	}

	/**
	 * Called when the input changes
	 * 
	 * @param arg0
	 *            the parent viewer
	 * @param arg1
	 *            the old input
	 * @param arg2
	 *            the new input
	 */
	public void inputChanged(final Viewer arg0, final Object arg1,
			final Object arg2) {
		// Nothing to do
	}
}

/**
 * This class provides the labels for the StockTableTree application
 */

class StockTreeLabelProvider extends StockLabelProvider {
	/**
	 * Gets the image for the specified column
	 * 
	 * @param arg0
	 *            the Stock or team
	 * @param arg1
	 *            the column
	 * @return Image
	 */
	@Override
	public Image getColumnImage(final Object arg0, final int arg1) {
		// Teams have no image
		if (arg0 instanceof Stock)
			return super.getColumnImage(arg0, arg1);
		return null;
	}

	/**
	 * Gets the text for the specified column
	 * 
	 * @param arg0
	 *            the Stock or team
	 * @param arg1
	 *            the column
	 * @return String
	 */
	@Override
	public String getColumnText(final Object arg0, final int arg1) {
		if (arg0 instanceof Stock)
			return super.getColumnText(arg0, arg1);
		Team team = (Team) arg0;
		return arg1 == 0 ? team.getYear() + " " + team.getName() : "";
	}
}

/**
 * This class contains the data model for the StockTable
 */

class StockTableModel {
	public Team[] teams;

	/**
	 * Constructs a StockTableModel Fills the model with data
	 */
	public StockTableModel() {
		teams = new Team[3];

		teams[0] = new Team("Celtics", "1985-86");
		teams[0].add(new Stock("Larry", "Bird", 25.8f, 9.8f, 6.8f));
		teams[0].add(new Stock("Kevin", "McHale", 21.3f, 8.1f, 2.7f));
		teams[0].add(new Stock("Robert", "Parish", 16.1f, 9.5f, 1.8f));
		teams[0].add(new Stock("Dennis", "Johnson", 15.6f, 3.4f, 5.8f));
		teams[0].add(new Stock("Danny", "Ainge", 10.7f, 2.9f, 5.1f));
		teams[0].add(new Stock("Scott", "Wedman", 8.0f, 2.4f, 1.1f));
		teams[0].add(new Stock("Bill", "Walton", 7.6f, 6.8f, 2.1f));
		teams[0].add(new Stock("Jerry", "Sichting", 6.5f, 1.3f, 2.3f));
		teams[0].add(new Stock("David", "Thirdkill", 3.3f, 1.4f, 0.3f));
		teams[0].add(new Stock("Sam", "Vincent", 3.2f, 0.8f, 1.2f));
		teams[0].add(new Stock("Sly", "Williams", 2.8f, 2.5f, 0.3f));
		teams[0].add(new Stock("Rick", "Carlisle", 2.6f, 1.0f, 1.4f));
		teams[0].add(new Stock("Greg", "Kite", 1.3f, 2.0f, 1.3f));

		teams[1] = new Team("Bulls", "1995-96");
		teams[1].add(new Stock("Michael", "Jordan", 30.4f, 6.6f, 4.3f));
		teams[1].add(new Stock("Scottie", "Pippen", 19.4f, 6.4f, 5.9f));
		teams[1].add(new Stock("Toni", "Kukoc", 13.1f, 4.0f, 3.5f));
		teams[1].add(new Stock("Luc", "Longley", 9.1f, 5.1f, 1.9f));
		teams[1].add(new Stock("Steve", "Kerr", 8.4f, 1.3f, 2.3f));
		teams[1].add(new Stock("Ron", "Harper", 7.4f, 2.7f, 2.6f));
		teams[1].add(new Stock("Dennis", "Rodman", 5.5f, 14.9f, 2.5f));
		teams[1].add(new Stock("Bill", "Wennington", 5.3f, 2.5f, 0.6f));
		teams[1].add(new Stock("Jack", "Haley", 5.0f, 2.0f, 0.0f));
		teams[1].add(new Stock("John", "Salley", 4.4f, 3.3f, 1.3f));
		teams[1].add(new Stock("Jud", "Buechler", 3.8f, 1.5f, 0.8f));
		teams[1].add(new Stock("Dickey", "Simpkins", 3.6f, 2.6f, 0.6f));
		teams[1].add(new Stock("James", "Edwards", 3.5f, 1.4f, 0.4f));
		teams[1].add(new Stock("Jason", "Caffey", 3.2f, 1.9f, 0.4f));
		teams[1].add(new Stock("Randy", "Brown", 2.7f, 1.0f, 1.1f));

		teams[2] = new Team("Lakers", "1987-1988");
		teams[2].add(new Stock("Magic", "Johnson", 23.9f, 6.3f, 12.2f));
		teams[2].add(new Stock("James", "Worthy", 19.4f, 5.7f, 2.8f));
		teams[2].add(new Stock("Kareem", "Abdul-Jabbar", 17.5f, 6.7f, 2.6f));
		teams[2].add(new Stock("Byron", "Scott", 17.0f, 3.5f, 3.4f));
		teams[2].add(new Stock("A.C.", "Green", 10.8f, 7.8f, 1.1f));
		teams[2].add(new Stock("Michael", "Cooper", 10.5f, 3.1f, 4.5f));
		teams[2].add(new Stock("Mychal", "Thompson", 10.1f, 4.1f, 0.8f));
		teams[2].add(new Stock("Kurt", "Rambis", 5.7f, 5.8f, 0.8f));
		teams[2].add(new Stock("Billy", "Thompson", 5.6f, 2.9f, 1.0f));
		teams[2].add(new Stock("Adrian", "Branch", 4.3f, 1.7f, 0.5f));
		teams[2].add(new Stock("Wes", "Matthews", 4.2f, 0.9f, 2.0f));
		teams[2].add(new Stock("Frank", "Brickowski", 4.0f, 2.6f, 0.3f));
		teams[2].add(new Stock("Mike", "Smrek", 2.2f, 1.1f, 0.1f));
	}
}

/**
 * This class represents a Stock
 */

class Stock {
	private Team team;

	private String lastName;

	private String firstName;

	private float points;

	private float rebounds;

	private float assists;

	/**
	 * Constructs an empty Stock
	 */
	public Stock() {
		this(null, null, 0.0f, 0.0f, 0.0f);
	}

	/**
	 * Constructs a Stock
	 * 
	 * @param firstName
	 *            the first name
	 * @param lastName
	 *            the last name
	 * @param points
	 *            the points
	 * @param rebounds
	 *            the rebounds
	 * @param assists
	 *            the assists
	 */
	public Stock(final String firstName, final String lastName,
			final float points, final float rebounds, final float assists) {
		setFirstName(firstName);
		setLastName(lastName);
		setPoints(points);
		setRebounds(rebounds);
		setAssists(assists);
	}

	/**
	 * Sets the team for theo Stock
	 * 
	 * @param team
	 *            the team
	 */
	public void setTeam(final Team team) {
		this.team = team;
	}

	/**
	 * Gets the assists
	 * 
	 * @return float
	 */
	public float getAssists() {
		return assists;
	}

	/**
	 * Sets the assists
	 * 
	 * @param assists
	 *            The assists to set.
	 */
	public void setAssists(final float assists) {
		this.assists = assists;
	}

	/**
	 * Gets the first name
	 * 
	 * @return String
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name
	 * 
	 * @param firstName
	 *            The firstName to set.
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name
	 * 
	 * @return String
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name
	 * 
	 * @param lastName
	 *            The lastName to set.
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the points
	 * 
	 * @return float
	 */
	public float getPoints() {
		return points;
	}

	/**
	 * Sets the points
	 * 
	 * @param points
	 *            The points to set.
	 */
	public void setPoints(final float points) {
		this.points = points;
	}

	/**
	 * Gets the rebounds
	 * 
	 * @return float
	 */
	public float getRebounds() {
		return rebounds;
	}

	/**
	 * Sets the rebounds
	 * 
	 * @param rebounds
	 *            The rebounds to set.
	 */
	public void setRebounds(final float rebounds) {
		this.rebounds = rebounds;
	}

	/**
	 * Gets the team
	 * 
	 * @return Team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Returns whether this Stock led the team in the specified category
	 * 
	 * @param column
	 *            the column (category)
	 * @return boolean
	 */
	public boolean ledTeam(final int column) {
		return team.led(this, column);
	}
}

/**
 * This class represents a team
 */

class Team {
	private final String name;

	private final String year;

	private final List Stocks;

	/**
	 * Constructs a Team
	 * 
	 * @param name
	 *            the name
	 * @param year
	 *            the year
	 */
	public Team(final String name, final String year) {
		this.name = name;
		this.year = year;
		Stocks = new LinkedList();
	}

	/**
	 * Gets the name
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the year
	 * 
	 * @return String
	 */
	public String getYear() {
		return year;
	}

	/**
	 * Adds a Stock
	 * 
	 * @param Stock
	 *            the Stock to add
	 * @return boolean
	 */
	public boolean add(final Stock Stock) {
		boolean added = Stocks.add(Stock);
		if (added)
			Stock.setTeam(this);
		return added;
	}

	/**
	 * Gets the Stocks
	 * 
	 * @return List
	 */
	public List getStocks() {
		return Collections.unmodifiableList(Stocks);
	}

	/**
	 * Returns whether the specified Stock led his team in the specified
	 * category
	 * 
	 * @param Stock
	 *            the Stock
	 * @param column
	 *            the category
	 * @return boolean
	 */
	public boolean led(final Stock Stock, final int column) {
		boolean led = true;

		// Go through all the Stocks on the team, comparing the specified
		// Stock's
		// stats with each other Stock.
		for (int i = 0, n = Stocks.size(); i < n && led; i++) {
			Stock test = (Stock) Stocks.get(i);
			if (Stock == test)
				continue;
			switch (column) {
			case StockConst.COLUMN_POINTS:
				if (Stock.getPoints() < test.getPoints())
					led = false;
				break;
			case StockConst.COLUMN_REBOUNDS:
				if (Stock.getRebounds() < test.getRebounds())
					led = false;
				break;
			case StockConst.COLUMN_ASSISTS:
				if (Stock.getAssists() < test.getAssists())
					led = false;
				break;
			}
		}
		return led;
	}
}

/**
 * This class contains constants for the StockTable application
 */

class StockConst {
	// Column constants
	public static final int COLUMN_FIRST_NAME = 0;

	public static final int COLUMN_LAST_NAME = 1;

	public static final int COLUMN_POINTS = 2;

	public static final int COLUMN_REBOUNDS = 3;

	public static final int COLUMN_ASSISTS = 4;
}

/**
 * This class provides the labels for StockTable
 */

class StockLabelProvider implements ITableLabelProvider {
	// Image to display if the Stock led his team
	private Image ball;

	// Constructs a StockLabelProvider
	public StockLabelProvider() {
		// Create the image
		try {
			ball = new Image(null, new FileInputStream("images/ball.png"));
		} catch (FileNotFoundException e) {
			// Swallow it
		}
	}

	/**
	 * Gets the image for the specified column
	 * 
	 * @param arg0
	 *            the Stock
	 * @param arg1
	 *            the column
	 * @return Image
	 */
	public Image getColumnImage(final Object arg0, final int arg1) {
		Stock Stock = (Stock) arg0;
		Image image = null;
		switch (arg1) {
		// A Stock can't lead team in first name or last name
		case StockConst.COLUMN_POINTS:
		case StockConst.COLUMN_REBOUNDS:
		case StockConst.COLUMN_ASSISTS:
			if (Stock.ledTeam(arg1))
				// Set the image
				image = ball;
			break;
		}
		return image;
	}

	/**
	 * Gets the text for the specified column
	 * 
	 * @param arg0
	 *            the Stock
	 * @param arg1
	 *            the column
	 * @return String
	 */
	public String getColumnText(final Object arg0, final int arg1) {
		Stock Stock = (Stock) arg0;
		String text = "";
		switch (arg1) {
		case StockConst.COLUMN_FIRST_NAME:
			text = Stock.getFirstName();
			break;
		case StockConst.COLUMN_LAST_NAME:
			text = Stock.getLastName();
			break;
		case StockConst.COLUMN_POINTS:
			text = String.valueOf(Stock.getPoints());
			break;
		case StockConst.COLUMN_REBOUNDS:
			text = String.valueOf(Stock.getRebounds());
			break;
		case StockConst.COLUMN_ASSISTS:
			text = String.valueOf(Stock.getAssists());
			break;
		}
		return text;
	}

	/**
	 * Adds a listener
	 * 
	 * @param arg0
	 *            the listener
	 */
	public void addListener(final ILabelProviderListener arg0) {
		// Throw it away
	}

	/**
	 * Dispose any created resources
	 */
	public void dispose() {
		// Dispose the image
		if (ball != null)
			ball.dispose();
	}

	/**
	 * Returns whether the specified property, if changed, would affect the
	 * label
	 * 
	 * @param arg0
	 *            the Stock
	 * @param arg1
	 *            the property
	 * @return boolean
	 */
	public boolean isLabelProperty(final Object arg0, final String arg1) {
		return false;
	}

	/**
	 * Removes the specified listener
	 * 
	 * @param arg0
	 *            the listener
	 */
	public void removeListener(final ILabelProviderListener arg0) {
		// Do nothing
	}
}
