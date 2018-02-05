/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import common_http = require('@angular/common/http');
import { NgModule } from '@angular/core';
import forms = require('@angular/forms');
import platform_browser = require('@angular/platform-browser');
import { CommandComponent } from './command/command.component';
import { MyComponent } from './my/my.component';
import { Logger } from './service/logger';
import { StockDetailComponent } from './stock/stock.detail.component';
import { StockListComponent } from './stock/stock.list.component';
import { AppComponent } from './app.component';
import { PageNotFoundComponent } from './page.not.found.component';
import { RouterModuleForRoot } from './router.module.for.root';
import HttpClientModule = common_http.HttpClientModule;
import FormsModule = forms.FormsModule;
import BrowserModule = platform_browser.BrowserModule;
/**
 * TODO show error info when bootstrap .
 *
 * @author wu
 * @class
 */
@NgModule({
    imports: [platform_browser.BrowserModule, FormsModule, HttpClientModule, RouterModuleForRoot.forRoot()],
    declarations: [AppComponent, MyComponent, CommandComponent, StockListComponent, StockDetailComponent, PageNotFoundComponent],
    bootstrap: [AppComponent], providers: [Logger]
})
export class AppModule {


}
