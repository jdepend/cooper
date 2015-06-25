package jdepend.client.ui.analyzer;

import java.io.IOException;
import java.lang.reflect.Method;

import jdepend.client.report.ui.SelectClassDialog;
import jdepend.metadata.JavaClass;
import jdepend.model.JDependUnitMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.AnalyzerException;
import jdepend.util.analyzer.framework.AnalyzerExecutorListener;
import jdepend.util.analyzer.framework.AnalyzerWorker;
import jdepend.util.analyzer.framework.ClassAttribute;

public class AnalyzerUIWrapper implements Analyzer {

	private Analyzer analyzer;

	public AnalyzerUIWrapper(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	@Override
	public int compareTo(Analyzer o) {
		return analyzer.compareTo(((AnalyzerUIWrapper) o).analyzer);
	}

	@Override
	public String getName() {
		return analyzer.getName();
	}

	@Override
	public String getTip() {
		return analyzer.getTip();
	}

	@Override
	public String getExplain() {
		return analyzer.getExplain();
	}

	@Override
	public String getType() {
		return analyzer.getType();
	}

	@Override
	public int getHeat() {
		return analyzer.getHeat();
	}

	@Override
	public void execute(final AnalysisResult result) throws AnalyzerException {

		Method[] methods = analyzer.getClass().getDeclaredMethods();

		Method theMethod = null;
		for (final Method method : methods) {
			if (method.getAnnotation(ClassAttribute.class) != null) {
				theMethod = method;
				break;
			}
		}

		if (theMethod != null) {
			final Method theMethod2 = theMethod;
			SelectClassDialog d = new SelectClassDialog() {
				@Override
				protected void callback() {
					JavaClass javaClass = JDependUnitMgr.getInstance().getResult().getTheClass(current).getJavaClass();
					try {
						theMethod2.invoke(analyzer, javaClass);
						analyzer.execute(result);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			d.setModal(true);
			d.setVisible(true);
		} else {
			analyzer.execute(result);
		}
	}

	@Override
	public void setWorker(AnalyzerWorker worker) {
		this.analyzer.setWorker(worker);
	}

	@Override
	public void setListener(AnalyzerExecutorListener listener) {
		this.analyzer.setListener(listener);
	}

	@Override
	public int getMaxProgress(AnalysisResult result) {
		return this.analyzer.getMaxProgress(result);
	}

	@Override
	public void init() throws AnalyzerException {
		this.analyzer.init();

	}

	@Override
	public void release() throws AnalyzerException {
		this.analyzer.release();

	}

	@Override
	public void save() throws IOException {
		this.analyzer.save();

	}

	@Override
	public boolean needSave() {
		return this.analyzer.needSave();
	}

}
