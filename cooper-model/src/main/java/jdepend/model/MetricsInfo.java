package jdepend.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 指标信息
 * 
 * @author <b>Abner</b>
 * 
 */
public class MetricsInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5782842839715963531L;

	private float metrics = 0F;

	private Map<String, Object> extendInfo = new HashMap<String, Object>();

	public float getMetrics() {
		return metrics;
	}

	public void setMetrics(float metrics) {
		this.metrics = metrics;
	}

	public Map<String, Object> getExtendInfo() {
		return extendInfo;
	}

	public void addExtendInfo(String key, Object info) {
		this.extendInfo.put(key, info);
	}

}
