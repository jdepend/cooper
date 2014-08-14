package jdepend.parse.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.framework.file.AnalyzeData;
import jdepend.framework.file.TargetFileInfo;
import jdepend.framework.log.LogUtil;
import jdepend.model.JavaClass;
import jdepend.model.util.JavaClassUtil;
import jdepend.parse.ParseConfigurator;
import jdepend.parse.ParseListener;

/**
 * The <code>JavaClassBuilder</code> builds <code>JavaClass</code> instances
 * from .class, .jar, .war, or .zip files.
 * 
 * @author <b>Abner</b>
 * 
 */

public class JavaClassBuilder extends AbstractClassBuilder {

	private AbstractParser parser;

	private Collection<JavaClass> javaClasses;

	public JavaClassBuilder(ParseConfigurator conf) {
		this.setConf(conf);

		this.parser = new ClassFileParserFactory().createParser(conf);
	}

	@Override
	public Collection<JavaClass> build(AnalyzeData data) {
		if (this.javaClasses == null || this.getConf().getEveryClassBuild()) {
			javaClasses = new HashSet<JavaClass>();
			// 解析Config
			this.parseConfigs(data.getConfigs());
			// 解析JavaClasses
			this.parseClasses(data.getClasses());
			// 补充JavaClassDetail信息
			JavaClassUtil.supplyJavaClassDetail(this.getJavaClasses());
			// 添加外部classes
			this.appendExtClasses();
			// 建立Class的关系
			if (this.isBuildClassRelation()) {
				LogUtil.getInstance(JavaClassBuilder.class).systemLog(
						"开始建立Class的关系，Class的个数为：" + this.getJavaClasses().size());
				(new JavaClassRelationCreator(this.getConf())).create(this.javaClasses);
			}
			// 发出事件
			this.onClassBuild(this.getJavaClasses());
		}
		return this.getJavaClasses();
	}

	private void parseConfigs(Map<String, List<TargetFileInfo>> configs) {
		try {
			ConfigParseMgr.getInstance().parse(configs);
		} catch (JDependException e) {
			e.printStackTrace();
		}
	}

	private void parseClasses(Map<String, List<TargetFileInfo>> classes) {

		for (final String place : classes.keySet()) {
			for (final TargetFileInfo classData : classes.get(place)) {
				InputStream is = null;
				try {
					is = new ByteArrayInputStream(classData.getContent());
					JavaClass javaClass = parser.parse(is);
					javaClass.setPlace(place);
					javaClass.calImportedPackages();
					if (parser.getFilter().accept(javaClass.getPackageName())) {
						if (!javaClasses.contains(javaClass)) {
							javaClasses.add(javaClass);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}
		}

	}

	private void appendExtClasses() {
		Collection<JavaClass> extClasses = new IdentifyExtClassesUtil(this.parser.getFilter())
				.identify(this.javaClasses);
		this.javaClasses.addAll(extClasses);
	}

	@Override
	public PackageFilter getFilter() {
		return this.parser.getFilter();
	}

	@Override
	public void addParseListener(ParseListener listener) {
		this.parser.addParseListener(listener);

	}

	@Override
	public void setWriter(PrintWriter writer) {
		this.parser.setWriter(writer);

	}

	private Collection<JavaClass> getJavaClasses() {
		return this.javaClasses;
	}
}
