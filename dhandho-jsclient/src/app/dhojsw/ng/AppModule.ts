/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import common_http = require('@angular/common/http');
import core = require('@angular/core');
import forms = require('@angular/forms');
import platform_browser = require('@angular/platform-browser');
import { CommandComponent } from './command/CommandComponent';
import { MyComponent } from './my/MyComponent';
import { Logger } from './service/Logger';
import { StockDetailComponent } from './stock/StockDetailComponent';
import { StockListComponent } from './stock/StockListComponent';
import { RouterModule_forRoot_REPLACEHOLDER } from './RouterModule_forRoot_REPLACEHOLDER';
import { AppComponent } from './AppComponent';
import { PageNotFoundComponent } from './PageNotFoundComponent';

import HttpClientModule = common_http.HttpClientModule;
import NgModule = core.NgModule;
import FormsModule = forms.FormsModule;
import BrowserModule = platform_browser.BrowserModule;
/**
 * TODO show error info when bootstrap .
 *
 * @author wu
 * @class
 */
@core.NgModule({ imports: [platform_browser.BrowserModule, FormsModule, HttpClientModule, RouterModule_forRoot_REPLACEHOLDER.call()], declarations: [AppComponent, MyComponent, CommandComponent, StockListComponent, StockDetailComponent, PageNotFoundComponent], bootstrap: [AppComponent], providers: [Logger] })
export class AppModule { }
AppModule['__class'] = 'app.dhojsw.ng.AppModule';



