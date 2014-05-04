package jdepend.service.local;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JavaClassRelationType;
import jdepend.model.Metrics;
import jdepend.model.result.AnalysisResult;
import jdepend.parse.BuildListener;
import jdepend.parse.ParseListener;
import jdepend.service.AnalyseDataDTO;

/**
 * 本地后台服务接口
 * 
 * @author <b>Abner</b>
 * 
 */
public interface JDependLocalService {

	/**
	 * 分析服务
	 * 
	 * @return
	 * @throws JDependException
	 */
	public abstract AnalysisResult analyze() throws JDependException;

	/**
	 * 设置组件模型
	 * 
	 * @param component
	 */
	public abstract void setComponent(Component component);

	/**
	 * 设置解析日志输出器
	 * 
	 * @param printWriter
	 */
	public abstract void setParseLogWriter(PrintWriter printWriter);

	/**
	 * 增加分析数据路径
	 * 
	 * @param name
	 * @throws IOException
	 */
	public abstract void addDirectory(String name) throws IOException;

	/**
	 * 增加分析数据
	 * 
	 * @param name
	 * @throws IOException
	 */
	public abstract void addAnalyzeData(AnalyseDataDTO data) throws JDependException;
	
	/**
	 * 计算分析目标的类个数
	 * @return
	 */
	public abstract int countClasses();
	
	/**
	 * 增加解析监听器
	 * @param listener
	 */
	public abstract void addParseListener(ParseListener listener);

	/**
	 * 增加构建监听器
	 * @param listener
	 */
	public abstract void addBuildListener(BuildListener listener);

	/**
	 * 增加分析监听器
	 * @param listener
	 */
	public abstract void addAnalyseListener(AnalyseListener listener);
	
	/**
	 * 注册指标
	 * 
	 * @param key
	 * @param metrics
	 * @throws JDependException
	 */
	public abstract void registMetrics(String key, Metrics metrics) throws JDependException;
	
	/**
	 * 注册类关系
	 * @param type
	 * @throws JDependException
	 */
	public abstract void registRelationType(JavaClassRelationType type) throws JDependException;

	/**
	 * 增加过滤掉的包
	 * 
	 * @param filteredPackages
	 */
	public abstract void addFilteredPackages(List<String> filteredPackages);
	/**
	 * 设置是否本地运行
	 * 
	 * @param isLocalRunning
	 */
	public void setLocalRunning(boolean isLocalRunning);
}
