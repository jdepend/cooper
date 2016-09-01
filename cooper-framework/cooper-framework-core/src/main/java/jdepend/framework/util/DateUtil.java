package jdepend.framework.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static Date getSysDate() {
		return new Date(System.currentTimeMillis()) {
			@Override
			public String toString() {
				return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(this);
			}
		};
	}

}
