package jdepend.client.core.remote.analyzer;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import jdepend.client.core.remote.config.ServerConfigurator;
import jdepend.framework.exception.JDependException;
import jdepend.service.remote.analyzer.AnalyzerService;

/**
 * 客户端Analyzer代理
 * 
 * @author wangdg
 * 
 */
public final class RemoteAnalyzerProxy {

	private static RemoteAnalyzerProxy proxy = new RemoteAnalyzerProxy();

	private AnalyzerService analyzerService;

	private static final String DEFAULT_ANALYZER_SERVICE = "rmi://localhost/AnalyzerService";

	private RemoteAnalyzerProxy() {
	}

	public static RemoteAnalyzerProxy getInstance() {
		return proxy;
	}

	public AnalyzerService getAnalyzerService() throws JDependException {
		if (this.analyzerService == null) {
			try {
				analyzerService = (AnalyzerService) Naming.lookup(getAnalyzerServiceURL());
			} catch (ConnectException e) {
				e.printStackTrace();
				throw new JDependException("连接服务器失败！", e);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				throw new JDependException("URL地址错误！", e);
			} catch (RemoteException e) {
				e.printStackTrace();
				throw new JDependException("远程服务失败！", e);
			} catch (NotBoundException e) {
				e.printStackTrace();
				throw new JDependException("没有查询到绑定服务！", e);
			}
		}
		return this.analyzerService;
	}

	private static String getAnalyzerServiceURL() {

		String analyzerServiceURL = (new ServerConfigurator()).getAnalyzerServiceURL();
		if (analyzerServiceURL == null) {
			analyzerServiceURL = DEFAULT_ANALYZER_SERVICE;
		}
		return analyzerServiceURL;
	}
}
