// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package jdepend.framework.ui.graph;

import java.awt.Dimension;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class StackedBarChartDemo1 extends ApplicationFrame {

	public StackedBarChartDemo1(String s) {
		super(s);
		JPanel jpanel = createDemoPanel();
		jpanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(jpanel);
	}

	private static CategoryDataset createDataset() {
		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		defaultcategorydataset.addValue(32.399999999999999D, "Series 1", "Category 1");
		defaultcategorydataset.addValue(17.800000000000001D, "Series 2", "Category 1");
		defaultcategorydataset.addValue(27.699999999999999D, "Series 3", "Category 1");
		defaultcategorydataset.addValue(43.200000000000003D, "Series 1", "Category 2");
		defaultcategorydataset.addValue(15.6D, "Series 2", "Category 2");
		defaultcategorydataset.addValue(18.300000000000001D, "Series 3", "Category 2");
		defaultcategorydataset.addValue(23D, "Series 1", "Category 3");
		defaultcategorydataset.addValue(11.300000000000001D, "Series 2", "Category 3");
		defaultcategorydataset.addValue(25.5D, "Series 3", "Category 3");
		defaultcategorydataset.addValue(13D, "Series 1", "Category 4");
		defaultcategorydataset.addValue(11.800000000000001D, "Series 2", "Category 4");
		defaultcategorydataset.addValue(29.5D, "Series 3", "Category 4");
		return defaultcategorydataset;
	}

	private static JFreeChart createChart(CategoryDataset categorydataset) {
		JFreeChart jfreechart = ChartFactory.createStackedBarChart("Stacked Bar Chart Demo 1", "Category", "Value",
				categorydataset, PlotOrientation.VERTICAL, true, true, false);
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		StackedBarRenderer stackedbarrenderer = (StackedBarRenderer) categoryplot.getRenderer();
		stackedbarrenderer.setDrawBarOutline(false);
		stackedbarrenderer.setBaseItemLabelsVisible(true);
		stackedbarrenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		return jfreechart;
	}

	public static JPanel createDemoPanel() {
		JFreeChart jfreechart = createChart(createDataset());
		return new ChartPanel(jfreechart);
	}

	public static void main(String args[]) {
		StackedBarChartDemo1 stackedbarchartdemo1 = new StackedBarChartDemo1("Stacked Bar Chart Demo 1");
		stackedbarchartdemo1.pack();
		RefineryUtilities.centerFrameOnScreen(stackedbarchartdemo1);
		stackedbarchartdemo1.setVisible(true);
	}
}
