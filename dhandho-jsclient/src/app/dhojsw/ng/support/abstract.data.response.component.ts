/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import jsnative = require('jsnative');
import common_http = require('@angular/common/http');
import core = require('@angular/core');
import rxjs = require('rxjs');
import { Logger } from '../service/logger';
import { JsonResponse } from './json.response';

import js_isNumber = jsnative.js_isNumber;
import HttpClient = common_http.HttpClient;
import OnInit = core.OnInit;
import Observable = rxjs.Observable;
export abstract class AbstractDataResponseComponent<T extends JsonResponse> {
    url: string;

    http: HttpClient;

    log: Logger;

    public responseArray: Array<T> = <any>(new Array<any>());

    public constructor(http: HttpClient, url: string, log: Logger) {
        if(this.url === undefined) this.url = null;
        if(this.http === undefined) this.http = null;
        if(this.log === undefined) this.log = null;
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

    public onButtonClick() {
        let requestTime: number = Date.now();
        let body: any = this.newRequestBody();
        let ores: Observable<any> = this.http.post(this.url, body);
        ores.toPromise().then(((requestTime, body) => {
            return (json) => {
                this.log.debug(json);
                this.onResponse(requestTime, body, json);
                this.log.debug('post response:' + json);
                return null;
            }
        })(requestTime, body)).catch((t) => {
            this.log.error('catched exception');
            this.log.error(t);
            return null;
        });
    }

    public showChart(): boolean {
        return true;
    }

    public isNumber(json: any): boolean {
        return js_isNumber(json);
    }
}
AbstractDataResponseComponent['__class'] = 'app.dhojsw.ng.support.AbstractDataResponseComponent';



