package def.dhojsw.jsnative;

import java.util.function.Consumer;

import def.jasmine.DoneFn;
import def.jasmine.jasmine.Matchers;

public class Globals {

	public static native Runnable angular_async(Runnable run);

	public static native void jasmine_describe(String desc, Runnable run);

	public static native void jasmine_it(String desc, Runnable run);

	public static native <T> Matchers<T> jasmine_expect(T obj);
	
	public static native void jasmine_beforeEach(Consumer<DoneFn> action);
	
	public static native void jasmine_beforeEach(Runnable action);
	
}
