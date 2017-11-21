package tmp;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

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
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LineChart {

	public static void main(String[] args) throws Exception {
		DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
		line_chart_dataset.addValue(15, "schools", "1970");
		line_chart_dataset.addValue(30, "schools", "1980");
		line_chart_dataset.addValue(60, "schools", "1990");
		line_chart_dataset.addValue(120, "schools", "2000");
		line_chart_dataset.addValue(240, "schools", "2010");
		line_chart_dataset.addValue(300, "schools", "2014");

		JFreeChart chart = ChartFactory.createLineChart("Schools Vs Years", "Year", "Schools Count", line_chart_dataset,
				PlotOrientation.VERTICAL, true, true, false);

		int width = 640; /* Width of the image */
		int height = 480; /* Height of the image */
		File lineChart = new File("LineChart.jpeg");
		// ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject, width ,height);
		ChartUtilities.saveChartAsPNG(new File("LineChart.png"), chart, width, height);

		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document
		Document doc = domImpl.createDocument(SVGConstants.SVG_NAMESPACE_URI, "svg", null);

		// Create an instance of the SVG Generator
		SVGGraphics2D svgGenerator = new SVGGraphics2D(doc);

		// Element root = doc.getDocumentElement();
		// svgGenerator.getRoot(root);

		// set the precision to avoid a null pointer exception in Batik 1.5
		svgGenerator.getGeneratorContext().setPrecision(6);

		// Ask the chart to render into the SVG Graphics2D implementation
		chart.draw(svgGenerator, new Rectangle2D.Double(0, 0, 400, 300), null);
		// Finally, stream out SVG to a file using UTF-8 character to
		// byte encoding
		Element root = svgGenerator.getRoot();

		root.setAttribute("viewBox", "0 0 " + width + " " + height);

		Writer out = new OutputStreamWriter(new FileOutputStream(new File("test.html")), "UTF-8");

		writeXmlWithDiv(width/2, height/2, root, out);

		//
		// boolean useCSS = false;
		// Writer out = new OutputStreamWriter(new FileOutputStream(new
		// File("test.svg")), "UTF-8");
		//
		// svgGenerator.stream(root, out, useCSS,false);

	}

	private static void writeXmlWithDiv(int width, int height, Element root, Writer out)
			throws TransformerException, IOException {
		out.write("<html>");
		out.write("<body>");
		out.write("<div style=\"width:" + width + "; height:" + height + ";\">");
		writeXml2(root, out);
		out.write("</div>");
		out.write("</body>");
		out.write("</html>");
		out.flush();
	}

	private static void writeXml2(Element root, Writer out) throws TransformerException {
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		transformer.transform(new DOMSource(root), new StreamResult(out));

	}

}