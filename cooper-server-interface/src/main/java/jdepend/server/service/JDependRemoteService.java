package jdepend.server.service;

import java.rmi.Remote;
import java.util.Collection;

import jdepend.metadata.JavaPackage;
import jdepend.model.result.AnalysisResult;

/**
 * 远程后台服务接口
 * 
 * @author <b>Abner</b>
 * 
 */
public interface JDependRemoteService extends Remote {
	/**
	 * 分析服务
	 * 
	 * @param request
	 * @param data
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public abstract AnalysisResult analyze(JDependRequest request, AnalyseDataDTO data) throws java.rmi.RemoteException;

	/**
	 * 获得远程分析进度
	 * 
	 * @param request
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public abstract int getAnalyzeSchedule(JDependRequest request) throws java.rmi.RemoteException;

	/**
	 * 增加服务监听者
	 * 
	 * @param monitor
	 */
	public void addMonitor(ServiceMonitor monitor) throws java.rmi.RemoteException;

	/**
	 * 得到分析目标包含的包集合
	 * 
	 * @param data
	 * @param request
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public abstract Collection<JavaPackage> getPackages(JDependRequest request, AnalyseDataDTO data)
			throws java.rmi.RemoteException;
}
