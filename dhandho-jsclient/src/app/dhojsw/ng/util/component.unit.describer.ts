/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import common = require('@angular/common');
import common_http_testing = require('@angular/common/http/testing');
import core = require('@angular/core');
import core_testing = require('@angular/core/testing');
import forms = require('@angular/forms');
import router = require('@angular/router');
import { Logger } from '../service/logger';
import { AngularUnitDescriber } from './angular.unit.describer';
import { TestBedHelper } from './test.bed.helper';

import Location = common.Location;
import HttpClientTestingModule = common_http_testing.HttpClientTestingModule;
import HttpTestingController = common_http_testing.HttpTestingController;
import DebugElement = core.DebugElement;
import ComponentFixture = core_testing.ComponentFixture;
import FormsModule = forms.FormsModule;
import Router = router.Router;
export class ComponentUnitDescriber<C> extends AngularUnitDescriber {
    public httpMock: HttpTestingController;

    public comp: C;

    public fixture: ComponentFixture<C>;

    public de: DebugElement;

    public ne: Element;

    public compType: any;

    public router: Router;

    public location: Location;

    public constructor(desc?: any, compType?: any) {
        if(((typeof desc === 'string') || desc === null) && ((compType != null) || compType === null)) {
            let __args = Array.prototype.slice.call(arguments);
            super(desc);
            if(this.httpMock === undefined) this.httpMock = null;
            if(this.comp === undefined) this.comp = null;
            if(this.fixture === undefined) this.fixture = null;
            if(this.de === undefined) this.de = null;
            if(this.ne === undefined) this.ne = null;
            if(this.compType === undefined) this.compType = null;
            if(this.router === undefined) this.router = null;
            if(this.location === undefined) this.location = null;
            if(this.httpMock === undefined) this.httpMock = null;
            if(this.comp === undefined) this.comp = null;
            if(this.fixture === undefined) this.fixture = null;
            if(this.de === undefined) this.de = null;
            if(this.ne === undefined) this.ne = null;
            if(this.compType === undefined) this.compType = null;
            if(this.router === undefined) this.router = null;
            if(this.location === undefined) this.location = null;
            (() => {
                this.compType = compType;
            })();
        } else if(((desc != null) || desc === null) && compType === undefined) {
            let __args = Array.prototype.slice.call(arguments);
            let compType: any = __args[0];
            super();
            if(this.httpMock === undefined) this.httpMock = null;
            if(this.comp === undefined) this.comp = null;
            if(this.fixture === undefined) this.fixture = null;
            if(this.de === undefined) this.de = null;
            if(this.ne === undefined) this.ne = null;
            if(this.compType === undefined) this.compType = null;
            if(this.router === undefined) this.router = null;
            if(this.location === undefined) this.location = null;
            if(this.httpMock === undefined) this.httpMock = null;
            if(this.comp === undefined) this.comp = null;
            if(this.fixture === undefined) this.fixture = null;
            if(this.de === undefined) this.de = null;
            if(this.ne === undefined) this.ne = null;
            if(this.compType === undefined) this.compType = null;
            if(this.router === undefined) this.router = null;
            if(this.location === undefined) this.location = null;
            (() => {
                this.compType = compType;
            })();
        } else throw new Error('invalid overload');
    }
}
ComponentUnitDescriber['__class'] = 'app.dhojsw.ng.util.component.unit.describer';
ComponentUnitDescriber['__interfaces'] = ['java.lang.Runnable'];



export namespace ComponentUnitDescriber {

    export class BeforeEach<T> {
        unit: ComponentUnitDescriber<T>;

        public constructor(unit: ComponentUnitDescriber<T>) {
            if(this.unit === undefined) this.unit = null;
            this.unit = unit;
        }

        /**
         * 
         */
        public run() {
            let testBed: TestBedHelper = new TestBedHelper().imports(FormsModule, HttpClientTestingModule).declarations(this.unit.compType).providers(Logger).compileComponents();
            this.unit.httpMock = <any>(testBed.get<any>(HttpTestingController));
            this.unit.fixture = testBed.createComponent<any>(this.unit.compType);
            this.unit.de = this.unit.fixture.debugElement;
            this.unit.comp = <T><any>this.unit.de.componentInstance;
            this.unit.ne = this.unit.de.nativeElement;
        }
    }
    BeforeEach['__class'] = 'app.dhojsw.ng.util.ComponentUnitDescriber.BeforeEach';
    BeforeEach['__interfaces'] = ['java.lang.Runnable'];


}



