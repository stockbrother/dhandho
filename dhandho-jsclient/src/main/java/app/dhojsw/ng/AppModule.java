package app.dhojsw.ng;

import app.dhojsw.ng.command.CommandComponent;
import app.dhojsw.ng.my.MyComponent;
import app.dhojsw.ng.service.Logger;
import app.dhojsw.ng.util.RouterModuleForRoot;
import def.angular.common_http.HttpClientModule;
import def.angular.core.NgModule;
import def.angular.forms.FormsModule;
import def.angular.platform_browser.BrowserModule;

@NgModule(//
		imports = { BrowserModule.class, FormsModule.class, HttpClientModule.class, RouterModuleForRoot.class }, //
		declarations = { AppComponent.class, MyComponent.class, CommandComponent.class, PageNotFoundComponent.class }, //
		bootstrap = { AppComponent.class, CommandComponent.class }, //
		providers = { Logger.class }//
)

public class AppModule {

}