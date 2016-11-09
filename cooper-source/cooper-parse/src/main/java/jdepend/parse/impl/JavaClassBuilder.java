package jdepend.parse.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import jdepend.framework.exception.JDependException;
import jdepend.framework.file.AnalyzeData;
import jdepend.framework.file.TargetFileInfo;
import jdepend.framework.log.LogUtil;
import jdepend.framework.util.ThreadPool;
import jdepend.metadata.JavaClass;
import jdepend.metadata.util.JavaClassCollection;
import jdepend.metadata.util.JavaClassUtil;
import jdepend.parse.ParseConfigurator;
import jdepend.parse.ParseListener;
import jdepend.parse.sql.ConfigParseMgr;

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

	private int count = 0;

	public JavaClassBuilder(ParseConfigurator conf) {
		this.setConf(conf);

		this.parser = new ClassFileParserFactory().createParser(conf);
	}

	@Override
	public Collection<JavaClass> build(AnalyzeData data) {
		if (this.javaClasses == null || this.getConf().getEveryClassBuild()) {
			javaClasses = new HashSet<JavaClass>();
			// 设置本次分析的classNames
			this.parser.getConf().getPackageFilter().setClassNames(data.getClassNames());
			// 解析Config
			if (this.isParseConfigs()) {
				this.parseConfigs(data.getConfigs());
			}
			// 解析JavaClasses
			this.parseClasses(data.getClasses());
			// 补充JavaClassDetail信息
			if (this.isSupplyJavaClassDetail()) {
				LogUtil.getInstance(JavaClassBuilder.class).systemLog(
						"开始填充Class细节，Class的个数为：" + this.javaClasses.size());
				JavaClassUtil.supplyJavaClassDetail(new JavaClassCollection(this.parser.getConf()
						.getJavaClassRelationTypes(), this.javaClasses));
			}

			// 添加外部classes
			this.appendExtClasses();
			// 建立Class的关系
			if (this.isBuildClassRelation()) {
				LogUtil.getInstance(JavaClassBuilder.class).systemLog("开始建立Class的关系");
				(new JavaClassRelationCreator(this.getConf())).create(new JavaClassCollection(this.parser.getConf()
						.getJavaClassRelationTypes(), this.javaClasses));
			}
			// 发出事件
			this.onClassBuild(this.javaClasses);
		}
		return this.javaClasses;
	}

	private void parseConfigs(Map<String, List<TargetFileInfo>> configs) {
		try {
			ConfigParseMgr.getInstance().parse(configs);
		} catch (JDependException e) {
			e.printStackTrace();
		}
	}

	private void parseClasses(Map<String, List<TargetFileInfo>> classes) {

		count = 0;
		String parsemodel = getParseModel();
		if (parsemodel != null) {
			parser.setModel(parsemodel);
		}

		ExecutorService pool = ThreadPool.getPool();

		for (final String place : classes.keySet()) {
			for (final TargetFileInfo classData : classes.get(place)) {
				pool.execute(new Runnable() {
					@Override
					public void run() {
						InputStream is = null;
						try {
							LogUtil.getInstance(JavaClassBuilder.class).systemLog(
									"开始解析文件[" + classData.getName() + "]" + (count++));
							is = new ByteArrayInputStream(classData.getContent());
							JavaClass javaClass = parser.parse(place, is);
							if (parser.getConf().getPackageFilter().accept(javaClass.getPackageName())) {
								synchronized (javaClasses) {
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
				});
			}
		}

		ThreadPool.awaitTermination(pool);
	}

	private void appendExtClasses() {
		Collection<JavaClass> extClasses = new IdentifyExtClassesUtil(this.parser.getConf().getPackageFilter())
				.identify(this.javaClasses);
		this.javaClasses.addAll(extClasses);
	}

	@Override
	public PackageFilter getFilter() {
		return parser.getConf().getPackageFilter();
	}

	@Override
	public void addParseListener(ParseListener listener) {
		this.parser.addParseListener(listener);

	}

	@Override
	public void setWriter(PrintWriter writer) {
		this.parser.setWriter(writer);

	}
}
