package jdepend.framework.ui.graph;

import org.jfree.chart.JFreeChart;

/**
 * 图形生成器
 * 
 * @author wangdg
 * 
 */
public interface ChartCreater {

	public JFreeChart create(GraphDataItem item);

	public String getType();

}
