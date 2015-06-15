package jdepend.service.remote;

import jdepend.framework.exception.JDependException;

/**
 * 服务监听者接口
 * 
 * @author wangdg
 * 
 */
public interface ServiceMonitor{

	public void onAnalyse(JDependRequest request) throws JDependException;

}
