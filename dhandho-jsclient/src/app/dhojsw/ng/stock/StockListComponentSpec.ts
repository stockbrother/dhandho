/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import common = require('@angular/common');
import common_http_testing = require('@angular/common/http/testing');
import core = require('@angular/core');
import forms = require('@angular/forms');
import platform_browser = require('@angular/platform-browser');
import router = require('@angular/router');
import router_testing = require('@angular/router/testing');
import { AppRoutes } from '../AppRoutes';
import { PageNotFoundComponent } from '../PageNotFoundComponent';
import { CommandComponent } from '../command/CommandComponent';
import { MyComponent } from '../my/MyComponent';
import { Logger } from '../service/Logger';
import { ComponentUnitDescriber } from '../util/ComponentUnitDescriber';
import { TestBedHelper } from '../util/TestBedHelper';
import { StockListComponent } from './StockListComponent';
import { StockDetailComponent } from './StockDetailComponent';
import { AngularUnitDescriber } from '../util/AngularUnitDescriber';

import Location = common.Location;
import HttpClientTestingModule = common_http_testing.HttpClientTestingModule;
import HttpTestingController = common_http_testing.HttpTestingController;
import RequestMatch = common_http_testing.RequestMatch;
import TestRequest = common_http_testing.TestRequest;
import DebugElement = core.DebugElement;
import FormsModule = forms.FormsModule;
import By = platform_browser.By;
import Router = router.Router;
import RouterTestingModule = router_testing.RouterTestingModule;
export class StockListComponentSpec extends ComponentUnitDescriber<StockListComponent> {
    public constructor() {
        super(StockListComponent);
    }

    /**
     * 
     */
    public run() {
        this.beforeEach(() => { return new StockListComponentSpec.__app_dhojsw_ng_stock_StockListComponentSpec_BeforeEach<StockListComponent>(this).run(); });
        this.itFakeAsync('Test click refresh stock list.', () => {
            this.expect<any>(this.comp).toBeTruthy();
            let jsonS: string = /* replace */('{\r\n            \'type\': \'table\',\r\n            \'columnNames\': [\'corpId\', \'corpName\'],\r\n            \'rowArray\': [\r\n                [\'000001\', \'Name1\'],\r\n                [\'000002\', \'Name2\'],\r\n                [\'000003\', \'Name3\'],\r\n                [\'000004\', \'Name4\']\r\n            ]\r\n        }').split('\'').join('\"');
            let response: any = JSON.parse(jsonS);
            this.expect<any>(this.comp.responseArray.length).toEqual(0);
            {
                let button: DebugElement = this.fixture.debugElement.query(By.css('button'));
                let yes: boolean = true;
                button.triggerEventHandler('click', null);
                let rm: RequestMatch = <any>Object.defineProperty({

                }, '__interfaces', { configurable: true, value: ['def.angular.common_http_testing.RequestMatch'] });
                rm.method = 'POST';
                let req: TestRequest = this.httpMock.expectOne(rm);
                req.flush(response);
                this.fixture.detectChanges();
                this.tick();
                this.expect<any>(this.comp.responseArray.length).toEqual(1);
                this.fixture.detectChanges();
            };
            {
                this.router.navigate(['/stockDetail/000001']);
                this.tick$double(50);
                this.expect<any>(this.location.path()).toBe('/stockDetail/000001');
            };
            {
            };
        });
    }

    public static main(args: string[]) {
        new StockListComponentSpec().describe();
    }
}
StockListComponentSpec['__class'] = 'app.dhojsw.ng.stock.StockListComponentSpec';
StockListComponentSpec['__interfaces'] = ['java.lang.Runnable'];



export namespace StockListComponentSpec {

    export class __app_dhojsw_ng_stock_StockListComponentSpec_BeforeEach<T> {
        unit: ComponentUnitDescriber<T>;

        public constructor(unit: ComponentUnitDescriber<T>) {
            if(this.unit === undefined) this.unit = null;
            this.unit = unit;
        }

        /**
         * 
         */
        public run() {
            let testBed: TestBedHelper = new TestBedHelper().imports(FormsModule, HttpClientTestingModule, RouterTestingModule.withRoutes(AppRoutes.getRoutes())).declarations(this.unit.compType, StockDetailComponent, CommandComponent, MyComponent, PageNotFoundComponent).providers(Logger).compileComponents();
            this.unit.httpMock = <any>(testBed.get<any>(HttpTestingController));
            this.unit.fixture = testBed.createComponent<any>(this.unit.compType);
            this.unit.de = this.unit.fixture.debugElement;
            this.unit.comp = <T><any>this.unit.de.componentInstance;
            this.unit.ne = this.unit.de.nativeElement;
            this.unit.router = <any>(testBed.get<any>(Router));
            this.unit.location = <any>(testBed.get<any>(Location));
        }
    }
    __app_dhojsw_ng_stock_StockListComponentSpec_BeforeEach['__class'] = 'app.dhojsw.ng.stock.StockListComponentSpec.BeforeEach';
    __app_dhojsw_ng_stock_StockListComponentSpec_BeforeEach['__interfaces'] = ['java.lang.Runnable'];


}




StockListComponentSpec.main(null);
