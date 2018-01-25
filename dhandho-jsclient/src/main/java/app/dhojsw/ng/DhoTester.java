package app.dhojsw.ng;

import static def.dhojsw.jsstub.Globals.angular_async;
import static def.dhojsw.jsstub.Globals.jasmine_describe;

public class DhoTester {

	public Runnable async(Runnable run) {
		return angular_async(run);
	}

	public void describe(String desc, Runnable run) {
		jasmine_describe(desc, run);
	}

}
