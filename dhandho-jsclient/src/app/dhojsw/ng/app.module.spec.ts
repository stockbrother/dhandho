/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import common_http_testing = require('@angular/common/http/testing');
import core_testing = require('@angular/core/testing');
import forms = require('@angular/forms');
import { CommandComponent } from './command/command.component';
import { Logger } from './service/logger';

import HttpClientTestingModule = common_http_testing.HttpClientTestingModule;
import TestBed = core_testing.TestBed;
import FormsModule = forms.FormsModule;

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
