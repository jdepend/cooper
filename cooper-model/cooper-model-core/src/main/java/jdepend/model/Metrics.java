package jdepend.model;

import java.io.Serializable;

/**
 * 定制指标接口
 * 
 * @author <b>Abner</b>
 * 
 */
public interface Metrics extends Serializable {

	/**
	 * 得到指标信息
	 * 
	 * @param unit
	 * @return
	 */
	public MetricsInfo getMetrics(JDependUnit unit);
}
