/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import common_http = require('@angular/common/http');
import core = require('@angular/core');
import router = require('@angular/router');
import { Logger } from '../service/logger';
import { AbstractComponent } from '../support/abstract.component';
import { JsonResponse } from '../support/json.response';
import { BackendInterface } from '../service/backend.interface';
import HttpClient = common_http.HttpClient;
import Component = core.Component;
import OnInit = core.OnInit;
import ActivatedRoute = router.ActivatedRoute;
@core.Component({
    templateUrl: './stock.detail.component.html',
    styleUrls: ['./stock.detail.component.css']
})
export class StockDetailComponent extends AbstractComponent implements OnInit {
    public corpId: string;

    route: ActivatedRoute;

    public priceDate: string;
    public unitPrice: number;
    public totalPrice: number;
    public corpName: string;
    constructor(backend: BackendInterface, log: Logger, route: ActivatedRoute) {
        super(backend, log, route);
    }

    public onResponse(requestTime: number, reqBody: any, json: any) {

        let type: string = <string>Object.getOwnPropertyDescriptor(json, 'type').value;
        this.log.debug('type:' + type);
        let rt: JsonResponse = new JsonResponse(requestTime, json);
        let mapData: any = <any>Object.getOwnPropertyDescriptor(json, 'mapData').value;

        this.priceDate = <string>Object.getOwnPropertyDescriptor(mapData, 'priceDate').value;
        this.unitPrice = <number>Object.getOwnPropertyDescriptor(mapData, 'unitPrice').value;
        this.totalPrice = <number>Object.getOwnPropertyDescriptor(mapData, 'totalPrice').value;
        this.corpName = <string>Object.getOwnPropertyDescriptor(mapData, 'corpName').value;

    }

    onRefreshStockDetail(): void {
        let reqTime: number = new Date().getDate();
        this.backend.newRequestForStockDetail().sendRequest('').then((json) => {
            this.onResponse(reqTime, '', json);
        });
    }

    /**
     *
     */
    public ngOnInit() {
        // this.stockId = this.route.snapshot.paramMap.get('id');
        this.route.paramMap.subscribe(params => {
            this.corpId = params.get('id'); //
            this.onRefreshStockDetail();
        });
    }
}
