package app.dhojsw.ng;

import app.dhojsw.ng.command.CommandComponent;
import app.dhojsw.ng.my.MyComponent;
import def.angular.common_http.HttpClientModule;
import def.angular.core.NgModule;
import def.angular.forms.FormsModule;
import def.angular.platform_browser.BrowserModule;

@NgModule(//
		imports = { BrowserModule.class, FormsModule.class, HttpClientModule.class }, //
		declarations = { AppComponent.class, MyComponent.class, CommandComponent.class }, //
		bootstrap = { AppComponent.class, CommandComponent.class }, //
		providers = {}//
)

public class AppModule {

}