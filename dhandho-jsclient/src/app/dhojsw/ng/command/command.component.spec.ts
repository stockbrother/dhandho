/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import common_http_testing = require('@angular/common/http/testing');
import core = require('@angular/core');
import forms = require('@angular/forms');
import platform_browser = require('@angular/platform-browser');
import { Logger } from '../service/logger';
import { ComponentUnitDescriber } from '../util/component.unit.describer';
import { TestBedHelper } from '../util/test.bed.helper';
import { JasmineDescriber } from '../../util/jasmine.describer';
import { CommandComponent } from './command.component';
import { AngularUnitDescriber } from '../util/angular.unit.describer';

import HttpClientTestingModule = common_http_testing.HttpClientTestingModule;
import HttpTestingController = common_http_testing.HttpTestingController;
import RequestMatch = common_http_testing.RequestMatch;
import TestRequest = common_http_testing.TestRequest;
import DebugElement = core.DebugElement;
import FormsModule = forms.FormsModule;
import By = platform_browser.By;
export class CommandComponentSpec {
    public static main(args: string[]) {
        new CommandComponentSpec.FirstDescribeContext('Command Component Test1').describe();
        new JasmineDescriber('Command Component Test2').describe();
    }
}
CommandComponentSpec['__class'] = 'app.dhojsw.ng.command.CommandComponentSpec';


export namespace CommandComponentSpec {

    export class FirstDescribeContext extends ComponentUnitDescriber<CommandComponent> {
        public constructor(desc: string) {
            super(desc, CommandComponent);
        }
        /**
         *
         */
        public run() {
            this.beforeEach(() => {
                let testBed: TestBedHelper = new TestBedHelper().imports(FormsModule, HttpClientTestingModule).declarations(CommandComponent).providers(Logger).compileComponents();
                this.httpMock = <any>(testBed.get<any>(HttpTestingController));
                this.fixture = testBed.createComponent<any>(CommandComponent);
                this.de = this.fixture.debugElement;
                this.comp = <CommandComponent>this.de.componentInstance;
                this.ne = this.de.nativeElement;
            });
            this.it$java_lang_String$java_util_function_Consumer('1.1.Should create the command', <any>(this.async<any>(() => {
                this.expect<any>(this.comp).toBeTruthy();
            })));
            this.it$java_lang_String$java_util_function_Consumer('1.2.Title check', <any>(this.async<any>(() => {
                let compiled: Element = this.fixture.debugElement.nativeElement;
                this.it$java_lang_String$java_util_function_Consumer('Title should not be binded until fixture.detectChanges()', (done) => {
                    this.expect<any>(compiled.querySelector('h1').textContent).toEqual('');
                });
                this.fixture.detectChanges();
                this.expect<any>(compiled.querySelector('h1').textContent).toContain(this.comp.title);
            })));
            this.it$java_lang_String$java_util_function_Consumer('1.3.Response Processing', <any>(this.fakeAsync<any>(() => {
                let jsonS: string = /* replace */('{\r\n            \'type\': \'table\',\r\n            \'columnNames\': [\'corpId\', \'corpName\', \'highLight\', \'ProfitMargin\', \'AssetTurnover\', \'EquityMultiplier\'],\r\n            \'rowArray\': [\r\n                [\'000001\', \'Name1\', false, 0.01, 0.11, 1.1],\r\n                [\'000002\', \'Name2\', true, 0.02, 0.12, 1.2],\r\n                [\'000003\', \'Name3\', false, 0.03, 0.13, 1.3],\r\n                [\'000004\', \'Name4\', false, 0.04, 0.14, 1.4]\r\n            ]\r\n        }').split('\'').join('\"');
                let response: any = JSON.parse(jsonS);
                this.expect<any>(this.comp.responseArray.length).toEqual(0);
                {
                    this.comp.command = 'dupont -r -c 000001 -y 2016 -f 0.01';
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
                }
            })));
        }
    }
    FirstDescribeContext['__class'] = 'app.dhojsw.ng.command.CommandComponentSpec.FirstDescribeContext';
    FirstDescribeContext['__interfaces'] = ['java.lang.Runnable'];


}




CommandComponentSpec.main(null);
