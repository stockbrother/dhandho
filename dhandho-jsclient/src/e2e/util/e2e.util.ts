import { Logs } from 'selenium-webdriver';
import { protractor, browser, element, by, ElementArrayFinder, ElementFinder } from 'protractor';
import util = require('util');


export class Console {

    logs: Logs = browser.manage().logs();

    log(obj: any) {
        this.logs.get('browser').then((browserLog) => {
            console.log(obj);
        }, (error) => {
            console.error('error:' + util.inspect(error));
        });
    }

    info(obj: any) {
        this.logs.get('browser').then((browserLog) => {
            console.info(obj);
        }, (error) => {
            console.error('error:' + util.inspect(error));
        });
    }


}

export class LogsUtil {
    static console: Console = new Console();
}
