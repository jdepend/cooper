package jdepend.core.serviceproxy;

import java.io.PrintWriter;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JavaClassRelationType;
import jdepend.model.Metrics;
import jdepend.model.result.AnalysisResult;
import jdepend.parse.BuildListener;
import jdepend.parse.ParseListener;
import jdepend.service.local.AnalyseListener;

/**
 * 后台服务代理
 * 
 * @author <b>Abner</b>
 * 
 */
public interface JDependServiceProxy {

	/**
	 * 分析过程
	 * 
	 * @return 分析单元集合
	 * @throws JDependException
	 */
	public abstract AnalysisResult analyze() throws JDependException;

	/**
	 * 设置组件
	 * 
	 * 该组件主要是判断哪些package集合是一个分析单元
	 * 
	 * @param component
	 */
	public abstract void setComponent(Component component);

	/**
	 * 设置log writer
	 * 
	 * @param printWriter
	 */
	public abstract void setLogWriter(PrintWriter printWriter);

	/**
	 * 设置分析目标路径
	 * 
	 * @param name
	 * @throws JDependException
	 */
	public abstract void addDirectory(String name) throws JDependException;

	/**
	 * 计算类总数
	 * 
	 * @return
	 */
	public abstract int countClasses();

	/**
	 * 增加解析接口
	 * 
	 * @param listener
	 */
	public abstract void addParseListener(ParseListener listener);

	/**
	 * 增加构建接口
	 * 
	 * @param listener
	 */
	public abstract void addBuildListener(BuildListener listener);

	/**
	 * 增加分析接口
	 * 
	 * @param listener
	 */
	public abstract void addAnalyseListener(AnalyseListener listener);

	/**
	 * 注册新的指标
	 * 
	 * @param key
	 * @param metrics
	 */
	public abstract void registMetrics(String key, Metrics metrics);

	/**
	 * 注册新的关系类型
	 * 
	 * @param type
	 */
	public abstract void registRelationType(JavaClassRelationType type);

	/**
	 * 设置filteredPackages
	 * 
	 * @param filteredPackages
	 */
	public abstract void addFilteredPackages(List<String> filteredPackages);
}
