package jdepend.framework.ui.graph.creater;

import java.awt.Font;

import jdepend.framework.ui.graph.model.GraphDataItem;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public final class BarCreater extends AbstractChartCreater {

	@Override
	public JFreeChart create(GraphDataItem item) {
		return createChart(item, createDataset(item));
	}

	@Override
	public String getType() {
		return GraphDataItem.BAR;
	}

	private static CategoryDataset createDataset(GraphDataItem item) {
		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		for (Object name : item.getDatas().keySet()) {
			defaultcategorydataset.addValue((Float) item.getDatas().get(name), "Series 1", (String) name);
		}
		return defaultcategorydataset;
	}

	private static JFreeChart createChart(GraphDataItem item, CategoryDataset categorydataset) {
		JFreeChart jfreechart = ChartFactory.createStackedBarChart(item.getTitle(), item.getLineXName(),
				item.getLineYName(), categorydataset, PlotOrientation.VERTICAL, true, true, false);

		Font font = new Font("宋体", Font.PLAIN, 13);
		jfreechart.getTitle().setFont(font);
		jfreechart.getLegend().setItemFont(font);

		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		categoryplot.getDomainAxis().setTickLabelFont(font);
		categoryplot.getDomainAxis().setLabelFont(font);
		categoryplot.getDomainAxis().setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(0.392D));
		categoryplot.getRangeAxis().setTickLabelFont(font);
		categoryplot.getRangeAxis().setLabelFont(font);
		categoryplot.setBackgroundPaint(ChartColor.WHITE);

		StackedBarRenderer stackedbarrenderer = (StackedBarRenderer) categoryplot.getRenderer();
		stackedbarrenderer.setDrawBarOutline(false);
		stackedbarrenderer.setBaseItemLabelsVisible(true);
		stackedbarrenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());

		int index = 0;
		for (Object name : item.getDatas().keySet()) {
			stackedbarrenderer
					.setSeriesPaint(categorydataset.getRowIndex((String) name), COLORS[index % COLORS.length]);
			index++;

		}

		return jfreechart;
	}

}
