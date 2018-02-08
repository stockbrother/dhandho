import { JasmineDescriber } from '../app/dhojsw/util/jasmine.describer';
import { printAll } from './util/element.printer';
import util = require('util');
import { protractor, browser, element, by, ElementArrayFinder, ElementFinder } from 'protractor';
import { Logs, promise } from 'selenium-webdriver';
import { LogsUtil } from './util/e2e.util';
let console = LogsUtil.console;
jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;

export class NgApimockTest extends JasmineDescriber {

    public run() {
        let ngApimock: any = browser['ngApimock'];

        this.it('ngApimock test', () => {
            ngApimock.selectScenario('api/some-post', 'successful');
        });

        this.it('test stock list with ngApimock', (done) => {

            ngApimock.selectScenario('api/stock-list', 'first-entry');
            browser.get('/');
            let buttons: ElementArrayFinder = element.all(by.css('a'));
            expect(buttons.count()).toEqual(3);

            let stockListButton: ElementFinder = buttons.get(0);
            expect(stockListButton.getText()).toEqual('Stock List');
            stockListButton.click(); // route to stock list.
            {// stock list component

                let refreshButton: ElementFinder = element(by.css('button'));
                expect(refreshButton.getText()).toEqual('Refresh');
                refreshButton.click();

                {// refresh stock list
                    let table: ElementFinder = element(by.css('table'));
                    expect(table).toBeTruthy();

                    let tbody: ElementFinder = table.element(by.tagName('tbody'));
                    expect(tbody).toBeTruthy('no <tbody> found');

                    let allStockTr: ElementArrayFinder = tbody.all(by.tagName('tr'));
                    expect(allStockTr).toBeTruthy('no <tr>...</> found');
                    expect(allStockTr.count()).toEqual(4); // 4 stock in list.

                    let firstTr: ElementFinder = allStockTr.get(0);
                    expect(firstTr).toBeTruthy('no first tr found?');
                    let routerA: ElementFinder = firstTr.element(by.tagName('a'));
                    expect(routerA).toBeTruthy('no router Link in first tr?');

                    routerA.click(); // router to detail component
                }
            }
            {// detail component
                let h2: ElementFinder = element(by.css('h2'));
                expect(h2).toBeTruthy('h2 not found');
                expect(h2.getText()).toEqual('Stock Id:000001'); // Why a promise from 3rd party can be equal to a string?
            }
            // TODO find way to printAll for any failure of expect.
            done();
        });

    }// end of run
}

