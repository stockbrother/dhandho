package cc.dhandho.report.query;

import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.util.SVGConstants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.ReportMetaInfos;
import com.age5k.jcps.JcpsException;
import cc.dhandho.input.xueqiu.DateUtil;
import cc.dhandho.util.JfreeChartUtil;
import cc.dhandho.util.JsonUtil;

/**
 * A sub class of MetricsQuery. Which will handle the result set and build a
 * jfree chart on that data. The result is a svg format string for being
 * rendered in a html document.
 * 
 * @author Wu
 *
 */
public class SvgChartMetricQueryBuilder extends MetricsQuery<StringBuffer> {

	private static Logger LOG = LoggerFactory.getLogger(SvgChartMetricQueryBuilder.class);

	public static SvgChartMetricQueryBuilder newInstance(JsonReader reader, ReportMetaInfos aliasInfos) {

		SvgChartMetricQueryBuilder rt = new SvgChartMetricQueryBuilder(reader, aliasInfos);
		return rt;
	}

	public static SvgChartMetricQueryBuilder newInstance(JsonObject reader, ReportMetaInfos aliasInfos) {

		SvgChartMetricQueryBuilder rt = new SvgChartMetricQueryBuilder(reader, aliasInfos);
		return rt;
	}

	public SvgChartMetricQueryBuilder(JsonReader reader, ReportMetaInfos aliasInfos) {
		super(reader, aliasInfos);
	}

	public SvgChartMetricQueryBuilder(JsonObject reader, ReportMetaInfos aliasInfos) {
		super(reader, aliasInfos);
	}

	/**
	 * Here is the method of processing data set.
	 */
	@Override
	public StringBuffer handle(OResultSet req) {

		JsonArray json = new JsonMetricQueryResultHandler().handle(req);

		JsonReader jreader = JsonUtil.toJsonReader(json);
		String title = "[" + this.query.getCorpId() + "]";
		DefaultCategoryDataset dataSet = createDataset(jreader);
		JFreeChart chart = ChartFactory.createLineChart(title, null, null, dataSet, PlotOrientation.VERTICAL, true,
				true, false);

		// set the line style.
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		for (int i = 0; i < dataSet.getRowCount(); i++) {

			plot.getRenderer().setSeriesStroke(i, new BasicStroke(3.0f));
		}
		// chart.getPlot().setBackgroundPaint(Color.white);

		//

		StringWriter writer = new StringWriter();
		JfreeChartUtil.writeSvg(chart, this.query.getWidth(), this.query.getHeight(), writer);
		return writer.getBuffer();

	}

	private DefaultCategoryDataset createDataset(JsonReader reader) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		JsonArray rows = Streams.parse(reader).getAsJsonArray();
		List<MetricJsonWrapper> mL = super.query.getMetricList();

		for (int i = 0; i < rows.size(); i++) {
			JsonObject row = rows.get(i).getAsJsonObject();
			long date = row.get("reportDate").getAsLong();
			String yearS = DateUtil.formatYear(new Date(date));

			for (int j = 0; j < mL.size(); j++) {

				String mKey = "m" + (j + 1);
				JsonElement jsonI = row.get(mKey);

				Double mValue = null;
				if (jsonI != null && !jsonI.isJsonNull()) {
					mValue = jsonI.getAsDouble();
				}

				String mName = this.query.getMetricName(j + 1);
				if (LOG.isInfoEnabled()) {
					LOG.info("addValue:" + mValue + "," + mName + "," + yearS);
				}
				dataset.addValue(mValue, mName, yearS);
			}
		}

		return dataset;
	}

}
