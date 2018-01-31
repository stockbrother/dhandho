package def.protractor.protractor;

import java.util.function.Consumer;

import def.jasmine.DoneFn;
import def.selenium.webdriver.promise;

public abstract class ProtractorBrowser {
	
	public native promise.Promise<?> get(String destination);
	public native promise.Promise<?> executeAsyncScript(Consumer<DoneFn> func);
}
