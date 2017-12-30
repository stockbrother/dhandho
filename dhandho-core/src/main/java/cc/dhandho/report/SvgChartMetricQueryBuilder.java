package cc.dhandho.report;

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
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.ReportMetaInfos;
import cc.dhandho.input.xueqiu.DateUtil;
import cc.dhandho.util.JsonUtil;

public class SvgChartMetricQueryBuilder extends JsonMetricSqlLinkQueryBuilder {
	private static Logger LOG = LoggerFactory.getLogger(SvgChartMetricQueryBuilder.class);

	public static SvgChartMetricQueryBuilder newInstance(JsonReader reader, ReportMetaInfos aliasInfos) {

		SvgChartMetricQueryBuilder rt = new SvgChartMetricQueryBuilder(reader, aliasInfos);
		return rt;
	}

	public SvgChartMetricQueryBuilder(JsonReader reader, ReportMetaInfos aliasInfos) {
		super(reader, aliasInfos);
	}

	public SvgChartMetricQueryBuilder query(ODatabaseSession db, Writer writer) throws IOException {
		StringWriter swriter = new StringWriter();
		JsonWriter jwriter = JsonUtil.newJsonWriter(swriter);
		super.build().query(db, jwriter);

		JsonReader jreader = JsonUtil.toJsonReader(swriter.getBuffer().toString());
		String title = "[" + this.query.getCorpId() + "]";
		DefaultCategoryDataset dataSet = createDataset(jreader);
		JFreeChart chart = ChartFactory.createLineChart(title, null, null, dataSet, PlotOrientation.VERTICAL, true,
				true, false);

		//
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		for (int i = 0; i < dataSet.getRowCount(); i++) {

			plot.getRenderer().setSeriesStroke(i, new BasicStroke(3.0f));
		}
		// chart.getPlot().setBackgroundPaint(Color.white);

		//

		int width = this.query.getWidth();
		int height = this.query.getHeight();

		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		Document doc = domImpl.createDocument(SVGConstants.SVG_NAMESPACE_URI, "svg", null);
		SVGGraphics2D svgGenerator = new SVGGraphics2D(doc);
		svgGenerator.getGeneratorContext().setPrecision(6);
		chart.draw(svgGenerator, new Rectangle2D.Double(0, 0, width, height), null);
		Element root = svgGenerator.getRoot();
		root.setAttribute("viewBox", "0 0 " + width + " " + height);

		// writeXmlWithDiv(width / 1, height / 1, root, writer);

		writeXml2(root, writer);
		return this;

	}

	private static void writeXmlWithDiv(int width, int height, Element root, Writer out) throws IOException {
		out.write("<html>\n");
		out.write("<head> <meta charset=\"UTF-8\"/> </head>\n");
		out.write("<body>\n");

		out.write("<div style=\"width:" + width + "; height:" + height + ";\">\n");
		writeXml2(root, out);
		out.write("</div>\n");
		out.write("</body>\n");
		out.write("</html>\n");
		out.flush();
	}

	private static void writeXml2(Element root, Writer out) throws IOException {
		try {
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			transformer.transform(new DOMSource(root), new StreamResult(out));
		} catch (TransformerException e) {
			throw new IOException(e);
		}

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
				if (!jsonI.isJsonNull()) {
					mValue = jsonI.getAsDouble();
				}

				String mName = this.query.getMetricName(j + 1);
				dataset.addValue(mValue, mName, yearS);
			}
		}
		return dataset;
	}

}
