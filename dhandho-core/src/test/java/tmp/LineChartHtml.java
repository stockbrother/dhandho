package tmp;

import java.awt.BasicStroke;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import cc.dhandho.util.JfreeChartUtil;

public class LineChartHtml {
	private String chartTitle = "Title";

	int width = 600;
	int height = 400;

	public LineChartHtml() {

	}

	public void start() throws Exception {
		JFreeChart chart = ChartFactory.createLineChart(chartTitle, null, null, createDataset(),
				PlotOrientation.VERTICAL, true, true, false);
		// set the line style.
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		
		plot.getDomainAxis();
		
		for (int i = 0; i < 2; i++) {
			plot.getRenderer().setSeriesStroke(i, new BasicStroke(3.0f));
			
		}
		Writer out = new FileWriter(new File("target" + File.separator + "svg.html"));

		JfreeChartUtil.writeHtmlDocument(chart, width, height, out);

	}

	private DefaultCategoryDataset createDataset() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		String[] keys = new String[] { "schools", "others" };
		
		dataset.addValue(0.175351662000465,"净资产收益率","2016");
		dataset.addValue(0.19037129247598877,"净资产收益率","2015");
		dataset.addValue(0.16642438592108955,"净资产收益率","2014");
		dataset.addValue(6.817591104059893E16,"利润率","2016");
		dataset.addValue(5.074390030576181E1,"利润率","2015");
		dataset.addValue(2.823462154186345E1,"利润率","2014");
		
		
		return dataset;
	}

	public static void main(String[] args) throws Exception {
		new LineChartHtml().start();
	}
}
