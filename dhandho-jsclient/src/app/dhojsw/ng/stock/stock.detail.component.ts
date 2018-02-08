/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import common_http = require('@angular/common/http');
import core = require('@angular/core');
import router = require('@angular/router');
import { Logger } from '../service/logger';
import { AbstractDataResponseComponent } from '../support/abstract.data.response.component';
import { JsonResponse } from '../support/json.response';

import HttpClient = common_http.HttpClient;
import Component = core.Component;
import OnInit = core.OnInit;
import ActivatedRoute = router.ActivatedRoute;
@core.Component({
    templateUrl: './stock.detail.component.html',
    styleUrls: ['./stock.detail.component.css']
})
export class StockDetailComponent extends AbstractDataResponseComponent<JsonResponse> implements OnInit {
    public stockId: string;

    route: ActivatedRoute;

    public constructor(http: HttpClient, log: Logger, route: ActivatedRoute) {
        super(http, '/web/stocks/', log);
        this.route = route;
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
        // this.stockId = this.route.snapshot.paramMap.get('id');
        this.route.paramMap.subscribe(params => {
            this.stockId = params.get('id'); //
        });
    }
}
