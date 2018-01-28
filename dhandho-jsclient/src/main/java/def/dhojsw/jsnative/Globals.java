package def.dhojsw.jsnative;

import java.util.function.Consumer;

import def.angular.core_testing.TestModuleMetadata;
import def.jasmine.DoneFn;
import def.jasmine.jasmine.Matchers;

public class Globals {

	//angular
	public static native Runnable angular_async(Runnable run);
	
	public static native Runnable angular_fakeAsync(Runnable run);
	
	public static native void angular_tick();

	public static native TestModuleMetadataBuilder angular_newTestModuleMetadataBuilder();
	
	//jasmine
	public static native void jasmine_describe(String desc, Runnable run);

	public static native void jasmine_it(String desc, Runnable run);

	public static native <T> Matchers<T> jasmine_expect(T obj);
	
	public static native void jasmine_beforeEach(Consumer<DoneFn> action);
	
	public static native void jasmine_beforeEach(Runnable action);	
	
	public static native boolean js_isNumber(Object jso);
	
}