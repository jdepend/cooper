package jdepend.framework.context;

import jdepend.framework.context.Scope.SCOPE;
import jdepend.framework.util.BundleUtil;

public class JDependContext {

	private static final AppliationScope appScope = new AppliationScope();
	private static final ThreadScope threadScope = new ThreadScope();

	public static final String RunEnv = "RunEnv";
	public static final String Client = "Client";
	public static final String Server = "Server";

	public static final String WorkspacePath = "WorkspacePath";

	public static final String prevSelectedPath = "prevSelectedPath";
	public static final String prevSelectedSrcPath = "prevSelectedSrcPath";

	public static void setInfo(SCOPE scope, String key, Object value) {
		if (scope == SCOPE.APP_SCOPSE)
			appScope.setInfo(key, value);
		else
			threadScope.setInfo(key, value);
	}

	public static Object getInfo(SCOPE scope, String key) {
		return getInfo(scope, key, null);
	}

	public static Object getInfo(SCOPE scope, String key, Object defaultValue) {
		Object value = null;
		if (scope == SCOPE.APP_SCOPSE)
			value = appScope.getInfo(key);
		else
			value = threadScope.getInfo(key);

		if (value == null) {
			value = defaultValue;
		}

		return value;
	}

	public static void setRunningPath(String path) {
		setInfo(SCOPE.APP_SCOPSE, "RunningPath", path);
	}

	public static String getRunningPath() {
		String path = (String) getInfo(SCOPE.APP_SCOPSE, "RunningPath");
		if (path == null) {
			return System.getProperty("user.dir");
		} else {
			return path;
		}
	}

	public static void setWorkspacePath(String path) {
		setInfo(SCOPE.APP_SCOPSE, WorkspacePath, path);
	}

	public static String getWorkspacePath() {
		return (String) getInfo(SCOPE.APP_SCOPSE, WorkspacePath);
	}
}
