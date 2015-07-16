package test.common;

import java.util.ArrayList;
import java.util.List;

public class TestConfigUtil {

	public static List<String> getSelfPath() {
		List<String> self = new ArrayList<String>();
		String[] envpath = System.getProperty("java.class.path").split(";");
		for (String path : envpath) {
			if (path.endsWith("classes") || path.indexOf("cooper.jar") != -1
					|| (path.indexOf("cooper-") != -1 && path.endsWith(".jar"))) {
				self.add(path);
			}
		}
		return self;

	}
}
