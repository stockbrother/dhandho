/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import common_http = require('@angular/common/http');
import core = require('@angular/core');
import router = require('@angular/router');
import { Logger } from '../service/logger';
import { JsonResponse } from '../support/json.response';
import { BackendInterface, StockCharts } from '../service/backend.interface';
import { AbstractComponent } from '../support/abstract.component';

import HttpClient = common_http.HttpClient;
import Component = core.Component;
import OnInit = core.OnInit;
import ActivatedRoute = router.ActivatedRoute;
@core.Component({
    templateUrl: './stock.charts.component.html',
    styleUrls: ['./stock.charts.component.css']
})
export class StockChartsComponent extends AbstractComponent implements OnInit {
    public stockId: string;
    public stockCharts: StockCharts;
    constructor(back: BackendInterface, log: Logger, route: ActivatedRoute) {
        super(back, log, route);
    }

    onRefreshButtonClick(): void {
        this.backend.getStockCharts(this.stockId).then((stockCharts) => {
            this.log.info('stockCharts response:' + stockCharts);
            this.stockCharts = stockCharts;
        });
    }

    /**
     *
     */
    public ngOnInit() {
        this.route.paramMap.subscribe(params => {
            this.stockId = params.get('id'); //
            this.onRefreshButtonClick();
        });

    }
}
