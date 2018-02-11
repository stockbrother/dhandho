import { JasmineDescriber } from '../app/util/jasmine.describer';
import { printAll } from './util/element.printer';
import util = require('util');
import { protractor, browser, element, by, ElementArrayFinder, ElementFinder } from 'protractor';
import { Logs, promise } from 'selenium-webdriver';
import { LogsUtil } from './util/e2e.util';
let console = LogsUtil.console;
jasmine.DEFAULT_TIMEOUT_INTERVAL = 60000;

export class NgApimockTest extends JasmineDescriber {

    public run() {
        let ngApimock: any = browser['ngApimock'];

        this.it('ngApimock test', () => {
            ngApimock.selectScenario('api/some-post', 'successful');
        });

        this.it('test stock list with ngApimock', (done) => {

            ngApimock.selectScenario('api/stock-list', 'first-entry');
            browser.get('/');
            // find route links
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
                expect(h2.getText()).toEqual('Stock Id:000001'); // Why???: a promise from 3rd party can be equal to a string?
                let quickLinks: ElementFinder = element(by.id('quick-links-of-stock'));
                expect(quickLinks).toBeTruthy();
                // printAll(quickLinks);
                let links: ElementArrayFinder = quickLinks.all(by.xpath('./tbody/tr/td/a'));
                expect(links.count()).toEqual(4);
                for (let i: number = 0; i < 4; i++) {
                    let linkI: ElementFinder = links.get(i);
                    linkI.click();
                    // TODO check link available, return a 200 not a 404 or other.
                }
                // refresh detail
                let refreshButton: ElementFinder = element(by.cssContainingText('button', '刷新'));
                refreshButton.click();
                {
                    let stockNameTd: ElementFinder = element(by.cssContainingText('#basic-info-of-stock td', '股票名称:'));
                    expect(stockNameTd).toBeTruthy();
                    expect(stockNameTd.getText()).toEqual('股票名称:平安银行');
                }
                {
                    let unitPriceTd: ElementFinder = element(by.cssContainingText('#basic-info-of-stock td', '股价'));
                    expect(unitPriceTd).toBeTruthy();
                    expect(unitPriceTd.getText()).toEqual('股价(2018/02/10T11:11:11+8000):9.9');
                }
                {
                    let stockNameTd: ElementFinder = element(by.cssContainingText('#basic-info-of-stock td', '市值:'));
                    expect(stockNameTd).toBeTruthy();
                    expect(stockNameTd.getText()).toEqual('市值:9900000000');
                }
                {
                    let showChartsButton: ElementFinder = element(by.cssContainingText('#basic-info-of-stock a', 'Show Charts:'));
                    expect(showChartsButton).toBeTruthy();

                    printAll();

                    showChartsButton.click();
                    // route to stockCharts Component
                    {
                        printAll();
                        h2 = element(by.css('h2'));
                        expect(h2).toBeTruthy();
                        expect(h2.getText()).toEqual('Stock Charts:000001');
                    }
                }
                done();
            }

        }); // end of it().
    } // end of run
}
