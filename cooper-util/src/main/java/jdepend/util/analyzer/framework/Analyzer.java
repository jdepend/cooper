package jdepend.util.analyzer.framework;

import java.io.IOException;
import java.io.Serializable;

import jdepend.framework.util.BundleUtil;
import jdepend.model.result.AnalysisResult;

public interface Analyzer extends Serializable, Comparable<Analyzer> {

	public final static String AntiPattern = BundleUtil.getString(BundleUtil.ClientWin_Culture_Analyzer_AntiPattern);

	public final static String Attention = BundleUtil.getString(BundleUtil.ClientWin_Culture_Analyzer_Attention);

	public String getName();

	public String getTip();

	public String getExplain();

	public String getType();

	public int getHeat();

	public void execute(AnalysisResult result) throws AnalyzerException;

	public void setWorker(AnalyzerWorker worker);

	public void setListener(AnalyzerExecutorListener listener);

	public int getMaxProgress(AnalysisResult result);

	public void init() throws AnalyzerException;

	public void release() throws AnalyzerException;

	public void save() throws IOException;

	public boolean needSave();

}
