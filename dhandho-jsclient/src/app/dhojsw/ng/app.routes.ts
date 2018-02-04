/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import router = require( '@angular/router' );
import { CommandComponent } from './command/command.component';
import { MyComponent } from './my/my.component';
import { StockDetailComponent } from './stock/stock.detail.component';
import { StockListComponent } from './stock/stock.list.component';
import { PageNotFoundComponent } from './page.not.found.component';

import Route = router.Route;

export class AppRoutes {
    public static initialized: boolean = false;

    public static routes: Array<Route>; public static routes_$LI$(): Array<Route> { if ( AppRoutes.routes == null ) AppRoutes.routes = <any>( new Array<any>() ); return AppRoutes.routes; };

    public static getRoutes(): Array<Route> {
        if ( !AppRoutes.initialized ) {
            AppRoutes.push( AppRoutes.routes_$LI$(), 'stockList', StockListComponent );
            AppRoutes.push( AppRoutes.routes_$LI$(), 'stockDetail/:id', StockDetailComponent );
            AppRoutes.push( AppRoutes.routes_$LI$(), 'command', CommandComponent );
            AppRoutes.push( AppRoutes.routes_$LI$(), 'my', MyComponent );
            AppRoutes.push( AppRoutes.routes_$LI$(), '**', PageNotFoundComponent );
            AppRoutes.initialized = true;
        }
        return AppRoutes.routes_$LI$();
    }

    public static push( routes: Array<Route>, path: string, component: any ) {
        let r: Route = <any>Object.defineProperty( {

        }, '__interfaces', { configurable: true, value: ['def.angular.router.Route'] } );
        r.component = component;
        r.path = path;
        routes.push( r );
    }
}
AppRoutes['__class'] = 'app.dhojsw.ng.AppRoutes';




AppRoutes.routes_$LI$();
