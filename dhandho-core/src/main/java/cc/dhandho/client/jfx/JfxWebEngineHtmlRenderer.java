package cc.dhandho.client.jfx;

import java.nio.charset.Charset;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.handler.Handler;

import cc.dhandho.client.DhandhoCliConsole;
import cc.dhandho.client.HtmlRenderer;
import cc.dhandho.commons.util.JfxUtil;
import cc.dhandho.util.VfsUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class JfxWebEngineHtmlRenderer extends BorderPane implements HtmlRenderer {

	public static interface ReadyCallback extends Handler {

	}

	private static final Logger LOG = LoggerFactory.getLogger(JfxWebEngineHtmlRenderer.class);

	WebEngine webEngine;

	JSObject jsoWin;

	DhandhoCliConsole console;

	ReadyCallback readHandler;

	String htmlToShow = "AAA";

	JfxWebEngineHtmlRenderer(DhandhoCliConsole console, ReadyCallback rh) {
		this.console = console;
		this.readHandler = rh;
		WebView webView = new WebView();
		webEngine = webView.getEngine();
		webEngine.setJavaScriptEnabled(true);

		this.setCenter(webView);

		String userDir = System.getProperty("user.dir");
		userDir = userDir.replace("\\", "/");		String htmlPath = "file:///" + userDir + "/src/html/main.html";

		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue ov, State oldState, State newState) {
				LOG.info("change:" + ov + ",oldState:" + oldState + ",newState:" + newState);
				if (newState == Worker.State.SUCCEEDED) {
					// webEngine.executeScript("test()");
					jsoWin = (JSObject) webEngine.executeScript("window");
					jsoWin.setMember("renderer", JfxWebEngineHtmlRenderer.this);

					readHandler.handle();
				} else if (newState == Worker.State.FAILED) {
					throw new JcpsException("failed to load html:"+htmlPath);
				}

			}
		});
		webEngine.load(htmlPath);
		// webEngine.loadContent(buildHtml(new StringBuilder()).toString());

	}

	private StringBuilder buildHtml(StringBuilder out) {
		try {
			FileObject resFo = console.getConsoleHome().getFileSystem().getFileSystemManager()
					.resolveFile("res:cc/dhandho/client/jfx/main.html");
			return VfsUtil.load(resFo, Charset.forName("utf8"), out);
		} catch (FileSystemException e) {
			throw JcpsException.toRtException(e);
		}
	}

	@Override
	public void showHtml(StringBuilder html) {
		this.showHtml(html.toString());
	}

	@Override
	public void showHtml(String html) {
		LOG.info(html);
		this.htmlToShow = html;

		// Object rt = webEngine.executeScript("window.dhoMain.showHtml()");
		Object rt = webEngine.executeScript("showHtml()");
		LOG.info("rt:" + rt);
		// webEngine.executeScript("showHtml()");
		// webEngine.executeScript("test()");

	}

	public String getHtmlToShow() {
		return this.htmlToShow;
	}

}
