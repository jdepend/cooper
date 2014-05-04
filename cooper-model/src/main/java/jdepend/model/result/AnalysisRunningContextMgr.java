package jdepend.model.result;

public final class AnalysisRunningContextMgr {

	private static AnalysisRunningContext context;

	public static AnalysisRunningContext getContext() {
		return context;
	}

	public static void setContext(AnalysisRunningContext context) {
		AnalysisRunningContextMgr.context = context;
	}

}
