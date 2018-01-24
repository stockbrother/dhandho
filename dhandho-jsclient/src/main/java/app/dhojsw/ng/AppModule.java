package app.dhojsw.ng;

import app.dhojsw.ng.my.MyComponent;
import def.angular.core.NgModule;
import def.angular.platform_browser.BrowserModule;

@NgModule(//
		imports = { BrowserModule.class }, //
		declarations = { AppComponent.class, MyComponent.class }, //
		bootstrap = { AppComponent.class }, //
		providers = {}//
)

public class AppModule {

}