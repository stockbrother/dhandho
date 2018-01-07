package cc.dhandho.report.chart;

import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

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
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.age5k.jcps.JcpsException;

import cc.dhandho.input.xueqiu.DateUtil;
import cc.dhandho.report.CorpDatedMetricReportData;
import cc.dhandho.report.dupont.CorpPoint;
import cc.dhandho.report.dupont.DupontPointFinder;

public class SvgChartWriter {

	public SvgChartWriter() {

	}

	public void writeScatterSvg(DupontPointFinder finder, String xType, String yType, String[] heighLightCorpIds,
			StringBuilder sb) {
		int xIdx = finder.getTypeIndex(xType);
		int yIdx = finder.getTypeIndex(yType);

		String xLabel = xType.substring(xType.lastIndexOf(".") + 1);
		String yLabel = yType.substring(xType.lastIndexOf(".") + 1);
		writeScatterSvg(xLabel, xIdx, yLabel, yIdx, finder.corpPointMap(), heighLightCorpIds, sb);
	}

	/**
	 * 
	 * @param xyPoints
	 *            A map: corpId => Point(x,y)
	 * @param sb
	 */
	public void writeScatterSvg(String xLabel, int xIdx, String yLabel, int yIdx, Map<String,CorpPoint> xyPoints,
			String[] heighLightKey, StringBuilder sb) {
		StringWriter sWriter = new StringWriter();
		writeScatterSvg(xLabel, xIdx, yLabel, yIdx, xyPoints, heighLightKey, sWriter);
		sb.append(sWriter.getBuffer().toString());
	}

	public void writeScatterSvg(String xLabel, int xIdx, String yLabel, int yIdx, Map<String,CorpPoint> xyPoints,
			String[] heighLightKey, Writer writer) {
		String title = "[" + "todo" + "]";
		XYDataset dataSet = createDataset(xyPoints, xIdx, yIdx, heighLightKey);

		JFreeChart chart = ChartFactory.createScatterPlot(title, xLabel, yLabel, dataSet, PlotOrientation.VERTICAL,
				true, true, false);

		//

		int width = 600;
		int height = 320;

		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		Document doc = domImpl.createDocument(SVGConstants.SVG_NAMESPACE_URI, "svg", null);
		SVGGraphics2D svgGenerator = new SVGGraphics2D(doc);
		svgGenerator.getGeneratorContext().setPrecision(6);
		chart.draw(svgGenerator, new Rectangle2D.Double(0, 0, width, height), null);
		Element root = svgGenerator.getRoot();
		root.setAttribute("viewBox", "0 0 " + width + " " + height);

		// writeXmlWithDiv(width / 1, height / 1, root, writer);

		writeXml2(root, writer);
	}

	private XYDataset createDataset(Map<String,CorpPoint> xyPoints,  int xIdx, int yIdx, String[] heighLightKey) {
		DefaultXYDataset rt = new DefaultXYDataset();
		Set<String> set = new HashSet<>(Arrays.asList(heighLightKey));

		double[][] otherData = new double[2][xyPoints.size() - set.size()];
		AtomicInteger otherIdx = new AtomicInteger(0);

		xyPoints.values().stream().forEach(new Consumer<CorpPoint>() {

			@Override
			public void accept(CorpPoint t) {
				String key = t.corpId;
				Double[] point = t.point;
				if (set.contains(key)) {// heighLightCorpId
					double[][] data = new double[2][1];
					data[0][0] = point[xIdx] == null ? 0D : point[xIdx];
					data[1][0] = point[yIdx] == null ? 0D : point[yIdx];
					rt.addSeries(key, data);
				} else {
					otherData[0][otherIdx.get()] = point[xIdx] == null ? 0D : point[xIdx];
					otherData[1][otherIdx.get()] = point[yIdx] == null ? 0D : point[yIdx];
					otherIdx.incrementAndGet();
				}
			}
		});

		rt.addSeries("Other", otherData);

		return rt;
	}

	public void writeSvg(CorpDatedMetricReportData rdata, StringBuilder sb) {
		StringWriter sWriter = new StringWriter();
		writeSvg(rdata, sWriter);
		sb.append(sWriter.getBuffer());
	}

	public void writeSvg(CorpDatedMetricReportData rdata, Writer writer) {
		String title = "[" + "todo" + "]";
		DefaultCategoryDataset dataSet = createDataset(rdata);
		JFreeChart chart = ChartFactory.createLineChart(title, null, null, dataSet, PlotOrientation.VERTICAL, true,
				true, false);

		//
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		for (int i = 0; i < dataSet.getRowCount(); i++) {

			plot.getRenderer().setSeriesStroke(i, new BasicStroke(3.0f));
		}
		// chart.getPlot().setBackgroundPaint(Color.white);

		//

		int width = 600;
		int height = 320;

		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		Document doc = domImpl.createDocument(SVGConstants.SVG_NAMESPACE_URI, "svg", null);
		SVGGraphics2D svgGenerator = new SVGGraphics2D(doc);
		svgGenerator.getGeneratorContext().setPrecision(6);
		chart.draw(svgGenerator, new Rectangle2D.Double(0, 0, width, height), null);
		Element root = svgGenerator.getRoot();
		root.setAttribute("viewBox", "0 0 " + width + " " + height);

		// writeXmlWithDiv(width / 1, height / 1, root, writer);

		writeXml2(root, writer);

	}

	private static void writeXml2(Element root, Writer out) {
		try {
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			transformer.transform(new DOMSource(root), new StreamResult(out));
		} catch (TransformerException e) {
			throw new JcpsException(e);
		}

	}

	private DefaultCategoryDataset createDataset(CorpDatedMetricReportData rdata) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<CorpDatedMetricReportData.ReportRow> rowL = rdata.getRowList();
		String[] headerA = rdata.getHeaderArray();
		for (int i = 0; i < rowL.size(); i++) {
			CorpDatedMetricReportData.ReportRow row = rowL.get(i);

			long date = row.getReportDate().getTime();//
			String yearS = DateUtil.formatYear(new Date(date));

			for (int j = 0; j < headerA.length; j++) {
				Double mValue = rdata.get(i, j);
				String mName = headerA[j];
				dataset.addValue(mValue, mName, yearS);
			}
		}
		return dataset;
	}

}
