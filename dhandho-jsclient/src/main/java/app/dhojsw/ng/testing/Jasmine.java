package app.dhojsw.ng.testing;

import static def.dhojsw.jsnative.Globals.jasmine_describe;
import static def.dhojsw.jsnative.Globals.jasmine_it;
import static def.dhojsw.jsnative.Globals.jasmine_expect;
import static def.dhojsw.jsnative.Globals.jasmine_beforeEach;
import java.util.function.Consumer;

import def.jasmine.DoneFn;
import def.jasmine.jasmine.Matchers;

public class Jasmine {
	public static void describe(String desc, Runnable runnable) {
		jasmine_describe(desc, runnable);
	}

	public static void it(String desc, Runnable runnable) {
		jasmine_it(desc, runnable);
	}

	public static <T> Matchers<T> expect(T obj) {
		return jasmine_expect(obj);
	}
	
	public static void beforeEach(Runnable action) {
		jasmine_beforeEach(action);
	}
	
	public static void beforeEach(Consumer<DoneFn> action) {
		jasmine_beforeEach(action);
	}
}
