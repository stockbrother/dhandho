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

    public priceDate: string;
    public unitPrice: number;
    public totalPrice: number;
    public stockName: string;
    public constructor(http: HttpClient, log: Logger, route: ActivatedRoute) {
        super(http, '/api/stock-detail', log);
        this.route = route;
    }

    public onResponse(requestTime: number, reqBody: any, json: any) {

        let type: string = <string>Object.getOwnPropertyDescriptor(json, 'type').value;
        this.log.debug('type:' + type);
        let rt: JsonResponse = new JsonResponse(requestTime, json);
        let mapData: any = <any>Object.getOwnPropertyDescriptor(json, 'mapData').value;

        this.priceDate = <string>Object.getOwnPropertyDescriptor(mapData, 'priceDate').value;
        this.unitPrice = <number>Object.getOwnPropertyDescriptor(mapData, 'unitPrice').value;
        this.totalPrice = <number>Object.getOwnPropertyDescriptor(mapData, 'totalPrice').value;
        this.stockName = <string>Object.getOwnPropertyDescriptor(mapData, 'stockName').value;

    }

    onRefreshStockDetail(): void {
        super.sendRequest();
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
