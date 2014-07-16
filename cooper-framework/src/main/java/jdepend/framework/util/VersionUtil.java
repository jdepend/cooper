package jdepend.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VersionUtil {

	public static String getVersion() {
		return "1.2.2_10";
	}

	public static Date getBuildDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return new Date(sdf.parse("2014-07-16").getTime()) {
				@Override
				public String toString() {
					return (new SimpleDateFormat("yyyy-MM-dd")).format(this);
				}
			};
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
