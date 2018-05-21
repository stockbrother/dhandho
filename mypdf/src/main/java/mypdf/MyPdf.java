package mypdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class MyPdf {
	private static final Logger LOG = LoggerFactory.getLogger(MyPdf.class);

	public static void main(String args[]) throws Exception {
		String fileName = "D:\\\\mypdf\\\\document(1712).pdf";
		String dest = fileName + ".dest.pdf";

		File file = new File(fileName);
		PdfReader reader = new PdfReader(fileName);
		Rectangle pageSize = reader.getPageSize(1);
		createPdf(reader, dest);
		reader.close();
	}

	public static void createPdf(PdfReader reader, String dest) throws Exception {
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		BaseFont bf = getFontByParser(reader);
		String text = "font:" + toString(bf.getFamilyFontName()) + "中国" + "Some text in,1,950.7 ";
		LOG.info(text);
		PdfContentByte canvas = stamper.getOverContent(1);
		Phrase phrase = new Phrase(text, new Font(bf, 12));
		ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrase, 36, 806, 0);
		stamper.close();
	}

	public static String toString(String[][] names) {
		StringBuffer sb = new StringBuffer();
		for (String[] name : names) {
			for (String nameI : name) {
				sb.append(nameI).append(",");
			}
			sb.append(";");
		}

		return sb.toString();
	}

	public static BaseFont getFontByParser(PdfReader reader) throws Exception {
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		MyRenderListener2 l = new MyRenderListener2();
		parser.processContent(1, l);
		if (l.font == null) {
			throw new Exception("font is null");
		}
		return l.font;
	}

	public static BaseFont getFont(PdfReader reader) {
		PdfDictionary root = reader.getCatalog();
		PdfDictionary acroform = root.getAsDict(PdfName.ACROFORM);
		if (acroform == null)
			return null;
		PdfDictionary dr = acroform.getAsDict(PdfName.DR);
		if (dr == null)
			return null;
		PdfDictionary font = dr.getAsDict(PdfName.FONT);
		if (font == null)
			return null;
		for (PdfName key : font.getKeys()) {

			return BaseFont.createFont((PRIndirectReference) font.getAsIndirectObject(key));

		}
		return null;
	}

	public static void parse2(PdfReader reader) throws Exception {
		String text = PdfTextExtractor.getTextFromPage(reader, 1);
		LOG.info(text);
	}

	public static void parse(PdfReader reader) throws Exception {

		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		parser.processContent(1, new MyRenderListener());
	}

	public static class MyRenderListener2 implements RenderListener {
		DocumentFont font;

		@Override
		public void beginTextBlock() {
			// TODO Auto-generated method stub

		}

		@Override
		public void renderText(TextRenderInfo renderInfo) {
			if (font != null) {
				return;
			}
			font = renderInfo.getFont();

		}

		@Override
		public void endTextBlock() {
			// TODO Auto-generated method stub

		}

		@Override
		public void renderImage(ImageRenderInfo renderInfo) {
			// TODO Auto-generated method stub

		}

	}

	public static class MyRenderListener implements RenderListener {

		@Override
		public void beginTextBlock() {
			LOG.info("beginTextBlock");

		}

		@Override
		public void renderText(TextRenderInfo renderInfo) {
			DocumentFont font = renderInfo.getFont();
			StringBuffer sb = new StringBuffer();
			sb.append("renderText").append("font:").append(font)

					.append(",fontFamilyFontName:");

			String[][] names = font.getFamilyFontName();
			for (String[] name : names) {
				for (String nameI : name) {
					sb.append(nameI).append(",");

				}
			}
			sb.append(",text:").append(renderInfo.getText()).append(",pdfString:").append(renderInfo.getPdfString());

			LOG.info(sb.toString());
		}

		@Override
		public void endTextBlock() {
			LOG.info("endTextBlock");
		}

		@Override
		public void renderImage(ImageRenderInfo renderInfo) {
			LOG.info("renderImage");
		}

	}
}
