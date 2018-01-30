package def.protractor.protractor;

import def.selenium.webdriver.promise;

public abstract class ProtractorBrowser {
	
	public native promise.Promise<?> get(String destination);
	
}
