/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import common_http = require('@angular/common/http');
import { Component, OnInit } from '@angular/core';
import { Logger } from '../service/logger';
import { AbstractDataResponseComponent } from '../support/abstract.data.response.component';
import { JsonResponse } from '../support/json.response';

import HttpClient = common_http.HttpClient;

@Component({ templateUrl: './stock.list.component.html', styleUrls: ['./stock.list.component.css'] })
export class StockListComponent extends AbstractDataResponseComponent<JsonResponse> implements OnInit {
    public constructor(http: HttpClient, log: Logger) {
        super(http, '/api/stock-list', log);
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

    onRefreshButtonClick(): void {
        super.sendRequest();
    }
    /**
     *
     */
    public ngOnInit() {
    }
}
