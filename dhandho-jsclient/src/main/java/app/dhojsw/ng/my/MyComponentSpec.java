package app.dhojsw.ng.my;

import static app.dhojsw.ng.testing.Jasmine.describe;
import static app.dhojsw.ng.testing.Jasmine.expect;
import static app.dhojsw.ng.testing.Jasmine.it;

import def.jasmine.jasmine.Matchers;

public class MyComponentSpec {

	public static void main(String[] args) {

		describe("My First Test", new Runnable() {
			@Override
			public void run() {
				it("1.1", new Runnable() {

					@Override
					public void run() {
						Matchers<String> matchers = expect("hello");
						matchers.toBeTruthy("No truth");
					}
				});
			}
		});

		describe("My Second Test", new Runnable() {

			@Override
			public void run() {
				it("2.1", new Runnable() {

					@Override
					public void run() {

					}
				});

			}
		});
	}
}
