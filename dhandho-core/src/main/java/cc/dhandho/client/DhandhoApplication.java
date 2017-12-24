package cc.dhandho.client;

import cc.dhandho.server.DhandhoServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class DhandhoApplication extends Application implements HtmlRenderer {
	WebEngine webEngine;

	// this SERVER must be started and set before launch this application.

	private static DhandhoServer SERVER;

	DhandhoConsole console;

	public DhandhoApplication() {

	}

	@Override
	public void init() throws Exception {

		console = new DhandhoConsole();
		console.server(SERVER);
		console.htmlRenderer(this);
	}

	@Override
	public void stop() throws Exception {

	}

	@Override
	public void start(Stage stage) throws Exception {
		console.start();

		stage.setTitle("Dhandho Client");

		WebView webView = new WebView();
		webEngine = webView.getEngine();
		StackPane sp = new StackPane();
		sp.getChildren().add(webView);
		Scene root = new Scene(sp);
		stage.setScene(root);
		stage.show();

	}

	@Override
	public void showHtml(String html) {
		Platform.runLater(new Runnable() {

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