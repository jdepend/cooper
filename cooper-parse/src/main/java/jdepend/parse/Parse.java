package jdepend.parse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.file.AnalyzeData;
import jdepend.framework.file.TargetFileManager;
import jdepend.framework.log.LogUtil;
import jdepend.metadata.CandidateUtil;
import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaPackage;
import jdepend.metadata.annotation.AnnotationMgr;
import jdepend.parse.impl.AbstractClassBuilder;
import jdepend.parse.impl.CSharpClassBuilder;
import jdepend.parse.impl.JavaClassBuilder;

/**
 * 解析器
 * 
 * @author user
 * 
 */
public class Parse {

	private Map<String, JavaPackage> packages;

	private TargetFileManager fileManager;

	private AbstractClassBuilder builder;

	private ParseConfigurator conf;

	private AnalyzeData data;

	public Parse() {
		init(new ParseConfigurator());
	}

	public Parse(ParseConfigurator conf) {
		init(conf);
	}

	public void setBuildClassRelation(boolean isBuildClassRelation) {
		this.getClassBuilder().setBuildClassRelation(isBuildClassRelation);
	}

	public void setSupplyJavaClassDetail(boolean isSupplyJavaClassDetail) {
		this.getClassBuilder().setSupplyJavaClassDetail(isSupplyJavaClassDetail);
	}

	public void setParseConfigs(boolean isParseConfigs) {
		this.getClassBuilder().setParseConfigs(isParseConfigs);
	}

	/**
	 * 解析目标对象
	 * 
	 * @return
	 * @throws ParseException
	 */
	public Collection<JavaPackage> execute() throws ParseException {

		AnnotationMgr.getInstance().reset();

		Collection<JavaClass> javaClasses = getClassBuilder().build(getAnalyseData());

		LogUtil.getInstance(Parse.class).systemLog("开始建立Package");
		for (JavaClass javaClass : javaClasses) {
			createPackage(javaClass);
		}

		return packages.values();
	}

	/**
	 * 增加分析目标地址
	 * 
	 * @param names
	 *            以“;”分割可以添加多个
	 * @throws IOException
	 */
	public void addDirectorys(String names) throws IOException {
		for (String name : names.split(TargetFileManager.FilePathSplit)) {
			if (fileManager.addDirectory(name)) {
				LogUtil.getInstance(Parse.class).systemLog("增加分析路径[" + name + "]");
			}
		}
	}

	/**
	 * 得到分析目标的地址
	 * 
	 * @return
	 */
	public String getDirectorys() {
		StringBuilder dir = new StringBuilder();
		for (File file : fileManager.getDirectories()) {
			try {
				dir.append(file.getCanonicalPath());
				dir.append(TargetFileManager.FilePathSplit);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dir.toString();
	}

	public void setAnalyseData(AnalyzeData data) {
		this.data = data;
	}

	/**
	 * 设置是否分析内部类
	 * 
	 * @param b
	 */
	public void analyzeInnerClasses(boolean b) {
		fileManager.acceptInnerClasses(b);
	}

	/**
	 * 计算类个数
	 * 
	 * @return
	 */
	public int countClasses() {
		return this.fileManager.countClasses();
	}

	/**
	 * 增加解析监听器
	 * 
	 * @param listener
	 */
	public void addParseListener(ParseListener listener) {
		getClassBuilder().addParseListener(listener);
	}

	/**
	 * 增加构建监听器
	 * 
	 * @param listener
	 */
	public void addBuildListener(BuildListener listener) {
		getClassBuilder().addBuildListener(listener);
	}

	/**
	 * 设置日志输出器
	 * 
	 * @param writer
	 */
	public void setLogWriter(PrintWriter writer) {
		this.getClassBuilder().setWriter(writer);
	}

	/**
	 * 增加不分析的包
	 * 
	 * @param filteredPackages
	 */
	public void addFilteredPackages(List<String> filteredPackages) {
		this.getClassBuilder().getFilter().addFilters(filteredPackages);
	}

	private void init(ParseConfigurator conf) {

		this.conf = conf;
		this.packages = new HashMap<String, JavaPackage>();
		this.fileManager = new TargetFileManager();

		analyzeInnerClasses(conf.getAnalyzeInnerClasses());
	}

	private AbstractClassBuilder getClassBuilder() {
		if (this.builder == null) {
			String dir = this.getDirectorys();
			if (dir == null || dir.length() == 0) {
				LogUtil.getInstance(Parse.class).systemWarning("分析路径没有初始化，应用JavaClassBuilder作为默认ClassBuilder");
				this.builder = new JavaClassBuilder(conf);
			}
			if (dir.indexOf(".DLL") != -1 || dir.indexOf(".dll") != -1) {
				this.builder = new CSharpClassBuilder(conf);
			} else {
				this.builder = new JavaClassBuilder(conf);
			}
		}
		return this.builder;
	}

	public AnalyzeData getAnalyseData() throws ParseException {
		if (this.data == null) {
			try {
				data = this.fileManager.getAnalyzeData();
			} catch (IOException e) {
				throw new ParseException(e);
			}
		}
		return data;
	}

	/**
	 * Adds the specified Java package name to the collection of analyzed
	 * packages.
	 * 
	 * @param name
	 *            Java package name.
	 * @return Added Java package.
	 */
	private JavaPackage addPackage(String place, String name) {

		JavaPackage pkg = packages.get(CandidateUtil.getId(place, name));
		if (pkg == null) {
			pkg = new JavaPackage(place, name);
			packages.put(pkg.getId(), pkg);
			LogUtil.getInstance(Parse.class).systemLog("创建JavaPackage[" + pkg.getId() + "]");
		}

		return pkg;
	}

	private void createPackage(JavaClass clazz) {

		String packageName = clazz.getPackageName();

		if (!this.getClassBuilder().getFilter().accept(packageName)) {
			return;
		}

		JavaPackage clazzPackage = addPackage(clazz.getPlace(), packageName);

		if (clazz.isInnerClass()) {
			clazz.setJavaPackage(clazzPackage);
		} else {
			clazzPackage.addClass(clazz);
		}
	}

}
