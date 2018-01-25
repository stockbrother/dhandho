package app.dhojsw.ng;

import jsweet.lang.Interface;

@Interface
public class DhoTester {
	public native Runnable angular_async(Runnable run);

	public native void jasmine_describe(String desc, Runnable run);

}
