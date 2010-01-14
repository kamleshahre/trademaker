package org.lifeform.util;

import org.lifeform.time.DateUtil;

public class Version {
	String comments = "";
	String compayName = "Lifeform Software";
	String buildDate = DateUtil.toString(DateUtil.Now());
	String major = "1";
	String minor = "0";
	String build = "0";
	String applicationName = "Trade Maker";

	public String getVersionString() {
		return new StringBuffer(major).append(".").append(minor).append(".")
				.append(build).toString();
	}

}
