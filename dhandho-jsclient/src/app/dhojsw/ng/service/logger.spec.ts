/* Generated from Java with JSweet 2.0.1 - http://www.jsweet.org */
import core_testing = require('@angular/core/testing');
import { JasmineDescriber } from '../../util/jasmine.describer';
import { Logger } from './logger';

import TestBed = core_testing.TestBed;
export class LoggerSpec extends JasmineDescriber {
    logger: Logger;

    public static main(args: string[]) {
        new LoggerSpec('Logger Test').describe();
    }

    public constructor(desc: string) {
        super(desc);
    }

    /**
     *
     */
    public run() {
        this.beforeEach(() => {
            let meta: any = {};
            meta.providers = [Logger];
            TestBed.configureTestingModule(meta);
            TestBed.compileComponents();
            this.logger = <any>(TestBed.get(Logger));
        });
        this.it$java_lang_String$java_util_function_Consumer('Assert logger is injected.', (done) => {
            this.expect<any>(this.logger).toBeTruthy('Logger not able being injected.');
            this.logger.debug('debug msg');
            this.logger.info('info msg');
            this.logger.warn('warn msg');
            this.logger.error('error msg');
            done();
        });
    }
}

LoggerSpec.main(null);
