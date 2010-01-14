/**
 * Original author Eugene Kononov <nonlinear5@yahoo.com> 
 * Adapted for JST by Florent Guiliani <florent@guiliani.fr>
 */
package org.lifeform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

import org.lifeform.configuration.Defaults;

public class PreferencesHolder {
	private static PreferencesHolder instance;
	private final Preferences prefs;

	public static synchronized PreferencesHolder getInstance() {
		if (instance == null) {
			instance = new PreferencesHolder();
		}
		return instance;
	}

	// private constructor for non-instantiability
	private PreferencesHolder() {
		prefs = Preferences.userNodeForPackage(getClass());
	}

	public long getLong(final Defaults pref) {
		String value = get(pref);
		return Long.valueOf(value);
	}

	public int getInt(final Defaults pref) {
		String value = get(pref);
		return Integer.valueOf(value);
	}

	public boolean getBool(final Defaults pref) {
		String value = get(pref);
		return Boolean.valueOf(value);
	}

	public String[] getStringArray(final Defaults pref) {
		String value = get(pref);
		StringTokenizer st = new StringTokenizer(value, ",");
		List<String> items = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			items.add(st.nextToken());
		}

		return items.toArray(new String[items.size()]);
	}

	public String get(final Defaults pref) {
		return prefs.get(pref.getName(), pref.getDefault());
	}

	public void set(final Defaults pref, final String propertyValue) {
		System.out.println(pref.toString() + "\t" + propertyValue);
		prefs.put(pref.getName(), propertyValue);
	}

	public void set(final Defaults pref, final int propertyValue) {
		set(pref, String.valueOf(propertyValue));
	}

	public void set(final Defaults pref, final long propertyValue) {
		set(pref, String.valueOf(propertyValue));
	}

	public void set(final Defaults pref, final boolean propertyValue) {
		set(pref, String.valueOf(propertyValue));
	}
}
