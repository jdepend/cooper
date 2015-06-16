package jdepend.server.service;

import jdepend.framework.exception.JDependException;
import jdepend.server.service.session.JDependRequest;

/**
 * 服务监听者接口
 * 
 * @author wangdg
 * 
 */
public interface ServiceMonitor{

	public void onAnalyse(JDependRequest request) throws JDependException;

}
