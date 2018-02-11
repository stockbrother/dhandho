import http = require('http');
import { JasmineDescriber } from '../app/util/jasmine.describer';
import { Logger } from '../app/ng/service/logger';
import { browser, by, element } from 'protractor';
function httpRequest(params, postData?) {
    return new Promise(function(resolve, reject) {
        let req = http.request(params, function(res) {
            // reject on bad status
            if (res.statusCode < 200 || res.statusCode >= 300) {
                return reject(new Error('statusCode=' + res.statusCode));
            }
            // cumulate data
            let body = [];
            res.on('data', function(chunk) {
                body.push(chunk);
            });
            // resolve on end
            res.on('end', function() {
                try {
                    body = JSON.parse(Buffer.concat(body).toString());
                } catch (e) {
                    reject(e);
                }
                resolve(body);
            });
        });
        // reject on request error
        req.on('error', function(err) {
            // This is not a "Second reject", just a different sort of failure
            reject(err);
        });
        if (postData) {
            req.write(postData);
        }
        // IMPORTANT
        req.end();
    });
}

export class HttpServerE2e extends JasmineDescriber {

    static LOG: Logger = new Logger();

    server: http.Server;
    constructor() {
        super();
    }

    public run() {
        this.beforeEach(() => {
            HttpServerE2e.LOG.info('before each');
            this.server = http.createServer((req, res) => {
                res.write('Hello World!');
                res.end();
            });
            this.server.listen(8080);

        });
        this.it('Test http server with protractor.', () => {
            browser.waitForAngularEnabled(false);
            browser.get('http://localhost:8080').then((data) => {
                HttpServerE2e.LOG.info(`on response:${data}`);
            });
        });

        this.it('Test http server with node.js', () => {
            let options: http.RequestOptions = { port: 8089, method: 'POST' };
            HttpServerE2e.LOG.info(`options: ${options}.`);

            httpRequest(options).then((res) => {
                HttpServerE2e.LOG.info('response');
            }).catch((e) => {
                HttpServerE2e.LOG.error(e);
            });
        });
        this.afterEach(() => {
            this.server.close();
            HttpServerE2e.LOG.info('after each');
        });
    }
}

