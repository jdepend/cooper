package jdepend.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.file.AnalyzeData;
import jdepend.metadata.JavaPackage;
import jdepend.model.Component;
import jdepend.model.result.AnalysisResult;
import jdepend.parse.BuildListener;
import jdepend.parse.ParseListener;

/**
 * 本地后台服务接口
 * 
 * @author <b>Abner</b>
 * 
 */
public interface JDependLocalService {

	/**
	 * 创建服务上下文
	 */
	public void initServiceContext(boolean isLocalRunning, String client, String userName);

	/**
	 * 分析服务
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public AnalysisResult analyze() throws ServiceException;

	/**
	 * 设置组件模型
	 * 
	 * @param component
	 */
	public void setComponent(Component component);

	/**
	 * 设置解析日志输出器
	 * 
	 * @param printWriter
	 */
	public void setParseLogWriter(PrintWriter printWriter);

	/**
	 * 增加分析数据路径
	 * 
	 * @param name
	 * @throws IOException
	 */
	public void addDirectory(String name) throws IOException;

	/**
	 * 增加分析数据
	 * 
	 * @param data
	 */
	public void setAnalyzeData(AnalyzeData data);

	/**
	 * 计算分析目标的类个数
	 * 
	 * @return
	 */
	public int countClasses();

	/**
	 * 增加解析监听器
	 * 
	 * @param listener
	 */
	public void addParseListener(ParseListener listener);

	/**
	 * 增加构建监听器
	 * 
	 * @param listener
	 */
	public void addBuildListener(BuildListener listener);

	/**
	 * 增加分析监听器
	 * 
	 * @param listener
	 */
	public void addAnalyseListener(AnalyseListener listener);

	/**
	 * 增加过滤掉的包
	 * 
	 * @param filteredPackages
	 */
	public void addFilteredPackages(List<String> filteredPackages);
	/**
	 * 得到服务分析目标包含的包集合
	 * 
	 * @return
	 * @throws JDependException
	 */
	public Collection<JavaPackage> getPackages() throws ServiceException;
}
