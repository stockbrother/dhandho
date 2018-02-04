/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import common_http = require('@angular/common/http');
import core = require('@angular/core');
import router = require('@angular/router');
import { Logger } from '../service/Logger';
import { AbstractDataResponseComponent } from '../support/AbstractDataResponseComponent';
import { JsonResponse } from '../support/JsonResponse';

import HttpClient = common_http.HttpClient;
import Component = core.Component;
import OnInit = core.OnInit;
import ActivatedRoute = router.ActivatedRoute;
@core.Component({ templateUrl: './StockDetailComponent.html', styleUrls: ['./StockDetailComponent.css'] })
export class StockDetailComponent extends AbstractDataResponseComponent<JsonResponse> implements OnInit {
    public stockId: string;

    route: ActivatedRoute;

    public constructor(http: HttpClient, log: Logger) {
        super(http, '/web/stocks/', log);
        if(this.stockId === undefined) this.stockId = null;
        if(this.route === undefined) this.route = null;
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
        this.route.paramMap.toPromise().then((map) => {
            this.stockId = map.get('id');
        });
    }
}
StockDetailComponent['__class'] = 'app.dhojsw.ng.stock.StockDetailComponent';
StockDetailComponent['__interfaces'] = ['def.angular.core.OnInit'];



