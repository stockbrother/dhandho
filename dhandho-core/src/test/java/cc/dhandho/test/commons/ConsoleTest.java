package cc.dhandho.test.commons;

import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import cc.dhandho.commons.console.jfx.DefaultHistoryStore;
import cc.dhandho.commons.console.jfx.JfxConsolePane;
import cc.dhandho.commons.util.JfxUtil;
import cc.dhandho.test.util.TestUtil;
import cc.dhandho.util.FileUtil;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import junit.framework.TestCase;

public class ConsoleTest extends TestCase {
	public static class BaseJfxTestApp extends Application {

		public static void start() {
			Application.launch(new String[] {});
		}

		@Override
		public void start(Stage s) throws Exception {
			FileObject file = TestUtil.newRamFile();

			DefaultHistoryStore store = new DefaultHistoryStore(file);

			JfxConsolePane console = new JfxConsolePane(store);

			Scene root = new Scene(console, 800, 400);
			s.setScene(root);
			s.setOnHiding(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					console.close();
				}
			});
			s.show();

			new Thread() {

				@Override
				public void run() {
					JfxUtil.runSafe(new Runnable() {

						@Override
						public void run() {
							console.enter("line 1");
							console.enter("line 2");
							s.close();
							try {
								InputStreamReader r = new InputStreamReader(file.getContent().getInputStream());
								String string = FileUtil.readAsString(file.getContent().getInputStream(), "utf8");
								queue.put(string);
							} catch (FileSystemException | InterruptedException e) {
								throw new RuntimeException(e);
							}

						}

					});
				}

			}.start();

		}

	}

	private static BlockingQueue<Object> queue = new LinkedBlockingQueue<>();

	public void test() throws Exception {
		BaseJfxTestApp.start();
		Object obj = queue.poll(5, TimeUnit.SECONDS);
		TestCase.assertEquals(String.class, obj.getClass());
		String string = (String) obj;
		TestCase.assertTrue("history store is empty.", string.length() > 0);
		TestCase.assertEquals("line 1\r\nline 2\r\n", string);
	}

}
