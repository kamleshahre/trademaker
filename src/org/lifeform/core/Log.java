package org.lifeform.core;

import java.util.Calendar;

public class Log {
	public enum Type {
		ERROR, INFO, WARN, SYSTEM
	};

	public static void log(final Type category, final Object... data) {
		StringBuffer buf = new StringBuffer(category.toString()).append("\t")
				.append(Calendar.getInstance()).append("+-->\t");
		for (Object o : data)
			buf.append(o);
		System.out.println(buf.toString());
	}

	public static void fine(final String msg) {
		log(Type.INFO, msg);
	}

	public static void log(final Type error, final String string) {
		// TODO Auto-generated method stub

	}

}
