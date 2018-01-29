package app.dhojsw.ng.testing.util;

import static def.jasmine.Globals.describe;
import static def.jasmine.Globals.beforeEach;
import static def.jasmine.Globals.expect;
import static def.jasmine.Globals.it;

import java.util.function.Consumer;

import def.jasmine.DoneFn;
import def.jasmine.jasmine.Matchers;

/**
 * Why this class? Reason: You cannot define a describe method in a class that
 * calling Globals.describe() method.So we define a bridge caller here.
 * 
 * @author wu
 *
 */
public class Jasmine_ {

	public static void describe_(String desc, Runnable run) {
		describe(desc, run);
	}

	public static void it_(String desc, Consumer<DoneFn> fun) {
		it(desc, fun);
	}

	public static void beforeEach_(Runnable run) {
		beforeEach(run);
	}

	public static <T> Matchers<T> expect_(T obj) {
		return expect(obj);
	}

}
