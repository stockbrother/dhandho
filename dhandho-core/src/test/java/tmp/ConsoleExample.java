package tmp;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.VFS;

import cc.dhandho.commons.console.jfx.DefaultHistoryStore;
import cc.dhandho.commons.console.jfx.JfxConsolePane;
import cc.dhandho.test.util.TestUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ConsoleExample extends Application {
	private boolean running = true;
	
	Thread thread;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("JavaFX: WebView");

		StackPane sp = new StackPane();
		FileObject tmp = TestUtil.newRamFile();

		JfxConsolePane console = new JfxConsolePane(new DefaultHistoryStore(tmp));
		thread = new Thread("my-thread") {
			public void run() {

				while (running) {
					try {
						System.out.println("reading..");
						char cI = (char) console.getReader().read();

						System.out.println("read:"+cI);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("exiting..");
				Platform.exit();
			}
		};
		thread.start();
		// sp.getChildren().add(console);
		Color color = Color.RED;
		BackgroundFill fill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
		Background background = new Background(fill);
		// Fill the background color on the matte
		// sp.setBackground(background);
		// console.setBackground(background);
		Scene root = new Scene(console, 800, 400);

		stage.setScene(root);

		root.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight,
					Number height) {
				System.out.println("Height: " + height);

			}
		});

		stage.setOnHidden(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				running = false;
				console.close();
				//Platform.exit();
			}
		});
				
		stage.show();
	}
}