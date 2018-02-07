import { JasmineDescriber } from '../app/dhojsw/util/jasmine.describer';
import { ElementPrinter } from './util/element.printer';
import util = require('util');
import { protractor, browser, element, by, ElementArrayFinder, ElementFinder } from 'protractor';
import { Logs, promise } from 'selenium-webdriver';
import { LogsUtil } from './util/e2e.util';
let console = LogsUtil.console;

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
                h2.getText().then((title) => {
                    let body = element(by.tagName('body'));
                    expect(body).toBeTruthy('body not found');

                    new ElementPrinter(body).print();

                    expect(title).toEqual('000001', 'hello here!');
                    // expect(title).toEqual('000001', new ElementPrinter());
                });
            }

            done();
        });

    }// end of run
}

