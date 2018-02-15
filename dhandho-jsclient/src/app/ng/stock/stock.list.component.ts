/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import common_http = require('@angular/common/http');
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Logger } from '../service/logger';
import { AbstractComponent } from '../support/abstract.component';
import { BackendInterface } from '../service/backend.interface';

import { JsonResponse } from '../support/json.response';

import HttpClient = common_http.HttpClient;

@Component({ templateUrl: './stock.list.component.html', styleUrls: ['./stock.list.component.css'] })
export class StockListComponent extends AbstractComponent implements OnInit {


    public responseArray: Array<JsonResponse> = <any>(new Array<any>());

    constructor(backend: BackendInterface, log: Logger, route: ActivatedRoute) {
        super(backend, log, route);
    }

    onRefreshButtonClick(): void {
        let reqTime: number = new Date().getDate();
        this.backend.newRequestForStockList().sendRequest('').then((json) => {
            let jsonR: JsonResponse = new JsonResponse(reqTime, json);
            this.responseArray.unshift(jsonR);
        });
    }
    /**
     *
     */
    public ngOnInit() {
        this.onRefreshButtonClick();
    }
}
