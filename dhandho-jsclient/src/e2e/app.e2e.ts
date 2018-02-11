/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import protractor = require('protractor');
import webdriver = require('selenium-webdriver');
import { JasmineDescriber } from '../app/util/jasmine.describer';

import browser = protractor.browser;
import by = protractor.by;
import element = protractor.element;
import ElementFinder = protractor.ElementFinder;
import By = webdriver.By;
import promise = webdriver.promise;
export class AppE2e extends JasmineDescriber {
    public constructor() {
        super();
    }

    public static main(args: string[]) {
        new AppE2e().describe();
    }

    public(done: DoneFn) {
        browser.get('/');
        done();
    }

    /**
     *
     */
    public run() {
        this.it$java_lang_String$java_lang_Runnable('test task asynchronously', () => {
            let pro: promise.Promise<any> = browser.get('/');
            let byy: By = by.css('app-root h1');
            let ef: ElementFinder = element(byy);
            let text: promise.Promise<string> = ef.getText();
            this.expect<any>(text).toBeTruthy();
        });
        this.it$java_lang_String$java_util_function_Consumer('test browser.executeAsyncScript', (done) => {
            browser.executeAsyncScript((t: DoneFn) => {
                t();
            });
            done();
        });
    }
}

export namespace AppE2e {

    export class MyFunction {
        /**
         *
         * @param {*} t
         */
        public accept(t: DoneFn) {
        }

        constructor() {
        }
    }

}

AppE2e.main(null);
