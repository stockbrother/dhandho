package cc.dhandho.client.jfx;

import cc.dhandho.client.DhandhoCliConsole;
import cc.dhandho.client.HtmlRenderer;
import cc.dhandho.commons.jfx.ConsolePane;
import cc.dhandho.commons.jfx.JfxUtil;
import cc.dhandho.server.DhandhoServer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class DhandhoApplication extends Application implements HtmlRenderer {
	WebEngine webEngine;

	// this SERVER must be started and set before launch this application.

	private static DhandhoServer SERVER;

	DhandhoCliConsole console;
	DhandhoJfxConsole console2;
	ConsolePane console3;
	Stage htmlStage;
	
	public DhandhoApplication() {

	}

	@Override
	public void init() throws Exception {

		console = new DhandhoCliConsole();
		console.server(SERVER);
		console.htmlRenderer(this);

	}

	@Override
	public void stop() throws Exception {

	}

	@Override
	public void start(Stage stage) throws Exception {
		console.start();
		this.startConsoleStage3(stage);
		this.htmlStage = new Stage();
		this.startHtmlStage(this.htmlStage);
	}

	private void startConsoleStage3(Stage stage) {
		stage.setTitle("Dhandho Console");
		console3 = new ConsolePane();		
		Scene root = new Scene(console3);
		stage.setScene(root);
		stage.show();

	}
	private void startConsoleStage2(Stage stage) {
		stage.setTitle("Dhandho Console");
		console2 = new DhandhoJfxConsole();		
		Scene root = new Scene(console2);
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