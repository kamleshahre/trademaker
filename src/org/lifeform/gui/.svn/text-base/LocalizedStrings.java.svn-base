/*******************************************************************************
 * Copyright (c) 2005 Stefan Zeiger and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.novocode.com/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Zeiger (szeiger@novocode.com) - initial API and implementation
 *******************************************************************************/

package org.lifeform.gui;

import java.util.ResourceBundle;

/**
 * Manages the localized strings for the InternalShell implementation.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Feb 26, 2005
 * @version $Id: LocalizedStrings.java 322 2005-02-26 20:31:26 +0000 (Sat, 26
 *          Feb 2005) szeiger $
 */

public class LocalizedStrings {
	public static final String POPUP_RESTORE = "popup.restore";
	public static final String POPUP_MOVE = "popup.move";
	public static final String POPUP_SIZE = "popup.size";
	public static final String POPUP_MINIMIZE = "popup.minimize";
	public static final String POPUP_MAXIMIZE = "popup.maximize";
	public static final String POPUP_CLOSE = "popup.close";

	private static final ResourceBundle b = loadResourceBundle();

	private static ResourceBundle loadResourceBundle() {
		String name = LocalizedStrings.class.getName();
		name = name.substring(0, name.lastIndexOf('.'));
		name += ".strings";
		return ResourceBundle.getBundle(name);
	}

	public static String get(String key) {
		return b.getString(key);
	}
}
