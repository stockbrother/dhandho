import { JasmineDescriber } from '../app/dhojsw/util/jasmine.describer';
import { protractor, browser, element, by, ElementArrayFinder, ElementFinder } from 'protractor';
export class NgApimockTest extends JasmineDescriber {

    public run() {
        let ngApimock: any = browser['ngApimock'];

        this.it('ngApimock test', () => {
            ngApimock.selectScenario('api/some-post', 'successful');
        });

        this.it('test stock list with ngApimock', () => {
            ngApimock.selectScenario('api/stock-list', 'first-entry');
            browser.get('/');
            let buttons: ElementArrayFinder = element.all(by.css('a'));
            expect(buttons.count()).toEqual(3);
            let stockListButton: ElementFinder = buttons.get(0);
            expect(stockListButton.getText()).toEqual('Stock List');
            stockListButton.click(); // route to stock list.
            let refreshButton: ElementFinder = element(by.css('button'));
            expect(refreshButton.getText()).toEqual('Refresh');
            refreshButton.click();
            let table: ElementFinder = element(by.css('table'));
            expect(table).toBeTruthy();
            let allStockTr: ElementArrayFinder = table.element('tbody').all('tr');
            expect(allStockTr.count()).toEqual(4); // 4 stock in list.
            // TODO click one of it.

        });
    }// end of run
}

