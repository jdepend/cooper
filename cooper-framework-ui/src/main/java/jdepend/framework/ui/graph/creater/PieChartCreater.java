package jdepend.framework.ui.graph.creater;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import jdepend.framework.ui.graph.model.GraphDataItem;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public final class PieChartCreater extends AbstractChartCreater {

	@Override
	public JFreeChart create(GraphDataItem item) {
		return createPieChart(createPieDataset(item), item);
	}

	@Override
	public String getType() {
		return GraphDataItem.PIE;
	}

	private static PieDataset createPieDataset(GraphDataItem item) {
		DefaultPieDataset defaultpiedataset = new DefaultPieDataset();
		for (Object name : item.getDatas().keySet()) {
			defaultpiedataset.setValue((String) name, (Float) item.getDatas().get(name));
		}
		return defaultpiedataset;
	}

	private static JFreeChart createPieChart(PieDataset piedataset, GraphDataItem item) {
		JFreeChart jfreechart = ChartFactory.createPieChart(item.getTitle(), piedataset, true, true, false);
		Font font = new Font("宋体", Font.PLAIN, 13);
		jfreechart.getTitle().setFont(font);
		jfreechart.getLegend().setItemFont(font);
		PiePlot pieplot = (PiePlot) jfreechart.getPlot();
		pieplot.setBackgroundPaint(ChartColor.WHITE);
		pieplot.setLabelFont(font);
		pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator(("{0}: ({2})"),
				NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
		pieplot.setLabelBackgroundPaint(new Color(220, 220, 220));
		pieplot.setSimpleLabels(true);
		pieplot.setInteriorGap(0.0D);

		int index = 0;
		for (Object name : item.getDatas().keySet()) {
			pieplot.setSectionPaint((String) name, COLORS[index % COLORS.length]);
			index++;

		}
		return jfreechart;
	}
}
