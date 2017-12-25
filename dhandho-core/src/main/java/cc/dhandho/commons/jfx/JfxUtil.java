package cc.dhandho.commons.jfx;

import java.util.Objects;

import javafx.application.Platform;

public class JfxUtil {
	public static void runSafe(final Runnable runnable) {

		Objects.requireNonNull(runnable, "runnable");

		if (Platform.isFxApplicationThread()) {
			runnable.run();
		} else {
			Platform.runLater(runnable);
		}
	}
}
