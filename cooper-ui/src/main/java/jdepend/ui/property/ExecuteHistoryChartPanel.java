package jdepend.ui.property;

import java.awt.BorderLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.component.TableSorter;
import jdepend.framework.util.MetricsFormat;
import jdepend.knowledge.database.AnalysisResultRepository;
import jdepend.knowledge.database.ExecuteResultSummry;
import jdepend.model.result.AnalysisResult;
import jdepend.ui.JDependCooper;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public final class ExecuteHistoryChartPanel extends JPanel {

	private JDependCooper frame;

	private Map<ExecuteResultSummry, AnalysisResult> results;

	public ExecuteHistoryChartPanel(JDependCooper frame, List<ExecuteResultSummry> historyList) throws JDependException {
		super();
		this.frame = frame;

		AnalysisResult result;

		results = new HashMap<ExecuteResultSummry, AnalysisResult>();
		for (ExecuteResultSummry summary : historyList) {
			result = AnalysisResultRepository.getResult(summary.getId());
			results.put(summary, result);
		}

		this.setLayout(new BorderLayout());

		this.add(BorderLayout.CENTER, createPanel());
	}

	public JTabbedPane createPanel() throws JDependException {

		JTabbedPane pane = new JTabbedPane();
		pane.setTabPlacement(JTabbedPane.BOTTOM);

		pane.addTab("折线图", createChartPanel());
		pane.addTab("表格", createTable());

		return pane;
	}

	public JPanel createChartPanel() throws JDependException {
		JFreeChart jfreechart = createChart(createDataset());
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		return chartpanel;
	}

	private JComponent createTable() throws JDependException {
		DefaultTableModel scoreListModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		TableSorter sorter = new TableSorter(scoreListModel);

		JTable scoreListTable = new JTable(sorter);

		sorter.setTableHeader(scoreListTable.getTableHeader());

		scoreListModel.addColumn("ID");
		scoreListModel.addColumn("抽象程度合理性");
		scoreListModel.addColumn("内聚性指数");
		scoreListModel.addColumn("关系合理性");
		scoreListModel.addColumn("总分");
		scoreListModel.addColumn("创建时间");

		scoreListTable.getColumn("ID").setMaxWidth(0);
		scoreListTable.getColumn("ID").setMinWidth(0);

		Object[] row;
		for (ExecuteResultSummry summary : this.results.keySet()) {
			row = new Object[6];

			row[0] = summary.getId();
			row[1] = MetricsFormat.toFormattedMetrics(this.results.get(summary).getDistance());
			row[2] = MetricsFormat.toFormattedMetrics(this.results.get(summary).getBalance());
			row[3] = MetricsFormat.toFormattedMetrics(this.results.get(summary).getRelationRationality());
			row[4] = MetricsFormat.toFormattedMetrics(this.results.get(summary).getScore());
			row[5] = summary.getCreateDate();

			scoreListModel.addRow(row);
		}

		sorter.setSortingStatus(5, TableSorter.DESCENDING);

		return new JScrollPane(scoreListTable);
	}

	private XYDataset createDataset() throws JDependException {

		TimeSeries scoreSeries = new TimeSeries("总分");
		TimeSeries dSeries = new TimeSeries("抽象程度合理性");
		TimeSeries balanceSeries = new TimeSeries("内聚性");
		TimeSeries relationSeries = new TimeSeries("关系合理性");

		AnalysisResult result;

		for (ExecuteResultSummry summary : this.results.keySet()) {

			result = this.results.get(summary);
			scoreSeries.add(new Minute(summary.getCreateDate()), result.getScore());
			dSeries.add(new Minute(summary.getCreateDate()), result.getDistance());
			balanceSeries.add(new Minute(summary.getCreateDate()), result.getBalance());
			relationSeries.add(new Minute(summary.getCreateDate()), result.getRelationRationality());
		}

		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		timeseriescollection.addSeries(scoreSeries);
		timeseriescollection.addSeries(dSeries);
		timeseriescollection.addSeries(balanceSeries);
		timeseriescollection.addSeries(relationSeries);

		return timeseriescollection;
	}

	private JFreeChart createChart(XYDataset xydataset) {
		JFreeChart jfreechart = ChartFactory.createXYLineChart("分数折线图", "时间", "分数", xydataset,
				PlotOrientation.VERTICAL, true, true, false);

		Font font = new Font("宋体", Font.PLAIN, 13);
		jfreechart.getTitle().setFont(font);
		jfreechart.getLegend().setItemFont(font);
		jfreechart.getXYPlot().getDomainAxis().setTickLabelFont(font);
		jfreechart.getXYPlot().getDomainAxis().setLabelFont(font);
		jfreechart.getXYPlot().getRangeAxis().setTickLabelFont(font);
		jfreechart.getXYPlot().getRangeAxis().setLabelFont(font);

		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setDomainPannable(true);
		xyplot.setRangePannable(true);
		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
		xylineandshaperenderer.setBaseShapesVisible(true);
		xylineandshaperenderer.setBaseShapesFilled(true);

		DateAxis dateaxis = new DateAxis("时间");
		dateaxis.setLowerMargin(0.0D);
		dateaxis.setUpperMargin(0.0D);

		SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateaxis.setDateFormatOverride(sfd);
		xyplot.setDomainAxis(dateaxis);
		xyplot.setDomainAxis(dateaxis);

		xyplot.setForegroundAlpha(0.5F);

		return jfreechart;
	}
}
