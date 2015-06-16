package jdepend.server.service.start;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.LogUtil;
import jdepend.framework.persistent.ConnectionFactory;
import jdepend.framework.util.JDependUtil;
import jdepend.framework.util.VersionUtil;
import jdepend.server.service.RemoteServiceFactory;
import jdepend.server.service.persistent.ServerConnectionProvider;
import jdepend.server.service.JDependRemoteService;
import jdepend.server.service.analyzer.AnalyzerService;
import jdepend.server.service.score.ScoreRemoteService;
import jdepend.server.service.session.JDependSessionService;
import jdepend.server.service.user.UserRemoteService;

/**
 * 后台启动远程服务
 * 
 * @author wangdg
 * 
 */
public final class RemoteServiceStart {

	private Registry registry;

	private JDependRemoteService service;

	/**
	 * @param args
	 * @throws JDependException
	 */
	public static void main(String[] args) throws JDependException {
		initEnv(args);
		RemoteServiceStart start = new RemoteServiceStart();
		System.out.println("启动服务[版本：" + VersionUtil.getVersion() + " 构建时间：" + VersionUtil.getBuildDate() + "]。。。");

		start.startService();
		System.out.println("启动服务完成，按[q]或[Q]退出服务。。。");
		int i;
		try {
			while (true) {
				i = System.in.read();
				if ((char) i == 'q' || (char) i == 'Q')
					break;
				if (i == 13 || i == 10)
					continue;
			}
			System.out.println("停止服务。。。");
			start.stopService();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void initEnv(String[] args) {
		// 设置workspacePath
		String workspacePath = JDependUtil.getArg(args, "-workspacePath");
		if (workspacePath != null) {
			JDependContext.setWorkspacePath(args[2]);
		} else {
			JDependContext.setWorkspacePath(System.getProperty("user.dir"));
		}
		// 设置RunningPath
		JDependContext.setRunningPath(System.getProperty("user.dir"));
		// 设置ConnectionProvider
		ConnectionFactory.setProvider(new ServerConnectionProvider());
		// 设置日志是否打印
		PropertyConfigurator conf = new PropertyConfigurator();
		BusiLogUtil.BUSINESSLOG = conf.isPrintBusiLog();
		LogUtil.SYSTEMLOG = conf.isPrintSystemLog();
		LogUtil.SYSTEMWARNING = conf.isPrintSystemWarning();

		// ServiceFactory.initClassList();
	}

	public synchronized void startService() throws JDependException {
		try {
			// 创建远程服务端口
			this.registry = LocateRegistry.createRegistry(1099);
			// 绑定Session服务
			LogUtil.getInstance(RemoteServiceStart.class).systemLog("绑定Session服务");
			JDependSessionService sessionService = RemoteServiceFactory.createJDependSessionService();
			Naming.rebind("rmi://localhost:1099/JDependSessionService", sessionService);
			// 绑定解析服务
			LogUtil.getInstance(RemoteServiceStart.class).systemLog("绑定解析服务");
			this.service = RemoteServiceFactory.createJDependRemoteService();
			Naming.rebind("rmi://localhost:1099/JDependRemoteService", service);
			// 分析器远程服务
			LogUtil.getInstance(RemoteServiceStart.class).systemLog("分析器远程服务");
			AnalyzerService analyzerService = RemoteServiceFactory.createAnalyzerService();
			Naming.rebind("rmi://localhost:1099/AnalyzerService", analyzerService);
			// 用户远程服务
			LogUtil.getInstance(RemoteServiceStart.class).systemLog("用户远程服务");
			UserRemoteService userRemoteService = RemoteServiceFactory.createUserRemoteService();
			Naming.rebind("rmi://localhost:1099/UserRemoteService", userRemoteService);
			// 分数远程服务
			LogUtil.getInstance(RemoteServiceStart.class).systemLog("分数远程服务");
			ScoreRemoteService scoreRemoteService = RemoteServiceFactory.createScoreRemoteService();
			Naming.rebind("rmi://localhost:1099/ScoreRemoteService", scoreRemoteService);
		} catch (Exception e) {
			throw new JDependException("启动远程服务错误！", e);
		}
	}

	public synchronized void stopService() throws JDependException {
		try {
			// 解除解析服务
			LogUtil.getInstance(RemoteServiceStart.class).systemLog("解除解析服务");
			Naming.unbind("rmi://localhost:1099/JDependRemoteService");
			// 解除Session服务
			LogUtil.getInstance(RemoteServiceStart.class).systemLog("解除Session服务");
			Naming.unbind("rmi://localhost:1099/JDependSessionService");
			// 解除分析器远程服务
			LogUtil.getInstance(RemoteServiceStart.class).systemLog("解除分析器远程服务");
			Naming.unbind("rmi://localhost:1099/AnalyzerService");
			// 解除用户远程服务
			LogUtil.getInstance(RemoteServiceStart.class).systemLog("解除用户远程服务");
			Naming.unbind("rmi://localhost:1099/UserRemoteService");
			// 解除分数远程服务
			LogUtil.getInstance(RemoteServiceStart.class).systemLog("解除分数远程服务");
			Naming.unbind("rmi://localhost:1099/ScoreRemoteService");
			// 停止服务
			UnicastRemoteObject.unexportObject(registry, true);
		} catch (Exception e) {
			throw new JDependException("解除解析服务绑定错误！", e);
		}
	}

}
