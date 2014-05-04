package jdepend.framework.context;

import jdepend.framework.context.Scope.SCOPE;
import jdepend.framework.util.BundleUtil;

public class JDependContext {

	private static final AppliationScope appScope = new AppliationScope();
	private static final ThreadScope threadScope = new ThreadScope();

	private static final int StandaloneMode = 0;// 单机版运行模式
	private static final int ClientMode = 1;// 客户端运行模式

	public static final String Local = BundleUtil.getString(BundleUtil.RunningModel_Local);
	public static final String Remote = BundleUtil.getString(BundleUtil.RunningModel_Remote);

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

	public static boolean isStandaloneMode() {
		Integer runMode = (Integer) getInfo(SCOPE.APP_SCOPSE, "RunMode");
		if (runMode == null) {
			return true;
		} else {
			return runMode == StandaloneMode;
		}
	}

	public static Boolean isLocalService() {
		return (Boolean) getInfo(SCOPE.APP_SCOPSE, "isLocalService");
	}

	public static void setRunMode(int runMode) {
		setInfo(SCOPE.APP_SCOPSE, "RunMode", runMode);
	}

	public static void setIsLocalService(boolean isLocalService) {
		setInfo(SCOPE.APP_SCOPSE, "isLocalService", isLocalService);
	}

	public static void setRunningPath(String path) {
		setInfo(SCOPE.APP_SCOPSE, "RunningPath", path);
	}

	public static void setRunEnv(String env) {
		setInfo(SCOPE.APP_SCOPSE, RunEnv, env);
	}

	public static String getRunEnv() {
		return (String) getInfo(SCOPE.APP_SCOPSE, RunEnv);
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
