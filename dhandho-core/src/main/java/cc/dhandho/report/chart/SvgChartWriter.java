package cc.dhandho.report.chart;

import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
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
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cc.dhandho.RtException;
import cc.dhandho.input.xueqiu.DateUtil;
import cc.dhandho.report.ReportData;

public class SvgChartWriter {

	public SvgChartWriter() {

	}

	public void writeSvg(ReportData rdata, StringBuilder sb) {
		StringWriter sWriter = new StringWriter();
		writeSvg(rdata, sWriter);
		sb.append(sWriter.getBuffer());
	}

	public void writeSvg(ReportData rdata, Writer writer) {
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
			throw new RtException(e);
		}

	}

	private DefaultCategoryDataset createDataset(ReportData rdata) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<ReportData.ReportRow> rowL = rdata.getRowList();
		String[] headerA = rdata.getHeaderArray();
		for (int i = 0; i < rowL.size(); i++) {
			ReportData.ReportRow row = rowL.get(i);

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
