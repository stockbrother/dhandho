package cc.dhandho.client.jfx;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.age5k.jcps.JcpsException;

import cc.dhandho.client.HtmlRenderer;
import cc.dhandho.commons.util.JfxUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class JfxWebEngineHtmlRenderer extends BorderPane implements HtmlRenderer {
	private static final Logger LOG = LoggerFactory.getLogger(JfxWebEngineHtmlRenderer.class);
	WebEngine webEngine;

	Document doc;
	Element htmlEle;
	Element bodyEle;

	JfxWebEngineHtmlRenderer() {
		WebView webView = new WebView();
		webEngine = webView.getEngine();
		this.setCenter(webView);
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue ov, State oldState, State newState) {
				LOG.info("change:" + ov + ",oldState:" + oldState + ",newState:" + newState);
				if (newState == Worker.State.SUCCEEDED) {

				}

			}
		});

		webEngine.loadContent(buildHtml(new StringBuilder()).toString());
	}

	private StringBuilder buildHtml(StringBuilder out) {

		out.append("<html>\n");
		out.append("  <head>\n");
		out.append("    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n");
		out.append("  </head>\n");
		out.append("  <body id=\"body\">\n");
		out.append("  </body>\n");
		out.append("</html>\n");
		return out;
	}

	@Override
	public void showHtml(StringBuilder html) {
		this.showHtml(html.toString());
	}

	@Override
	public void showHtml(String html) {
		LOG.info(html);
		
		if (this.doc == null) {

			this.doc = webEngine.getDocument();
			if (this.doc == null) {
				return;// not ready
			}
			this.htmlEle = doc.getDocumentElement();
			this.bodyEle = this.doc.getElementById("body");
		}

		try {
			Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new ByteArrayInputStream(html.getBytes("utf8"))).getDocumentElement();
			this.bodyEle.appendChild(node);
		} catch (SAXException e) {
			throw JcpsException.toRtException(e);
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		} catch (ParserConfigurationException e) {
			throw JcpsException.toRtException(e);
		}

	}

}
