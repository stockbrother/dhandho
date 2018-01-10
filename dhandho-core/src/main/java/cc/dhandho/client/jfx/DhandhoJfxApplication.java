package cc.dhandho.client.jfx;

import org.apache.commons.vfs2.FileObject;

import cc.dhandho.DhoDataHome;
import cc.dhandho.client.HtmlRenderer;
import cc.dhandho.client.jfx.JfxWebEngineHtmlRenderer.ReadyCallback;
import cc.dhandho.commons.util.JfxUtil;
import cc.dhandho.rest.server.DhoServer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DhandhoJfxApplication extends Application implements HtmlRenderer {

	// this SERVER must be started and set before launch this application.

	private static DhoServer SERVER;

	private static FileObject CONSOLE_HOME;
	DhandhoJfxConsole console;
	Stage htmlStage;

	protected double width = 800;

	protected double height = 300;

	JfxWebEngineHtmlRenderer htmlRenderer;

	public DhandhoJfxApplication() {

	}

	public static void launch(DhoServer server, FileObject consoleHome) {
		SERVER = server;
		CONSOLE_HOME = consoleHome;
		Application.launch(DhandhoJfxApplication.class);

	}

	@Override
	public void init() throws Exception {
		DhoDataHome home = SERVER.getDataHome();

		console = new DhandhoJfxConsole(CONSOLE_HOME);
		console.server(SERVER);
		console.htmlRenderer(this);

	}

	@Override
	public void stop() throws Exception {
		console.shutdown();
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.htmlRenderer = new JfxWebEngineHtmlRenderer(console, new ReadyCallback() {

			@Override
			public void handle() {
				onHtmlRendererReady(stage);
			}
		});

	}

	private void onHtmlRendererReady(Stage stage) {
		console.start();
		stage.setTitle("Dhandho Console");
		Scene root = new Scene(console.consolePane, this.width, this.height);
		stage.setScene(root);
		stage.show();
		stage.setOnHidden(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				SERVER.shutdown();
			}
		});
	}

	private void startHtmlStage(Stage stage) {
		stage.setTitle("Dhandho Html");
		Scene root = new Scene(this.htmlRenderer);
		stage.setScene(root);
		stage.show();

	}

	private void doShowHtml(String html) {
		if (this.htmlStage == null) {
			this.htmlStage = new Stage();
			this.startHtmlStage(this.htmlStage);
		}
		if (!this.htmlStage.isShowing()) {
			this.htmlStage.show();
		}
		this.htmlRenderer.showHtml(html);

	}

	@Override
	public void showHtml(String html) {
		JfxUtil.runSafe(new Runnable() {

			@Override
			public void run() {
				doShowHtml(html);
			}

		});

	}

	public static void setServer(DhoServer server) {
		SERVER = server;
	}

	@Override
	public void showHtml(StringBuilder html) {
		this.showHtml(html.toString());
	}

}