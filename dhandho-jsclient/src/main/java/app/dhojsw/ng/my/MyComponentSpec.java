package app.dhojsw.ng.my;

import static app.dhojsw.ng.testing.Jasmine.describe;
import static app.dhojsw.ng.testing.Jasmine.expect;
import static app.dhojsw.ng.testing.Jasmine.it;
import static def.dhojsw.jsnative.Globals.js_isNumber;

public class MyComponentSpec {

	public static void main(String[] args) {

		describe("Some native test", new Runnable() {
			@Override
			public void run() {

				it("js_isNumber() method test", new Runnable() {

					@Override
					public void run() {
						boolean yes = js_isNumber(1.1);
						expect(yes).toBe(true);
					}

				});
			}
		});
	}
}
