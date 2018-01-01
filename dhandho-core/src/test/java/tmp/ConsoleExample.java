package tmp;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;

import cc.dhandho.commons.console.jfx.DefaultHistoryStore;
import cc.dhandho.commons.console.jfx.JfxConsolePane;
import cc.dhandho.test.util.TestUtil;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ConsoleExample extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("JavaFX: WebView");

		StackPane sp = new StackPane();
		FileObject tmp = TestUtil.newRamFile();
		
		JfxConsolePane console = new JfxConsolePane(new DefaultHistoryStore(tmp));
		new Thread() {
			public void run() {

				while (true) {
					try {
						char cI = (char) console.getReader().read();
						
						System.out.println(cI);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
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

		stage.show();
	}
}