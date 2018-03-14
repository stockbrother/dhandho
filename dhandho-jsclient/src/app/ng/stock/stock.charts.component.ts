/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import common_http = require('@angular/common/http');
import core = require('@angular/core');
import router = require('@angular/router');
import { DomSanitizer } from '@angular/platform-browser';
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
    public corpId: string;
    public charts: StockCharts;
    private sanitizer: DomSanitizer;
    constructor(back: BackendInterface, log: Logger, route: ActivatedRoute, sanitizer: DomSanitizer) {
        super(back, log, route);
        this.sanitizer = sanitizer;
    }

    onRefreshButtonClick(): void {
        this.backend.getStockCharts(this.corpId).then((charts) => {
            this.log.info('charts response:' + charts);
            this.charts = charts;
        });
    }

    transform(html) {
        return this.sanitizer.bypassSecurityTrustHtml(html);
    }
    /**
     *
     */
    public ngOnInit() {
        this.route.paramMap.subscribe(params => {
            this.corpId = params.get('id'); //
            this.onRefreshButtonClick();
        });

    }
}
