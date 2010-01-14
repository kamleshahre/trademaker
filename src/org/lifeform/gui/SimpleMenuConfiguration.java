package org.lifeform.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.lifeform.util.Configuration;

public class SimpleMenuConfiguration extends Configuration {
	public SimpleMenuConfiguration() {
		super();
	}

	Image iBold, iItalic, iUnderline, iStrikeout, iLeftAlignment,
			iRightAlignment, iCenterAlignment, iJustifyAlignment, iCopy, iCut,
			iLink;
	Image iPaste, iSpacing, iIndent, iTextForeground, iTextBackground,
			iBaselineUp, iBaselineDown, iBulletList, iNumberedList,
			iBlockSelection, iBorderStyle;
	Font font, textFont;
	Color textForeground, textBackground, strikeoutColor, underlineColor,
			borderColor;
	StyledText styledText;

	public Menu getMenu(final Shell shell) {
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setText(getResourceString("File_menuitem"));
		fileItem.setMenu(fileMenu);

		MenuItem openItem = new MenuItem(fileMenu, SWT.PUSH);
		openItem.setText(getResourceString("Open_menuitem"));
		openItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
			}
		});

		final MenuItem saveItem = new MenuItem(fileMenu, SWT.PUSH);
		saveItem.setText(getResourceString("Save_menuitem"));
		saveItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
			}
		});

		fileMenu.addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(final MenuEvent event) {
			}
		});

		MenuItem saveAsItem = new MenuItem(fileMenu, SWT.PUSH);
		saveAsItem.setText(getResourceString("SaveAs_menuitem"));
		saveAsItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
			}
		});

		new MenuItem(fileMenu, SWT.SEPARATOR);

		MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
		exitItem.setText(getResourceString("Exit_menuitem"));
		exitItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				shell.dispose();
			}
		});

		MenuItem editItem = new MenuItem(menu, SWT.CASCADE);
		final Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
		editItem.setText(getResourceString("Edit_menuitem"));
		editItem.setMenu(editMenu);
		final MenuItem cutItem = new MenuItem(editMenu, SWT.PUSH);
		cutItem.setText(getResourceString("Cut_menuitem"));
		cutItem.setImage(iCut);
		cutItem.setAccelerator(SWT.MOD1 | 'x');
		cutItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
			}
		});

		final MenuItem copyItem = new MenuItem(editMenu, SWT.PUSH);
		copyItem.setText(getResourceString("Copy_menuitem"));
		copyItem.setImage(iCopy);
		copyItem.setAccelerator(SWT.MOD1 | 'c');
		copyItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				styledText.copy();
			}
		});

		MenuItem pasteItem = new MenuItem(editMenu, SWT.PUSH);
		pasteItem.setText(getResourceString("Paste_menuitem"));
		pasteItem.setImage(iPaste);
		pasteItem.setAccelerator(SWT.MOD1 | 'v');
		pasteItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				styledText.paste();
			}
		});

		new MenuItem(editMenu, SWT.SEPARATOR);
		final MenuItem selectAllItem = new MenuItem(editMenu, SWT.PUSH);
		selectAllItem.setText(getResourceString("SelectAll_menuitem"));
		selectAllItem.setAccelerator(SWT.MOD1 | 'a');
		selectAllItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				styledText.selectAll();
			}
		});

		editMenu.addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(final MenuEvent event) {
				int selectionCount = styledText.getSelectionCount();
				cutItem.setEnabled(selectionCount > 0);
				copyItem.setEnabled(selectionCount > 0);
				selectAllItem.setEnabled(selectionCount < styledText
						.getCharCount());
			}
		});

		MenuItem wrapItem = new MenuItem(editMenu, SWT.CHECK);
		wrapItem.setText(getResourceString("Wrap_menuitem"));
		wrapItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				MenuItem item = (MenuItem) event.widget;
			}
		});

		MenuItem justifyItem = new MenuItem(editMenu, SWT.CHECK);
		justifyItem.setText(getResourceString("Justify_menuitem"));
		justifyItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				MenuItem item = (MenuItem) event.widget;
			}
		});
		justifyItem.setEnabled(false);

		MenuItem setFontItem = new MenuItem(editMenu, SWT.PUSH);
		setFontItem.setText(getResourceString("SetFont_menuitem"));
		setFontItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
			}
		});

		MenuItem alignmentItem = new MenuItem(editMenu, SWT.CASCADE);
		alignmentItem.setText(getResourceString("Alignment_menuitem"));
		Menu alignmentMenu = new Menu(shell, SWT.DROP_DOWN);
		alignmentItem.setMenu(alignmentMenu);
		final MenuItem leftAlignmentItem = new MenuItem(alignmentMenu,
				SWT.RADIO);
		leftAlignmentItem.setText(getResourceString("Left_menuitem"));
		leftAlignmentItem.setSelection(true);
		leftAlignmentItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
			}
		});
		alignmentItem.setEnabled(false);

		final MenuItem centerAlignmentItem = new MenuItem(alignmentMenu,
				SWT.RADIO);
		centerAlignmentItem.setText(getResourceString("Center_menuitem"));
		centerAlignmentItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
			}
		});

		MenuItem rightAlignmentItem = new MenuItem(alignmentMenu, SWT.RADIO);
		rightAlignmentItem.setText(getResourceString("Right_menuitem"));
		rightAlignmentItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
			}
		});

		MenuItem editOrientationItem = new MenuItem(editMenu, SWT.CASCADE);
		editOrientationItem.setText(getResourceString("Orientation_menuitem"));
		Menu editOrientationMenu = new Menu(shell, SWT.DROP_DOWN);
		editOrientationItem.setMenu(editOrientationMenu);

		MenuItem leftToRightItem = new MenuItem(editOrientationMenu, SWT.RADIO);
		leftToRightItem.setText(getResourceString("LeftToRight_menuitem"));
		leftToRightItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				styledText.setOrientation(SWT.LEFT_TO_RIGHT);
			}
		});
		leftToRightItem.setSelection(true);

		MenuItem rightToLeftItem = new MenuItem(editOrientationMenu, SWT.RADIO);
		rightToLeftItem.setText(getResourceString("RightToLeft_menuitem"));
		rightToLeftItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				styledText.setOrientation(SWT.RIGHT_TO_LEFT);
			}
		});

		new MenuItem(editMenu, SWT.SEPARATOR);
		MenuItem insertObjectItem = new MenuItem(editMenu, SWT.CASCADE);
		insertObjectItem.setText(getResourceString("InsertObject_menuitem"));
		Menu insertObjectMenu = new Menu(shell, SWT.DROP_DOWN);
		insertObjectItem.setMenu(insertObjectMenu);

		MenuItem insertControlItem = new MenuItem(insertObjectMenu, SWT.CASCADE);
		insertControlItem.setText(getResourceString("Controls_menuitem"));
		Menu controlChoice = new Menu(shell, SWT.DROP_DOWN);
		insertControlItem.setMenu(controlChoice);

		MenuItem buttonItem = new MenuItem(controlChoice, SWT.PUSH);
		buttonItem.setText(getResourceString("Button_menuitem"));
		MenuItem comboItem = new MenuItem(controlChoice, SWT.PUSH);
		comboItem.setText(getResourceString("Combo_menuitem"));

		buttonItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
			}
		});

		comboItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
			}
		});

		MenuItem insertImageItem = new MenuItem(insertObjectMenu, SWT.PUSH);
		insertImageItem.setText(getResourceString("Image_menuitem"));

		insertImageItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
			}
		});

		new MenuItem(editMenu, SWT.SEPARATOR);
		MenuItem loadProfileItem = new MenuItem(editMenu, SWT.CASCADE);
		loadProfileItem.setText(getResourceString("LoadProfile_menuitem"));
		Menu loadProfileMenu = new Menu(shell, SWT.DROP_DOWN);
		loadProfileItem.setMenu(loadProfileMenu);
		SelectionAdapter adapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
			}
		};

		MenuItem profileItem = new MenuItem(loadProfileMenu, SWT.PUSH);
		profileItem.setText(getResourceString("Profile1_menuitem"));
		profileItem.setData("1");
		profileItem.addSelectionListener(adapter);
		profileItem = new MenuItem(loadProfileMenu, SWT.PUSH);
		profileItem.setText(getResourceString("Profile2_menuitem"));
		profileItem.setData("2");
		profileItem.addSelectionListener(adapter);
		profileItem = new MenuItem(loadProfileMenu, SWT.PUSH);
		profileItem.setText(getResourceString("Profile3_menuitem"));
		profileItem.setData("3");
		profileItem.addSelectionListener(adapter);
		profileItem = new MenuItem(loadProfileMenu, SWT.PUSH);
		profileItem.setText(getResourceString("Profile4_menuitem"));
		profileItem.setData("4");
		profileItem.addSelectionListener(adapter);

		return menu;
	}
}
