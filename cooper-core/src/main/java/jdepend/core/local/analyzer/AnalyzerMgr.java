package jdepend.core.local.analyzer;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.metadata.util.ClassSearchUtil;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.Analyzers;

/**
 * 客户端分析管理器
 * 
 * @author wangdg
 * 
 */
public final class AnalyzerMgr {

	private static AnalyzerMgr mgr;

	private List<String> types;

	private Map<String, List<Analyzer>> analyzers;

	private AnalyzerMgr() {
		this.init();
	}

	public static AnalyzerMgr getInstance() {
		if (mgr == null) {
			mgr = new AnalyzerMgr();
		}
		return mgr;
	}

	public void refresh() {
		this.init();
	}

	public void save() {
		for (String type : types) {
			for (Analyzer analyzer : this.analyzers.get(type)) {
				if (!analyzer.needSave()) {
					continue;
				}
				LogUtil.getInstance(AnalyzerMgr.class).systemLog("保存分析器[" + analyzer.getName() + "]配置。。。");
				try {
					analyzer.save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void init() {

		this.analyzers = this.getDynamicAnalyzers();
		if (this.analyzers.isEmpty()) {
			this.analyzers = Analyzers.getStaticAnalyzers();
		}

		this.types = new ArrayList<String>();
		for (String type : analyzers.keySet()) {
			this.types.add(type);
			for (Analyzer analyzer : analyzers.get(type)) {
				try {
					analyzer.init();
				} catch (JDependException e) {
					e.printStackTrace();
					LogUtil.getInstance(AnalyzerMgr.class).systemError("分析器[" + analyzer.getName() + "]启动失败");
				}
			}
		}

		// 按热度排序
		for (String type : types) {
			Collections.sort(this.analyzers.get(type));
		}
	}

	public void delete(String className) throws JDependException {
		for (String type : this.types) {
			for (Analyzer analyzer : this.analyzers.get(type)) {
				if (analyzer.getClass().getName().equals(className)) {
					analyzer.release();
					this.getAnalyzers(type).remove(analyzer);
					break;
				}
			}
		}
	}

	private Map<String, List<Analyzer>> getDynamicAnalyzers() {
		List<String> analyzerNames = ClassSearchUtil.getInstance().getSubClassNames(Analyzer.class.getName());
		Map<String, List<Analyzer>> analyzers = new LinkedHashMap<String, List<Analyzer>>();
		List<Analyzer> analyzerTypes;
		for (String analyzerName : analyzerNames) {
			try {
				Class analyzerClass = Class.forName(analyzerName);
				if (!analyzerClass.isInterface() && !Modifier.isAbstract(analyzerClass.getModifiers())) {
					Analyzer analyzer = (Analyzer) analyzerClass.newInstance();
					analyzerTypes = analyzers.get(analyzer.getType());
					if (analyzerTypes == null) {
						analyzerTypes = new ArrayList<Analyzer>();
						analyzers.put(analyzer.getType(), analyzerTypes);
					}
					if (!analyzerTypes.contains(analyzer)) {
						analyzerTypes.add(analyzer);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return analyzers;
	}

	public List<String> getTypes() {
		return this.types;
	}

	public List<Analyzer> getAnalyzers(String type) {
		return this.analyzers.get(type);
	}

	public Map<String, List<Analyzer>> getAnalyzers() {
		return analyzers;
	}

	public boolean containsAnalyzer(Analyzer analyzer) {
		for (String type : this.analyzers.keySet()) {
			for (Analyzer obj : this.analyzers.get(type)) {
				if (obj.equals(analyzer)) {
					return true;
				}
			}
		}
		return false;
	}

	public void addAnalyzer(Analyzer analyzer) {
		this.analyzers.get(analyzer.getType()).add(analyzer);
	}
}
