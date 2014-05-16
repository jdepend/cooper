package jdepend.service.local.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.model.Component;
import jdepend.model.JavaClassRelationType;
import jdepend.model.JavaPackage;
import jdepend.model.Metrics;
import jdepend.model.MetricsMgr;
import jdepend.model.component.JavaPackageComponent;
import jdepend.model.relationtype.JavaClassRelationTypeMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.model.result.AnalysisRunningContext;
import jdepend.model.result.AnalysisRunningContextMgr;
import jdepend.parse.BuildListener;
import jdepend.parse.Parse;
import jdepend.parse.ParseListener;
import jdepend.parse.impl.ParseData;
import jdepend.parse.impl.ParseConfigurator;
import jdepend.service.AnalyseDataDTO;
import jdepend.service.avertcheat.framework.AvertCheat;
import jdepend.service.avertcheat.framework.AvertCheatMgr;
import jdepend.service.context.AnalyseContext;
import jdepend.service.context.AnalyseContextMgr;
import jdepend.service.local.AnalyseData;
import jdepend.service.local.AnalyseListener;
import jdepend.service.local.JDependLocalService;

public final class JDependLocalServiceImpl implements JDependLocalService {

	private String group;

	private String command;

	private Parse parse;

	private Component component;// 组织分析单元的Component

	private ParseConfigurator conf;

	public boolean isLocalRunning = true;

	private ArrayList<AnalyseListener> listeners = new ArrayList<AnalyseListener>();

	public JDependLocalServiceImpl(String groupName, String commandName) {
		this(groupName, commandName, new ParseConfigurator());
	}

	private JDependLocalServiceImpl(String groupName, String commandName,
			ParseConfigurator conf) {
		this.group = groupName;
		this.command = commandName;
		parse = new Parse(conf);
		component = new JavaPackageComponent();
		this.conf = conf;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.core.JDependAnalyssisService#analyze()
	 */
	public AnalysisResult analyze() throws JDependException {

		LogUtil.getInstance(JDependLocalServiceImpl.class).systemLog(
				"analyze is start!");
		// 创建服务上下文
		initServiceContext();
		// 创建运行上下文
		AnalysisRunningContext context = this.createRunningContext();
		// 启动防作弊器
		startAvertCheat(context);
		// 调用解析服务
		Collection<JavaPackage> javaPackages = parse.execute();
		context.setJavaPackages(new ArrayList<JavaPackage>(javaPackages));
		// 设置目标文件分组信息
		context.setTargetFiles(parse.getTargetFileGroupInfo());
		// 组织成组件
		List<Component> components = component.list(javaPackages);

		LogUtil.getInstance(JDependLocalServiceImpl.class).systemLog(
				components.size() + " components is created!");
		// 创建返回结果
		final AnalysisResult result = new AnalysisResult(components, context);
		// 调用分析监听器
		this.onAnalyse(result);
		// 设置End时间
		AnalyseContextMgr.getContext().setExecuteEndTime(
				System.currentTimeMillis());

		return result;
	}

	private void startAvertCheat(AnalysisRunningContext context) {
		for (AvertCheat avertCheat : AvertCheatMgr.getInstance()
				.getAvertCheats()) {
			if (avertCheat.enable(context)) {
				if (avertCheat instanceof AnalyseListener) {
					this.addAnalyseListener((AnalyseListener) avertCheat);
				}
			}
		}
	}

	/**
	 * 创建服务上下文
	 */
	protected void initServiceContext() {
		if (AnalyseContextMgr.getContext() == null) {
			AnalyseContext context = new AnalyseContext();
			context.setGroup(this.group);
			context.setCommand(this.command);
			context.setLocalRunning(true);
			context.setClient("127.0.0.1");
			context.setExecuteStartTime(System.currentTimeMillis());

			AnalyseContextMgr.setContext(context);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jdepend.core.JDependAnalyssisService#setComponent(jdepend.core.Component)
	 */
	public void setComponent(Component component) {
		this.component = component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.core.JDependAnalyssisService#setWriter(java.io.PrintWriter)
	 */
	public void setParseLogWriter(PrintWriter printWriter) {
		parse.setLogWriter(printWriter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.core.JDependAnalyssisService#addDirectory(java.lang.String)
	 */
	public void addDirectory(String name) throws IOException {
		parse.addDirectorys(name);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.core.JDependAnalyssisService#countClasses()
	 */
	public int countClasses() {
		return parse.countClasses();
	}

	public void addParseListener(ParseListener listener) {
		parse.addParseListener(listener);

	}

	public void addBuildListener(BuildListener listener) {
		parse.addBuildListener(listener);
	}

	public void addAnalyzeData(AnalyseDataDTO data) throws JDependException {
		List<byte[]> classes = data.getClasses();
		List<byte[]> configs = data.getConfigs();
		String path = data.getPath();
		boolean ok = false;
		if (classes != null && classes.size() > 0) {
			ParseData analyseData = new ParseData();
			analyseData.setClasses(classes);
			analyseData.setConfigs(configs);
			this.parse.setAnalyseData(analyseData);
			ok = true;
		}

		if (path != null && path.length() > 0) {
			try {
				if (this.parse.getDirectorys() != null
						&& this.parse.getDirectorys().length() > 0
						&& this.parse.getDirectorys().equals(path)) {
					throw new JDependException("分析数据路径已经存在["
							+ this.parse.getDirectorys() + "]");
				} else {
					this.parse.addDirectorys(path);
					ok = true;
				}
			} catch (IOException e) {
				throw new JDependException(e);
			}
		}
		if (!ok) {
			throw new JDependException("分析数据有问题.[" + data + "]");
		}
		// 设置目标文件分组信息
		this.parse.setTargetFileGroupInfo(data.getTargetFiles());
		// 设置组织包的组件
		if (data.getComponent() != null) {
			this.setComponent(data.getComponent());
		}
	}
	
	@Override
	public void setAnalyzeData(AnalyseData data){
		this.parse.setAnalyseData(data.toParseData());
		this.parse.setTargetFileGroupInfo(data.getTargetFiles());
	}

	public void registMetrics(String key, Metrics metrics)
			throws JDependException {
		MetricsMgr.getInstance().addMetrics(key, metrics);
	}

	public void registRelationType(JavaClassRelationType type)
			throws JDependException {
		JavaClassRelationTypeMgr.getInstance().registType(type);

	}

	public void addFilteredPackages(List<String> filteredPackages) {
		this.parse.addFilteredPackages(filteredPackages);

	}

	private AnalysisRunningContext createRunningContext()
			throws JDependException {

		AnalysisRunningContext context = new AnalysisRunningContext();
		context.setLocalRunning(isLocalRunning);

		context.setGroup(group);
		context.setCommand(command);

		context.setPath(parse.getDirectorys());

		context.setComponent(component);

		context.setAnalyzeInnerClasses(conf.getAnalyzeInnerClasses());
		context.setEnableAbstractClassCountQualificationConfirmer(conf
				.enableAbstractClassCountQualificationConfirmer());
		context.setSaveResult((new PropertyConfigurator()).isSaveResult());

		context.setClient(AnalyseContextMgr.getContext().getClient());
		context.setUserName(AnalyseContextMgr.getContext().getUserName());

		AnalysisRunningContextMgr.setContext(context);

		return context;
	}

	@Override
	public void setLocalRunning(boolean isLocalRunning) {
		this.isLocalRunning = isLocalRunning;
	}

	public void addAnalyseListener(AnalyseListener listener) {
		listeners.add(listener);
	}

	protected void onAnalyse(AnalysisResult result) throws JDependException {
		Collections.sort(listeners);
		for (AnalyseListener listener : listeners) {
			listener.onAnalyse(result);
		}
	}
}
