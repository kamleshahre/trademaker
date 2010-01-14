package org.lifeform.util;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.database.JDBCPropertySet;

public class Configuration extends  JDBCPropertySet  implements PropertySet {

	
	public String getResourceString(final String string) {
			return string;
	}
	
	public void showError (final String title, final String message) {
		AppUtil.showError(title, message);
	}

}
