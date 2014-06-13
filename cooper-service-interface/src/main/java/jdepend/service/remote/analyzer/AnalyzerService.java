package jdepend.service.remote.analyzer;

import java.rmi.Remote;
import java.util.List;

import jdepend.framework.exception.JDependException;

/**
 * 分析器远程服务
 * 
 * @author wangdg
 * 
 */
public interface AnalyzerService extends Remote {

	/**
	 * 上传分析器
	 * 
	 * @param analyzerDTO
	 *            分析器信息
	 * @throws java.rmi.RemoteException
	 */
	public void upload(AnalyzerDTO analyzerDTO) throws java.rmi.RemoteException;

	/**
	 * 获得指定类型的分析器摘要集合
	 * 
	 * @param type
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public List<AnalyzerSummaryDTO> getAnalyzsers(String type) throws java.rmi.RemoteException;

	/**
	 * 下载指定分析器
	 * 
	 * @param className
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public AnalyzerDTO download(String className) throws java.rmi.RemoteException;

	/**
	 * 删除分析器
	 * 
	 * @param className
	 * @throws JDependException 
	 */
	public void delete(String className) throws JDependException;

	/**
	 * 获取全部的分析器信息
	 * 
	 * @return
	 * @throws JDependException 
	 */
	public List<AnalyzerSummaryDTO> queryAll() throws JDependException;

}
