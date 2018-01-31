package app.dhojsw.ng.testing.e2e;

import static def.protractor.protractor.Globals.browser;
import static def.protractor.protractor.Globals.by;
import static def.protractor.protractor.Globals.element;

import app.dhojsw.ng.testing.util.UnitDescriber;
import def.protractor.protractor.ElementFinder;
import def.selenium.webdriver.By;
import def.selenium.webdriver.promise;

public class AppE2e extends UnitDescriber {

	public AppE2e(String desc) {
		super(desc);
	}

	public static void main(String[] args) {
		new AppE2e("AppE2e").describe();

	}

	@Override
	public void run() {

		this.it("test", (done) -> {
			promise.Promise<?> pro = browser.get("/");
			By byy = by.css("app-root h1");
			ElementFinder ef = element.$apply(byy);
			promise.Promise<String> text = ef.getText();
			this.expect(text).toBeTruthy();
			done.$apply();
		});

		this.it("test browser.executeAsyncScript", (done) -> {
			browser.executeAsyncScript((done2) -> {
				// browser is not defined:
				// TODO make a function as argument for callable.
				// promise.Promise<?> pro = browser.get("/");
				// By byy = by.css("app-root h1");
				// ElementFinder ef = element.$apply(byy);
				// promise.Promise<String> text = ef.getText();
				// this.expect(text).toBeTruthy();

				done2.$apply();
			});

			done.$apply();
		});

	}

}
