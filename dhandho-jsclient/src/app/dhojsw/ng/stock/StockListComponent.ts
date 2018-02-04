/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import common_http = require('@angular/common/http');
import core = require('@angular/core');
import { Logger } from '../service/Logger';
import { AbstractDataResponseComponent } from '../support/AbstractDataResponseComponent';
import { JsonResponse } from '../support/JsonResponse';

import HttpClient = common_http.HttpClient;
import Component = core.Component;
import OnInit = core.OnInit;
@core.Component({ templateUrl: './StockListComponent.html', styleUrls: ['./StockListComponent.css'] })
export class StockListComponent extends AbstractDataResponseComponent<JsonResponse> implements OnInit {
    public constructor(http: HttpClient, log: Logger) {
        super(http, '/web/stocks/', log);
    }

    /**
     * 
     * @return {*}
     */
    newRequestBody(): any {
        return '';
    }

    /**
     * 
     * @param {number} requestTime
     * @param {*} reqBody
     * @param {*} json
     * @return {JsonResponse}
     */
    newResponse(requestTime: number, reqBody: any, json: any): JsonResponse {
        return new JsonResponse(requestTime, json);
    }

    /**
     * 
     */
    public ngOnInit() {
    }
}
StockListComponent['__class'] = 'app.dhojsw.ng.stock.StockListComponent';
StockListComponent['__interfaces'] = ['def.angular.core.OnInit'];



