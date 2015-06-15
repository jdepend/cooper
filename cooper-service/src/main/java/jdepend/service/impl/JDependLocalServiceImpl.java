package jdepend.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.file.AnalyzeData;
import jdepend.framework.log.LogUtil;
import jdepend.metadata.JavaPackage;
import jdepend.model.Component;
import jdepend.model.result.AnalysisResult;
import jdepend.model.result.AnalysisRunningContext;
import jdepend.parse.BuildListener;
import jdepend.parse.Parse;
import jdepend.parse.ParseConfigurator;
import jdepend.parse.ParseException;
import jdepend.parse.ParseListener;
import jdepend.service.AnalyseListener;
import jdepend.service.JDependLocalService;
import jdepend.service.ServiceException;
import jdepend.service.avertcheat.framework.AvertCheat;
import jdepend.service.avertcheat.framework.AvertCheatMgr;
import jdepend.service.config.ServiceConfigurator;

public final class JDependLocalServiceImpl implements JDependLocalService {

	private String group;

	private String command;

	private Parse parse;

	private Component component;// 组织分析单元的Component

	private ParseConfigurator parseConf;

	private ServiceConfigurator serviceConf;

	private AnalyseContext context;

	private ArrayList<AnalyseListener> listeners = new ArrayList<AnalyseListener>();

	public JDependLocalServiceImpl(String groupName, String commandName) {
		this(groupName, commandName, new ParseConfigurator());
	}

	private JDependLocalServiceImpl(String groupName, String commandName, ParseConfigurator parseConf) {
		this.group = groupName;
		this.command = commandName;
		parse = new Parse(parseConf);
		component = Component.getDefaultComponent();
		this.parseConf = parseConf;
		this.serviceConf = new ServiceConfigurator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.client.core.JDependAnalyssisService#analyze()
	 */
	public AnalysisResult analyze() throws ServiceException {

		try {
			LogUtil.getInstance(JDependLocalServiceImpl.class).systemLog("analyze is start!");
			// 创建服务上下文
			initServiceContext();
			// 创建运行上下文
			AnalysisRunningContext context = this.createRunningContext();
			// 启动防作弊器
			startAvertCheat(context);
			// 调用解析服务
			Collection<JavaPackage> javaPackages = parse.execute();
			context.setJavaPackages(new ArrayList<JavaPackage>(javaPackages));
			// 组织成组件
			List<Component> components = component.list(javaPackages);

			LogUtil.getInstance(JDependLocalServiceImpl.class).systemLog(components.size() + " components is created!");
			// 创建返回结果
			final AnalysisResult result = new AnalysisResult(components, context);

			LogUtil.getInstance(JDependLocalServiceImpl.class).systemLog("analysisResult is created!");
			// 调用分析监听器
			this.onAnalyse(result);

			LogUtil.getInstance(JDependLocalServiceImpl.class).systemLog("onAnalyse is finished!");
			// 设置End时间
			this.context.setExecuteEndTime(System.currentTimeMillis());

			return result;
		} catch (JDependException e) {
			throw new ServiceException(e);
		}
	}

	private void initServiceContext() {
		if (this.context == null) {
			this.initServiceContext(true, null, null);
		}
	}

	private AnalysisRunningContext createRunningContext() {

		AnalysisRunningContext context = new AnalysisRunningContext();

		context.setGroup(group);
		context.setCommand(command);

		context.setPath(parse.getDirectorys());

		context.setComponent(component);

		context.setAnalyzeInnerClasses(parseConf.getAnalyzeInnerClasses());
		context.setEnableAbstractClassCountQualificationConfirmer(serviceConf
				.enableAbstractClassCountQualificationConfirmer());
		context.setCalJavaClassCycle(serviceConf.isCalJavaClassCycle());
		context.setSaveResult(serviceConf.isSaveResult());

		context.setLocalRunning(context.isLocalRunning());
		context.setClient(context.getClient());
		context.setUserName(context.getUserName());

		return context;
	}

	private void startAvertCheat(AnalysisRunningContext context) {
		for (AvertCheat avertCheat : AvertCheatMgr.getInstance().getAvertCheats()) {
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
	@Override
	public void initServiceContext(boolean isLocalRunning, String client, String userName) {

		context = new AnalyseContext();

		context.setLocalRunning(isLocalRunning);
		context.setClient(client);
		context.setUserName(userName);

		context.setExecuteStartTime(System.currentTimeMillis());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jdepend.client.core.JDependAnalyssisService#setComponent(jdepend.client
	 * .core.Component)
	 */
	public void setComponent(Component component) {
		this.component = component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jdepend.client.core.JDependAnalyssisService#setWriter(java.io.PrintWriter
	 * )
	 */
	public void setParseLogWriter(PrintWriter printWriter) {
		parse.setLogWriter(printWriter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * jdepend.client.core.JDependAnalyssisService#addDirectory(java.lang.String
	 * )
	 */
	public void addDirectory(String name) throws IOException {
		parse.addDirectorys(name);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jdepend.client.core.JDependAnalyssisService#countClasses()
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

	@Override
	public void setAnalyzeData(AnalyzeData data) {
		this.parse.setAnalyseData(data);
	}

	public void addFilteredPackages(List<String> filteredPackages) {
		this.parse.addFilteredPackages(filteredPackages);

	}

	public void addAnalyseListener(AnalyseListener listener) {
		listeners.add(listener);
	}

	protected void onAnalyse(AnalysisResult result) throws ServiceException {
		Collections.sort(listeners);
		for (AnalyseListener listener : listeners) {
			listener.onAnalyse(result);
		}
	}

	@Override
	public Collection<JavaPackage> getPackages() throws ServiceException {

		this.parse.setParseConfigs(false);
		this.parse.setSupplyJavaClassDetail(false);
		this.parse.setBuildClassRelation(false);

		try {
			return new ArrayList<JavaPackage>(this.parse.execute());
		} catch (ParseException e) {
			throw new ServiceException(e);
		}
	}
}
