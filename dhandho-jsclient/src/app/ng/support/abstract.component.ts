/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import jsnative = require('jsnative');
import common_http = require('@angular/common/http');
import core = require('@angular/core');
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { Logger } from '../service/logger';
import { JsonResponse } from './json.response';
import { BackendInterface } from '../service/backend.interface';

import js_isNumber = jsnative.js_isNumber;
import HttpClient = common_http.HttpClient;
import OnInit = core.OnInit;

export abstract class AbstractComponent {

    log: Logger;
    route: ActivatedRoute;
    backend: BackendInterface;

    constructor(backend: BackendInterface, log: Logger, route: ActivatedRoute) {
        this.backend = backend;
        this.log = log;
        this.route = route;
    }

}
