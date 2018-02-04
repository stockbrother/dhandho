/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import common_http_testing = require('@angular/common/http/testing');
import core_testing = require('@angular/core/testing');
import forms = require('@angular/forms');
import { CommandComponent } from './command/CommandComponent';
import { Logger } from './service/Logger';

import HttpClientTestingModule = common_http_testing.HttpClientTestingModule;
import TestBed = core_testing.TestBed;
import FormsModule = forms.FormsModule;
export class AppModuleSpec {
    public static main(args: string[]) {
        describe('App Module Test1', () => {
            it('1.1', (done) => {
                let meta: any = {};
                meta.imports = [FormsModule, HttpClientTestingModule];
                meta.declarations = [CommandComponent];
                meta.providers = [Logger];
                TestBed.configureTestingModule(meta);
                TestBed.compileComponents();
                done();
            });
        });
    }
}
AppModuleSpec['__class'] = 'app.dhojsw.ng.AppModuleSpec';




AppModuleSpec.main(null);
