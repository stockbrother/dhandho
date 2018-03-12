import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Logger } from './logger';
import { GlobalConfig } from './global.config';

export class JsonRequestBuilder {
    log: Logger = new Logger();

    http: HttpClient;
    url: string;

    constructor(http: HttpClient) {
        this.http = http;
    }
    setUrl(url: string): JsonRequestBuilder {
        this.url = url;
        return this;
    }

    sendRequest(body: any): Promise<any> {
        this.log.info('sendRequest');
        let requestTime: number = Date.now();
        let ores: Observable<any> = this.http.post(this.url, body);
        return ores.toPromise().then((json) => {
            this.log.debug(json);
            return json;
        }).catch((t) => {
            this.log.error('catched exception');
            this.log.error(t);
            return Promise.reject(t); //
        });
    }
}
export class StockCharts {
    json: any;
    constructor(json: any) {
        this.json = json;
    }
}

@Injectable()
export class BackendInterface {
    static URL_STOCK_LIST = '/rest/stock-list';
    static URL_STOCK_CHARTS = '/rest/stock-charts';
    static URL_STOCK_DETAIL = '/rest/stock-detail';

    http: HttpClient;

    log: Logger;

    globalConfig: GlobalConfig;

    constructor(http: HttpClient, log: Logger, globalConfig: GlobalConfig) {
        this.http = http;
        this.log = log;
        this.globalConfig = globalConfig;
    }

    newRequest(url: string): JsonRequestBuilder {
        let rt: JsonRequestBuilder = new JsonRequestBuilder(this.http).setUrl(url);
        return rt;
    }

    newRequestForStockList() {
        return this.newRequest(BackendInterface.URL_STOCK_LIST);
    }

    newRequestForStockCharts() {
        return this.newRequest(BackendInterface.URL_STOCK_CHARTS);
    }

    newRequestForStockDetail() {
        return this.newRequest(BackendInterface.URL_STOCK_DETAIL);
    }

    getStockCharts(stockId: string): Promise<StockCharts> {
        let req: JsonRequestBuilder = this.newRequestForStockCharts();
        let body = { stockId: stockId };
        return req.sendRequest(body).then((json) => {
            return new StockCharts(json);
        });
    }
}

export class MockBackend extends BackendInterface {

}
