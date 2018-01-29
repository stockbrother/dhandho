package app.dhojsw.ng.testing.util;

import static def.angular.core_testing.Globals.async;
import static def.angular.core_testing.Globals.fakeAsync;
import static def.angular.core_testing.Globals.tick;

import java.util.function.Consumer;

public class Angular_ {

	public static <T> Consumer<T> async_(Runnable run) {
		return async(run);
	}

	public static <T> Consumer<T> fakeAsync_(Runnable run) {
		return fakeAsync(run);
	}

	public static void tick_() {
		tick();
	}

}
