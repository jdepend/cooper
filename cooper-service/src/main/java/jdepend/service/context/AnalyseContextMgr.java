package jdepend.service.context;

/**
 * 分析上下文管理器
 * 
 * @author wangdg
 * 
 */
public final class AnalyseContextMgr {

	private static ThreadLocal<AnalyseContext> contexts = new ThreadLocal<AnalyseContext>();

	public static AnalyseContext getContext() {
		return contexts.get();
	}

	public static void setContext(AnalyseContext context) {
		contexts.set(context);
	}
}
