package jdepend.model.area;

import java.util.List;

import jdepend.model.AreaComponent;
import jdepend.model.result.AnalysisResult;

/**
 * 组件区域创建器
 * 
 * @author user
 * 
 */
public interface AreaCreator {

	public String getName();

	/**
	 * 可以确定属于Area的组件总数
	 * 
	 * @param result
	 * @return
	 */
	public int coverCount(AnalysisResult result);

	/**
	 * 可以确定的Area的总数
	 * 
	 * @return
	 */
	public int areaCount();

	/**
	 * 创建组件区域
	 * 
	 * @return
	 */
	public List<AreaComponent> create();

}
