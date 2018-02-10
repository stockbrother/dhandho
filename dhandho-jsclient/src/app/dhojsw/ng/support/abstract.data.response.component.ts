/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import jsnative = require('jsnative');
import common_http = require('@angular/common/http');
import core = require('@angular/core');
import { Observable } from 'rxjs/Observable';
import { Logger } from '../service/logger';
import { JsonResponse } from './json.response';

import js_isNumber = jsnative.js_isNumber;
import HttpClient = common_http.HttpClient;
import OnInit = core.OnInit;
/**
 * TODO use object/promise reuse for HTTP processing, deprecated base class reuse.
 */
export abstract class AbstractDataResponseComponent<T extends JsonResponse> {
    url: string;

    http: HttpClient;

    log: Logger;

    public responseArray: Array<T> = <any>(new Array<any>());

    public constructor(http: HttpClient, url: string, log: Logger) {
        this.http = http;
        this.url = url;
        this.log = log;
    }

    public onResponse(requestTime: number, reqBody: any, json: any) {
        let typeP: PropertyDescriptor = Object.getOwnPropertyDescriptor(json, 'type');
        let type: string = <string>typeP.value;
        this.log.debug('type:' + type);
        let rt: T = this.newResponse(requestTime, reqBody, json);
        this.responseArray.unshift(rt);
    }

    abstract newRequestBody(): any;

    abstract newResponse(requestTime: number, reqBody: any, json: any): T;

    protected sendRequest() {
        this.log.info('onButtonClick');
        let requestTime: number = Date.now();
        let body: any = this.newRequestBody();
        let ores: Observable<any> = this.http.post(this.url, body);
        ores.toPromise().then(((trequestTime, tbody) => {
            return (json) => {
                this.log.debug(json);
                this.onResponse(trequestTime, tbody, json);
                this.log.debug('post response:' + json);
                return null;
            };
        })(requestTime, body)).catch((t) => {
            this.log.error('catched exception');
            this.log.error(t);
            return null;
        });
    }

    private showChart(): boolean {
        return true;
    }

    public isNumber(json: any): boolean {
        return js_isNumber(json);
    }
}
