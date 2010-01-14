package org.lifeform.gui.dialog;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class AboutBox extends ApplicationWindow {

	StatusLineManager slm = new StatusLineManager();
	Ch4_StatusAction status_action = new Ch4_StatusAction(slm);
	ActionContributionItem aci = new ActionContributionItem(status_action);

	public AboutBox() {
		super(null);
	}

	protected Control createContents(final Composite parent) {
		getShell().setText("Action/Contribution Example");
		
//		ButtonGroup btnGroup = new ButtonGroup(parent);
//		parent.setSize(290, 150);
		aci.fill(parent); 
		return parent;
	}

	protected MenuManager createMenuManager() {
		MenuManager main_menu = new MenuManager(null);
		MenuManager action_menu = new MenuManager("Menu");
		main_menu.add(action_menu);
		action_menu.add(status_action);
		return main_menu;
	}

	protected ToolBarManager createToolBarManager(final int style) {
		ToolBarManager tool_bar_manager = new ToolBarManager(style);
		tool_bar_manager.add(status_action);
		return tool_bar_manager;
	}

	protected StatusLineManager createStatusLineManager() {
		return slm;
	}

	Label output;

	public class Ch4_StatusAction extends Action
	{
	  StatusLineManager statman;
	  short triggercount = 0;
	    
	  public Ch4_StatusAction(final StatusLineManager sm)
	  {
	  	super("&Trigger@Ctrl+T", AS_PUSH_BUTTON);
	    statman = sm;
	    setToolTipText("Trigger the Action");
//	    setImageDescriptor(ImageDescriptor.createFromFile(this.getClass(),"eclipse.gif"));
	  }

	  public void run()
	  {
	  	triggercount++;
		statman.setMessage("The status action has fired. Count: " + triggercount);
	  }
	}
	
	public class Ch4_MouseKey extends Composite {
		Ch4_MouseKey(final Composite parent) {
			super(parent, SWT.NULL);
			Button typed = new Button(this, SWT.PUSH);
			typed.setText("Typed");
			typed.setLocation(2, 10);
			typed.pack();

			typed.addKeyListener(new KeyAdapter() {
				public void keyPressed(final KeyEvent e) {
					keyHandler();
				}
			});
			Button untyped = new Button(this, SWT.PUSH);
			untyped.setText("Untyped");
			untyped.setLocation(80, 10);
			untyped.pack();
			untyped.addListener(SWT.MouseEnter, UntypedListener);
			untyped.addListener(SWT.MouseExit, UntypedListener);

			output = new Label(this, SWT.SHADOW_OUT);
			output.setBounds(40, 70, 90, 40);
			output.setText("No Event");

			pack();
		}

		Listener UntypedListener = new Listener() {
			public void handleEvent(final Event event) {
				switch (event.type) {
				case SWT.MouseEnter:
					output.setText("Mouse Enter");
					break;
				case SWT.MouseExit:
					output.setText("Mouse Exit");
					break;
				}
			}
		};

		void keyHandler() {
			output.setText("Key Event");
		}
	}

	public static void main(final String[] args) {
		AboutBox cv = new AboutBox();
		cv.setBlockOnOpen(true);
		cv.open();
		Display.getCurrent().dispose();
	}

	public class ButtonGroup extends Composite {
		public ButtonGroup(final Composite parent) {
			super(parent, SWT.NONE);
			Group group = new Group(this, SWT.SHADOW_ETCHED_IN);
			group.setText("Find Trades");

			Label label = new Label(group, SWT.NONE);
			label.setText("Choose Action:");
			label.setLocation(5, 20);
			label.pack();

			Button pushBtn = new Button(group, SWT.PUSH);
			pushBtn.setText("Run");
			pushBtn.setLocation(80, 20);
			pushBtn.pack();

			Button checkBtn = new Button(group, SWT.PUSH);
			checkBtn.setText("Chart");
			checkBtn.setLocation(120, 20);
			group.pack();
		}
	}
}
