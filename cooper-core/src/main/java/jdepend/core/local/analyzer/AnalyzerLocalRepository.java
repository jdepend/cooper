package jdepend.core.local.analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.file.TargetFileManager;
import jdepend.framework.log.LogUtil;
import jdepend.framework.util.FileUtil;
import jdepend.metadata.util.ClassSearchUtil;
import jdepend.service.remote.analyzer.AnalyzerDTO;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.Analyzers;

public final class AnalyzerLocalRepository {

	public final static String DEFAULT_ANALYZERDEF_DIR = "analyzerDef";
	public final static String DEFAULT_ANALYZERDATA_DIR = "analyzerData";

	private final static String defDir = JDependContext.getWorkspacePath() + "\\" + DEFAULT_ANALYZERDEF_DIR + "\\";
	private final static String dataDir = JDependContext.getWorkspacePath() + "\\" + DEFAULT_ANALYZERDATA_DIR + "\\";

	private List<String> extendClass = new ArrayList<String>();

	public InputStream getDef(Analyzer analyzer) throws FileNotFoundException {
		if (this.getExtendClass().contains(analyzer.getClass().getName())) {
			return new FileInputStream(defDir + analyzer.getClass().getName() + ".class");
		} else {
			return analyzer.getClass().getClassLoader()
					.getResourceAsStream(analyzer.getClass().getCanonicalName().replace('.', '/') + ".class");
		}
	}

	public Map<String, List<Analyzer>> getAnalyzers() {

		Map<String, List<Analyzer>> analyzers = this.getDynamicAnalyzers();
		if (analyzers.isEmpty()) {
			analyzers = Analyzers.getStaticAnalyzers();
		}

		this.extendClass = new ArrayList<String>();
		List<Analyzer> extendAnalyzers = null;
		try {
			extendAnalyzers = this.initExtendAnalyzers();
			for (Analyzer analyzer : extendAnalyzers) {
				if (analyzers.containsKey(analyzer.getType())) {
					if (!analyzers.get(analyzer.getType()).contains(analyzer)) {
						analyzers.get(analyzer.getType()).add(analyzer);
						this.extendClass.add(analyzer.getClass().getName());
					}
				} else {
					List<Analyzer> temps = new ArrayList<Analyzer>();
					temps.add(analyzer);
					this.extendClass.add(analyzer.getClass().getName());
					analyzers.put(analyzer.getType(), temps);
				}
			}
		} catch (JDependException e) {
			e.printStackTrace();
		}

		return analyzers;
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

	private List<Analyzer> initExtendAnalyzers() throws JDependException {

		List<Analyzer> analyzers = new ArrayList<Analyzer>();
		TargetFileManager fileManager = new TargetFileManager();

		String className = null;
		byte[] def;
		byte[] data;

		try {
			fileManager.addDirectory(defDir);
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new JDependException(e1);
		}
		List<File> files = fileManager.extractClassFiles();
		for (File file : files) {
			try {
				if (file.getName().endsWith(".class")) {
					def = FileUtil.getFileData(file.getAbsolutePath());
					className = file.getName().substring(0, file.getName().indexOf(".class"));
					data = FileUtil.getFileData(dataDir + className);
					Analyzer analyzer = AnalyzerConvertUtil.createAnalyzer(def, className, data);
					if (analyzer.getName() == null || analyzer.getType() == null) {
						throw new AnalyzerFormatException();
					}
					analyzers.add(analyzer);
				}
			} catch (AnalyzerFormatException e) {
				LogUtil.getInstance(AnalyzerLocalRepository.class).systemError("分析器格式错误");
				FileUtil.deleteFile(file.getName());
				FileUtil.deleteFile(dataDir + className);
			} catch (IOException e) {
				e.printStackTrace();
				FileUtil.deleteFile(file.getName());
				FileUtil.deleteFile(dataDir + className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				FileUtil.deleteFile(file.getName());
				FileUtil.deleteFile(dataDir + className);
			}
		}

		return analyzers;
	}

	public void save(AnalyzerDTO analyzerDTO) throws JDependException {

		byte[] def = analyzerDTO.getDef();
		String filePath = defDir + analyzerDTO.getClassName() + ".class";
		FileUtil.saveFile(filePath, def);

		byte[] data = analyzerDTO.getDefaultData();
		filePath = dataDir + analyzerDTO.getClassName();
		FileUtil.saveFile(filePath, data);

		// 内存登记
		if (!this.extendClass.contains(analyzerDTO.getClassName())) {
			this.extendClass.add(analyzerDTO.getClassName());
		}

	}

	public List<String> getExtendClass() {
		return extendClass;
	}

	public void setExtendClass(List<String> extendClass) {
		this.extendClass = extendClass;
	}

	public void delete(String className) throws JDependException {
		if (this.extendClass != null && !this.extendClass.contains(className)) {
			throw new JDependException("只能删除下载的分析器");
		}
		this.deleteAnalyzer(className);

	}

	private void deleteAnalyzer(String className) throws JDependException {

		String defFilePath = defDir + className + ".class";
		FileUtil.deleteFile(defFilePath);

		String dataFilePath = dataDir + className;
		FileUtil.deleteFile(dataFilePath);
	}

}
