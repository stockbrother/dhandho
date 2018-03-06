/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import core = require('@angular/core');
import router = require('@angular/router');
import { CommandComponent } from './command/command.component';
import { MyComponent } from './my/my.component';
import { StockChartsComponent } from './stock/stock.charts.component';
import { StockDetailComponent } from './stock/stock.detail.component';
import { StockListComponent } from './stock/stock.list.component';
import { PageNotFoundComponent } from './page.not.found.component';

import ModuleWithProviders = core.ModuleWithProviders;
import Route = router.Route;
import RouterModule = router.RouterModule;
/**
 *
 * @author wu
 * @class
 */
export class RouterModuleForRoot {
    public static forRoot(): ModuleWithProviders {
        let routes: Array<Route> = <any>(new Array<any>());
        RouterModuleForRoot.push(routes, 'stockList', StockListComponent);
        RouterModuleForRoot.push(routes, 'stockDetail/:id', StockDetailComponent);
        RouterModuleForRoot.push(routes, 'stockCharts/:id', StockChartsComponent);
        RouterModuleForRoot.push(routes, 'command', CommandComponent);
        RouterModuleForRoot.push(routes, 'my', MyComponent);
        RouterModuleForRoot.push(routes, '**', PageNotFoundComponent);
        return RouterModule.forRoot(routes);
    }

    public static push(routes: Array<Route>, path: string, component: any) {
        let r: Route = <any>Object.defineProperty({

        }, '__interfaces', { configurable: true, value: ['def.angular.router.Route'] });
        r.component = component;
        r.path = path;
        routes.push(r);
    }
}


