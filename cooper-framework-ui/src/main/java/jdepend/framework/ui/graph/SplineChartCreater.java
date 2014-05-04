package jdepend.framework.ui.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CustomXYToolTipGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

public final class SplineChartCreater implements ChartCreater {

	@Override
	public JFreeChart create(GraphDataItem item) {
		return this.createChart(item);
	}

	@Override
	public String getType() {
		return GraphDataItem.SPLINE;
	}

	private JFreeChart createChart(final GraphDataItem item) {

		JFreeChart jfreechart = ChartFactory.createXYLineChart(item.getTitle(), item.getLineXName(), item
				.getLineYName(), this.createData(item), PlotOrientation.VERTICAL, true, true, true);

		Font font = new Font("宋体", Font.PLAIN, 13);
		jfreechart.getTitle().setFont(font);
		jfreechart.getLegend().setItemFont(font);
		jfreechart.getXYPlot().getDomainAxis().setTickLabelFont(font);
		jfreechart.getXYPlot().getDomainAxis().setLabelFont(font);
		jfreechart.getXYPlot().getRangeAxis().setTickLabelFont(font);
		jfreechart.getXYPlot().getRangeAxis().setLabelFont(font);
		jfreechart.getXYPlot().setBackgroundPaint(ChartColor.WHITE);

		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setDomainPannable(true);
		xyplot.setRangePannable(true);

		if (item.getBgColordData() != null) {
			for (RegionColor region : item.getBgColordData().getData()) {
				IntervalMarker intervalmarker = new IntervalMarker(region.begin, region.end);
				intervalmarker.setLabel(region.title);
				intervalmarker.setLabelFont(new Font("宋体", 2, 11));
				intervalmarker.setLabelAnchor(RectangleAnchor.RIGHT);
				intervalmarker.setLabelTextAnchor(TextAnchor.CENTER_RIGHT);
				intervalmarker.setPaint(region.color);
				xyplot.addRangeMarker(intervalmarker, Layer.BACKGROUND);
			}
		}

		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
		xylineandshaperenderer.setBaseShapesVisible(true);
		xylineandshaperenderer.setBaseShapesFilled(true);
		xylineandshaperenderer.setDrawOutlines(true);
		xylineandshaperenderer.setUseFillPaint(true);
		xylineandshaperenderer.setBaseFillPaint(Color.white);
		xylineandshaperenderer.setSeriesStroke(0, new BasicStroke(3F));
		xylineandshaperenderer.setSeriesOutlineStroke(0, new BasicStroke(2.0F));
		xylineandshaperenderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-5D, -5D, 10D, 10D));
		xylineandshaperenderer.setBaseItemLabelFont(new Font("宋体", 2, 11));
		if (item.getFgColorData() != null) {
			xylineandshaperenderer.setSeriesPaint(0, item.getFgColorData().mainColor);
		}

		final List<Object> values = new ArrayList<Object>();
		for (Object name : item.getDatas().keySet()) {
			values.add(name);
		}

		XYToolTipGenerator generator = new CustomXYToolTipGenerator() {
			@Override
			public String generateToolTip(XYDataset data, int series, int i) {
				return item.getTip(values.get(i));
			}
		};
		xylineandshaperenderer.setBaseToolTipGenerator(generator);
		xylineandshaperenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator() {
			@Override
			public String generateLabel(XYDataset dataset, int series, int i) {
				return item.getTip(values.get(i));
			}

		});
		xylineandshaperenderer.setBaseItemLabelsVisible(true);

		return jfreechart;
	}

	private XYDataset createData(GraphDataItem item) {
		XYSeries xyseries = new XYSeries(item.getLineName());
		for (Object name : item.getDatas().keySet()) {
			xyseries.add((Integer) name, (Float) item.getDatas().get(name));
		}
		XYSeriesCollection xyseriescollection = new XYSeriesCollection(xyseries);
		return xyseriescollection;
	}

}
