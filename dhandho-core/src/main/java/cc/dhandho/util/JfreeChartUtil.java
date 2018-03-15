package cc.dhandho.util;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.StringWriter;
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
import org.jfree.chart.JFreeChart;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.age5k.jcps.JcpsException;

public class JfreeChartUtil {

	public static void writeSvg(JFreeChart chart, int width, int height, Writer writer) {

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

	public static void writeHtmlDocument(JFreeChart chart, int width, int height, Writer out) throws IOException {
		out.write("<html>\n");
		out.write("<head> <meta charset=\"UTF-8\"/> </head>\n");
		out.write("<body>\n");

		out.write("<div style=\"width:" + width + "; height:" + height + ";\">\n");

		writeSvg(chart, width, height, out);

		out.write("</div>\n");
		out.write("</body>\n");
		out.write("</html>\n");
		out.flush();
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
}
