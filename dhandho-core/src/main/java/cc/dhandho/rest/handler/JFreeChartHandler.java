package cc.dhandho.rest.handler;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtils;
import org.jfree.graphics2d.svg.SVGGraphics2D;

import com.age5k.jcps.JcpsException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.rest.RestRequestHandler;

/**
 * 
 * scale svg to fit the div container:
 * 
 * 1)div style="width:300; height:400;"<br>
 * 2)svg viewBox="0 0 600 400"<br>
 * 3)svg no width; no height;<br>
 * 
 */
public class JFreeChartHandler implements RestRequestHandler {

	@Override
	public void handle(RestRequestContext rrc) {
		try {

			JsonWriter writer = rrc.getWriter();
			JsonElement json = rrc.parseReader();
			JsonObject obj = json.getAsJsonObject();
			JFreeChart chart = createLineChart();
			int width = 600;
			int height = 400;
			if (true) {

				SVGGraphics2D g2 = new SVGGraphics2D(width, height);
				chart.draw(g2, new Rectangle2D.Double(0, 0, width, height), null, null);

				String svgElement = g2.getSVGElement();
				g2.dispose();
				writer.beginObject();
				writer.name("svgString").value(svgElement);
				writer.endObject();
				writer.close();
			}
			if (true) {
				ChartUtils.saveChartAsPNG(new File("tmp.png"), chart, width, height);
			}
			if (false) {
				BufferedImage image = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2 = image.createGraphics();
				chart.draw(g2, new Rectangle2D.Double(0, 0, 200, 100), null, null);
				g2.dispose();

				FileOutputStream fos = new FileOutputStream(new File("tmp.png"));
				byte[] bytes = ChartUtils.encodeAsPNG(image);
				fos.write(bytes);
				fos.close();

			}
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	private DefaultCategoryDataset createDataset() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(15, "schools", "1970");
		dataset.addValue(30, "schools", "1980");
		dataset.addValue(60, "schools", "1990");
		dataset.addValue(120, "schools", "2000");
		dataset.addValue(240, "schools", "2010");
		dataset.addValue(300, "schools", "2014");
		return dataset;
	}

	private JFreeChart createLineChart() {
		JFreeChart lineChart = ChartFactory.createLineChart("Chart2", "Years", "Number of Schools", createDataset(),
				PlotOrientation.VERTICAL, true, true, false);
		return lineChart;
	}

	private JFreeChart createLineChart2() {

		Number[][] data = new Integer[][] { { new Integer(-3), new Integer(-2) }, { new Integer(-1), new Integer(1) },
				{ new Integer(2), new Integer(3) } };

		CategoryDataset dataset = DatasetUtils.createCategoryDataset("S", "C", data);

		return ChartFactory.createLineChart("Line Chart", "Domain", "Range", dataset);

	}

}
