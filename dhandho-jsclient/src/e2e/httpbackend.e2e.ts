import http = require('http');
import { JasmineDescriber } from '../app/dhojsw/util/jasmine.describer';
import { Logger } from '../app/dhojsw/ng/service/logger';
import { browser, by, element } from 'protractor';
import HttpBackend = require('httpbackend');

export class HttpBackendE2e extends JasmineDescriber {

    static LOG: Logger = new Logger();

    backend: HttpBackend;
    constructor() {
        super();
    }

    public run() {
        this.beforeEach(() => {
            this.backend = new HttpBackend(browser);
        });
        this.it('Test httpbackend with protractor.', () => {
            this.backend.whenGET('/').respond('hi');
            browser.get('/').catch((err) => {
                // https://stackoverflow.com/questions/36354233/protractor-mocking-backend-with-angular2-on-api-request
                HttpBackendE2e.LOG.info(`error:${err}`);
            });
        });

        this.afterEach(() => {
            this.backend.clear();
        });
    }
}

