package cc.dhandho.jsclient.ng;

import def.angular.core.NgModule;
import def.angular.forms.FormsModule;
import def.angular.http.HttpModule;
import def.angular.platform_browser.BrowserModule;

@NgModule(//
		imports = { BrowserModule.class, FormsModule.class, HttpModule.class }, //
		declarations = { AppComponent.class, CommandComponent.class }, //
		bootstrap = { AppComponent.class, CommandComponent.class }, //
		providers = { LoggerService.class }//
)

public class AppModule {

}
