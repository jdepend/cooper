package jdepend.service.remote.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.exception.JDependException;
import jdepend.knowledge.database.AnalysisResultRepository;
import jdepend.model.JavaClassRelationType;
import jdepend.model.result.AnalysisResult;
import jdepend.service.context.AnalyseContext;
import jdepend.service.context.AnalyseContextMgr;
import jdepend.service.local.JDependLocalService;
import jdepend.service.local.impl.JDependLocalServiceImpl;
import jdepend.service.remote.AnalyseDataDTO;
import jdepend.service.remote.JDependRemoteService;
import jdepend.service.remote.JDependRequest;
import jdepend.service.remote.JDependSession;
import jdepend.service.remote.ServiceMonitor;
import jdepend.service.remote.ServiceObserved;

public class JDependRemoteServiceImpl extends UnicastRemoteObject implements JDependRemoteService, ServiceObserved {

	private List<ServiceMonitor> monitors = new ArrayList<ServiceMonitor>();

	public JDependRemoteServiceImpl() throws java.rmi.RemoteException {
		super();
	}

	protected void onAnalyse(JDependRequest request) throws JDependException {
		for (ServiceMonitor monitor : monitors) {
			monitor.onAnalyse(request);
		}
	}

	public AnalysisResult analyze(JDependRequest request, AnalyseDataDTO data) throws RemoteException {
		try {
			// 创建服务上下文
			this.initServiceContext(request);
			// 发送分析提醒
			this.onAnalyse(request);
			// 创建本地服务
			JDependLocalService localService = new JDependLocalServiceImpl(request.getGroupName(),
					request.getCommandName());
			// 设置组织包的组件
			if (data.getComponent() != null) {
				localService.setComponent(data.getComponent());
			}
			// 设置分析数据
			localService.setAnalyzeData(data.getAnalyzeData());

			String path = data.getPath();
			if (path != null && path.length() > 0) {
				localService.addDirectory(path);
			}

			// 设置FileterPackages
			localService.addFilteredPackages(data.getFilteredPackages());
			// 增加解析监听器
			localService.addParseListener(new RemoteParseListener(JDependSessionMgr.getInstance().getSession(request)));
			// 设置运行环境
			localService.setLocalRunning(false);
			// 分析服务
			AnalysisResult result = localService.analyze();
			// 保存分析结果
			if (new PropertyConfigurator().isSaveResult()) {
				AnalysisResultRepository.save(result);
			}
			// 返回结果
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}

	protected void initServiceContext(JDependRequest request) throws JDependException {
		if (AnalyseContextMgr.getContext() == null) {
			JDependSession session = JDependSessionMgr.getInstance().getSession(request);
			AnalyseContext context = new AnalyseContext();
			context.setClient(session.getClient());
			context.setCommand(request.getCommandName());
			context.setExecuteStartTime(System.currentTimeMillis());
			context.setGroup(request.getGroupName());
			context.setLocalRunning(false);
			context.setUserName(session.getUserName());

			AnalyseContextMgr.setContext(context);
		}
	}

	public void addMonitor(ServiceMonitor monitor) {
		if (!this.monitors.contains(monitor)) {
			this.monitors.add(monitor);
		}
	}

	@Override
	public int getAnalyzeSchedule(JDependRequest request) throws RemoteException {
		try {
			return JDependSessionMgr.getInstance().getSession(request).getAnalyzeSchedule();
		} catch (JDependException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}
}
