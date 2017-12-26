package cc.dhandho.client.jfx;

import cc.dhandho.client.HtmlRenderer;
import cc.dhandho.commons.jfx.JfxUtil;
import cc.dhandho.server.DhandhoServer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class DhandhoJfxApplication extends Application implements HtmlRenderer {
	WebEngine webEngine;

	// this SERVER must be started and set before launch this application.

	private static DhandhoServer SERVER;

	DhandhoJfxConsole console;
	Stage htmlStage;

	protected double width = 800;

	protected double height = 300;

	public DhandhoJfxApplication() {

	}

	@Override
	public void init() throws Exception {

		console = new DhandhoJfxConsole();
		console.server(SERVER);
		console.htmlRenderer(this);

	}

	@Override
	public void stop() throws Exception {

	}

	@Override
	public void start(Stage stage) throws Exception {
		console.start();
		this.startConsoleStage1(stage);
		this.htmlStage = new Stage();
		this.startHtmlStage(this.htmlStage);
	}

	private void startConsoleStage1(Stage stage) {
		stage.setTitle("Dhandho Console");
		Scene root = new Scene(console.consolePane, this.width, this.height);
		stage.setScene(root);
		stage.show();
	}

	private void startHtmlStage(Stage stage) {
		stage.setTitle("Dhandho Html");
		BorderPane sp = new BorderPane();

		WebView webView = new WebView();
		webEngine = webView.getEngine();

		sp.setCenter(webView);
		Scene root = new Scene(sp);
		stage.setScene(root);
		stage.show();
	}

	@Override
	public void showHtml(String html) {
		JfxUtil.runSafe(new Runnable() {

			@Override
			public void run() {
				webEngine.loadContent(html);
			}
		});

	}

	public static void setServer(DhandhoServer server) {
		SERVER = server;
	}

}